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

namespace Com.Bj58.Spat.Gaea.Serializer
{
    public class DecimalSerizlizer : SerializerBase
    {
        public override void WriteObject(object obj, Com.Bj58.Spat.Gaea.Serializer.Component.GaeaStream outStream)
        {
            SerializerFactory.GetSerializer(typeof(string)).WriteObject(obj.ToString(), outStream);
        }

        public override object ReadObject(Com.Bj58.Spat.Gaea.Serializer.Component.GaeaStream inStream, Type defType)
        {
            object value = SerializerFactory.GetSerializer(typeof(string)).ReadObject(inStream, typeof(string));
            if (value != null)
                return decimal.Parse(value.ToString());
            return default(decimal);
        }
    }
}
