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
using System.Xml;
using System.Collections.Generic;
using Com.Bj58.Spat.Gaea.Client.Configuration.Commmunication;
using Com.Bj58.Spat.Gaea.Client.Configuration.Loadbalance;

namespace Com.Bj58.Spat.Gaea.Client.Configuration
{
    internal sealed class ServiceConfig
    {
        private FileSystemWatcher watcher = new FileSystemWatcher();
        private ServiceConfig()
        {
            FileInfo fi = new FileInfo(CONST.CONFIG_PATH);
            watcher.Path = fi.Directory.ToString();
            watcher.Filter = fi.Name;
            watcher.NotifyFilter = NotifyFilters.LastWrite;
            watcher.Changed += new FileSystemEventHandler(watcher_Changed);
            watcher.EnableRaisingEvents = true;
        }

        private void watcher_Changed(object sender, FileSystemEventArgs e)
        {
            var tem = GetConfig(this.ServiceName);
            this.SocketPool = tem.SocketPool;
            this.Servers = tem.Servers;
            this.Protocol = tem.Protocol;
            this.ServiceID = tem.ServiceID;
            this.ServiceName = tem.ServiceName;
            this.MaxThreadCount = tem.MaxThreadCount;
        }

        public string ServiceName
        {
            get;
            set;
        }

        public int ServiceID
        {
            get;
            set;
        }

        public int MaxThreadCount
        {
            get;
            set;
        }

        public SocketPoolProfile SocketPool
        {
            get;
            set;
        }

        public ProtocolProfile Protocol
        {
            get;
            set;
        }

        public IList<ServerProfile> Servers
        {
            get;
            set;
        }

        public static ServiceConfig GetConfig(string serviceName)
        {
            XmlDocument xmlDoc = new XmlDocument();
            xmlDoc.Load(CONST.CONFIG_PATH);
            XmlNode serviceNode = xmlDoc.SelectSingleNode("//Service[@name='" + serviceName + "']");
            if (serviceNode == null)
                throw new Exception("Not find xml node with " + serviceName + " service.");
            ServiceConfig config = new ServiceConfig();
            config.ServiceName = serviceNode.Attributes["name"].Value;
            config.ServiceID = int.Parse(serviceNode.Attributes["id"].Value);
            var attMaxThreadCount = serviceNode.Attributes["maxThreadCount"];
            if (attMaxThreadCount != null)
            {
                config.MaxThreadCount = int.Parse(attMaxThreadCount.Value);
            }
            else
            {
                config.MaxThreadCount = CONST.DEFAULT_MAX_THREAD_COUNT;
            }
            XmlNode xnSocketPool = serviceNode.SelectSingleNode("Commmunication/SocketPool");
            config.SocketPool = new SocketPoolProfile(xnSocketPool);
            XmlNode xnProtocol = serviceNode.SelectSingleNode("Commmunication/Protocol");
            config.Protocol = new ProtocolProfile(xnProtocol);
            XmlNodeList xnServers = serviceNode.SelectNodes("Loadbalance/Server/add");
            List<ServerProfile> servers = new List<ServerProfile>();
            foreach (XmlNode xnServer in xnServers)
            {
                servers.Add(new ServerProfile(xnServer));
            }
            config.Servers = servers;
            config.ServiceName = serviceName;
            return config;
        }
    }
}
