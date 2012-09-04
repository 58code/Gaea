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
using Com.Bj58.Spat.Gaea.Client.Utility.Logger;
using Com.Bj58.Spat.Gaea.Client.Communication.Socket;
using Com.Bj58.Spat.Gaea.Client.Loadbalance.Component;

namespace Com.Bj58.Spat.Gaea.Client.Loadbalance
{
    internal class Dispatcher
    {
        private IList<Server> ServerPool = new List<Server>();
        private LoggerBase logger = LoggerBase.GetLogger();
        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="config">Configuration.Configuration object</param>
        public Dispatcher(Configuration.ServiceConfig config)
        {
            logger.Info("starting init servers");
            foreach(var server in config.Servers)
            {
                if (server.WeithtRate > 0)
                {
                    var s = new Server(server);
                    if (s.State != ServerState.Disable)
                    {
                        SocketPool sp = new SocketPool(s, config.SocketPool);
                        s.ScoketPool = sp;
                        ServerPool.Add(s);
                    }
                }
            }
            logger.Info("init servers end");
        }

        /// <summary>
        /// get server from server pool
        /// </summary>
        /// <returns>return a server minimum of current user </returns>
        public Server GetServer()
        {
            if (ServerPool == null || ServerPool.Count == 0)
                return null;
            Server result = null;
            int currUserCount = -1;
            int count = ServerPool.Count;
            Random ran = new Random();
            int start = ran.Next(0, count);
            for (int i = start; i < start + count; i++)
            {
                var index = i;
                if (index >= count)
                    index -= count;
                var server = ServerPool[index];
                if (server.State == ServerState.Dead && (DateTime.Now - server.DeadTime) > server.DeadTimeout)
                {
                    lock (this)
                    {
                        if (server.State == ServerState.Dead && (DateTime.Now - server.DeadTime) > server.DeadTimeout)
                        {
                            result = server;
                            server.State = ServerState.Testing;
                            logger.Debug("find server that need to test!host:" + server.Address);
                            break;
                        }
                    }
                }
                else
                {
                    if ((server.CurrUserCount < currUserCount * server.WeightRate || currUserCount < 0)
                        && server.State == ServerState.Normal
                        )
                    {
                        currUserCount = server.CurrUserCount;
                        result = server;
                    }
                }
            }
            return result;
        }
        public Server GetServer(string name)
        {
            foreach (var s in ServerPool)
            {
                if (s.Name.Equals(name, StringComparison.CurrentCultureIgnoreCase))
                    return s;
            }
            return null;
        }
    }
}
