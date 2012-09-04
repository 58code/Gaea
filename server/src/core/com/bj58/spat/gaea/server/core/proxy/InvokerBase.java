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

import com.bj58.spat.gaea.protocol.sdp.RequestProtocol;
import com.bj58.spat.gaea.protocol.sdp.ResponseProtocol;
import com.bj58.spat.gaea.protocol.sfp.v1.Protocol;
import com.bj58.spat.gaea.protocol.utility.KeyValuePair;
import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.context.IProxyStub;
import com.bj58.spat.gaea.server.contract.context.GaeaContext;
import com.bj58.spat.gaea.server.contract.context.GaeaResponse;
import com.bj58.spat.gaea.server.contract.context.StopWatch;
import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;
import com.bj58.spat.gaea.server.util.ErrorState;
import com.bj58.spat.gaea.server.util.ExceptionHelper;
import com.bj58.spat.gaea.server.util.ServiceFrameException;

/**
 * GaeaBinaryConvert
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class InvokerBase implements IInvokerHandle {

	/**
	 * log
	 */
	private final static ILog logger = LogFactory.getLogger(InvokerBase.class);
	
	/**
	 * 调用真实服务
	 * @param context
	 */
	void doInvoke(GaeaContext context) {
		logger.debug("------------------------------ begin request-----------------------------");

		StringBuffer sbInvokerMsg = new StringBuffer();
		StopWatch sw = context.getStopWatch();
		Object response = null;
		Protocol protocol = null;
		
		try {
		    protocol = context.getGaeaRequest().getProtocol();
			RequestProtocol request = (RequestProtocol)protocol.getSdpEntity();
			
			sbInvokerMsg.append("protocol version:");
			sbInvokerMsg.append(protocol.getVersion());
			sbInvokerMsg.append("\nfromIP:");
			sbInvokerMsg.append(context.getChannel().getRemoteIP());
			sbInvokerMsg.append("\nlookUP:");
			sbInvokerMsg.append(request.getLookup());
			sbInvokerMsg.append("\nmethodName:");
			sbInvokerMsg.append(request.getMethodName());
			sbInvokerMsg.append("\nparams:");
			
			if(request.getParaKVList() != null){
				for (KeyValuePair kv : request.getParaKVList()) {
					if(kv != null) {
						sbInvokerMsg.append("\n--key:");
						sbInvokerMsg.append(kv.getKey());
						sbInvokerMsg.append("\n--value:");
						sbInvokerMsg.append(kv.getValue());
					} else {
						logger.error("KeyValuePair is null  Lookup:" + request.getLookup() + "--MethodName:" + request.getMethodName());
					}
				}
			}
			
			logger.debug(sbInvokerMsg.toString());
			logger.debug("begin get proxy factory");
			
			// get local proxy
			IProxyStub localProxy = Global.getSingleton().getProxyFactory().getProxy(request.getLookup());
			logger.debug("proxyFactory.getProxy finish");

			if (localProxy == null) {
				ServiceFrameException sfe = new ServiceFrameException(
						"method:ProxyHandle.invoke--msg:" + request.getLookup() + "." + request.getMethodName() + " not fond",
						context.getChannel().getRemoteIP(), 
						context.getChannel().getLocalIP(), 
						request,
						ErrorState.NotFoundServiceException, 
						null);
				response = ExceptionHelper.createError(sfe);
				logger.error("localProxy is null", sfe);
			} else {
				logger.debug("begin localProxy.invoke");
				String swInvoderKey = "InvokeRealService_" + request.getLookup() + "." + request.getMethodName();
				sw.startNew(swInvoderKey, sbInvokerMsg.toString());
				sw.setFromIP(context.getChannel().getRemoteIP());
				sw.setLocalIP(context.getChannel().getLocalIP());
				
				//invoker real service
				GaeaResponse gaeaResponse = localProxy.invoke(context);
				
				sw.stop(swInvoderKey);
				
				logger.debug("end localProxy.invoke");
				context.setGaeaResponse(gaeaResponse);
				response = createResponse(gaeaResponse);
				logger.debug("localProxy.invoke finish");
			}
		} catch (ServiceFrameException sfe) {
			logger.error("ServiceFrameException when invoke service fromIP:" + context.getChannel().getRemoteIP() + "  toIP:" + context.getChannel().getLocalIP(), sfe);
			response = ExceptionHelper.createError(sfe);
			context.setError(sfe);
		} catch (Throwable e) {
			logger.error("Exception when invoke service fromIP:" + context.getChannel().getRemoteIP() + "  toIP:" + context.getChannel().getLocalIP(), e);
			response = ExceptionHelper.createError(e);
			context.setError(e);
		}
		
		protocol.setSdpEntity(response);
		logger.debug("---------------------------------- end --------------------------------");
	}
	
	
	/**
	 * create response message body
	 * @param gaeaResponse
	 * @return
	 */
	ResponseProtocol createResponse(GaeaResponse gaeaResponse) {
		if(gaeaResponse.getOutParaList()!= null && gaeaResponse.getOutParaList().size() > 0){
			int outParaSize = gaeaResponse.getOutParaList().size();
			Object[] objArray = new Object[outParaSize];
			for(int i=0; i<outParaSize; i++) {
				objArray[i] = gaeaResponse.getOutParaList().get(i).getOutPara();
			}
            return new ResponseProtocol(gaeaResponse.getReturnValue(), objArray);
        } else {
            return new ResponseProtocol(gaeaResponse.getReturnValue(), null);
        }
	}
}