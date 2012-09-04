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

import com.bj58.spat.gaea.serializer.component.GaeaInStream;
import com.bj58.spat.gaea.serializer.component.GaeaOutStream;
import com.bj58.spat.gaea.serializer.component.exception.ClassNoMatchException;
import com.bj58.spat.gaea.serializer.component.exception.StreamException;
import com.bj58.spat.gaea.serializer.component.helper.TypeHelper;
import com.bj58.spat.gaea.serializer.serializer.SerializerBase;

import java.util.Iterator;
import java.util.Map;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * MapSerializer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class MapSerializer extends SerializerBase {

    @Override
    public void WriteObject(Object obj, GaeaOutStream outStream) throws Exception {
        if (obj == null) {
            SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
        }
        int typeId = TypeHelper.GetTypeId(Map.class);
        outStream.WriteInt32(typeId);
        if (outStream.WriteRef(obj)) {
            return;
        }
        Map map = (Map) obj;
        outStream.WriteInt32(map.size());
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            Class keyType = entry.getKey().getClass();
            int keyTypeId = TypeHelper.GetTypeId(keyType);
            outStream.WriteInt32(keyTypeId);
            SerializerFactory.GetSerializer(keyType).WriteObject(entry.getKey(), outStream);

            Object value = entry.getValue();
            if (value == null) {
                SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
            } else {
                Class valueType = value.getClass();
                int valueTypeId = TypeHelper.GetTypeId(valueType);
                outStream.WriteInt32(valueTypeId);
                SerializerFactory.GetSerializer(valueType).WriteObject(value, outStream);
            }
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
        int len = inStream.ReadInt32();
        if (len > inStream.MAX_DATA_LEN) {
            throw new StreamException("Data length overflow.");
        }
        Class type = TypeHelper.GetType(typeId);
        if (type == null) {
            throw new ClassNotFoundException("Cannot find class with typId,target class:" + defType.getName() + ",typeId:" + typeId);
        }
        if (type != Map.class) {
            throw new ClassNoMatchException("Class must be map!type:" + type.getName());
        }
        int modifier = defType.getModifiers();
        if (!Modifier.isAbstract(modifier) && !Modifier.isInterface(modifier) && Map.class.isAssignableFrom(defType)) {
            type = defType;
        } else {
            type = HashMap.class; //default map type
            if (!defType.isAssignableFrom(type)) {
                throw new ClassNoMatchException("Defind type and value type not match !defind type:" + defType.getName() + ",value type:" + type.getName());
            }
        }
        Map map = (Map) type.newInstance();
        for (int i = 0; i < len; i++) {
            int keyTypeId = inStream.ReadInt32();
            Class keyType = TypeHelper.GetType(keyTypeId);
            if (keyType == null) {
                throw new ClassNotFoundException("Cannot find class with typId,target class:map[key]" + ",typeId:" + keyTypeId);
            }
            Object key = SerializerFactory.GetSerializer(keyType).ReadObject(inStream, keyType);

            int valueTypeId = inStream.ReadInt32();
            Class valueType = TypeHelper.GetType(valueTypeId);
            Object value = null;
            if (valueType != null) {
            	value = SerializerFactory.GetSerializer(valueType).ReadObject(inStream, valueType);
            }
            
            map.put(key, value);
        }
        inStream.SetRef(hashcode, map);
        return map;
    }
}
