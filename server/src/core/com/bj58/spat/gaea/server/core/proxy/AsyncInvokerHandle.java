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
package com.bj58.spat.gaea.server.core.proxy;

import com.bj58.spat.gaea.protocol.sfp.v1.Protocol;
import com.bj58.spat.gaea.server.contract.context.ExecFilterType;
import com.bj58.spat.gaea.server.contract.context.GaeaContext;
import com.bj58.spat.gaea.server.contract.context.GaeaResponse;
import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.context.SecureContext;
import com.bj58.spat.gaea.server.contract.context.ServerType;
import com.bj58.spat.gaea.server.contract.filter.IFilter;
import com.bj58.spat.gaea.server.contract.http.HttpThreadLocal;
import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;
import com.bj58.spat.gaea.server.util.ExceptionHelper;
import com.bj58.spat.sfft.utility.async.AsyncInvoker;
import com.bj58.spat.sfft.utility.async.IAsyncHandler;


/**
 * async service invoke handle
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class AsyncInvokerHandle extends InvokerBase {
	/**
	 * log
	 */
	private final static ILog logger = LogFactory.getLogger(AsyncInvokerHandle.class);
	/**
	 * 异步执行器
	 */
	private AsyncInvoker asyncInvoker;
	private HttpThreadLocal httpThreadLocal;
	private int taskTimeOut = 1000;
	
	public AsyncInvokerHandle() {
		try {
			httpThreadLocal = HttpThreadLocal.getInstance();
			int workerCount = Global.getSingleton().getServiceConfig().getInt("gaea.async.worker.count");
			if(workerCount > 0) {
				asyncInvoker = AsyncInvoker.getInstance(workerCount);
			} else  {
				asyncInvoker = AsyncInvoker.getInstance();
			}
			String sTaskTimeOut = Global.getSingleton().getServiceConfig().getString("gaea.server.tcp.task.timeout");
			if(sTaskTimeOut != null && !"".equals(sTaskTimeOut)){
				taskTimeOut = Integer.parseInt(sTaskTimeOut);
			}
			logger.info("async worker count:" + workerCount);
		} catch (Exception e) {
			logger.error("init AsyncInvokerHandle error", e);
		}
	}
	
	@Override
	public void invoke(final GaeaContext context) throws Exception {
		logger.debug("-------------------begin async invoke-------------------");
		asyncInvoker.run(taskTimeOut, new IAsyncHandler(){
			@Override
			public Object run() throws Throwable {
				logger.debug("begin request filter");
				// request filter
				for(IFilter f : Global.getSingleton().getGlobalRequestFilterList()) {
					if(context.getExecFilter() == ExecFilterType.All || context.getExecFilter() == ExecFilterType.RequestOnly) {
						f.filter(context);
					}
				}
				
				if(context.isDoInvoke()) {
					if(context.getServerType() == ServerType.HTTP){
						httpThreadLocal.set(context.getHttpContext());
					}
					doInvoke(context);
				}
				
				logger.debug("begin response filter");
				// response filter
				for(IFilter f : Global.getSingleton().getGlobalResponseFilterList()) {
					if(context.getExecFilter() == ExecFilterType.All || context.getExecFilter() == ExecFilterType.ResponseOnly) {
						f.filter(context);
					}
				}
				return context;
			}
			
			@Override
			public void messageReceived(Object obj) {
				if(context.getServerType() == ServerType.HTTP){
					httpThreadLocal.remove();
				}
				if(obj != null) {
					GaeaContext ctx = (GaeaContext)obj;
					ctx.getServerHandler().writeResponse(ctx);
				} else {
					logger.error("context is null!");
				}
			}
			
			@Override
			public void exceptionCaught(Throwable e) {
				if(context.getServerType() == ServerType.HTTP){
					httpThreadLocal.remove();
				}
				
				if(context.getGaeaResponse() == null){
					GaeaResponse respone = new GaeaResponse();
					context.setGaeaResponse(respone);
				}
				
				try {
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
					
					Protocol protocol = context.getGaeaRequest().getProtocol();
					if(protocol == null){
						protocol = Protocol.fromBytes(context.getGaeaRequest().getRequestBuffer(),global.getGlobalSecureIsRights(),desKeyByte);
						context.getGaeaRequest().setProtocol(protocol);
					}
					protocol.setSdpEntity(ExceptionHelper.createError(e));
					context.getGaeaResponse().setResponseBuffer(protocol.toBytes(Global.getSingleton().getGlobalSecureIsRights(),desKeyByte));
				} catch (Exception ex) {
					context.getGaeaResponse().setResponseBuffer(new byte[]{0});
					logger.error("AsyncInvokerHandle invoke-exceptionCaught error", ex);
				}
				
				context.getServerHandler().writeResponse(context);
				logger.error("AsyncInvokerHandle invoke error", e);
			}
		});
	}
}