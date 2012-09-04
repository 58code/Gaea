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
package com.bj58.spat.gaea.server.core.communication.tcp;

import java.nio.ByteBuffer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.bj58.spat.gaea.protocol.utility.ProtocolConst;
import com.bj58.spat.gaea.protocol.utility.ProtocolHelper;
import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.context.GaeaChannel;
import com.bj58.spat.gaea.server.contract.context.GaeaContext;
import com.bj58.spat.gaea.server.contract.context.SecureContext;
import com.bj58.spat.gaea.server.contract.context.ServerType;
import com.bj58.spat.gaea.server.contract.filter.IFilter;
import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;
import com.bj58.spat.gaea.server.contract.server.IServerHandler;
import com.bj58.spat.gaea.server.util.ExceptionHelper;

/**
 * netty event handler
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
@ChannelPipelineCoverage("all")
public class SocketHandler extends SimpleChannelUpstreamHandler implements IServerHandler {
	
	private static ILog logger = LogFactory.getLogger(SocketHandler.class);
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		try {
			logger.debug("message receive");
			ByteBuffer buffer = ((ChannelBuffer)e.getMessage()).toByteBuffer();
			byte[] reciveByte = buffer.array();
			logger.debug("reciveByte.length:" + reciveByte.length);
			
			byte[] headDelimiter = new byte[0];
			System.arraycopy(reciveByte, 0, headDelimiter, 0, 0);
			
			byte[] requestBuffer = new byte[reciveByte.length];
			System.arraycopy(reciveByte, 0, requestBuffer, 0, (reciveByte.length));
			
			GaeaContext gaeaContext = new GaeaContext(requestBuffer,
					new GaeaChannel(e.getChannel()), 
					ServerType.TCP,
					this);
			
			SocketServer.invokerHandle.invoke(gaeaContext);
		} catch(Throwable ex) {
			byte[] response = ExceptionHelper.createErrorProtocol();
			e.getChannel().write(response);
			logger.error("SocketHandler invoke error", ex);
		}
	}
	
	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if (e instanceof ChannelStateEvent) {
			logger.debug(e.toString());
		}
		super.handleUpstream(ctx, e);
	}
	
	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
		SocketServer.allChannels.add(e.getChannel());
		logger.info("new channel open:" + e.getChannel().getRemoteAddress().toString());
		/**
		 * 如果当前服务启动权限认证,则增加当前连接对应的SecureContext
		 * @author HaoXB
		 */
		if(Global.getSingleton().getGlobalSecureIsRights()){// Channel
			Global.getSingleton().addChannelMap(e.getChannel(), new SecureContext());
		}
		
	}

	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		for(IFilter filter : Global.getSingleton().getConnectionFilterList()) {
			filter.filter(new GaeaContext(new GaeaChannel(e.getChannel())));
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.error("unexpected exception from downstream remoteAddress(" + 
					e.getChannel().getRemoteAddress().toString() + 
					")",
					e.getCause());
	}
	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e){
		logger.info("channel is closed:" + e.getChannel().getRemoteAddress().toString());
		e.getChannel().close();
		
		/**
		 * 如果当前服务启动权限认证,则删除当前连接对应的SecureContext
		 * @author HaoXB
		 */
		if(Global.getSingleton().getGlobalSecureIsRights()){
			Global.getSingleton().removeChannelMap(e.getChannel());
		}
	}

	
	@Override
	public void writeResponse(GaeaContext context) {
		if(context != null && context.getGaeaResponse() != null){
			context.getChannel().write(context.getGaeaResponse().getResponseBuffer());
		} else {
			context.getChannel().write(new byte[]{0});
			logger.error("context is null or response is null in writeResponse");
		}
	}
}