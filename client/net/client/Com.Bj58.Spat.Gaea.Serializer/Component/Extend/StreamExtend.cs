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
using Com.Bj58.Spat.Gaea.Serializer;
using Com.Bj58.Spat.Gaea.Serializer.Component;

public static class StreamExtend
{
    public static void SafeRead(this Stream stream, byte[] buffer, int offset, int count)
    {
        if (stream.Read(buffer, offset, count) != count)
        {
            throw new Com.Bj58.Spat.Gaea.Serializer.Component.Exceptions.StreamException();
        }
    }

    public static int ReadInt32(this Stream stream)
    {
        byte[] tem = new byte[4];
        stream.SafeRead(tem, 0, 4);
        return tem.ToInt32(0);
    }

    public static uint ReadUInt32(this Stream stream)
    {
        byte[] tem = new byte[4];
        stream.SafeRead(tem, 0, 4);
        return tem.ToUInt32(0);
    }

    public static void WriteInt32(this Stream stream, int value)
    {
        stream.Write(value.GetBytes(), 0, 4);
    }

    public static void WriteInt64(this Stream stream, long value)
    {
        stream.Write(value.GetBytes(), 0, 8);
    }

    public static void WriteUInt32(this Stream stream, uint value)
    {
        stream.Write(value.GetBytes(), 0, 4);
    }
}