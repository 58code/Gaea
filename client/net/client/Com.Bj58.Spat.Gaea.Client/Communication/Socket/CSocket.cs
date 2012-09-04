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
using System.Net;
using System.Linq;
using System.Threading;
using System.Collections;
using System.Net.Sockets;
using System.Diagnostics;
using System.Collections.Generic;
using Com.Bj58.Spat.Gaea.Client.Exceptions;
using Com.Bj58.Spat.Gaea.Client.Utility.Helper;
using Com.Bj58.Spat.Gaea.Client.Utility.Logger;
using Com.Bj58.Spat.Gaea.Client.Communication.Protocol.SFP;
using Com.Bj58.Spat.Gaea.Client.Configuration.Commmunication;

namespace Com.Bj58.Spat.Gaea.Client.Communication.Socket
{
    internal class CSocket : IDisposable
    {
        private LoggerBase logger = LoggerBase.GetLogger();
        private System.Net.Sockets.Socket socket;
        private SocketPool pool;
        private IPEndPoint endpoint;
        private BufferedStream inputStream;
        private SocketPoolProfile socketConfig;
        private Dictionary<int, WindowData> ReceiveWindows = new Dictionary<int, WindowData>();
        public CSocket(IPEndPoint _endPoint, SocketPool _pool, SocketPoolProfile config)
        {
            socketConfig = config;
            endpoint = _endPoint;
            pool = _pool;
            System.Net.Sockets.Socket _socket = new System.Net.Sockets.Socket(_endPoint.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
            _socket.SetSocketOption(SocketOptionLevel.Socket, SocketOptionName.SendTimeout, (int)config.SendTimeout.TotalMilliseconds);
            _socket.SetSocketOption(SocketOptionLevel.Socket, SocketOptionName.ReceiveTimeout, 24 * 3600 * 1000/*one day*/);//(int)config.ReceiveTimeout.TotalMilliseconds);
            _socket.SetSocketOption(SocketOptionLevel.Socket, SocketOptionName.NoDelay, !config.Nagle);
            _socket.Connect(_endPoint);
            socket = _socket;
            this.inputStream = new BufferedStream(new NetworkStream(this.socket), config.BufferSize);
            Thread th = new Thread(delegate() {
                while (true)
                {
                    try
                    {
                        Receive();
                    }
                    catch (Exception ex)
                    {
                        logger.Notice(ex.Message);
                    }
                }
            });
            th.Start();
        }
        public void Reset()
        {
            try
            {
                this.inputStream.Flush();
            }
            catch { }
        }
        public bool InPool
        {
            get;
            set;
        }
        public bool Connected
        {
            get { return socket.Connected; }
        }
        public void Disconnect(bool reuseSocket)
        {
            socket.Disconnect(reuseSocket);
        }

        public int Send(byte[] data)
        {
            ThreadPool.QueueUserWorkItem(delegate(object param)
            {
                byte[] sendData = new byte[data.Length + CONST.P_START_TAG.Length + CONST.P_END_TAG.Length];
                Array.Copy(CONST.P_START_TAG, 0, sendData, 0, CONST.P_START_TAG.Length);
                Array.Copy(data, 0, sendData, CONST.P_START_TAG.Length, data.Length);
                Array.Copy(CONST.P_END_TAG, 0, sendData, CONST.P_START_TAG.Length + data.Length, CONST.P_END_TAG.Length);
                SocketError error;
                int sendcount = socket.Send(sendData, 0, sendData.Length, SocketFlags.None, out error);
                if (sendcount > 0 && error == SocketError.Success)
                {
                    return;
                }
                else if (sendcount <= 0)
                {
                    logger.Error(String.Format("Failed to write to the socket '{0}'. Error: {1}", endpoint, error));
                    throw new IOException(String.Format("Failed to write to the socket '{0}'. Error: {1}", endpoint, error));
                }
                else if (error == SocketError.TimedOut)
                {
                    logger.Warn("socket send timeout! host:" + endpoint.Address);
                    throw new SendTimeoutException();
                }
                else
                {
                    logger.Error("socket exception,error:" + error.ToString() + " Code:" + (int)error + ",host:" + endpoint.Address);
                    throw new OtherException("socket exception,error:" + error.ToString() + " Code:" + (int)error);
                }
            });
            return data.Length;
        }

        private void Receive()
        {
            Stopwatch watcher = new Stopwatch();
            watcher.Start();
            IList<byte> pData = new List<byte>();

            //find start tag
            byte[] stag = new byte[CONST.P_START_TAG.Length];
            Read(stag, 0, CONST.P_START_TAG.Length);
            if (!ArrayHelper.Equals<byte>(stag, CONST.P_START_TAG))
            {
                while (true)
                {
                    byte b = (byte)inputStream.ReadByte();
                    ArrayHelper.LeftMove<byte>(stag, 1);
                    stag[stag.Length - 1] = b;
                    if (ArrayHelper.Equals<byte>(stag, CONST.P_START_TAG))
                        break;
                }
            }

            //read version
            byte[] bVersion = new byte[SFPStruct.Version];
            Read(bVersion, 0, SFPStruct.Version);
            if (bVersion[0] == 0)
            {
                logger.Error("protocol error.");
                throw new ProtocolException();
            }
            Array.ForEach(bVersion, p => pData.Add(p));

            //read total length
            byte[] bTotalLen = new byte[SFPStruct.TotalLen];
            Read(bTotalLen, 0, SFPStruct.TotalLen);
            Array.ForEach(bTotalLen, p => pData.Add(p));
            int totalLen = BitConverter.ToInt32(bTotalLen, 0);

            //read sessionid
            byte[] bSessionId = new byte[SFPStruct.SessionId];
            Read(bSessionId, 0, SFPStruct.SessionId);
            Array.ForEach(bSessionId, p => pData.Add(p));
            int pSessionId = BitConverter.ToInt32(bSessionId, 0);

            //read other data
            byte[] otherData = new byte[totalLen - SFPStruct.Version - SFPStruct.TotalLen - SFPStruct.SessionId];
            Read(otherData, 0, otherData.Length);
            Array.ForEach(otherData, p => pData.Add(p));

            //read end tag and check
            byte[] etag = new byte[CONST.P_END_TAG.Length];
            Read(etag, 0, CONST.P_END_TAG.Length);
            if (!ArrayHelper.Equals(etag, CONST.P_END_TAG))
            {
                throw new Exception("not find end tag from stream!");
            }
            WindowData channelData;
            if (ReceiveWindows.TryGetValue(pSessionId, out channelData))
            {
                channelData.Data = pData.ToArray();
                channelData.Event.Set();
            }
            logger.Debug("total receive time:" + watcher.ElapsedMilliseconds + "ms");
        }

        public byte[] Receive(int sessionId)
        {
            AutoResetEvent ReceiveEvent = new AutoResetEvent(false);
            var channelData = new WindowData(ReceiveEvent);
            ReceiveWindows[sessionId] = channelData;
            try
            {
                if (!ReceiveEvent.WaitOne(socketConfig.ReceiveTimeout))
                {
                    throw new Com.Bj58.Spat.Gaea.Client.Exceptions.TimeoutException("Receive data timeout!");
                    
                }
            }
            finally
            {
                ReceiveWindows.Remove(sessionId);
            }
            return channelData.Data;
        }

        public void Close()
        {
            pool.Release(this);
        }

        public void Read(byte[] buffer, int offset, int count)
        {
            int read = 0;
            int shouldRead = count;

            while (read < count)
            {
                //int currentRead = this.socket.Receive(buffer, offset, shouldRead, SocketFlags.None);
                int currentRead = this.inputStream.Read(buffer, offset, shouldRead);
                if (currentRead < 1)
                    continue;
                read += currentRead;
                offset += currentRead;
                shouldRead -= currentRead;
            }
        }

        #region IDisposable 成员

        public void Dispose()
        {
            Dispose(false);
        }

        public void Dispose(bool flag)
        {
            if (flag)
            {
                pool.Destroy(this);
                inputStream.Close();
                inputStream.Dispose();
            }
            else
            {
                Close();
            }
        }

        #endregion
    }
    public class WindowData
    {
        public WindowData(AutoResetEvent even)
        {
            Event = even;
        }
        public AutoResetEvent Event
        {
            get;
            set;
        }
        public byte[] Data
        {
            get;
            set;
        }
    }
}
