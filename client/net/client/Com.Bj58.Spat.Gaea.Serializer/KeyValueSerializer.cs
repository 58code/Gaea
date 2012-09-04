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
using System.IO;
using System.Collections.Generic;
using Com.Bj58.Spat.Gaea.Serializer.Component;

namespace Com.Bj58.Spat.Gaea.Serializer
{
    internal class KeyValueSerializer : SerializerBase
    {
        public override void WriteObject(object obj, GaeaStream outStream)
        {
            if (obj == null)
                SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
            Type type = obj.GetType();
            uint typeId = type.GetTypeId();
            outStream.WriteUInt32(typeId);
            if (outStream.WriteRef(obj))
            {
                return;
            }
            object key = type.GetProperty("Key").GetValue(obj, null);
            object value = type.GetProperty("Value").GetValue(obj, null);
            var keyType = key.GetType();
            outStream.Write(keyType.GetTypeId().GetBytes(), 0, 4);
            SerializerFactory.GetSerializer(keyType).WriteObject(key, outStream);
            if (value == null)
            {
                SerializerFactory.GetSerializer(null).WriteObject(value, outStream);
            }
            else
            {
                var valueType = value.GetType();
                outStream.Write(valueType.GetTypeId().GetBytes(), 0, 4);
                SerializerFactory.GetSerializer(valueType).WriteObject(value, outStream);
            }
        }

        public override object ReadObject(GaeaStream inStream, Type defType)
        {
            var typeId = inStream.ReadUInt32();
            if (typeId == 0)
                return null;
            byte isRef = (byte)inStream.ReadByte();
            int hashcode = inStream.ReadInt32();
            if (isRef > 0)
            {
                return inStream.GetRef(hashcode);
            }
            var type = defType;
            var keyTypeId = inStream.ReadUInt32();
            var keyType = keyTypeId.ToType();
            object key = SerializerFactory.GetSerializer(keyType).ReadObject(inStream,defType.GetGenericArguments()[0]);

            object value = null;
            var valueTypeId = inStream.ReadUInt32();
            var valueType = valueTypeId.ToType();
            if (valueTypeId != 0)
            {
                value = SerializerFactory.GetSerializer(valueType).ReadObject(inStream, defType.GetGenericArguments()[1]);
            }
            object obj = Activator.CreateInstance(type, key, value);
            inStream.SetRef(hashcode, obj);
            return obj;
        }
    }
}
