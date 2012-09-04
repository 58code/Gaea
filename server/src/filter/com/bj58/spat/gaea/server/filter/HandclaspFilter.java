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

import com.bj58.spat.gaea.protocol.sdp.ExceptionProtocol;
import com.bj58.spat.gaea.protocol.sdp.HandclaspProtocol;
import com.bj58.spat.gaea.protocol.sfp.enumeration.PlatformType;
import com.bj58.spat.gaea.protocol.sfp.v1.Protocol;
import com.bj58.spat.gaea.server.contract.context.ExecFilterType;
import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.context.GaeaContext;
import com.bj58.spat.gaea.server.contract.context.GaeaResponse;
import com.bj58.spat.gaea.server.contract.context.SecureContext;
import com.bj58.spat.gaea.server.contract.context.ServerType;
import com.bj58.spat.gaea.server.contract.filter.IFilter;
import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;
import com.bj58.spat.gaea.server.secure.SecureKey;
import com.bj58.spat.gaea.server.secure.StringUtils;
import com.bj58.spat.gaea.server.util.ExceptionHelper;

/**
 * CheckNode
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class HandclaspFilter implements IFilter {
	
	private static ILog logger = LogFactory.getLogger(HandclaspFilter.class);
	
	@Override
	public int getPriority() {
		return 0;
	}

	/**
	 * 权限认证filter
	 */
	@Override
	public void filter(GaeaContext context) throws Exception {
		
		Protocol protocol = context.getGaeaRequest().getProtocol();		
		if(protocol.getPlatformType() == PlatformType.Java && context.getServerType() == ServerType.TCP){//java 客户端支持权限认证
			GaeaResponse response = new GaeaResponse();
			Global global = Global.getSingleton();
			//是否启用权限认证
			if(Global.getSingleton().getGlobalSecureIsRights()){
				SecureContext sc = global.getGlobalSecureContext(context.getChannel().getNettyChannel());
				//判断当前channel是否通过认证
				if(!sc.isRights()){
					//没有通过认证
					if(protocol != null && protocol.getSdpEntity() instanceof HandclaspProtocol){
						SecureKey sk = new SecureKey();
						HandclaspProtocol handclaspProtocol = (HandclaspProtocol)protocol.getSdpEntity();
						/**
						 * 接收 客户端公钥
						 */
						if("1".equals(handclaspProtocol.getType())){
							sk.initRSAkey();
							//客户端发送公钥数据
							String clientPublicKey = handclaspProtocol.getData();
							if(null == clientPublicKey || "".equals(clientPublicKey)){
								logger.warn("get client publicKey warn!");
							}
							//java 客户端
							if(protocol.getPlatformType() == PlatformType.Java){
								//服务器生成公/私钥,公钥传送给客户端
								sc.setServerPublicKey(sk.getStringPublicKey());
								sc.setServerPrivateKey(sk.getStringPrivateKey());
								sc.setClientPublicKey(clientPublicKey);
								handclaspProtocol.setData(sk.getStringPublicKey());//服务器端公钥
							}
							
							protocol.setSdpEntity(handclaspProtocol);
							response.setResponseBuffer(protocol.toBytes());
							context.setGaeaResponse(response);
							this.setInvokeAndFilter(context);
							logger.info("send server publieKey sucess!");
						} 
						/**
						 * 接收权限文件
						 */
						else if("2".equals(handclaspProtocol.getType())){
							//客户端加密授权文件
							String clientSecureInfo = handclaspProtocol.getData();
							if(null == clientSecureInfo || "".equals(clientSecureInfo)){
								logger.warn("get client secureKey warn!");
							}
							//授权文件客户端原文(服务器私钥解密)
							String sourceInfo = sk.decryptByPrivateKey(clientSecureInfo, sc.getServerPrivateKey());
							//校验授权文件是否相同
							//判断是否合法,如果合法服务器端生成DES密钥，通过客户端提供的公钥进行加密传送给客户端
							if(global.containsSecureMap(sourceInfo)){
								logger.info("secureKey is ok!");
								String desKey = StringUtils.getRandomNumAndStr(8);
								//设置当前channel属性
								sc.setDesKey(desKey);
								sc.setRights(true);
								handclaspProtocol.setData(sk.encryptByPublicKey(desKey, sc.getClientPublicKey()));
								protocol.setSdpEntity(handclaspProtocol);
								response.setResponseBuffer(protocol.toBytes());
								context.setGaeaResponse(response);
							}else{
								logger.error("It's bad secureKey!");
								this.ContextException(context, protocol, response, "授权文件错误!");
							}
							this.setInvokeAndFilter(context);
						}else{
							//权限认证 异常情况
							logger.error("权限认证异常!");
							this.ContextException(context, protocol, response, "权限认证 异常!");
						}
						response = null;
						sk = null;
						handclaspProtocol = null;
					}else{
						//客户端没有启动权限认证
						logger.error("客户端没有启用权限认证!");
						this.ContextException(context, protocol, response, "客户端没有启用权限认证!");
					}
				}
			}else{
				if(protocol != null && protocol.getSdpEntity() instanceof HandclaspProtocol){
					//异常--当前服务器没有启动权限认证
					logger.error("当前服务没有启用权限认证!");
					this.ContextException(context, protocol, response, "当前服务没有启用权限认证!");
				}
			}
		}
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
