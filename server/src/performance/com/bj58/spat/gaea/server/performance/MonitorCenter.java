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
package com.bj58.spat.gaea.server.performance;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.MessageEvent;

import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.context.GaeaContext;
import com.bj58.spat.gaea.server.contract.filter.IFilter;
import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;
import com.bj58.spat.gaea.server.filter.MonitorRequestFilter;
import com.bj58.spat.gaea.server.filter.MonitorResponseFilter;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class MonitorCenter {

	/**
	 * thread pool
	 */
	private static ExecutorService executor = Executors.newCachedThreadPool();
	
	/**
	 * MonitorRequestFilter
	 */
	private static IFilter monitorRequestFilter = new MonitorRequestFilter();
	
	/**
	 * MonitorResponseFilter
	 */
	private static IFilter monitorResponseFilter = new MonitorResponseFilter();

	/**
	 * log
	 */
	private static ILog logger = LogFactory.getLogger(MonitorCenter.class);
	
	private static Command command = null;

	/**
	 * monitor event receive
	 * 
	 * @param e
	 * @throws Exception
	 * 
	 */
	public static void messageReceived(MessageEvent e) throws Exception {
		ByteBuffer buffer = ((ChannelBuffer) e.getMessage()).toByteBuffer();
		byte[] reciveByte = buffer.array();
		String msg = new String(reciveByte, "utf-8");
		
		command = Command.create(msg);
		logger.info("command:" + msg + "--commandType:" + command.getCommandType());
		command.exec(e);
		
		removeChannel(e.getChannel());
	}

	/**
	 * add monitor filter
	 */
	public synchronized static void addFilter() {
		if (!Global.getSingleton().getGlobalRequestFilterList().contains(monitorRequestFilter)) {
			logger.info("add monitorRequestFilter");
			Global.getSingleton().addGlobalRequestFilter(monitorRequestFilter);
		}
		if (!Global.getSingleton().getGlobalResponseFilterList().contains(monitorResponseFilter)) {
			logger.info("add monitorResponseFilter");
			Global.getSingleton().addGlobalResponseFilter(monitorResponseFilter);
		}
	}

	/**
	 * remove monitor filter
	 */
	public synchronized static void removeFilter() {
		Global.getSingleton().removeGlobalRequestFilter(monitorRequestFilter);
		Global.getSingleton().removeGlobalResponseFilter(monitorResponseFilter);
		logger.info("remove monitorRequestFilter");
		logger.info("remove monitorResponseFilter");
	}


	/**
	 * add monitor task
	 * @param context
	 */
	public static void addMonitorTask(final GaeaContext context) {
		executor.execute(new Runnable() {
			public void run() {
				if(command != null) {
					command.messageReceived(context);
				}
			}
		});
	}
	
	
	public static void removeChannel(Channel channel) {
		if(command != null) {
			command.removeChannel(channel);
			
			if(command.getChannelCount() <= 0) {
				removeFilter();
			}
		}
	}
}