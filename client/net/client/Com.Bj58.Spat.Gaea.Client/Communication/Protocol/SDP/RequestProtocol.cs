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
using System.Collections.Generic;
using System.Runtime.Serialization;
using Com.Bj58.Spat.Gaea.Serializer.Component.Attributes;

namespace Com.Bj58.Spat.Gaea.Client.Communication.Protocol.SDP
{
    [DataContract]
    [GaeaSerializable]
    public class RequestProtocol
    {
        private RequestProtocol() { }
        public RequestProtocol(string typeName, string methodName, params RpParameter[] paras)
        {
            this.Lookup = typeName;
            this.MethodName = methodName;
            this.ParaKVList = paras.ToList();
        }
        private String _Lookup;
        private String _MethodName;
        private List<RpParameter> _ParaKVList;

        [DataMember(Name = "lookup")]
        [GaeaMember]
        public String Lookup
        {
            get { return _Lookup; }
            set { _Lookup = value; }
        }

        [DataMember(Name = "methodName")]
        [GaeaMember]
        public String MethodName
        {
            get { return _MethodName; }
            set { _MethodName = value; }
        }

        [DataMember(Name = "paraKVList")]
        [GaeaMember]
        public List<RpParameter> ParaKVList
        {
            get { return _ParaKVList; }
            set { _ParaKVList = value; }
        }
    }

    [GaeaSerializable]
    public class RpParameter
    {
        public RpParameter() { }
        public RpParameter(string name, object value)
        {
            Name = name;
            Value = value;
        }
        [GaeaMember]
        public string Name
        {
            get;
            set;
        }
        [GaeaMember]
        public object Value
        {
            get;
            set;
        }
    }
}
