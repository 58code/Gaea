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
package com.bj58.spat.gaea.server.util.async;

public interface IAsyncHandler {
	
	/**
	 * 异步执行的任务
	 * @return
	 */
	public Object run() throws Throwable;
	
	/**
	 * 响应消息到达
	 * @param obj 返回值
	 */
	public void messageReceived(Object obj);
	
	/**
	 * 发生异常
	 * @param e 异常
	 */
	public void exceptionCaught(Throwable e);
	
}
