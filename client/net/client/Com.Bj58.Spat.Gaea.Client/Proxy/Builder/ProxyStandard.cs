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
using System.Reflection;
using System.Runtime.Remoting;
using System.Runtime.Remoting.Messaging;
using System.Runtime.Remoting.Proxies;

namespace Com.Bj58.Spat.Gaea.Client.Proxy.Builder
{
	public class ProxyStandard :  RealProxy, IRemotingTypeInfo, IProxyStandard
	{
		private Type m_proxyType = null;
		private MethodCaller m_methodCaller = null;

		/// <summary>
		/// Constructor
		/// </summary>
		/// <param name="proxyType">Interface type that has to be proxied</param>
		/// <param name="ProxyFactory">ServiceProxyFactory - Instance</param>
		/// <param name="uri">Server-Proxy uri</param>
		public ProxyStandard(Type proxyType,string serviceName,string type) : base(typeof(IProxyStandard))
		{
			this.m_proxyType = proxyType;	
			this.m_methodCaller = new MethodCaller(serviceName,type);
		}

		/// <summary>
		/// This reflective method for invoking methods. Overriden from RealProxy.
		/// Handles the object invocation. This method wrapps an instance call to the service 
		/// requests, sends it to the service and translates the reply of this call to the C# - data type
		/// <see cref="System.Runtime.Remoting.Proxies.RealProxy"/>
		/// </summary>
		/// <param name="msg"></param>
		/// <returns></returns>
		public override IMessage Invoke(IMessage msg)
		{
            // Convert to a MethodCallMessage
            IMethodCallMessage methodMessage = new MethodCallMessageWrapper((IMethodCallMessage)msg);
            MethodInfo methodInfo = this.GetMethodInfoForMethodBase(methodMessage);

            var ps = methodInfo.GetParameters();
            Type[] argumentTypes = new Type[ps.Length];
            for(int i=0;i<ps.Length;i++)
            {
                argumentTypes[i] = ps[i].ParameterType;
            }
            object objReturnValue = null;
            if (methodInfo != null)
            {
                if (methodInfo.Name.Equals("Equals") && argumentTypes != null &&
                    argumentTypes.Length == 1 && argumentTypes[0].IsAssignableFrom((typeof(Object))))
                {
                    Object value = methodMessage.Args[0];
                    if (value == null)
                    {
                        objReturnValue = false;
                    }
                    else
                    {
                        objReturnValue = false;
                    }
                }
                else if(methodInfo.Name.Equals("GetHashCode") && argumentTypes.Length == 0)
                {
                    objReturnValue = msg.GetHashCode();
                }
                else if (methodInfo.Name.Equals("ToString") && argumentTypes.Length == 0)
                {
                    objReturnValue = this.ToString();
                }
                else if (methodInfo.Name.Equals("GetType") && argumentTypes.Length == 0)
                {
                    objReturnValue = this.m_proxyType;
                }
                else
                {
                    objReturnValue = this.m_methodCaller.DoMethodCall(methodMessage.Args, methodInfo);
                }
            }
            else
            {
                if (methodMessage.MethodName.Equals("GetType") && (methodMessage.ArgCount == 0))
                {
                    objReturnValue = this.m_proxyType;
                }
            }
           
            // Create the return message (ReturnMessage)
            return new ReturnMessage(objReturnValue, methodMessage.Args, methodMessage.ArgCount, methodMessage.LogicalCallContext, methodMessage);
		}
		/// <summary>
		/// Checks whether the proxy representing the specified object 
		/// type can be cast to the type represented by the IRemotingTypeInfo interface
		/// </summary>
		/// <param name="fromType">Cast - Type</param>
		/// <param name="obj">Proxy object</param>
		/// <returns>True if the cast type equals or is assingable from the interface type,
		/// wich was used for proxy initialization
		/// </returns>
		public bool CanCastTo(Type fromType, object obj)
		{
			return fromType.Equals(this.m_proxyType) || fromType.IsAssignableFrom(this.m_proxyType);
		}

		/// <summary>
		/// Gets the name of the interface type, 
		/// that has to be proxied 
		/// </summary>
		public string TypeName
		{
			get { return m_proxyType.Name; }
			set {  }
		}
		/// <summary>
		/// Gets method info instance, according to the given method base
		/// </summary>
		/// <param name="methodMessage">Method message, that describes the method call</param>
		/// <returns>MethodInfo - Instance</returns>
		private MethodInfo GetMethodInfoForMethodBase(IMethodCallMessage methodMessage)
		{
            return (MethodInfo)methodMessage.MethodBase;
		}
	}
}
