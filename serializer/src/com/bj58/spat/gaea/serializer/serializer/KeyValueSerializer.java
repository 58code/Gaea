/*
 *  Copyright Beijing 58 Information Technology Co.,Ltd.
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package com.bj58.spat.gaea.serializer.serializer;

import com.bj58.spat.gaea.serializer.classes.GKeyValuePair;
import com.bj58.spat.gaea.serializer.component.GaeaInStream;
import com.bj58.spat.gaea.serializer.component.GaeaOutStream;
import com.bj58.spat.gaea.serializer.component.helper.TypeHelper;
import com.bj58.spat.gaea.serializer.serializer.SerializerBase;

/**
 * KeyValueSerializer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class KeyValueSerializer extends SerializerBase {

    @Override
    public void WriteObject(Object obj, GaeaOutStream outStream) throws Exception {
        if (obj == null) {
            SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
        }
        Class type = obj.getClass();
        int typeId = TypeHelper.GetTypeId(type);
        outStream.WriteInt32(typeId);
        if (outStream.WriteRef(obj)) {
            return;
        }
        GKeyValuePair obj2 = (GKeyValuePair) obj;
        Object key = obj2.getKey();
        Object value = obj2.getValue();
        Class itemKeyType = key.getClass();
        int itemKeyTypeId = TypeHelper.GetTypeId(itemKeyType);
        outStream.WriteInt32(itemKeyTypeId);
        SerializerFactory.GetSerializer(itemKeyType).WriteObject(key, outStream);

        if (value == null) {
            SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
        } else {

            Class itemValueType = value.getClass();
            int itemValueTypeId = TypeHelper.GetTypeId(itemValueType);
            outStream.WriteInt32(itemValueTypeId);
            SerializerFactory.GetSerializer(itemValueType).WriteObject(value, outStream);
        }
    }

    @Override
    public Object ReadObject(GaeaInStream inStream, Class defType) throws Exception {
        int typeId = inStream.ReadInt32();
        if (typeId == 0) {
            return null;
        }
        byte isRef = (byte) inStream.read();
        int hashcode = inStream.ReadInt32();
        if (isRef > 0) {
            return inStream.GetRef(hashcode);
        }

        int itemKeyTypeId = inStream.ReadInt32();
        Class itemKeyType = TypeHelper.GetType(itemKeyTypeId);
        if (itemKeyType == null) {
            throw new ClassNotFoundException("Cannot find class with typId,target class:KeyValue[key]" + ",typeId:" + itemKeyTypeId);
        }
        Object key = SerializerFactory.GetSerializer(itemKeyType).ReadObject(inStream, itemKeyType);

        int itemValueTypeId = inStream.ReadInt32();
        Object value = null;
        if (itemValueTypeId != 0) {
            Class itemValueType = TypeHelper.GetType(itemValueTypeId);
            if (itemValueType == null) {
                throw new ClassNotFoundException("Cannot find class with typId,target class:KeyValue[value]" + ",typeId:" + itemValueTypeId);
            }
            value = SerializerFactory.GetSerializer(itemValueType).ReadObject(inStream, itemValueType);
        }
        GKeyValuePair kv = new GKeyValuePair(key, value);
        inStream.SetRef(hashcode, kv);
        return kv;
    }
}
