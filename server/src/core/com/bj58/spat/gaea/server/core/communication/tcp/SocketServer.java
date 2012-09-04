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
import com.bj58.spat.gaea.server.core.proxy.IInvokerHandle;

/**
 * start netty server
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class SocketServer implements IServer {
	
	public SocketServer() {

	}
	
	static ILog logger = LogFactory.getLogger(SocketServer.class);
	
	/**
	 * netty ServerBootstrap
	 */
	static final ServerBootstrap bootstrap = new ServerBootstrap();

	/**
	 * record all channel
	 */
	static final ChannelGroup allChannels = new DefaultChannelGroup("Gaea-SockerServer");
	
	/**
	 * invoker handle
	 */
	static IInvokerHandle invokerHandle = null;
	
	/**
	 * start netty server
	 */
	@Override
	public void start() throws Exception {
		logger.info("loading invoker...");
		String invoker = Global.getSingleton().getServiceConfig().getString("gaea.proxy.invoker.implement");
		invokerHandle = (IInvokerHandle) Class.forName(invoker).newInstance();
		logger.info("initing server...");
		initSocketServer();
	}
	
	/**
	 * stop netty server
	 */
	@Override
	public void stop() throws Exception {
		logger.info("----------------------------------------------------");
		logger.info("-- socket server closing...");
		logger.info("-- channels count : " + allChannels.size());
		ChannelGroupFuture future = allChannels.close();
		logger.info("-- closing all channels...");
		future.awaitUninterruptibly();
		logger.info("-- closed all channels...");
		bootstrap.getFactory().releaseExternalResources();
		logger.info("-- released external resources");
		logger.info("-- close success !");
		logger.info("----------------------------------------------------");
	}
	
	/**
	 * 初始化socket server
	 * @throws Exception
	 */
	private void initSocketServer() throws Exception {
		final boolean tcpNoDelay = true;
		logger.info("-- socket server config --");
		logger.info("-- listen ip: " 
				+ Global.getSingleton().getServiceConfig().getString("gaea.server.tcp.listenIP"));
        logger.info("-- port: " 
        		+ Global.getSingleton().getServiceConfig().getInt("gaea.server.tcp.listenPort"));
        logger.info("-- tcpNoDelay: " + tcpNoDelay);
        logger.info("-- receiveBufferSize: " 
        		+ Global.getSingleton().getServiceConfig().getInt("gaea.server.tcp.receiveBufferSize"));
        logger.info("-- sendBufferSize: " 
        		+ Global.getSingleton().getServiceConfig().getInt("gaea.server.tcp.sendBufferSize"));
        logger.info("-- frameMaxLength: " 
        		+ Global.getSingleton().getServiceConfig().getInt("gaea.server.tcp.frameMaxLength"));
        logger.info("-- worker thread count: " 
        		+ Global.getSingleton().getServiceConfig().getInt("gaea.server.tcp.workerCount"));
        logger.info("--------------------------");
		
		logger.info(Global.getSingleton().getServiceConfig().getString("gaea.service.name")
				+ " SocketServer starting...");
		
        bootstrap.setFactory(new NioServerSocketChannelFactory(
			                        	Executors.newCachedThreadPool(),
			                        	Executors.newCachedThreadPool(),
			                        	Global.getSingleton().getServiceConfig().getInt("gaea.server.tcp.workerCount")
			                        )
        					);
        
        SocketHandler handler = new SocketHandler();
        bootstrap.setPipelineFactory(new SocketPipelineFactory(handler, 
        		Global.getSingleton().getServiceConfig().getInt("gaea.server.tcp.frameMaxLength")));
        bootstrap.setOption("child.tcpNoDelay", tcpNoDelay);
        bootstrap.setOption("child.receiveBufferSize", 
        		Global.getSingleton().getServiceConfig().getInt("gaea.server.tcp.receiveBufferSize"));
        bootstrap.setOption("child.sendBufferSize", 
        		Global.getSingleton().getServiceConfig().getInt("gaea.server.tcp.sendBufferSize"));

        try {
        	InetSocketAddress socketAddress = null;
        	socketAddress = new InetSocketAddress(Global.getSingleton().getServiceConfig().getString("gaea.server.tcp.listenIP"),
        			Global.getSingleton().getServiceConfig().getInt("gaea.server.tcp.listenPort"));
            Channel channel = bootstrap.bind(socketAddress);
            allChannels.add(channel);
		} catch (Exception e) {
			logger.error("init socket server error", e);
			System.exit(1);
		}
	}
}