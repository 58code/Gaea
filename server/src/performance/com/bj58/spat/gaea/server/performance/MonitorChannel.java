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

import java.net.SocketAddress;

import org.jboss.netty.channel.Channel;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class MonitorChannel {
	private Channel channel;
	private SocketAddress socketAddress;
	private Command command;
	
	private int convergeCount;
	private long convergeTime;
	private long beginTime;


	public MonitorChannel() {

	}

	public MonitorChannel(Command command, 
			Channel channel,
			SocketAddress socketAddress) {
		this.setCommand(command);
		this.setChannel(channel);
		this.setSocketAddress(socketAddress);
	}

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public SocketAddress getSocketAddress() {
		return socketAddress;
	}

	public void setSocketAddress(SocketAddress socketAddress) {
		this.socketAddress = socketAddress;
	}

	public void setCommand(Command command) {
		this.command = command;
	}

	public Command getCommand() {
		return command;
	}

	public void setConvergeCount(int convergeCount) {
		this.convergeCount = convergeCount;
	}

	public int getConvergeCount() {
		return convergeCount;
	}
	
	public long getConvergeTime() {
		return convergeTime;
	}

	public void setConvergeTime(long convergeTime) {
		this.convergeTime = convergeTime;
	}

	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}

	public long getBeginTime() {
		return beginTime;
	}
}
