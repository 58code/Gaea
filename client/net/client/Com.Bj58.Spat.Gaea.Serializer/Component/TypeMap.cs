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
using System.Reflection;
using System.Collections;
using System.Collections.Generic;
using Com.Bj58.Spat.Gaea.Serializer;
using Com.Bj58.Spat.Gaea.Serializer.Component.Attributes;
using Com.Bj58.Spat.Gaea.Serializer.Component.Exceptions;

namespace Com.Bj58.Spat.Gaea.Serializer.Component
{
    public static class TypeMap
    {
        private static Dictionary<Type, ClassItem> TypeIdMap = new Dictionary<Type, ClassItem>();
        private static Dictionary<uint, ClassItem> IdTypeMap = new Dictionary<uint, ClassItem>();
        static TypeMap()
        {
            IList<ClassItem> ClassList = new List<ClassItem>();
            ClassList.Add(new ClassItem(1,typeof(DBNull)));
            ClassList.Add(new ClassItem(2,typeof(object)));
            ClassList.Add(new ClassItem(3,typeof(bool)));
            ClassList.Add(new ClassItem(4,typeof(char)));
            ClassList.Add(new ClassItem(5,typeof(byte),typeof(SByte)));
            ClassList.Add(new ClassItem(7,typeof(short),typeof(UInt16)));
            ClassList.Add(new ClassItem(9,typeof(int),typeof(UInt32)));
            ClassList.Add(new ClassItem(11,typeof(long),typeof(UInt64)));
            ClassList.Add(new ClassItem(13,typeof(float)));
            ClassList.Add(new ClassItem(14,typeof(double)));
            ClassList.Add(new ClassItem(15,typeof(decimal)));
            ClassList.Add(new ClassItem(16,typeof(DateTime)));
            ClassList.Add(new ClassItem(18,typeof(string)));
            ClassList.Add(new ClassItem(19,typeof(IList)));
            ClassList.Add(new ClassItem(22,typeof(KeyValuePair<,>)));
            ClassList.Add(new ClassItem(23,typeof(Array)));
            ClassList.Add(new ClassItem(24,typeof(IDictionary)));
            foreach (ClassItem item in ClassList)
            {
                uint id = item.TypeId;
                foreach(Type t in item.Types)
                {
                    TypeIdMap[t] = item;
                }
                IdTypeMap[id] = item;
            }
            LoadCustemType();
        }

        private static void LoadCustemType()
        {
            var assemblies = AppDomain.CurrentDomain.GetAssemblies();
            foreach (var assembly in assemblies)
            {
                Module[] modules = null;
                try
                {
                    modules = assembly.GetModules();
                }
                catch
                {
                    continue;
                }
                foreach (var module in modules)
                {
                    Type[] types = null;
                    try
                    {
                        types = module.GetTypes();
                    }
                    catch
                    {
                        continue;
                    }
                    foreach (var type in types)
                    {
                        try
                        {
                            var attrs = type.GetCustomAttributes(typeof(GaeaSerializableAttribute), false);
                            if (attrs.Length > 0)
                            {
                                uint typeId = type.GetTypeId();
                                var ci = new ClassItem(typeId,type);
                                IdTypeMap[typeId] = ci;
                                TypeIdMap[type] = ci;
                            }
                        }
                        catch
                        {
                        }
                    }
                }
            }
            string[] files = Directory.GetFiles(AppDomain.CurrentDomain.BaseDirectory, "*.dll", SearchOption.AllDirectories);
            foreach (var file in files)
            {
                try
                {
                    FileInfo fi = new FileInfo(file);
                    var modules = System.Reflection.Assembly.Load(fi.Name.Replace(fi.Extension, string.Empty)).GetModules();
                    foreach (var module in modules)
                    {
                        try
                        {
                            var types = module.GetTypes();
                            foreach (var type in types)
                            {
                                var attrs = type.GetCustomAttributes(typeof(GaeaSerializableAttribute), false);
                                if (attrs.Length > 0)
                                {
                                    uint typeId = type.GetTypeId();
                                    var ci = new ClassItem(typeId,type);
                                    IdTypeMap[typeId] = ci;
                                    TypeIdMap[type] = ci;
                                }
                            }
                        }
                        catch { }
                    }
                }
                catch
                { }
            }
        }

        public static uint GetTypeId(Type type)
        {
            if (type.IsGenericType)
            {
                type = type.GetGenericTypeDefinition();
            }
            else if (type.IsArray)
            {
                type = typeof(Array);
            }
            if (typeof(IDictionary).IsAssignableFrom(type))
            {
                type = typeof(IDictionary);
            }
            else if (typeof(IList).IsAssignableFrom(type) && type!=typeof(Array))
            {
                type = typeof(IList);
            }
            ClassItem ci;
            if (TypeIdMap.TryGetValue(type, out ci))
            {
                return ci.TypeId;
            }
            var attributes = type.GetCustomAttributes(typeof(GaeaSerializableAttribute), false);
            if (attributes.Length > 0)
            {
                string name = ((GaeaSerializableAttribute)attributes[0]).Name;
                if (string.IsNullOrEmpty(name))
                {
                    name = type.Name;
                }
                uint typeId = (uint)name.GaeaHashCode();
                var ci2 = new ClassItem(typeId,type);
                IdTypeMap[typeId] = ci;
                TypeIdMap[type] = ci;
                return typeId;
            }
            throw new DisallowedSerializeException(type);
        }
        public static Type GetType(uint typeId)
        {
            ClassItem ci;
            if (IdTypeMap.TryGetValue(typeId, out ci))
            {
                return ci.Types[0];
            }
            return null;
        }
    }

    public class ClassItem
    {
        private Type[] _types;
        private uint _typeId;
        public ClassItem(uint typeid, params Type[] types)
        {
            _types = types;
            _typeId = typeid;
        }

        public Type[] Types
        {
            get { return _types; }
        }
        public uint TypeId
        {
            get { return _typeId; }
        }
    }
}
