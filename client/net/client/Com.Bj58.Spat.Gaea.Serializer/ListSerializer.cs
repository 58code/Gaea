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
using Com.Bj58.Spat.Gaea.Serializer.Component;
using Com.Bj58.Spat.Gaea.Serializer.Component.Exceptions;

namespace Com.Bj58.Spat.Gaea.Serializer
{
    internal class ListSerializer : SerializerBase
    {
        public override void WriteObject(object obj, GaeaStream outStream)
        {
            if (obj == null)
                SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
            IList list = (IList)obj;
            Type type = typeof(IList);
            uint typeId = type.GetTypeId();
            outStream.WriteUInt32(typeId);
            if (outStream.WriteRef(obj))
            {
                return;
            }
            outStream.WriteInt32(list.Count);
            foreach (var item in list)
            {
                if (item == null)
                {
                    SerializerFactory.GetSerializer(null).WriteObject(item, outStream);
                }
                else
                {
                    var itemType = item.GetType();
                    outStream.Write(itemType.GetTypeId().GetBytes(), 0, 4);
                    SerializerFactory.GetSerializer(itemType).WriteObject(item, outStream);
                }
            }
        }

        public override object ReadObject(GaeaStream inStream, Type defType)
        {
            var typeId = inStream.ReadUInt32();
            if (typeId == 0)
                return null;
            byte isRef = (byte)inStream.ReadByte();
            int hashcode = inStream.ReadInt32();
            IList obj;
            if (isRef > 0)
            {
                obj = (IList)inStream.GetRef(hashcode);
            }
            else
            {
                var type = typeId.ToType();
                if (type == null)
                {
                    throw new TypeNoMatchException("Type id can't convert to type !typeId:" + typeId);
                }
                if (type != typeof(IList))
                {
                    throw new TypeNoMatchException("Type must be IList!type:" + type.FullName);
                }
                if (!defType.IsAbstract && !defType.IsInterface && typeof(IList).IsAssignableFrom(defType))
                {
                    type = defType;
                }
                else
                {
                    if (defType.IsGenericType)
                    {
                        var gtypes = defType.GetGenericArguments();
                        if (gtypes.Length != 1)
                        {
                            throw new TypeNoMatchException("Defind type Generic parameters length error!deftype:" + defType.FullName);
                        }
                        type = typeof(List<>).MakeGenericType(gtypes);
                    }
                    else
                    {
                        type = typeof(ArrayList);
                    }
                    if (!defType.IsAssignableFrom(type))
                    {
                        throw new TypeNoMatchException("Defind type and value type not match !defind type:" + defType.FullName + ",value type:" + type.FullName); 
                    }
                }               
                var len = inStream.ReadInt32();
                obj = (IList)Activator.CreateInstance(type, true);
                for (int i = 0; i < len; i++)
                {
                    var itemTypeId = inStream.ReadUInt32();
                    if (itemTypeId == 0)
                    {
                        obj.Add(null);

                    }
                    else
                    {
                        var itemType = itemTypeId.ToType();
                        if (itemType == null)
                        {
                            throw new NotFindTypeException("Not find the type from current application.typeId:" + itemTypeId);
                        }
                        Type dType = itemType;//item define type
                        if (type.IsGenericType)
                        {
                            dType = type.GetGenericArguments()[0];
                        }
                        object value = SerializerFactory.GetSerializer(dType).ReadObject(inStream, dType);
                        obj.Add(value);
                    }
                }
                inStream.SetRef(hashcode, obj);
            }

            return obj;
        }
    }
}
