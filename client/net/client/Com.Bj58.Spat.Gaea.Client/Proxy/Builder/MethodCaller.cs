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
using System.Collections.Generic;
using System.Reflection;
using Com.Bj58.Spat.Gaea.Client.Proxy.Component;

namespace Com.Bj58.Spat.Gaea.Client.Proxy.Builder
{
	internal class MethodCaller
	{

        private string _serviceName;
        private string _type;

		/// <summary>
		/// Constructor
		/// </summary>
		/// <param name="ProxyFactory">ProxyFactory - Instance</param>
		/// <param name="uri">Server-Proxy uri</param>
		public MethodCaller(string serviceName,string type)
		{
            this._serviceName = serviceName;
            this._type = type;
		}

		/// <summary>
		/// This method wrapps an instance call
		/// </summary>
		/// <param name="methodInfo">The method to call</param>
		/// <param name="arrMethodArgs">The arguments to the method call</param>
		public object DoMethodCall(object[] arrMethodArgs, MethodInfo methodInfo)
		{   
            ParameterInfo[] pis = methodInfo.GetParameters();
            if ((arrMethodArgs.Length != pis.Length))
            {
                throw new ArgumentException("Arguments count not match!");
            }
            var proxy = ServiceProxy.GetProxy(_serviceName);
            Parameter[] paras = new Parameter[arrMethodArgs.Length];
            List<int> outParas = new List<int>();
            for (int i = 0; i < pis.Length; i++)
            {
                var pi = pis[i];
                if (pi.IsOut || pi.ParameterType.IsByRef)
                {
                    paras[i] = new Parameter(arrMethodArgs[i], pi.ParameterType.GetElementType(), Com.Bj58.Spat.Gaea.Client.Proxy.Enum.ParaType.Out);
                    outParas.Add(i);
                }
                else
                {
                    paras[i] = new Parameter(arrMethodArgs[i], pi.ParameterType, Com.Bj58.Spat.Gaea.Client.Proxy.Enum.ParaType.In);
                }
            }
            string methodName = methodInfo.Name;
            var atts = methodInfo.GetCustomAttributes(typeof(Com.Bj58.Spat.Gaea.OperationContractAttribute), true);
            if (atts != null && atts.Length > 0)
            {
                OperationContractAttribute ma = (OperationContractAttribute)atts[0];
                if (!string.IsNullOrEmpty(ma.Name))
                {
                    methodName = ma.Name;
                }
                methodName = "$" + methodName;
            }
            var result = proxy.Invoke(methodInfo.ReturnType, _type, methodName, paras);
            for (int i = 0; i < outParas.Count && i < result.OutPara.Length; i++)
            {
                arrMethodArgs[outParas[i]] = result.OutPara[i];
            }
            return result.Result;
		}
    }
}
