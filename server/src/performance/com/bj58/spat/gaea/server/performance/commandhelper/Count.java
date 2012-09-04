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
package com.bj58.spat.gaea.server.performance.commandhelper;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.MessageEvent;

import com.bj58.spat.gaea.server.contract.context.GaeaContext;
import com.bj58.spat.gaea.server.contract.context.StopWatch;
import com.bj58.spat.gaea.server.performance.Command;
import com.bj58.spat.gaea.server.performance.CommandType;
import com.bj58.spat.gaea.server.performance.MonitorCenter;
import com.bj58.spat.gaea.server.performance.MonitorChannel;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Count extends CommandHelperBase {
	
	private static List<MonitorChannel> taskList = new ArrayList<MonitorChannel>();

	@Override
	public Command createCommand(String commandStr) {
		if(commandStr != null && !commandStr.equalsIgnoreCase("")) {
			String[] args = commandStr.split("\\|");
			if(args[0].trim().equalsIgnoreCase("count")) {
				Command entity = new Command();
				entity.setCommandType(CommandType.Count);
				entity.setSecond(1);
				entity.setMethod("#all#");
				if(args.length > 1) {
					for(int i=1; i<args.length; i++) {
						if(args[i].trim().startsWith("second")) {
							entity.setSecond(Integer.parseInt(args[i].trim().replaceFirst("second ", "").trim()));
						} else if(args[i].trim().startsWith("method")) {
							entity.setMethod(args[i].trim().replaceFirst("method ", "").trim());
						}
					}
				}
				return entity;
			}
		}
		return null;
	}

	
	
	@Override
	public void execCommand(Command command, MessageEvent event) throws Exception{
		if(command.getCommandType() == CommandType.Count) {
			MonitorCenter.addFilter(); // add filter
			logger.info("add count monitor channel:" + event.getChannel().getRemoteAddress());
			for(int i=0; i<taskList.size(); i++) {
				if(taskList.get(i).getChannel().equals(event.getChannel()) || !taskList.get(i).getChannel().isOpen()) {
					taskList.remove(i);
				}
			}
			taskList.add(new MonitorChannel(command, event.getChannel(), event.getChannel().getRemoteAddress()));
		}
	}



	@SuppressWarnings("rawtypes")
	@Override
	public void messageReceived(GaeaContext context) {
		if(taskList.size() <= 0) {
			return;
		}
		
		StopWatch sw = context.getStopWatch();
		if (sw != null) {
			Map<String, StopWatch.PerformanceCounter> mapCounter = sw.getMapCounter();
			StringBuilder sbAllMsg = new StringBuilder();
			sbAllMsg.append("#all#");
			Iterator itSW = mapCounter.entrySet().iterator();
			while (itSW.hasNext()) {
				Map.Entry entrySW = (Map.Entry) itSW.next();
				StopWatch.PerformanceCounter pc = (StopWatch.PerformanceCounter) entrySW.getValue();
				sbAllMsg.append("key:");
				sbAllMsg.append(pc.getKey());
			}
			
			try {
				String allMsg = sbAllMsg.toString();
				for (MonitorChannel mc : taskList) {
					long now = System.currentTimeMillis();
					if((now - mc.getBeginTime()) > (mc.getCommand().getSecond() * 1000)) {
						String msg = mc.getCommand().getMethod() + "  " + String.valueOf(mc.getConvergeCount()) + "\r\n";
						byte[] responseByte = msg.getBytes("utf-8");
						mc.getChannel().write(ChannelBuffers.copiedBuffer(responseByte));
						mc.setBeginTime(now);
						mc.setConvergeCount(0);
					} else {
						if(allMsg.indexOf(mc.getCommand().getMethod()) >= 0) {
							mc.setConvergeCount(mc.getConvergeCount() + 1);
						}
					}
				}
			} catch (UnsupportedEncodingException e) {
				logger.error("send monitor data", e);
			}
		}
	}



	@Override
	public void removeChannel(Command command, Channel channel) {
		if(command.getCommandType() != CommandType.Count) {
			for(int i=0; i<taskList.size(); i++) {
				if(taskList.get(i).getChannel().equals(channel) || !taskList.get(i).getChannel().isOpen()) {
					taskList.remove(i);
				}
			}
		}
	}
	
	@Override
	public int getChannelCount() {
		for(int i=0; i<taskList.size(); i++) {
			if(!taskList.get(i).getChannel().isOpen()) {
				taskList.remove(i);
			}
		}
		return taskList.size();
	}
}