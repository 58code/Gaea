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
using Com.Bj58.Spat.Gaea.Serializer.Component.Exceptions;

namespace Com.Bj58.Spat.Gaea.Serializer
{
    internal class ArraySerializer : SerializerBase
    {
        public override void WriteObject(object obj, GaeaStream outStream)
        {
            if (obj == null)
            {
                SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
                return;
            }
            Array array = (Array)obj;
            Type t = obj.GetType();
            Type type = t.GetElementType();
            uint typeId = type.GetTypeId();
            outStream.WriteUInt32(typeId);
            if (outStream.WriteRef(obj))
            {
                return;
            }
            outStream.WriteInt32(array.Length);
            if (t == (typeof(byte)))
            {
                outStream.Write((byte[])obj, 0, array.Length);
                return;
            }
            if (!type.IsPrimitives())
            {
                foreach (var item in array)
                {
                    if (item == null)
                    {
                        SerializerFactory.GetSerializer(null).WriteObject(item, outStream); //if item is null write 0x00000000 to type
                    }
                    else
                    {
                        Type itemT = item.GetType();
                        uint itemTypeId = itemT.GetTypeId();
                        outStream.WriteUInt32(itemTypeId);
                        SerializerFactory.GetSerializer(itemT).WriteObject(item, outStream);
                    }
                }
            }
            else
            {
                foreach (var item in array)
                {
                    SerializerFactory.GetSerializer(type).WriteObject(item, outStream);
                }
            }
        }

        public override object ReadObject(GaeaStream inStream,Type defType)
        {
            uint typeId = inStream.ReadUInt32();
            if (typeId == 0)
                return null;
            byte isRef = (byte)inStream.ReadByte();
            int hashCode = inStream.ReadInt32();
            if (isRef > 0)
            {
                return inStream.GetRef(hashCode);
            }
            int len = inStream.ReadInt32();
            Type type = typeId.ToType();
            if (type == typeof(byte))
            {
                byte[] buffer = new byte[len];
                inStream.SafeRead(buffer, 0, len);
                return buffer;
            }
            if (type.IsGenericType)
            {
                type = GenericHelper.GetRealGenType(type, defType.GetElementType());
            }
            var array = Array.CreateInstance(type, len);
            if (!type.IsPrimitive)
            {
                for (int i = 0; i < len; i++)
                {
                    var itemTypeId = inStream.ReadUInt32();
                    if (itemTypeId == 0)
                    {
                        array.SetValue(null, i);
                    }
                    else
                    {
                        Type itemType = itemTypeId.ToType();
                        var value = SerializerFactory.GetSerializer(itemType).ReadObject(inStream, defType.GetElementType());
                        array.SetValue(value, i);
                    }
                }
            }
            else
            {
                for (int i = 0; i < len; i++)
                {
                    var value = SerializerFactory.GetSerializer(type).ReadObject(inStream,defType.GetElementType());
                    array.SetValue(value, i);
                }
            }
            inStream.SetRef(hashCode, array);
            return array;
        }
    }
}
