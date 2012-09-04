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
using Com.Bj58.Spat.Gaea.Client.Utility.Logger;

namespace Com.Bj58.Spat.Gaea.Client.Configuration
{
    internal class LoggerConfig
    {
        private FileSystemWatcher watcher = new FileSystemWatcher();
        private void Init()
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
            var tem = GetConfig();
            this.Level = tem.Level;
            this.File = tem.File;
            this.UDP = tem.UDP;
            this.Logger = tem.Logger;
        }
        private LoggerConfig()
        {
            XmlDocument xmlDoc = new XmlDocument();
            xmlDoc.Load(CONST.CONFIG_PATH);
            XmlNode node = xmlDoc.SelectSingleNode("//Logger");
            XmlAttribute attribute = node.Attributes["level"];
            if (attribute != null)
            {
                this.Level = (Level)Enum.Parse(typeof(Level), attribute.Value, true);
            }
            else
            {
                this.Level = Level.Error;
            }
            XmlNode udpNode = node.SelectSingleNode("UDP");
            if (udpNode != null)
            {
                this.UDP = new UDPProfile(udpNode);

            }
            XmlNode fileNode = node.SelectSingleNode("File");
            if (fileNode != null)
            {
                this.File = new FileProfile(fileNode);
            }
            string type = node.Attributes["type"].Value;
            this.Logger = Activator.CreateInstance(Type.GetType(type), this) as LoggerBase;
            this.Logger.Level = this.Level;
            Init();
        }

        private static LoggerConfig CACHE = null;
        public static LoggerConfig GetConfig()
        {
            if (CACHE == null)
                CACHE = new LoggerConfig();
            return CACHE;
        }

        public Level Level
        {
            get;
            set;
        }

        public LoggerBase Logger
        {
            get;
            set;
        }

        public UDPProfile UDP
        {
            get;
            set;
        }

        public FileProfile File
        {
            get;
            set;
        }
    }

    internal class UDPProfile
    {
        public UDPProfile(XmlNode node)
        {
            try
            {
                this.Host = node.Attributes["host"].Value;
                this.Port = int.Parse(node.Attributes["port"].Value);
            }
            catch
            { 
                
            }
        }
        public string Host
        {
            get;
            set;
        }
        public int Port
        {
            get;
            set;
        }
    }
    internal class FileProfile
    {
        public FileProfile(XmlNode node)
        {
            this.Path = node.Attributes["path"].Value;
        }
        public string Path
        {
            get;
            set;
        }
    }
}
