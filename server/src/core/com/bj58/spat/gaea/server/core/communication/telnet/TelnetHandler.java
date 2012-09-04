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
package com.bj58.spat.gaea.server.core.communication.telnet;

import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;
import com.bj58.spat.gaea.server.performance.MonitorCenter;

/**
 * netty event handler
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
@ChannelPipelineCoverage("all")
public class TelnetHandler extends SimpleChannelUpstreamHandler {
	
	private static ILog logger = LogFactory.getLogger(TelnetHandler.class);
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		try {
			logger.debug("control message receive");
			MonitorCenter.messageReceived(e);
		} catch(Exception ex) {
			logger.error("control msg error", ex);
		}
	}
	
	
	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e) throws Exception {
		if (e instanceof ChannelStateEvent) {
			logger.info("control event--"+e.toString());
		}
		super.handleUpstream(ctx, e);
	}
	
	
	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
		TelnetServer.allChannels.add(e.getChannel());
		logger.info("new control channel open:" + e.getChannel().getRemoteAddress().toString());
	}

	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		logger.info("new control channel conected:" + e.getChannel().getRemoteAddress().toString());
	}

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.error("control channel exception(" + 
					e.getChannel().getRemoteAddress().toString() + 
					")",
					e.getCause());
		MonitorCenter.removeChannel(e.getChannel());
		e.getChannel().close();
	}
	
	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e){
		logger.info("channel is closed:" + e.getChannel().getRemoteAddress().toString());
		MonitorCenter.removeChannel(e.getChannel());
		e.getChannel().close();
	}
}