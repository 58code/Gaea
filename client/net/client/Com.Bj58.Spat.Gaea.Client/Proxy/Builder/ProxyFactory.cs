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
using System.Collections;
namespace Com.Bj58.Spat.Gaea.Client.Proxy.Builder
{
	/// <summary>
	/// Factory for Proxy - creation.
	/// </summary>
	public static class ProxyFactory {

        private static Hashtable cache = new Hashtable();
		public static T Create<T>(string strUrl)
		{
            string key = strUrl.ToLower();
            object proxy = null;
            if (cache.ContainsKey(key))
            {
                proxy = cache[key];
            }
            if (proxy == null)
            {
                proxy = CreateStandardProxy(strUrl, typeof(T));
                if (proxy != null)
                    cache[key] = proxy;
            }
            return (T)proxy;
		}

		/// <summary>
		/// Creates proxy object using .NET - Remote proxy framework
		/// </summary>
		/// <param name="type">the interface the proxy class needs to implement</param>
		/// <param name="strUrl">the URL where the client object is located</param>
		/// <returns>a proxy to the object with the specified interface</returns>
		private static object CreateStandardProxy(string strUrl, Type type)
		{
            string serviceName = string.Empty, typeName=string.Empty;
            strUrl = strUrl.Replace("tcp://", "");
            string[] splits = strUrl.Split('/');
            if (splits.Length == 2)
            {
                serviceName = splits[0];
                typeName = splits[1];
            }
            return new ProxyStandard(type, serviceName, typeName).GetTransparentProxy();
		}
	}
}