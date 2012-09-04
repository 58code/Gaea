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

namespace Com.Bj58.Spat.Gaea.Client.Communication.Serialize
{
    internal class BinarySerialize : SerializeBase
    {
        private Com.Bj58.Spat.Gaea.Serializer.Serializer serializer = new Com.Bj58.Spat.Gaea.Serializer.Serializer();
        public override byte[] Serialize(object obj)
        {
            return serializer.Serialize(obj);
        }

        public override object Deserialize(byte[] data, Type type)
        {
            return serializer.Derialize(data,type);
        }

        public override T Deserialize<T>(byte[] data)
        {
            return (T)Deserialize(data, typeof(T));
        }
    }
}
