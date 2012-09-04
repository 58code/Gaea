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
using Com.Bj58.Spat.Gaea.Serializer.Component.Exceptions;
using Com.Bj58.Spat.Gaea.Serializer.Component;

namespace Com.Bj58.Spat.Gaea.Serializer
{
    internal class Int32Serializer : SerializerBase
    {
        private readonly int BYTE_LEN = 4; 
        public override void WriteObject(object obj, GaeaStream outStream)
        {
            var buffer = Convert.ToInt32(obj).GetBytes();
            outStream.Write(buffer, 0, BYTE_LEN);
        }

        public override object ReadObject(GaeaStream inStream,Type defType)
        {
            byte[] buffer = new byte[BYTE_LEN];
            inStream.SafeRead(buffer, 0, BYTE_LEN);
            if (defType == typeof(UInt32))
                return buffer.ToUInt32(0);
            else
                return buffer.ToInt32(0);
        }
    }
    internal class Int64Serializer : SerializerBase
    {
        private readonly int BYTE_LEN = 8; 
        public override void WriteObject(object obj, GaeaStream outStream)
        {
            var buffer = Convert.ToInt64(obj).GetBytes();
            outStream.Write(buffer, 0, BYTE_LEN);
        }

        public override object ReadObject(GaeaStream inStream,Type defType)
        {
            byte[] buffer = new byte[BYTE_LEN];
            inStream.SafeRead(buffer, 0, BYTE_LEN);
            if (defType == typeof(UInt64))
                return buffer.ToInt64(0);
            else
                return buffer.ToInt64(0);
        }
    }
    internal class Int16Serializer : SerializerBase
    {
        private readonly int BYTE_LEN = 2; 
        public override void WriteObject(object obj, GaeaStream outStream)
        {
            var buffer = Convert.ToInt16(obj).GetBytes();
            outStream.Write(buffer, 0, BYTE_LEN);
        }

        public override object ReadObject(GaeaStream inStream,Type defType)
        {
            byte[] buffer = new byte[BYTE_LEN];
            inStream.SafeRead(buffer, 0, BYTE_LEN);
            if (defType == typeof(UInt16))
                return buffer.ToInt16(0);
            else
                return buffer.ToInt16(0);
        }
    }
    internal class FloatSerializer : SerializerBase
    {
        private readonly int BYTE_LEN = 4; 
        public override void WriteObject(object obj, GaeaStream outStream)
        {
            var buffer = ((float)obj).GetBytes();
            outStream.Write(buffer, 0, BYTE_LEN);
        }

        public override object ReadObject(GaeaStream inStream,Type defType)
        {
            byte[] buffer = new byte[BYTE_LEN];
            inStream.SafeRead(buffer, 0, BYTE_LEN);
            return buffer.ToFloat(0);
        }
    }
    internal class DoubleSerializer : SerializerBase
    {
        private readonly int BYTE_LEN = 8; 
        public override void WriteObject(object obj, GaeaStream outStream)
        {
            var buffer = ((double)obj).GetBytes();
            outStream.Write(buffer, 0, BYTE_LEN);
        }

        public override object ReadObject(GaeaStream inStream,Type defType)
        {
            byte[] buffer = new byte[BYTE_LEN];
            inStream.SafeRead(buffer, 0, BYTE_LEN);
            return buffer.ToDouble(0);
        }
    }
    internal class ByteSerializer : SerializerBase
    {
        public override void WriteObject(object obj, GaeaStream outStream)
        {
            outStream.WriteByte((byte)obj);
        }

        public override object ReadObject(GaeaStream inStream,Type defType)
        {
            return (byte)inStream.ReadByte();
        }
    }
}
