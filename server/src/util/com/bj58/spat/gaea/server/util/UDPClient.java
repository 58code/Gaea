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
package com.bj58.spat.gaea.server.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

/**
 * udp client for send msg
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class UDPClient {
	
	private String encode;
	
	private DatagramSocket sock = null;
	
	private InetSocketAddress addr = null;
	
	
	public static UDPClient getInstrance(String ip, int port, String encode) throws SocketException {
		UDPClient client = new UDPClient();
		client.encode = encode;
		client.sock = new DatagramSocket();
		client.addr = new InetSocketAddress(ip, port);
		
		return client;
	}
	
	private UDPClient() {
		
	}
	
	public void close() {
		sock.close();
	}

	/**
	 * send udp msg
	 * @param msg
	 */
	public void send(String msg, String encoding) throws Exception {
		byte[] buf = msg.getBytes(encoding);
		send(buf);
	}
	
	public void send(String msg) throws IOException {
		byte[] buf = msg.getBytes(encode);
		send(buf);
	}
	
	public void send(byte[] buf) throws IOException {
		DatagramPacket dp = new DatagramPacket(buf, buf.length, addr);
		sock.send(dp);
	}
}