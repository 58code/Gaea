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
using System.Reflection;
using System.Collections.Generic;
using Com.Bj58.Spat.Gaea.Serializer.Component;
using Com.Bj58.Spat.Gaea.Serializer.Component.Attributes;
using Com.Bj58.Spat.Gaea.Serializer.Component.Exceptions;

namespace Com.Bj58.Spat.Gaea.Serializer
{
    internal class ObjectSerializer : SerializerBase
    {
        private static Dictionary<Type, TypeInfo> Cache = new Dictionary<Type, TypeInfo>();
        public override void WriteObject(object obj, GaeaStream outStream)
        {
            if (obj == null)
            {
                SerializerFactory.GetSerializer(null).WriteObject(null, outStream);
                return;
            }
            Type type = obj.GetType();
            TypeInfo typeInfo = GetTypeInfo(type);
            var typId = typeInfo.TypeId.GetBytes();
            outStream.Write(typId, 0, 4);
            if (outStream.WriteRef(obj))
                return;
            foreach (var p in typeInfo.Properies)
            {
                var value = p.GetValue(obj, null);
                if (value == null)
                {
                    SerializerFactory.GetSerializer(null).WriteObject(value, outStream);
                }
                else
                {
                    outStream.WriteUInt32(value.GetType().GetTypeId());
                    if (value is IGaeaSerializer)
                    {
                        ((IGaeaSerializer)value).Serialize(outStream);
                    }
                    else
                    {
                        try
                        {
                            SerializerFactory.GetSerializer(value.GetType()).WriteObject(value, outStream);
                        }
                        catch (Exception err)
                        {
                            var e = new SerializeException(err);
                            e.Type = type;
                            e.PropertyName = p.Name;
                            throw e;
                        }

                    }
                }
            }
        }

        public override object ReadObject(GaeaStream inStream,Type defType)
        {
            uint typeId = inStream.ReadUInt32();
            if (typeId == 0)
                return null;
            Type type = typeId.ToType();
            if (type == null)
            {
                throw new NotFindTypeException("Not find the type from current application.type:" + defType.ToString() + ".typeId:" + typeId);
            }
            if (type.IsGenericType)
                type = type.MakeGenericType(defType.GetGenericArguments());
            int fbyte = inStream.ReadByte();
            int hashcode = inStream.ReadInt32();
            if (fbyte > 0)
            {
                return inStream.GetRef(hashcode);
            }
            TypeInfo typeInfo = GetTypeInfo(type);
            object obj = Activator.CreateInstance(type, true);
            foreach (var p in typeInfo.Properies)
            {
                var ptypeId = inStream.ReadUInt32();
                if (ptypeId == 0)
                {
                    p.SetValue(obj, null, null);
                    continue;
                }
                Type t = ptypeId.ToType();
                if (t == null)
                {
                    throw new NotFindTypeException("Not find the type from current application.type:" + p.PropertyType.Name + ",propery name:" + p.Name);
                }
                if (t.GetInterface("IGaeaSerializer") != null)
                {
                    var value = ((IGaeaSerializer)Activator.CreateInstance(t));
                    value.Derialize(inStream);
                    p.SetValue(obj, value, null);
                }
                else
                {
                    try
                    {
                        var value = SerializerFactory.GetSerializer(t).ReadObject(inStream, p.PropertyType);
                        object pValue = value;
                        if (p.PropertyType == typeof(byte))
                        {
                            pValue = Convert.ToByte(value);
                        }
                        else if (p.PropertyType == typeof(int))
                        {
                            pValue = Convert.ToInt32(value);
                        }
                        else if (p.PropertyType == typeof(long))
                        {
                            pValue = Convert.ToInt64(value);
                        }
                        p.SetValue(obj, pValue, null);
                    }
                    catch (Exception err)
                    {
                        var e = new SerializeException(err);
                        e.Type = type;
                        e.PropertyName = p.Name;
                        throw e;
                    }
                }
            }
            inStream.SetRef(hashcode, obj);
            return obj;
        }

        #region private method
        private TypeInfo GetTypeInfo(Type type)
        {
            TypeInfo typeInfo;
            if (!Cache.TryGetValue(type, out typeInfo))
            {
                lock (Cache)
                {
                    if (Cache.TryGetValue(type, out typeInfo))
                        return typeInfo;
                    uint typeId = type.GetTypeId();
                    typeInfo = new TypeInfo(typeId);
                    var ps = type.GetProperties();
                    var tAtts = type.GetCustomAttributes(typeof(GaeaSerializableAttribute), true);
                    if (tAtts == null || tAtts.Length == 0)
                        throw new DisallowedSerializeException();
                    var tAtt = (GaeaSerializableAttribute)tAtts[0];
                    Dictionary<int, PropertyInfo> dicPs = new Dictionary<int, PropertyInfo>();
                    List<int> indexIds = new List<int>();
                    if (tAtt.DefaultAll)
                    {
                        foreach (var p in ps)
                        {
                            var attMembers = p.GetCustomAttributes(typeof(GaeaNotMemberAttribute), false);
                            if (attMembers.Length == 0)
                            {
                                int indexId = p.Name.ToLower().GaeaHashCode();
                                dicPs[indexId] = p;
                                indexIds.Add(indexId);
                            }
                        }
                    }
                    else
                    {
                        foreach (var p in ps)
                        {
                            var attMembers = p.GetCustomAttributes(typeof(GaeaMemberAttribute), false);
                            if (attMembers.Length > 0)
                            {
                                string name = ((GaeaMemberAttribute)attMembers[0]).Name;
                                if (string.IsNullOrEmpty(name))
                                    name = p.Name;
                                int indexId = (name.ToLower().GaeaHashCode());
                                dicPs[indexId] = p;
                                indexIds.Add(indexId);
                            }
                        }
                    }
                    int len = indexIds.Count;
                    for (int i = 0; i < len; i++)
                    {
                        for (int j = i + 1; j < len; j++)
                        {
                            int item = indexIds[j];
                            if (indexIds[i] > item)
                            {
                                indexIds[j] = indexIds[i];
                                indexIds[i] = item;
                            }
                        }
                    }
                    foreach (var index in indexIds)
                    {
                        typeInfo.Properies.Add(dicPs[index]);
                    }
                    Cache[type] = typeInfo;
                }
            }
            return typeInfo;
        }
        #endregion
    }
}
