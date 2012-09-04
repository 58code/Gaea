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
using System.Runtime.InteropServices;
using Com.Bj58.Spat.Gaea.Serializer.Component;
using System.Runtime.Serialization.Formatters.Binary;

namespace Com.Bj58.Spat.Gaea.Serializer
{
    internal class StringSerializer:SerializerBase
    {
        public override void WriteObject(object obj, GaeaStream outStream)
        {
            if (outStream.WriteRef(obj))
            {
                return;
            }
            var buffer = outStream.Encoder.GetBytes(obj.ToString());
            var bLen = buffer.Length.GetBytes();
            var bytes = new byte[buffer.Length + 4];
            Array.Copy(bLen, 0, bytes, 0, 4);
            Array.Copy(buffer, 0, bytes, 4, buffer.Length);
            outStream.Write(bytes, 0, bytes.Length);
        }

        public override object ReadObject(GaeaStream inStream, Type defType)
        {
            byte[] head = new byte[5];
            inStream.SafeRead(head, 0, 5);
            int isRef = head.ToByte(0);
            int hashcode = head.ToInt32(1);
            if (isRef > 0)
            {
                object o = inStream.GetRef(hashcode);
                if (o == null)
                    return string.Empty;
                if (!(o is string))
                    throw new Exception("Type error!");
                return o;
            }
            int len = inStream.ReadInt32();
            if (len == 0)
            {
                inStream.SetRef(hashcode, string.Empty);
                return string.Empty;
            }
            byte[] buffer = new byte[len];
            inStream.SafeRead(buffer, 0, len);
            string str = inStream.Encoder.GetString(buffer);
            inStream.SetRef(hashcode, str);
            return str;
        }
    }
}
