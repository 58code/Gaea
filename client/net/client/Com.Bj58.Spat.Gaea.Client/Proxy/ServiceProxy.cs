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
using System.Text;
using System.Linq;
using System.Collections;
using System.Diagnostics;
using System.Collections.Generic;
using Com.Bj58.Spat.Gaea.Client.Loadbalance;
using Com.Bj58.Spat.Gaea.Client.Utility.Logger;
using Com.Bj58.Spat.Gaea.Client.Proxy.Component;
using Com.Bj58.Spat.Gaea.Client.Communication.Protocol.SDP;
using Com.Bj58.Spat.Gaea.Client.Communication.Protocol.SFP;
using Com.Bj58.Spat.Gaea.Client.Communication.Protocol.SFP.Enum;

namespace Com.Bj58.Spat.Gaea.Client.Proxy
{
    public class ServiceProxy
    {
        private LoggerBase logger = LoggerBase.GetLogger();
        private string _ServiceName;
        private Loadbalance.Dispatcher dispatcher;
        private Configuration.ServiceConfig config;
        private int ThreadCount;
        private ServiceProxy(string serviceName)
        {
            _ServiceName = serviceName;
            config = Configuration.ServiceConfig.GetConfig(serviceName);
            dispatcher = new Com.Bj58.Spat.Gaea.Client.Loadbalance.Dispatcher(config);
        }

        private static IDictionary<string, ServiceProxy> Proxys = new Dictionary<string, ServiceProxy>();
        private static object LockHelper = new object();
        public static ServiceProxy GetProxy(string serviceName)
        {
            ServiceProxy p;
            if (!Proxys.TryGetValue(serviceName.ToLower(), out p))
            {
                lock (LockHelper)
                {
                    if (!Proxys.TryGetValue(serviceName.ToLower(), out p))
                    {
                        p = new ServiceProxy(serviceName);
                        Proxys[serviceName.ToLower()] = p;
                    }
                }
            }
            return p;
        }
        public InvokeResult Invoke(Type returnType, string typeName, string methodName, params Parameter[] paras)
        {
            logger.Debug("current thread count:" + ThreadCount);
            if (ThreadCount > config.MaxThreadCount)
            {
                logger.Error("server is too busy(" + ThreadCount + ").Service:" + _ServiceName);
                throw new Exception("server is too busy.(" + _ServiceName + ")");
            }
            try
            {
                System.Threading.Interlocked.Increment(ref ThreadCount);
                Stopwatch watcher = new Stopwatch();
                watcher.Start();
                List<RpParameter> listPara = new List<RpParameter>();
                IList<Type> outParaTypes = new List<Type>();
                Array.ForEach(paras, delegate(Parameter p)
                {
                    listPara.Add(new RpParameter(TypeMapping.ParseType(p.DataType.ToString()), p.Para));
                    if (p.ParaType == Com.Bj58.Spat.Gaea.Client.Proxy.Enum.ParaType.Out)
                        outParaTypes.Add(p.DataType);
                });

                RequestProtocol requestProtocol = new RequestProtocol(typeName, methodName, listPara.ToArray());

                byte[] userData = config.Protocol.Serializer.Serialize(requestProtocol);
                Protocol sendP = new Protocol(config.Protocol, CreateSessionId(), UserDataType.Request, userData);

                var server = dispatcher.GetServer();
                if (server == null)
                {
                    logger.Error("can't get server");
                    throw new Exception("can't get server");
                }
                Protocol receiveP = server.Request(sendP);//request server
                if (receiveP.UserDataType == UserDataType.Response)
                {
                    var rpGtype = returnType;
                    if (returnType == typeof(void))
                    {
                        rpGtype = typeof(VOID);
                    }
                    var rpType = typeof(ResponseProtocol<>).MakeGenericType(rpGtype);
                    var rp = config.Protocol.Serializer.Deserialize(receiveP.UserData, rpType);
                    object result = rpType.GetProperty("Result").GetValue(rp, null);
                    object[] outPara = (object[])rpType.GetProperty("Outpara").GetValue(rp, null);
                    logger.Debug("invoke time:" + watcher.ElapsedMilliseconds + "ms");
                    return new InvokeResult(result, outPara);
                }
                else if (receiveP.UserDataType == UserDataType.Exception)
                {
                    var ep = config.Protocol.Serializer.Deserialize<ExceptionProtocol>(receiveP.UserData);
                    throw ExceptionProvider.ThrowServiceError(ep.ErrorCode, ep.ErrorMsg);
                }
                else
                {
                    logger.Error("userdatatype error!");
                    throw new Exception("userdatatype error!");
                }
            }
            finally
            {
                System.Threading.Interlocked.Decrement(ref ThreadCount);
            }
        }
        /// <summary>
        /// invoke method at remote server
        /// </summary>
        /// <typeparam name="T">results data type</typeparam>
        /// <param name="typeName">invoked class name.</param>
        /// <param name="methodName">invoked method name.</param>
        /// <param name="paras">method parameters</param>
        /// <returns>InvokeResult object</returns>
        public InvokeResult<T> Invoke<T>(string typeName, string methodName, params Parameter[] paras)
        {
            var result = Invoke(typeof(T), typeName, methodName, paras);
            InvokeResult<T> r = new InvokeResult<T>(result.Result, result.OutPara);
            return r;
        }
        /// <summary>
        /// get server info
        /// </summary>
        /// <param name="name">server name</param>
        /// <returns>if server exist return server info else return empty</returns>
        public string GetServer(string name)
        {
            var server = dispatcher.GetServer(name);
            if (server == null)
                return string.Empty;
            return server.ToString();
        }
        public string GetServer()
        {
            var servers = config.Servers;
            StringBuilder sb = new StringBuilder();
            sb.AppendLine("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
            sb.AppendLine("<Service name=\"" + _ServiceName + "\">");
            foreach (var s in servers)
            {
                var server = dispatcher.GetServer(s.Name);
                sb.AppendLine("<Server name=\"" + server.Name + "\">");
                sb.Append("<State>" + server.State.ToString() + "</State>");
                sb.Append("<Address>" + server.Address + "</Address>");
                sb.Append("<Port>" + server.Port + "</Port>");
                sb.Append("<Weight>" + server.Weight + "</Weight>");
                sb.Append("<CurrUserCount>" + server.CurrUserCount + "</CurrUserCount>");
                sb.Append("<ScoketPool>" + server.ScoketPool == null ? "0" : server.ScoketPool.Count.ToString() + "</ScoketPool>");
            }
            sb.AppendLine("</Service>");
            return sb.ToString();
        }
        private int sessionId = 1;
        private int CreateSessionId()
        {
            lock (this)
            {
                if (sessionId > CONST.MAX_SESSIONID)
                {
                    sessionId = 1;
                }
                return sessionId++;
            }
        }
    }
    /// <summary>
    /// invoke result
    /// </summary>
    /// <typeparam name="T">result data type</typeparam>
    public class InvokeResult<T>
    {
        internal InvokeResult(object result, object[] outPara)
        {
            Result = (T)result;
            OutPara = outPara;
        }
        public T Result
        {
            get;
            set;
        }

        public object[] OutPara
        {
            get;
            set;
        }
    }
    public class InvokeResult:InvokeResult<object>
    {
        internal InvokeResult(object result, object[] outPara)
            : base(result, outPara)
        {

        }
    }
}
