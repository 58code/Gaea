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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bj58.spat.gaea.protocol.sfp.v1.Protocol;
import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.context.GaeaContext;
import com.bj58.spat.gaea.server.contract.context.GaeaResponse;
import com.bj58.spat.gaea.server.contract.context.SecureContext;
import com.bj58.spat.gaea.server.contract.context.ServerType;
import com.bj58.spat.gaea.server.contract.filter.IFilter;

/**
 * A filter for create protocol from byte[]
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ProtocolCreateFilter implements IFilter {
	
	private static final Log logger = LogFactory.getLog(ProtocolCreateFilter.class);
	
	@Override
	public void filter(GaeaContext context) throws Exception {
		try{
			if(context.getServerType() == ServerType.TCP) {
				Protocol protocol = context.getGaeaRequest().getProtocol();
				byte[] desKeyByte = null;
				String desKeyStr = null;
				boolean bool = false;
				
				Global global = Global.getSingleton();
				if(global != null){
					//判断当前服务启用权限认证
					if(global.getGlobalSecureIsRights()){
						SecureContext securecontext = global.getGlobalSecureContext(context.getChannel().getNettyChannel());
						bool = securecontext.isRights();
						if(bool){
							desKeyStr = securecontext.getDesKey();
						}
					}
				}
				
				if(desKeyStr != null){
					desKeyByte = desKeyStr.getBytes("utf-8");
				}
				
				if(context.getGaeaResponse() == null){
					GaeaResponse respone = new GaeaResponse();
					context.setGaeaResponse(respone);
				}
		
				context.getGaeaResponse().setResponseBuffer(protocol.toBytes(Global.getSingleton().getGlobalSecureIsRights(),desKeyByte));
			}
		}catch(Exception ex){
			logger.error("Server ProtocolCreateFilter error!");
		}
	}

	@Override
	public int getPriority() {
		return 50;
	}

}