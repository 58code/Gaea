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

/**
 * StringUtils
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class SecureContext {
	/**
	 * DES密钥
	 */
	private String desKey;
	/**
	 * 服务器端RSA公钥
	 */
	private String serverPublicKey;
	/**
	 * 服务器端RSA私钥
	 */
	private String serverPrivateKey;
	/**
	 * 客户端RSA公钥
	 */
	private String clientPublicKey;
	/**
	 * 客户端RSA私钥
	 */
	private String clientPrivateKey;
	/**
	 * 是否通过认证
	 */
	private boolean isRights = false;

	public boolean isRights() {
		return isRights;
	}

	public void setRights(boolean isRights) {
		this.isRights = isRights;
	}

	public String getDesKey() {
		return desKey;
	}
	
	public void setDesKey(String desKey) {
		this.desKey = desKey;
	}
	
	public String getServerPublicKey() {
		return serverPublicKey;
	}
	
	public void setServerPublicKey(String serverPublicKey) {
		this.serverPublicKey = serverPublicKey;
	}
	
	public String getServerPrivateKey() {
		return serverPrivateKey;
	}
	
	public void setServerPrivateKey(String serverPrivateKey) {
		this.serverPrivateKey = serverPrivateKey;
	}
	
	public String getClientPublicKey() {
		return clientPublicKey;
	}
	
	public void setClientPublicKey(String clientPublicKey) {
		this.clientPublicKey = clientPublicKey;
	}
	
	public String getClientPrivateKey() {
		return clientPrivateKey;
	}
	
	public void setClientPrivateKey(String clientPrivateKey) {
		this.clientPrivateKey = clientPrivateKey;
	}
}
