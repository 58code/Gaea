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

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * ListSerializer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class ListSerializer extends SerializerBase {

    @Override
    public void WriteObject(Object obj, GaeaOutStream outStream) throws Exception {
        if (obj == null) {
            SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
        }
        List list = (List) obj;
        int typeId = TypeHelper.GetTypeId(List.class);
        outStream.WriteInt32(typeId);
        if (outStream.WriteRef(obj)) {
            return;
        }
        outStream.WriteInt32(list.size());
        for (Object item : list) {
            if (item == null) {
                SerializerFactory.GetSerializer(null).WriteObject(item, outStream);
            } else {
                Class itemType = item.getClass();
                outStream.WriteInt32(TypeHelper.GetTypeId(itemType));
                SerializerFactory.GetSerializer(itemType).WriteObject(item, outStream);
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
        if (type != List.class) {
            throw new ClassNoMatchException("Class must be list!type:" + type.getName());
        }
        int modifier = defType.getModifiers();
        if (!Modifier.isAbstract(modifier) && !Modifier.isInterface(modifier) && List.class.isAssignableFrom(defType)) {
            type = defType;
        } else {
            type = ArrayList.class; //default list type
            if (!defType.isAssignableFrom(type)) {
                throw new ClassNoMatchException("Defind type and value type not match !defind type:" + defType.getName() + ",value type:" + type.getName());
            }
        }
        List list = (List) type.newInstance();
        for (int i = 0; i < len; i++) {
            int itemTypeId = inStream.ReadInt32();
            if (itemTypeId == 0) {
                list.add(null);
            } else {
                Class itemType = TypeHelper.GetType(itemTypeId);
                if (itemType == null) {
                    throw new ClassNotFoundException("Cannot find class with typId,target class:(list[item])" + ",typeId:" + itemTypeId);
                }
                Object value = SerializerFactory.GetSerializer(itemType).ReadObject(inStream, itemType);
                list.add(value);
            }
        }
        inStream.SetRef(hashcode, list);
        return list;
    }
}
