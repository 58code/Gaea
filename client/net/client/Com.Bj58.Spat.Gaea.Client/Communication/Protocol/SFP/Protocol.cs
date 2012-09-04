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
using Com.Bj58.Spat.Gaea.Serializer.Component.Attributes;
using Com.Bj58.Spat.Gaea.Client.Configuration.Commmunication;
using Com.Bj58.Spat.Gaea.Client.Communication.Protocol.SFP.Enum;

namespace Com.Bj58.Spat.Gaea.Client.Communication.Protocol.SFP
{
    internal class Protocol
    {
        private readonly byte VERSION = 1;
        public Protocol(ProtocolProfile config,int sessionId,UserDataType userDataType, byte[] userData)
        {
            _UserData = userData;
            _Version = VERSION;
            _SessionID = sessionId;
            _UserDataType = userDataType;
            _CompressType = config.Compress;
            _SerializeType = config.SerializerType;
            _ServiceID = config.ServiceID;
            _TotalLen = SFPStruct.Version + SFPStruct.TotalLen + SFPStruct.SessionId + SFPStruct.ServerId + SFPStruct.SerializeType + SFPStruct.SDPType + SFPStruct.Platform + SFPStruct.CompressType + userData.Length;

        }
        public Protocol(byte[] buffer)
        {
            this.FromBytes(buffer);
        }

        private byte _Version;
        private int _TotalLen;
        private int _SessionID;
        private byte _ServiceID;
        private UserDataType _UserDataType;
        private CompressType _CompressType;
        private SerializeType _SerializeType;
        private PlatformType _Platform = PlatformType.Dotnet;
        private byte[] _UserData;

        /// <summary>
        /// get protocol version
        /// </summary>
        public byte Version
        {
            get { return _Version; }
            set { _Version = value; }
        }
        /// <summary>
        /// get or set total length of packet
        /// </summary>
        public int TotalLen
        {
            get { return _TotalLen; }
            set { _TotalLen = value; }
        }
        public int SessionID
        {
            get { return _SessionID; }
            set { _SessionID = value; }
        }
        public byte ServiceID
        {
            get { return _ServiceID; }
            set { _ServiceID = value; }
        }
        public UserDataType UserDataType
        {
            get { return _UserDataType; }
            set { _UserDataType = value; }
        }
        public CompressType CompressType
        {
            get { return _CompressType; }
            set { _CompressType = value; }
        }
        public SerializeType SerializeType
        {
            get { return _SerializeType; }
            set { _SerializeType = value; }
        }
        public PlatformType Platform
        {
            get { return _Platform; }
            set { _Platform = value; }
        }
        public byte[] UserData
        {
            get { return _UserData; }
            set { _UserData = value; }
        }
    }
}