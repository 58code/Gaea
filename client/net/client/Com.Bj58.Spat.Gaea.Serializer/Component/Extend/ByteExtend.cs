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
using System.Collections.Generic;

public static class ByteExtend
{
    public static byte[] GetBytes(this ushort obj)
    {
        return BitConverter.GetBytes(obj);
    }

    public static byte[] GetBytes(this uint obj)
    {
        return BitConverter.GetBytes(obj);
    }

    public static byte[] GetBytes(this ulong obj)
    {
        return BitConverter.GetBytes(obj);
    }

    public static byte[] GetBytes(this short obj)
    {
        return BitConverter.GetBytes(obj);
    }

    public static byte[] GetBytes(this int obj)
    {
        return BitConverter.GetBytes(obj);
    }

    public static byte[] GetBytes(this long obj)
    {
        return BitConverter.GetBytes(obj);
    }

    public static byte[] GetBytes(this char obj)
    {
        return BitConverter.GetBytes(obj);
    }

    public static byte[] GetBytes(this bool obj)
    {
        return BitConverter.GetBytes(obj);
    }

    public static byte[] GetBytes(this float obj)
    {
        return BitConverter.GetBytes(obj);
    }

    public static byte[] GetBytes(this double obj)
    {
        return BitConverter.GetBytes(obj);
    }

    public static short ToInt16(this byte[] bytes, int index)
    {
        return BitConverter.ToInt16(bytes, index);
    }

    public static int ToInt32(this byte[] bytes, int index)
    {
        return BitConverter.ToInt32(bytes, index);
    }

    public static long ToInt64(this byte[] bytes, int index)
    {
        return BitConverter.ToInt64(bytes, index);
    }

    public static ushort ToUInt16(this byte[] bytes, int index)
    {
        return BitConverter.ToUInt16(bytes, index);
    }

    public static uint ToUInt32(this byte[] bytes, int index)
    {
        return BitConverter.ToUInt32(bytes, index);
    }

    public static ulong ToUInt64(this byte[] bytes, int index)
    {
        return BitConverter.ToUInt64(bytes, index);
    }

    public static char ToChar(this byte[] bytes, int index)
    {
        return BitConverter.ToChar(bytes, index);
    }

    public static bool ToBoolean(this byte[] bytes, int index)
    {
        return BitConverter.ToBoolean(bytes, index);
    }

    public static float ToFloat(this byte[] bytes, int index)
    {
        return BitConverter.ToSingle(bytes, index);
    }

    public static double ToDouble(this byte[] bytes, int index)
    {
        return BitConverter.ToDouble(bytes, index);
    }

    public static byte ToByte(this byte[] bytes, int index)
    {
        return bytes[index];
    }
}