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
using Com.Bj58.Spat.Gaea.Serializer.Component;

public static class TypeExtend
{
    public static uint GetTypeId(this Type type)
    {
        return TypeMap.GetTypeId(type);
    }

    public static Type ToType(this uint id)
    {
        return TypeMap.GetType(id);
    }

    public static bool IsPrimitives(this Type type)
    {
        if (type == typeof(int))
            return true;
        else if (type == typeof(short))
            return true;
        else if (type == typeof(long))
            return true;
        else if (type == typeof(float))
            return true;
        else if (type == typeof(double))
            return true;
        else if (type == typeof(char))
            return true;
        else if (type == typeof(bool))
            return true;
        else if (type == typeof(byte))
            return true;
        else if (type == typeof(uint))
            return true;
        else if (type == typeof(ushort))
            return true;
        else if (type == typeof(ulong))
            return true;
        return false;
    }
}