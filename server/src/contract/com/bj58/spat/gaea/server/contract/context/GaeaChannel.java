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

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import com.bj58.spat.gaea.protocol.utility.ProtocolConst;
import com.bj58.spat.gaea.server.util.ExceptionHelper;

/**
 * StringUtils
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class GaeaChannel {
	
	private Channel nettyChannel;
	private String remoteIP;
	private int remotePort;
	private String localIP;
	private int localPort;

	public GaeaChannel() {
		
	}
	
	public GaeaChannel(Channel nettyChannel) {
		super();
		this.nettyChannel = nettyChannel;
		SocketAddress remoteAddress = nettyChannel.getRemoteAddress();
		this.remoteIP = ((InetSocketAddress)remoteAddress).getAddress().getHostAddress();
		this.remotePort = ((InetSocketAddress)remoteAddress).getPort();
		SocketAddress localAddress = nettyChannel.getLocalAddress();
		this.localIP = ((InetSocketAddress)localAddress).getAddress().getHostAddress();
		this.localPort = ((InetSocketAddress)localAddress).getPort();
	}

	public void close() {
		nettyChannel.close();
	}
	
	public void write(byte[] buffer) {
		if(buffer == null) {
			buffer = ExceptionHelper.createErrorProtocol();
		}
		//this.nettyChannel.write(ChannelBuffers.copiedBuffer(ProtocolConst.P_START_TAG, buffer, ProtocolConst.P_END_TAG));
		this.nettyChannel.write(ChannelBuffers.copiedBuffer(buffer, ProtocolConst.P_END_TAG));
	}
	
	public Channel getNettyChannel() {
		return nettyChannel;
	}

	public String getRemoteIP() {
		return remoteIP;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public String getLocalIP() {
		return localIP;
	}

	public int getLocalPort() {
		return localPort;
	}

	public void setNettyChannel(Channel nettyChannel) {
		this.nettyChannel = nettyChannel;
	}

	public void setRemoteIP(String remoteIP) {
		this.remoteIP = remoteIP;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public void setLocalIP(String localIP) {
		this.localIP = localIP;
	}

	public void setLocalPort(int localPort) {
		this.localPort = localPort;
	}
}
