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
using System.Text;
using System.Threading;
using Com.Bj58.Spat.Gaea.Client.Configuration;

namespace Com.Bj58.Spat.Gaea.Client.Utility.Logger
{
    internal class FileLogger : Com.Bj58.Spat.Gaea.Client.Utility.Logger.LoggerBase
    {
        private string SavePath;
        public FileLogger(LoggerConfig config)
        {
            SavePath = config.File.Path;
        }
        private Level level;
        public override Level Level
        {
            get
            {
                return level;
            }
            set
            {
                level = value;
            }
        }
        public override void Debug(string msg)
        {
            if (level <= Level.Debug)
            {
                WriteLog(msg, Level.Debug);
            }
        }
        public override void Info(string msg)
        {
            if (level <= Level.Info)
            {
                WriteLog(msg,Level.Info);
            }
        }
        public override void Notice(string msg)
        {
            if (level <= Level.Notice)
            {
                WriteLog(msg, Level.Notice);
            }
        }
        public override void Warn(string msg)
        {
            if (level <= Level.Warn)
            {
                WriteLog(msg, Level.Warn);
            }
        }
        public override void Error(string msg)
        {
            if (level <= Level.Error)
            {
                WriteLog(msg, Level.Error);
            }
        }
        public override void Fatal(string msg)
        {
            if (level <= Level.Fatal)
            {
                WriteLog(msg, Level.Fatal);
            }
        }

        private string GetPath()
        {
            string dir = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, SavePath);
            if (!Directory.Exists(dir))
            {
                Directory.CreateDirectory(dir);
            }
            string path = Path.Combine(dir, DateTime.Now.ToString("yyMMdd") + ".log");
            return path;
        }

        private StringBuilder cache = new StringBuilder();
        private void WriteLog(string msg, Level _level)
        {
            ThreadPool.QueueUserWorkItem(delegate(object state) {
                try
                {
                    StringBuilder content = new StringBuilder();
                    content.AppendLine("Time:" + DateTime.Now.ToString("yy-MM-dd hh:mm:ss"));
                    content.AppendLine("level:" + _level.ToString());
                    content.AppendLine("message:" + msg);
                    content.AppendLine();
                    content.AppendLine();
                    cache.Append(content);
                    if (cache.Length > 1024)
                    {
                        lock (cache)
                        {
                            if (cache.Length > 1024)
                            {
                                File.AppendAllText(GetPath(), content.ToString());
                                cache = cache.Remove(0, cache.Length);
                            }
                        }
                    }
                }
                catch(Exception err)
                {
                    try
                    {
                        string dir = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "Gaea_log");
                        StringBuilder content = new StringBuilder();
                        content.Append(DateTime.Now);
                        content.AppendLine();
                        content.Append(err.Message);
                        File.AppendAllText(Path.Combine(dir, "error.log"), content.ToString());
                    }
                    catch { }
                }
            });
        }
    }
}
