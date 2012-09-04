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
package com.bj58.spat.sfft.utility.async;

public class AsyncTask {
	
	/**
	 * 默认超时时间为3秒
	 */
	private static final int DEFAULT_TIME_OUT = 2000;
	
	private int timeout;
	
	private int qtimeout;
	
	private long addTime;
	
	private IAsyncHandler handler;
	

	/**
	 * 构造异步任务
	 * @param timeout 超时时间(单位：豪秒)
	 * @param handler 执行句柄
	 */
	public AsyncTask(int timeout, IAsyncHandler handler) {
		super();
		if(timeout < 0){
			timeout = 1000;
		}
		this.timeout = timeout;
		this.qtimeout = ((timeout * 3)/2)+1;
		this.handler = handler;
		this.addTime = System.currentTimeMillis();
	}
	
	/**
	 * 构造异步任务
	 * @param handler 执行句柄
	 */
	public AsyncTask(IAsyncHandler handler) {
		super();
		this.timeout = DEFAULT_TIME_OUT;
		this.qtimeout = ((timeout * 3)/2)+1;
		this.handler = handler;
		this.addTime = System.currentTimeMillis();
	}
	
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
	public IAsyncHandler getHandler() {
		return handler;
	}

	public void setHandler(IAsyncHandler handler) {
		this.handler = handler;
	}

	public long getAddTime() {
		return addTime;
	}

	public int getQtimeout() {
		return qtimeout;
	}

	public void setQtimeout(int qtimeout) {
		this.qtimeout = qtimeout;
	}
}