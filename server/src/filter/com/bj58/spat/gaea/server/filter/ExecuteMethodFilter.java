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
package com.bj58.spat.gaea.server.filter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.bj58.spat.gaea.protocol.sdp.ExceptionProtocol;
import com.bj58.spat.gaea.protocol.sdp.RequestProtocol;
import com.bj58.spat.gaea.protocol.sfp.enumeration.PlatformType;
import com.bj58.spat.gaea.protocol.sfp.v1.Protocol;
import com.bj58.spat.gaea.protocol.utility.KeyValuePair;
import com.bj58.spat.gaea.server.contract.context.ExecFilterType;
import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.context.GaeaContext;
import com.bj58.spat.gaea.server.contract.context.GaeaResponse;
import com.bj58.spat.gaea.server.contract.context.SecureContext;
import com.bj58.spat.gaea.server.contract.context.ServerType;
import com.bj58.spat.gaea.server.contract.filter.IFilter;
import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;
import com.bj58.spat.gaea.server.util.ExceptionHelper;

/**
 * CheckNode
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ExecuteMethodFilter implements IFilter  {
	
	private static ILog logger = LogFactory.getLogger(ExecuteMethodFilter.class);
	
	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void filter(GaeaContext context) throws Exception {
		
		Global global = Global.getSingleton();
		Protocol p = context.getGaeaRequest().getProtocol();
		GaeaResponse response = new GaeaResponse();
		
		if(p.getPlatformType() == PlatformType.Java && context.getServerType() == ServerType.TCP){
			//当前服务启动权限认证,并且当前channel通过校验，则进行方法校验
			SecureContext securecontext = global.getGlobalSecureContext(context.getChannel().getNettyChannel());
			if(global.getGlobalSecureIsRights()){
				//当前服务启用权限认证,判断当前channel是否通过授权
				if(securecontext.isRights()){
					RequestProtocol request = (RequestProtocol)p.getSdpEntity();
					if(request != null){
						StringBuffer buff = new StringBuffer(request.getLookup() + "." +request.getMethodName());//接口实现类.方法名(参数序列)
						buff.append("(");
						List<KeyValuePair> list = request.getParaKVList();
						if(list != null){
							int i=0;
							for(KeyValuePair k : list){
								if(k != null){
									if(i > 0){
										buff.append(",");
									}
									buff.append(k.getKey());
									++i;
								}
							}
						}
						buff.append(")");
						
						boolean bool = true;
						Map map = global.getSecureMap();
						if(map != null){
							Iterator<Map.Entry<String, List<String>>> iter = map.entrySet().iterator();
							while(iter.hasNext()){
								Map.Entry<String, List<String>> enty = (Map.Entry<String, List<String>>)iter.next();
								for(String str:enty.getValue()){
									if(str.equalsIgnoreCase(buff.toString())){
										bool = false;
										break;
									}
								}
							}
						}
						
						if(bool){
							logger.error("当前调用方法没有授权!");
							this.ContextException(context, p, response, "当前调用方法没有授权!",global.getGlobalSecureIsRights(),securecontext.getDesKey().getBytes("utf-8"));
						}
					}
				}else{
					logger.error("当前连接没有通过权限认证!");
					this.ContextException(context, p, response, "当前连接没有通过权限认证!");
				}
			}
		}
	}
	
	public void ContextException(GaeaContext context,Protocol protocol,GaeaResponse response,String message,boolean bool,byte[] key) throws Exception{
		ExceptionProtocol ep = ExceptionHelper.createError(new Exception());
		ep.setErrorMsg(message);
		protocol.setSdpEntity(ep);
		response.setResponseBuffer(protocol.toBytes(bool,key));
		context.setGaeaResponse(response);
		this.setInvokeAndFilter(context);
	}
	
	public void ContextException(GaeaContext context,Protocol protocol,GaeaResponse response,String message) throws Exception{
		ExceptionProtocol ep = ExceptionHelper.createError(new Exception());
		ep.setErrorMsg(message);
		protocol.setSdpEntity(ep);
		response.setResponseBuffer(protocol.toBytes());
		context.setGaeaResponse(response);
		this.setInvokeAndFilter(context);
	}
	
	public void setInvokeAndFilter(GaeaContext context){
		context.setExecFilter(ExecFilterType.None);
		context.setDoInvoke(false);
	}
}
