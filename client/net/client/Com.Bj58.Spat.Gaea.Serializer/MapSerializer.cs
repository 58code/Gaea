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
using System.IO;
using System.Linq;
using System.Text;
using System.Collections;
using System.Collections.Generic;
using Com.Bj58.Spat.Gaea.Serializer.Component.Attributes;
using Com.Bj58.Spat.Gaea.Serializer.Component;
using Com.Bj58.Spat.Gaea.Serializer.Component.Exceptions;

namespace Com.Bj58.Spat.Gaea.Serializer
{
    internal class MapSerializer : SerializerBase
    {
        public override void WriteObject(object obj, GaeaStream outStream)
        {
            if (obj == null)
                SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
            IDictionary dic = (IDictionary)obj;
            Type type = typeof(IDictionary);
            uint typeId = type.GetTypeId();
            outStream.WriteUInt32(typeId);
            if (outStream.WriteRef(obj))
            {
                return;
            }
            outStream.WriteInt32(dic.Count);
            foreach (DictionaryEntry item in dic)
            {
                var keyType = item.Key.GetType();
                outStream.Write(keyType.GetTypeId().GetBytes(), 0, 4);
                SerializerFactory.GetSerializer(keyType).WriteObject(item.Key, outStream);
                if (item.Value == null)
                {
                    SerializerFactory.GetSerializer(null).WriteObject(item.Value, outStream);
                }
                else
                {
                    var valueType = item.Value.GetType();
                    outStream.Write(valueType.GetTypeId().GetBytes(), 0, 4);
                    SerializerFactory.GetSerializer(valueType).WriteObject(item.Value, outStream);
                }
            }
        }

        public override object ReadObject(GaeaStream inStream,Type defType)
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
            var type = typeId.ToType();
            if (type == null)
            {
                throw new NotFindTypeException("Type id can't convert to type !typeId:" + typeId);
            }
            if (type != typeof(IDictionary))
            {
                throw new TypeNoMatchException("Type must be IDictionary!type:" + type.FullName);
            }
            if (!defType.IsAbstract && !defType.IsInterface && typeof(IDictionary).IsAssignableFrom(defType))
            {
                type = defType;
            }
            else
            {
                if (defType.IsGenericType)
                {
                    type = typeof(Dictionary<,>).MakeGenericType(defType.GetGenericArguments());
                }
                else
                {
                    type = typeof(Hashtable);
                }
                if (!defType.IsAssignableFrom(type))
                {
                    throw new TypeNoMatchException("Defind type and value type not match !defind type:" + defType.FullName + ",value type:" + type.FullName);
                }
            }
            var len = inStream.ReadInt32();
            IDictionary obj = (IDictionary)Activator.CreateInstance(type, true);
            Type[] gtypes = null;
            if (type.IsGenericType)
            {
                gtypes = type.GetGenericArguments();
            }
            for (int i = 0; i < len; i++)
            {
                var keyTypeId = inStream.ReadUInt32();
                var keyType = keyTypeId.ToType();
                var dkeyType = keyType;
                if (type.IsGenericType)
                {
                    dkeyType = gtypes[0];
                }
                object key = SerializerFactory.GetSerializer(keyTypeId.ToType()).ReadObject(inStream, dkeyType);
                byte[] valueTypeBuffer = new byte[4];
                inStream.SafeRead(valueTypeBuffer, 0, 4);
                var valueTypeId = valueTypeBuffer.ToUInt32(0);
                if (valueTypeId == 0)
                {
                    obj[key] = null;
                }
                else
                {
                    var valueType = valueTypeId.ToType();
                    var dvalueType = valueType;
                    if (type.IsGenericType)
                        dvalueType = gtypes[1];
                    object value = SerializerFactory.GetSerializer(valueTypeId.ToType()).ReadObject(inStream, dvalueType);
                    obj[key] = value;
                }
            }
            inStream.SetRef(hashcode, obj);
            return obj;
        }
    }
}
