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
import com.bj58.spat.gaea.serializer.component.helper.TypeHelper;
import com.bj58.spat.gaea.serializer.serializer.SerializerBase;

/**
 * EnumSerializer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class EnumSerializer extends SerializerBase {

    @Override
    public void WriteObject(Object obj, GaeaOutStream outStream) throws Exception {
        int typeId = TypeHelper.GetTypeId(obj.getClass());
        outStream.WriteInt32(typeId);
        String value = obj.toString();
        SerializerFactory.GetSerializer(String.class).WriteObject(value, outStream);
    }

    @Override
    public Object ReadObject(GaeaInStream inStream, Class defType) throws Exception {
        int typeId = inStream.ReadInt32();
        Class type = TypeHelper.GetType(typeId);
        if (type == null) {
            throw new ClassNotFoundException("Cannot find class with typId,target class:" + defType.getName() + ",typeId:" + typeId);
        }
        String value = (String) SerializerFactory.GetSerializer(String.class).ReadObject(inStream, defType);
        return Enum.valueOf(type, value);
    }
}
