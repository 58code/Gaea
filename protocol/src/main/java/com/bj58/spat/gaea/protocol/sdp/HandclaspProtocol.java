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
package com.bj58.spat.gaea.protocol.sdp;

import com.bj58.spat.gaea.serializer.component.annotation.GaeaMember;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;

/**
 * RequestProtocol
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
@GaeaSerializable(name="HandclaspProtocol")
public class HandclaspProtocol {
	/**
	 * 权限认证类型(1、客户端发送公钥至服务器 2、客户端发送授权文件密文至服务器)
	 */
	@GaeaMember
	private String type;
	/**
	 * 信息内容
	 */
	@GaeaMember
	private String data; 
	
	public HandclaspProtocol(){
		
	}
	/**
	 * 实例化HandclaspProtocol
	 * @param type ("1"表示客户端发送公钥至服务器 "2"表示客户端发送授权文件密文至服务器)
	 * @param data (密文)
	 */
	public HandclaspProtocol(String type,String data){
		this.type = type;
		this.data = data;
	}

	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
