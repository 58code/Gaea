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
using System.Collections.Generic;
using Com.Bj58.Spat.Gaea.Serializer.Component.Exceptions;

namespace Com.Bj58.Spat.Gaea.Serializer.Component
{
    public static class GenericHelper
    {
        public static Type GetRealGenType(Type type)
        {
            if (!type.IsGenericType)
                return type;
            else if (!type.IsAbstract && !type.IsInterface)
                return type;
            else if (type.GetGenericTypeDefinition() == typeof(IList<>).GetGenericTypeDefinition())
                return typeof(List<>).MakeGenericType(type.GetGenericArguments());
            else if (type.GetGenericTypeDefinition() == typeof(ICollection<>).GetGenericTypeDefinition())
                return typeof(List<>).MakeGenericType(type.GetGenericArguments());
            else if (type.GetGenericTypeDefinition() == typeof(IEnumerable<>).GetGenericTypeDefinition())
                return typeof(List<>).MakeGenericType(type.GetGenericArguments());
            else if (type.GetGenericTypeDefinition() == typeof(IDictionary<,>).GetGenericTypeDefinition())
                return typeof(Dictionary<,>).MakeGenericType(type.GetGenericArguments());
            else
                return null;
        }

        public static Type GetRealGenType(Type type, Type defType)
        {
            Type t = GetRealGenType(defType);
            if (t != null)
                return t;
            if (type.IsGenericType)
            {
                if (defType.IsGenericType)
                {
                    return type.MakeGenericType(defType.GetGenericArguments());
                }
                else
                {
                    var err = new SerializeException("defType must be generic type.");
                    err.Type = type;
                    throw err;

                }
            }
            return type;
        }
    }
}
