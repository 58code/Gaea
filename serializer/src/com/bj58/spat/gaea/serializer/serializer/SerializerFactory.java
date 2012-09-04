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

import com.bj58.spat.gaea.serializer.component.exception.DisallowedSerializeException;
import com.bj58.spat.gaea.serializer.component.helper.TypeHelper;
import com.bj58.spat.gaea.serializer.serializer.ArraySerializer;
import com.bj58.spat.gaea.serializer.serializer.BooleanSerializer;
import com.bj58.spat.gaea.serializer.serializer.ByteSerializer;
import com.bj58.spat.gaea.serializer.serializer.CharSerializer;
import com.bj58.spat.gaea.serializer.serializer.DateTimeSerializer;
import com.bj58.spat.gaea.serializer.serializer.DecimalSerializer;
import com.bj58.spat.gaea.serializer.serializer.DoubleSerializer;
import com.bj58.spat.gaea.serializer.serializer.EnumSerializer;
import com.bj58.spat.gaea.serializer.serializer.FloatSerializer;
import com.bj58.spat.gaea.serializer.serializer.Int16Serializer;
import com.bj58.spat.gaea.serializer.serializer.Int32Serializer;
import com.bj58.spat.gaea.serializer.serializer.Int64Serializer;
import com.bj58.spat.gaea.serializer.serializer.KeyValueSerializer;
import com.bj58.spat.gaea.serializer.serializer.ListSerializer;
import com.bj58.spat.gaea.serializer.serializer.MapSerializer;
import com.bj58.spat.gaea.serializer.serializer.NullSerializer;
import com.bj58.spat.gaea.serializer.serializer.ObjectSerializer;
import com.bj58.spat.gaea.serializer.serializer.SerializerBase;
import com.bj58.spat.gaea.serializer.serializer.StringSerializer;

/**
 * SerializerFactory
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class SerializerFactory {

    private final static SerializerBase arraySerializer = new ArraySerializer();
    private final static SerializerBase boolSerializer = new BooleanSerializer();
    private final static SerializerBase byteSerializer = new ByteSerializer();
    private final static SerializerBase charSerializer = new CharSerializer();
    private final static SerializerBase dateTimeSerializer = new DateTimeSerializer();
    private final static SerializerBase decimalSerializer = new DecimalSerializer();
    private final static SerializerBase doubleSerializer = new DoubleSerializer();
    private final static SerializerBase enumSerializer = new EnumSerializer();
    private final static SerializerBase floatSerializer = new FloatSerializer();
    private final static SerializerBase int16Serializer = new Int16Serializer();
    private final static SerializerBase int32Serializer = new Int32Serializer();
    private final static SerializerBase int64Serializer = new Int64Serializer();
    private final static SerializerBase keyValueSerializer = new KeyValueSerializer();
    private final static SerializerBase listSerializer = new ListSerializer();
    private final static SerializerBase mapSerializer = new MapSerializer();
    private final static SerializerBase nullSerializer = new NullSerializer();
    private final static SerializerBase objectSerializer = new ObjectSerializer();
    private final static SerializerBase stringSerializer = new StringSerializer();

    public static SerializerBase GetSerializer(Class<?> type) throws ClassNotFoundException, DisallowedSerializeException {
        if (type == null) {
            return nullSerializer;
        } else if (type.isEnum()) {
            return enumSerializer;
        }
        int typeId = TypeHelper.GetTypeId(type);
        SerializerBase serializer = null;
        switch (typeId) {
            case 0:
            case 1:
                serializer = nullSerializer;
                break;
            case 2:
                serializer = objectSerializer;
                break;
            case 3:
                serializer = boolSerializer;
                break;
            case 4:
                serializer = charSerializer;
                break;
            case 5:
            case 6:
                serializer = byteSerializer;
                break;
            case 7:
            case 8:
                serializer = int16Serializer;
                break;
            case 9:
            case 10:
                serializer = int32Serializer;
                break;
            case 11:
            case 12:
                serializer = int64Serializer;
                break;
            case 13:
                serializer = floatSerializer;
                break;
            case 14:
                serializer = doubleSerializer;
                break;
            case 15:
                serializer = decimalSerializer;
                break;
            case 16:
                serializer = dateTimeSerializer;
                break;
            case 18:
                serializer = stringSerializer;
                break;
            case 19:
            case 20:
            case 21:
                serializer = listSerializer;
                break;
            case 22:
                serializer = keyValueSerializer;
                break;
            case 23:
                serializer = arraySerializer;
                break;
            case 24:
            case 25:
                serializer = mapSerializer;
                break;
            default:
                serializer = objectSerializer;
        }
        return serializer;
    }
}
