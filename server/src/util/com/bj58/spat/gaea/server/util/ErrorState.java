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

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public enum ErrorState {
	
	/**
	 * 数据库错误
	 */
	DBException(1), 
	
	/**
	 * 网络错误
	 */
	NetException(2), 
	
	/**
	 * 超时
	 */
	TimoutException(3), 
	
	/**
	 * 协议错误
	 */
	ProtocolException(4),
	
	/**
	 * 消息体json格式错误
	 */
	JsonException(5),
	
	/**
	 * 方法调用参数错误
	 */
	ParaException(6),
	
	/**
	 * 找不到方法
	 */
	NotFoundMethodException(7),
	
	/**
	 * 找不到服务
	 */
	NotFoundServiceException(8),
	
	/**
	 * JSON序列化错误
	 */
	JSONSerializeException(9),
	
	/**
	 * 服务错误
	 */
	ServiceException(10),
	
	/**
	 * 数据溢出错误
	 */
	DataOverFlowException(11),
	
	/**
	 * 其它错误
	 */
	OtherException(99);
	

	private final int stateNum;

	public int getStateNum() {
		return stateNum;
	}

	private ErrorState(int stateNum) {
		this.stateNum = stateNum;
	}
}