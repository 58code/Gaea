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
package com.bj58.spat.gaea.server.contract.context;

import com.bj58.spat.gaea.protocol.sdp.RequestProtocol;
import com.bj58.spat.gaea.server.contract.http.HttpContext;
import com.bj58.spat.gaea.server.contract.server.IServerHandler;

/**
 * gaea request/response context
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class GaeaContext {
	
	private boolean monitor;
	
	private StopWatch stopWatch = new StopWatch();
	
	private GaeaRequest gaeaRequest = new GaeaRequest();
	
	private GaeaResponse gaeaResponse;
	
	private ServerType serverType;
	
	private Throwable error;
	
	private GaeaChannel channel;
	
	private IServerHandler serverHandler;
	
	private boolean isDoInvoke = true;
	
	private HttpContext httpContext;//http 上下文内容
	
	private ExecFilterType execFilter = ExecFilterType.All;
	
	public GaeaContext() {
		
	}
	
	public GaeaContext(GaeaChannel channel) {
		this.setChannel(channel);
	}

	public GaeaContext(byte[] requestBuffer, 
			GaeaChannel channel, 
			ServerType serverType,
			IServerHandler handler) throws Exception {
		
		this.gaeaRequest.setRequestBuffer(requestBuffer);
		this.setChannel(channel);
		this.setServerType(serverType);
		this.setServerHandler(handler);
	}
	
	/**
	 * 从ThreadLocal里获取GaeaContext
	 * @return
	 */
	public static GaeaContext getFromThreadLocal() {
		return Global.getSingleton().getThreadLocal().get();
	}
	
	public boolean isMonitor() {
		return monitor;
	}

	public void setMonitor(boolean monitor) {
		this.monitor = monitor;
	}

	public GaeaRequest getGaeaRequest() {
		return gaeaRequest;
	}

	public void setGaeaRequest(GaeaRequest gaeaRequest) {
		this.gaeaRequest = gaeaRequest;
		
		RequestProtocol r = (RequestProtocol)gaeaRequest.getProtocol().getSdpEntity();
		this.stopWatch.setLookup(r.getLookup());
		this.stopWatch.setMethodName(r.getMethodName());
	}

	public GaeaResponse getGaeaResponse() {
		return gaeaResponse;
	}

	public void setGaeaResponse(GaeaResponse gaeaResponse) {
		this.gaeaResponse = gaeaResponse;
	}

	public StopWatch getStopWatch() {
		return stopWatch;
	}

	public void setDoInvoke(boolean isDoInvoke) {
		this.isDoInvoke = isDoInvoke;
	}

	public boolean isDoInvoke() {
		return isDoInvoke;
	}

	public void setError(Throwable error) {
		this.error = error;
	}

	public Throwable getError() {
		return error;
	}

	public void setServerType(ServerType requestType) {
		this.serverType = requestType;
	}

	public ServerType getServerType() {
		return serverType;
	}

	public void setServerHandler(IServerHandler responseHandler) {
		this.serverHandler = responseHandler;
	}

	public IServerHandler getServerHandler() {
		return serverHandler;
	}

	public void setChannel(GaeaChannel channel) {
		this.channel = channel;
	}

	public GaeaChannel getChannel() {
		return channel;
	}

	public void setExecFilter(ExecFilterType execFilter) {
		this.execFilter = execFilter;
	}

	public ExecFilterType getExecFilter() {
		return execFilter;
	}
	
	public HttpContext getHttpContext() {
		return httpContext;
	}

	public void setHttpContext(HttpContext httpContext) {
		this.httpContext = httpContext;
	}
}