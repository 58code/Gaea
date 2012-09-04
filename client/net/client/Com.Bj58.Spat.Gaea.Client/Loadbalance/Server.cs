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
using System.Text;
using System.Timers;
using System.Threading;
using System.Diagnostics;
using System.Net.Sockets;
using Com.Bj58.Spat.Gaea.Client.Utility.Logger;
using Com.Bj58.Spat.Gaea.Client.Communication.Socket;
using Com.Bj58.Spat.Gaea.Client.Loadbalance.Component;
using Com.Bj58.Spat.Gaea.Client.Communication.Protocol.SFP;

namespace Com.Bj58.Spat.Gaea.Client.Loadbalance
{
    internal class Server
    {
        private int MAX_CURRUSER = 500;
        private readonly int NOBUSY_CURRUSR = 5;
        private LoggerBase logger = LoggerBase.GetLogger();
        /// <summary>
        /// Constructor
        /// </summary>
        /// <param name="config">Configuration.Loadbalance.ServerProfile object</param>
        public Server(Configuration.Loadbalance.ServerProfile config)
        {
            MAX_CURRUSER = config.MaxCurrentUser;
            this.Name = config.Name;
            this.Address = config.Host;
            this.Port = config.Port;
            this.WeightRate = config.WeithtRate;
            this.DeadTimeout = config.DeadTimeout;
            if (this.WeightRate >= 0)
            {
                this.State = ServerState.Normal;
            }
            else
            {
                this.State = ServerState.Disable;
            }
        }
        /// <summary>
        /// server name
        /// </summary>
        public string Name
        {
            get;
            set;
        }
        /// <summary>
        /// this server using address(IP)
        /// </summary>
        public string Address
        {
            get;
            set;
        }
        /// <summary>
        /// this server using port
        /// </summary>
        public int Port
        {
            get;
            set;
        }
        /// <summary>
        /// this server's weight
        /// </summary>
        public int Weight
        {
            get;
            set;
        }
        /// <summary>
        /// weight rate of this server
        /// </summary>
        public float WeightRate
        {
            get;
            set;
        }
        /// <summary>
        /// server state
        /// </summary>
        public ServerState State
        {
            get;
            set;
        }
        /// <summary>
        /// this server's socket pool
        /// </summary>
        public SocketPool ScoketPool
        {
            get;
            set;
        }
        private int _CurrUserCount;
        /// <summary>
        /// executing user count
        /// </summary>
        public int CurrUserCount
        {
            get { return _CurrUserCount; }
            set { _CurrUserCount = value; }
        }
        public TimeSpan DeadTimeout
        {
            get;
            set;
        }
        /// <summary>
        /// server dead time
        /// </summary>
        internal DateTime DeadTime
        {
            get;
            set;
        }

        public override string ToString()
        {
            return "Name:" + Name + ",Address:" + Address + ",Port:" + Port + ",Weight:" + Weight + ",State:" + State.ToString() + ",CurrUserCount:" + CurrUserCount + ",ScoketPool:" + ScoketPool.Count;
        }

        /// <summary>
        /// send a protocol data to server and get a result
        /// </summary>
        /// <param name="p">protocol data(SFP)</param>
        /// <returns>result data(SFP Data)</returns>
        public virtual Protocol Request(Protocol p)
        {
            Stopwatch watcher = new Stopwatch();
            StringBuilder log = new StringBuilder();
            IncreaseCU();
            if (State != ServerState.Normal && State != ServerState.Testing)
            {
                logger.Warn("this server too busy.state:" + State + "+host:" + Address);
                throw new Exception("this server too busy.state:" + State + "+host:" + Address);
            }
            CSocket socket = null;
            try
            {
                byte[] data = p.ToBytes();
                using (socket = this.ScoketPool.GetScoket())
                {
                    socket.Send(data);
                }
                byte[] buffer = socket.Receive(p.SessionID);
                Protocol result = new Protocol(buffer);
                if (this.State == ServerState.Testing)
                    Relive();
                ErrorCount = 0;
                return result;
            }
            catch (SocketException err)
            {
                logger.Error("Occur socket exception.server:" + this.Address + ":" + this.Port + ".message:" + err.Message);
                if (State == ServerState.Testing)
                {
                    MarkAsDead();
                }
                else if (!Test())
                {
                    MarkAsDead();
                }
                throw;
            }
            catch (System.IO.IOException err)
            {
                logger.Error("Occur IO exception.server:" + this.Address + ":" + this.Port + ".message:" + err.Message);
                if (State == ServerState.Testing)
                {
                    MarkAsDead();
                }
                else if (!Test())
                {
                    MarkAsDead();
                }
                throw;
            }
            catch
            {
                if (State == ServerState.Testing)
                {
                    MarkAsDead();
                }
                throw;
            }
            finally
            {
                DecreaseCU();
            }
        }

        #region Helper method
        /// <summary>
        /// Increase current user
        /// </summary>
        public void IncreaseCU()
        {
            Interlocked.Increment(ref _CurrUserCount);
            logger.Debug("server " + Name + " current User Count:" + CurrUserCount);
            if (CurrUserCount > MAX_CURRUSER)
            {
                this.State = ServerState.Busy;
                logger.Warn("this server state change to busy.CurrentUserCount:" + CurrUserCount);
            }
        }
        /// <summary>
        /// Decrease current user
        /// </summary>
        private void DecreaseCU()
        {
            Interlocked.Decrement(ref _CurrUserCount);
            if (CurrUserCount < NOBUSY_CURRUSR && State == ServerState.Busy)
            {
                this.State = ServerState.Normal;
                logger.Warn("this server state change to normal.");
            }
        }
        /// <summary>
        /// mark server to dead
        /// </summary>
        private void MarkAsDead()
        {
            if (this.State == ServerState.Dead)
                return;
            lock (this)
            {
                if (this.State == ServerState.Dead)
                    return;
                logger.Warn("this server is dead!host:" + Address);
                this.State = ServerState.Dead;
                if (this.DeadTimeout.TotalMilliseconds == 0)
                    this.DeadTime = DateTime.Now.AddYears(100);
                else
                    this.DeadTime = DateTime.Now;
                this.ScoketPool.Destroy();
            }
        }
        /// <summary>
        /// relive server if this server is died
        /// </summary>
        private void Relive()
        {
            if (this.State == ServerState.Normal)
                return;
            lock (this)
            {
                if (this.State == ServerState.Normal)
                    return;
                logger.Notice("this server is relive!host:" + Address);
                this.State = ServerState.Normal;;
            }
        }

        private int ErrorCount = 0; //Continuous error count
        private bool Test()
        {
            if (ErrorCount > 50)
            {
                return false;
            }
            else
            {
                Interlocked.Increment(ref ErrorCount);
                return true;
            }
        }
        #endregion
    }
}
