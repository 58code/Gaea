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
using Com.Bj58.Spat.Gaea.Client.Configuration;

namespace Com.Bj58.Spat.Gaea.Client.Utility.Logger
{
    internal class UDPLogger : LoggerBase
    {
        System.Net.Sockets.UdpClient uc;
        string _host;
        int _port;
        public UDPLogger(LoggerConfig config)
        {
            _host = config.UDP.Host;
            _port = config.UDP.Port;
            uc = new System.Net.Sockets.UdpClient();
        }
        public override Level Level
        {
            get;
            set;
        }

        public override void Debug(string msg)
        {
            if (Level <= Level.Debug)
            {
                SendData(msg, Level.Debug);
            }
        }

        public override void Error(string msg)
        {
            if (Level <= Level.Error)
            {
                SendData(msg, Level.Error);
            }
        }

        public override void Fatal(string msg)
        {
            if (Level <= Level.Fatal)
            {
                SendData(msg, Level.Fatal);
            }
        }

        public override void Info(string msg)
        {
            if (Level <= Level.Info)
            {
                SendData(msg, Level.Info);
            }
        }

        public override void Notice(string msg)
        {
            if (Level <= Level.Notice)
            {
                SendData(msg, Level.Notice);
            }
        }

        public override void Warn(string msg)
        {
            if (Level <= Level.Warn)
            {
                SendData(msg, Level.Warn);
            }
        }
        private void SendData(string msg, Level level)
        {
            StringBuilder content = new StringBuilder();
            content.Append("Gaea-Time:" + DateTime.Now.ToString("yy-MM-dd hh:mm:ss"));
            content.Append(" level:" + level);
            content.Append(" message:" + msg.Replace("\n", string.Empty));
            try
            {
                byte[] data = Encoding.UTF8.GetBytes(content.ToString());
                uc.Send(data, data.Length, _host, _port);
            }
            catch { }
        }
    }
}
