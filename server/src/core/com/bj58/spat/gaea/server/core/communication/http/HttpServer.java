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
package com.bj58.spat.gaea.server.core.communication.http;

import java.io.File;
import java.io.IOException;
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
import com.bj58.spat.gaea.server.util.FileHelper;

/**
 * ServerStateType
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class HttpServer implements IServer {
	
	public HttpServer() {

	}
	
	private static ILog logger = LogFactory.getLogger(HttpServer.class);
	
	/**
	 * netty ServerBootstrap
	 */
	private static final ServerBootstrap bootstrap = new ServerBootstrap();

	/**
	 * record all channel
	 */
	static final ChannelGroup allChannels = new DefaultChannelGroup("Gaea-HttpServer");
	
	/**
	 * sync invoker handle
	 */
	static IInvokerHandle invokerHandle = null;
	
	/**
	 * error path html
	 */
	static String errorPageHTML = null;
	
	/**
	 * start netty server
	 */
	@Override
	public void start() throws Exception {
		logger.info("-----------------starting http server-----------------");
		
		logger.info("loading invoker...");
		String invoker = Global.getSingleton().getServiceConfig().getString("gaea.proxy.invoker.implement");
		invokerHandle = (IInvokerHandle) Class.forName(invoker).newInstance();
		
		logger.info("initing url mapping...");
		RequestMapping.init();
		
		logger.info("initing http server...");
		initHttpServer();
		
		logger.info("loading error html...");
		loadErrorHTML();
	}
	
	
	/**
	 * stop netty server
	 */
	@Override
	public void stop() throws Exception {
		logger.info("----------------------------------------------------");
		logger.info("-- http server closing...");
		logger.info("-- channels count : " + allChannels.size());
		ChannelGroupFuture future = allChannels.close();
		logger.info("-- closing all channels...");
		future.awaitUninterruptibly();
		bootstrap.getFactory().releaseExternalResources();
		logger.info("-- released external resources");
		logger.info("-- close success !");
		logger.info("----------------------------------------------------");
	}
	
	/**
	 * load error page html
	 */
	private void loadErrorHTML() {
		String errorPage = Global.getSingleton().getRootPath() + "service/deploy/" + Global.getSingleton().getServiceConfig().getString("gaea.service.name") + "/error.html";
		File file = new File(errorPage);
		if(file.exists()) {
			try {
				errorPageHTML = FileHelper.getContentByLines(errorPage);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}
	
	/**
	 * 初始化http server
	 * @throws Exception
	 */
	private void initHttpServer() throws Exception {
		final boolean tcpNoDelay = true;
		logger.info("-----------------http server config-----------------");
		logger.info("-- listen ip: " + Global.getSingleton().getServiceConfig().getString("gaea.server.http.listenIP"));
        logger.info("-- port: " + Global.getSingleton().getServiceConfig().getInt("gaea.server.http.listenPort"));
        logger.info("-- tcpNoDelay: " + tcpNoDelay);
        logger.info("-- receiveBufferSize: " + Global.getSingleton().getServiceConfig().getInt("gaea.server.http.receiveBufferSize"));
        logger.info("-- sendBufferSize: " + Global.getSingleton().getServiceConfig().getInt("gaea.server.http.sendBufferSize"));
        logger.info("-- frameMaxLength: " + Global.getSingleton().getServiceConfig().getInt("gaea.server.http.frameMaxLength"));
        logger.info("-- worker thread count: " + Global.getSingleton().getServiceConfig().getInt("gaea.server.http.workerCount"));
        logger.info("-----------------------------------------------------");		
		logger.info(Global.getSingleton().getServiceConfig().getString("gaea.service.name") + " HttpServer starting...");

        bootstrap.setFactory(new NioServerSocketChannelFactory(
			                        	Executors.newCachedThreadPool(),
			                        	Executors.newCachedThreadPool(),
			                        	Global.getSingleton().getServiceConfig().getInt("gaea.server.http.workerCount")
			                        )
        					);
        HttpHandler handler = new HttpHandler();
        bootstrap.setPipelineFactory(new HttpPipelineFactory(handler));
        bootstrap.setOption("child.tcpNoDelay", tcpNoDelay);
        bootstrap.setOption("child.receiveBufferSize", 
        		Global.getSingleton().getServiceConfig().getInt("gaea.server.http.receiveBufferSize"));
        bootstrap.setOption("child.sendBufferSize", 
        		Global.getSingleton().getServiceConfig().getInt("gaea.server.http.sendBufferSize"));

        try {
        	InetSocketAddress socketAddress = null;
        	socketAddress = new InetSocketAddress(Global.getSingleton().getServiceConfig().getString("gaea.server.http.listenIP"),
        			Global.getSingleton().getServiceConfig().getInt("gaea.server.http.listenPort"));
            Channel channel = bootstrap.bind(socketAddress);
            allChannels.add(channel);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}