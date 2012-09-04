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

import com.bj58.spat.gaea.protocol.sdp.ResetProtocol;
import com.bj58.spat.gaea.protocol.sfp.enumeration.PlatformType;
import com.bj58.spat.gaea.protocol.sfp.v1.Protocol;
import com.bj58.spat.gaea.server.contract.context.ExecFilterType;
import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.context.GaeaContext;
import com.bj58.spat.gaea.server.contract.context.GaeaResponse;
import com.bj58.spat.gaea.server.contract.context.SecureContext;
import com.bj58.spat.gaea.server.contract.context.ServerStateType;
import com.bj58.spat.gaea.server.contract.context.ServerType;
import com.bj58.spat.gaea.server.contract.filter.IFilter;

/**
 * A filter for parse protocol from byte[]
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ProtocolParseFilter implements IFilter {

	@Override
	public void filter(GaeaContext context) throws Exception {
		if(context.getServerType() == ServerType.TCP) {
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
			Protocol protocol = Protocol.fromBytes(context.getGaeaRequest().getRequestBuffer(),global.getGlobalSecureIsRights(),desKeyByte);
			context.getGaeaRequest().setProtocol(protocol);
			/**
			 * 服务重启直接返回
			 */	
			if(Global.getSingleton().getServerState() == ServerStateType.Reboot && protocol.getPlatformType() == PlatformType.Java){
				GaeaResponse response = new GaeaResponse();
				ResetProtocol rp = new ResetProtocol();
				rp.setMsg("This server is reboot!");
				protocol.setSdpEntity(rp);
				response.setResponseBuffer(protocol.toBytes(global.getGlobalSecureIsRights(),desKeyByte));
				context.setGaeaResponse(response);
				context.setExecFilter(ExecFilterType.None);
				context.setDoInvoke(false);
			}
		}
	}

	@Override
	public int getPriority() {
		return 50;
	}

}
