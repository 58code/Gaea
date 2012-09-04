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
using System.Runtime.Serialization;
using Com.Bj58.Spat.Gaea.Serializer.Component.Attributes;

namespace Com.Bj58.Spat.Gaea.Client.Communication.Protocol.SDP
{
    [DataContract]
    [GaeaSerializable]
    internal class ExceptionProtocol
    {
        private int _ErrorCode;
        private String _ToIP;
        private String _FromIP;
        private String _ErrorMsg;

        [DataMember(Name = "errorCode")]
        [GaeaMember]
        public int ErrorCode
        {
            get { return _ErrorCode; }
            set { _ErrorCode = value; }
        }

        [DataMember(Name = "toIP")]
        [GaeaMember]
        public String ToIP
        {
            get { return _ToIP; }
            set { _ToIP = value; }
        }

        [DataMember(Name = "fromIP")]
        [GaeaMember]
        public String FromIP
        {
            get { return _FromIP; }
            set { _FromIP = value; }
        }

        [DataMember(Name = "errorMsg")]
        [GaeaMember]
        public String ErrorMsg
        {
            get { return _ErrorMsg; }
            set { _ErrorMsg = value; }
        }
    }
}
