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
using System.Xml;

namespace Com.Bj58.Spat.Gaea.Client.Configuration.Commmunication
{
    internal class SocketPoolProfile
    {
        public SocketPoolProfile(XmlNode node)
        {
            this.MinPoolSize = int.Parse(node.Attributes["minPoolSize"].Value);
            this.MaxPoolSize = int.Parse(node.Attributes["maxPoolSize"].Value);
            this.SendTimeout = TimeSpan.Parse(node.Attributes["sendTimeout"].Value);
            this.ReceiveTimeout = TimeSpan.Parse(node.Attributes["receiveTimeout"].Value);
            this.WaitTimeout = TimeSpan.Parse(node.Attributes["waitTimeout"].Value);
            this.Nagle = bool.Parse(node.Attributes["nagle"].Value);
            this.ShrinkInterval = TimeSpan.Parse(node.Attributes["autoShrink"].Value);
            this.BufferSize = int.Parse(node.Attributes["bufferSize"].Value);
        }
        public int MinPoolSize
        {
            get;
            set;
        }
        public int MaxPoolSize
        {
            get;
            set;
        }
        public TimeSpan SendTimeout
        {
            get;
            set;
        }
        public TimeSpan ReceiveTimeout
        {
            get;
            set;
        }
        public TimeSpan WaitTimeout
        {
            get;
            set;
        }
        public bool Nagle
        {
            get;
            set;
        }
        public int BufferSize
        {
            get;
            set;
        }
        public bool AutoShrink
        {
            get { return ShrinkInterval.TotalSeconds > 0; }
        }
        public TimeSpan ShrinkInterval
        {
            get;
            set;
        }
    }
}
