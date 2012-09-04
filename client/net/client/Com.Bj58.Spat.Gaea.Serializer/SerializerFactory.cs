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
//----------------------------------------------------------------------
//<Copyright company="58.com">
//      Team:SPAT
//      Blog:http://blog.58.com/spat/  
//      Website:http://www.58.com
//</Copyright>
//-----------------------------------------------------------------------
using System;
using System.Linq;
using System.Text;
using System.Collections;
using System.Collections.Generic;
using Com.Bj58.Spat.Gaea.Serializer.Component;

namespace Com.Bj58.Spat.Gaea.Serializer
{
    public static class SerializerFactory 
    {
        private static SerializerBase arraySerializer = new ArraySerializer();
        private static SerializerBase boolSerializer = new BooleanSerializer();
        private static SerializerBase byteSerializer = new ByteSerializer();
        private static SerializerBase charSerializer = new CharSerializer();
        private static SerializerBase dateTimeSerializer = new DateTimeSerizlizer();
        private static SerializerBase decimalSerializer = new DecimalSerizlizer();
        private static SerializerBase doubleSerializer = new DoubleSerializer();
        private static SerializerBase enumSerializer = new EnumSerializer();
        private static SerializerBase floatSerializer = new FloatSerializer();
        private static SerializerBase int16Serializer = new Int16Serializer();
        private static SerializerBase int32Serializer = new Int32Serializer();
        private static SerializerBase int64Serializer = new Int64Serializer();
        private static SerializerBase keyValueSerializer = new KeyValueSerializer();
        private static SerializerBase listSerializer = new ListSerializer();
        private static SerializerBase mapSerializer = new MapSerializer();
        private static SerializerBase nullSerializer = new NullSerializer();
        private static SerializerBase objectSerializer = new ObjectSerializer();
        private static SerializerBase stringSerializer = new StringSerializer();
      

        public static SerializerBase GetSerializer(Type type)
        {
            if (type == null)
            {
                return nullSerializer;
            }
            else if (type.IsEnum)
            {
                return enumSerializer;
            }
            uint typeId = type.GetTypeId();
            SerializerBase serializer = null;
            switch (typeId)
            {
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
                    break;
            }
            return serializer;
        }
    }
}
