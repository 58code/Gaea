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

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.ChannelGroupFuture;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;
import com.bj58.spat.gaea.server.contract.server.IServer;

/**
 * start netty server
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class TelnetServer implements IServer {
	
	public TelnetServer() {

	}
	
	private static ILog logger = LogFactory.getLogger(TelnetServer.class);
	
	/**
	 * netty ServerBootstrap
	 */
	private static final ServerBootstrap bootstrap = new ServerBootstrap();

	/**
	 * record all channel
	 */
	public static final ChannelGroup allChannels = new DefaultChannelGroup("58ControlServer");
	
	/**
	 * start netty server
	 */
	@Override
	public void start() throws Exception {

		logger.info("----------------telnet server config------------------");
        logger.info("-- telnet server listen ip: " 
        		+ Global.getSingleton().getServiceConfig().getString("gaea.server.telnet.listenIP"));
        logger.info("-- telnet server port: " 
        		+ Global.getSingleton().getServiceConfig().getInt("gaea.server.telnet.listenPort"));
        logger.info("------------------------------------------------------");
		
        bootstrap.setFactory(new NioServerSocketChannelFactory(
			                        	Executors.newCachedThreadPool(),
			                        	Executors.newCachedThreadPool()));
        TelnetHandler handler = new TelnetHandler();
        bootstrap.setPipelineFactory(new TelnetPipelineFactory(handler, 
        		Global.getSingleton().getServiceConfig().getInt("gaea.server.telnet.frameMaxLength")));
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.receiveBufferSize", 
        		Global.getSingleton().getServiceConfig().getInt("gaea.server.telnet.receiveBufferSize"));
        bootstrap.setOption("child.sendBufferSize", 
        		Global.getSingleton().getServiceConfig().getInt("gaea.server.telnet.sendBufferSize"));

        try {
        	InetSocketAddress socketAddress = null;
        	socketAddress = new InetSocketAddress(Global.getSingleton().getServiceConfig().getString("gaea.server.telnet.listenIP"),
        			Global.getSingleton().getServiceConfig().getInt("gaea.server.telnet.listenPort"));
            Channel channel = bootstrap.bind(socketAddress);
            allChannels.add(channel);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * stop netty server
	 */
	@Override
	public void stop() throws Exception {
		logger.info("----------------------------------------------------");
		logger.info("-- telnet Server closing...");
		logger.info("-- channels count : " + allChannels.size());
		ChannelGroupFuture future = allChannels.close();
		future.awaitUninterruptibly();
		bootstrap.getFactory().releaseExternalResources();
		logger.info("-- close success !");
		logger.info("----------------------------------------------------");
	}
}