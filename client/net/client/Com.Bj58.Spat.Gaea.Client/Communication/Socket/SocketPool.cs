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
using System.Net;
using System.Threading;
using System.Collections.Generic;
using Com.Bj58.Spat.Gaea.Client.Loadbalance;
using Com.Bj58.Spat.Gaea.Client.Utility.Logger;
using Com.Bj58.Spat.Gaea.Client.Configuration.Commmunication;

namespace Com.Bj58.Spat.Gaea.Client.Communication.Socket
{
    internal class SocketPool
    {
        private LoggerBase logger = LoggerBase.GetLogger();
        private IPEndPoint endPoint;
        private SocketPoolProfile socketPoolConfig;
        private ConnPool pool = null;
        private AutoResetEvent releasedEvent = new AutoResetEvent(false);
        private Server _Server;
        public SocketPool(Server server,SocketPoolProfile config)
        {
            _Server = server;
            endPoint = new IPEndPoint(IPAddress.Parse(server.Address), server.Port);
            socketPoolConfig = config;
            pool = new ConnPool(config.ShrinkInterval, config.MinPoolSize);
        }
        public int Count
        {
            get { return pool.Total; }
        }
        public CSocket GetScoket()
        {            
            CSocket rSocket = null;
            lock (pool)
            {
                if (pool.Count > 0)
                {
                    rSocket = pool.Dequeue();
                }
                else if (pool.Total < socketPoolConfig.MaxPoolSize)
                {
                    var socket = new CSocket(endPoint, this, socketPoolConfig);
                    pool.AllSocket.Add(socket);
                    rSocket = socket;
                    logger.Info("created new socket.server:" + _Server.Name);
                }
                else
                {
                    if (!releasedEvent.WaitOne((int)socketPoolConfig.WaitTimeout.TotalMilliseconds))
                    {
                        logger.Error("socket connection pool is full(" + pool.Total + ")!server cu:" + _Server.CurrUserCount);
                        throw new TimeoutException("socket connection pool is full!");
                    }
                    else
                    {
                        rSocket = pool.Dequeue();
                    }
                }
            }
            if (rSocket == null)
            {
                throw new Exception("can't get socket from pool!");
            }
            rSocket.Reset();
            rSocket.InPool = false;
            return rSocket;
        }
        public void Release(CSocket socket)
        {
            if (!socket.Connected)
            {
                logger.Warn("socket Destroyed,reason: disconnected!host:" + endPoint.Address);
                Destroy(socket);
            }
            else if (socketPoolConfig.AutoShrink && pool.Shrink())
            {
                logger.Info("socket Destroyed,reason: auto shrink!host:" + endPoint.Address);
                Destroy(socket);
            }
            else if(!socket.InPool)
            {
                pool.Enqueue(socket);
                releasedEvent.Set();
            }
        }
        public void Destroy(CSocket socket)
        {
            try
            {
                socket.Disconnect(false);
            }
            catch (Exception err)
            {
                logger.Error("socket destroy error!host:" + endPoint.Address + ",message:" + err.Message);
            }
            finally
            {
                if (pool != null)
                {
                    pool.AllSocket.Remove(socket);
                    pool.AllSocket.Remove(null);
                }
                socket = null;
            }
        }
        public void Destroy()
        {
            foreach (var c in pool)
            {
                c.Dispose(true);
            }
            logger.Info("Destroy all socket.!host:" + endPoint.Address);
        }
    }
    internal class ConnPool: IEnumerable<CSocket>
    {
        Queue<CSocket> queue = new Queue<CSocket>();

        private TimeSpan _duration;
        private int _minConn;
        public ConnPool(TimeSpan duration, int minConn)
        {
            _duration = duration;
            _minConn = minConn;
        }
        public int Total
        {
            get 
            {
                return AllSocket.Count;
            }
        }
        private IList<CSocket> _AllSocket = new List<CSocket>();
        public IList<CSocket> AllSocket
        {
            get { return _AllSocket; }
        }
  
        private DateTime lastCheckTime = DateTime.Now;
        private int freeCount = -1;
        private int shrinkCount = 0;
        public bool Shrink()
        {
            lock (this)
            {
                if (shrinkCount > 0)
                {
                    shrinkCount--;
                    return true;
                }
                if ((DateTime.Now - lastCheckTime) > _duration)
                {
                    lastCheckTime = DateTime.Now;
                    bool b = freeCount > 0 && Total > _minConn;
                    if (b)
                    {
                        shrinkCount = System.Math.Min((Total - _minConn), freeCount);
                        if (shrinkCount < 0)
                            shrinkCount = 0;
                    }
                    return false;
                }
                int currFreeCount = this.Count;
                if (currFreeCount < freeCount || freeCount < 0)
                {
                    freeCount = currFreeCount;
                }
                return false;
            }
        }

        public CSocket Dequeue()
        {
            lock (this)
            {
                return queue.Dequeue();
            }
        }

        public void Enqueue(CSocket socket)
        {
            lock (this)
            {
                queue.Enqueue(socket);
            }
        }

        public int Count
        {
            get { return queue.Count; }
            private set { }
        }


        #region IEnumerable<CSocket> 成员

        public IEnumerator<CSocket> GetEnumerator()
        {
            return queue.GetEnumerator();
        }

        #endregion

        #region IEnumerable 成员

        System.Collections.IEnumerator System.Collections.IEnumerable.GetEnumerator()
        {
            return queue.GetEnumerator();
        }

        #endregion
    }
}
