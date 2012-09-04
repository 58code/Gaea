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
import com.bj58.spat.gaea.serializer.component.annotation.GaeaMember;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaNotMember;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;
import com.bj58.spat.gaea.serializer.component.exception.ClassNoMatchException;
import com.bj58.spat.gaea.serializer.component.exception.DisallowedSerializeException;
import com.bj58.spat.gaea.serializer.component.helper.StrHelper;
import com.bj58.spat.gaea.serializer.component.helper.TypeHelper;
import com.bj58.spat.gaea.serializer.serializer.IGaeaSerializer;
import com.bj58.spat.gaea.serializer.serializer.SerializerBase;
import com.bj58.spat.gaea.serializer.serializer.TypeInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ObjectSerializer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class ObjectSerializer extends SerializerBase {

    @Override
    public void WriteObject(Object obj, GaeaOutStream outStream) throws Exception {
        if (obj == null) {
            SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
            return;
        }
        Class<?> type = obj.getClass();
        TypeInfo typeInfo = GetTypeInfo(type);
        outStream.WriteInt32(typeInfo.TypeId);
        if (outStream.WriteRef(obj)) {
            return;
        }
        for (Field f : typeInfo.Fields) {
            Object value = f.get(obj);
            if (value == null) {
                SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
            } else {
                if (value instanceof IGaeaSerializer) {
                    ((IGaeaSerializer) value).Serialize(outStream);
                } else {
                    Class valueType = value.getClass();
                    outStream.WriteInt32(TypeHelper.GetTypeId(valueType));
                    SerializerFactory.GetSerializer(valueType).WriteObject(value, outStream);
                }
            }
        }
    }

    @Override
    public Object ReadObject(GaeaInStream inStream, Class defType) throws Exception {
        int typeId = inStream.ReadInt32();
        if (typeId == 0) {
            return null;
        }
        Class type = TypeHelper.GetType(typeId);
        if (type == null) {
            throw new ClassNotFoundException("Cannot find class with typId,target class:" + defType.getName() + ",typeId:" + typeId);
        }
        if (!defType.isAssignableFrom(type) && defType != type) {
            throw new ClassNoMatchException("Class not match!class:" + type.getName() + ",require " + defType.getName());
        }
        byte isRef = (byte) inStream.read();
        int hashcode = inStream.ReadInt32();
        if (isRef > 0) {
            return inStream.GetRef(hashcode);
        }
        TypeInfo typeInfo = GetTypeInfo(type);
        Object obj = type.newInstance();
        for (Field f : typeInfo.Fields) {
            int ptypeId = inStream.ReadInt32();
            if (ptypeId == 0) {
                f.set(obj, null);
                continue;
            }
            Class ptype = TypeHelper.GetType(ptypeId);
            if (ptype == null) {
                throw new ClassNotFoundException("Cannot find class with typId,target class: " + f.getType().getName() + ",typeId:" + ptypeId);
            }
            if (IGaeaSerializer.class.isAssignableFrom(ptype)) {
                IGaeaSerializer value = (IGaeaSerializer) ptype.newInstance();
                value.Derialize(inStream);
                f.set(obj, value);
            } else {
                Object value = SerializerFactory.GetSerializer(ptype).ReadObject(inStream, f.getType());
                f.set(obj, value);
            }
        }
        inStream.SetRef(hashcode, obj);
        return obj;
    }
    private static Map<Class<?>, TypeInfo> TypeInfoMap = new HashMap<Class<?>, TypeInfo>();

    private TypeInfo GetTypeInfo(Class<?> type) throws ClassNotFoundException, DisallowedSerializeException {
        if (TypeInfoMap.containsKey(type)) {
            return TypeInfoMap.get(type);
        }
        GaeaSerializable cAnn = type.getAnnotation(GaeaSerializable.class);
        if (cAnn == null) {
            throw new DisallowedSerializeException();
        }
        int typeId = TypeHelper.GetTypeId(type);
        TypeInfo typeInfo = new TypeInfo(typeId);
        ArrayList<Field> fields = new ArrayList<Field>();
        Class temType = type;
        while (true) {
            Field[] fs = temType.getDeclaredFields();
            for (Field f : fs) {
                fields.add(f);
            }
            Class superClass = temType.getSuperclass();
            if (superClass == null) {
                break;
            }
            temType = superClass;
        }

        Map<Integer, Field> mapFildes = new HashMap<Integer, Field>();
        List<Integer> indexIds = new ArrayList<Integer>();
        if (cAnn.defaultAll()) {
            for (Field f : fields) {
                GaeaNotMember ann = f.getAnnotation(GaeaNotMember.class);
                if (ann != null) {
                    continue;
                }
                f.setAccessible(true);
                Integer indexId = StrHelper.GetHashcode(f.getName().toLowerCase());
                mapFildes.put(indexId, f);
                indexIds.add(indexId);
            }
        } else {
            for (Field f : fields) {
                GaeaMember ann = f.getAnnotation(GaeaMember.class);
                if (ann == null) {
                    continue;
                }
                f.setAccessible(true);
                String name = ann.name();

                if (ann.name() == null || ann.name().length() == 0) {
                    name = f.getName();
                }
                /*
                 * 2011-6-28修改，支持服务器端增加字段客户端不需要更新功能
                 */
                Integer indexId = 0;
                if(name.startsWith("#")){
                    indexId = Integer.MAX_VALUE;
                }else{
                    indexId = StrHelper.GetHashcode(name.toLowerCase());
                }
                mapFildes.put(indexId, f);
                indexIds.add(indexId);
            }
        }
        int len = indexIds.size();
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j < len; j++) {
                Integer item = indexIds.get(j);
                if (indexIds.get(i) > item) {
                    indexIds.set(j, indexIds.get(i));
                    indexIds.set(i, item);
                }
            }
        }
        for (Integer index : indexIds) {
            typeInfo.Fields.add(mapFildes.get(index));
        }
        TypeInfoMap.put(type, typeInfo);
        return typeInfo;
    }
}

class TypeInfo {

    public int TypeId;

    public TypeInfo(int typeId) {
        TypeId = typeId;
    }
    public List<Field> Fields = new ArrayList<Field>();
}
