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
package com.bj58.spat.gaea.server.contract.http;

import java.util.HashMap;
import java.util.Map;

/**
 * ServerStateType
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class HttpResponse {

	private Map<String, String> headers = new HashMap<String, String>();
	
	private String contentType;
	
	private int code;
	
	/**
	 * 
	 * @param content
	 */
	public void write(String content) {
		write(content, "utf-8");
	}
	
	/**
	 * 
	 * @param content
	 * @param encoding
	 */
	public void write(String content, String encoding) {
		
	}
	
	/**
	 * 
	 * @param buffer
	 */
	public void write(byte[] buffer) {
		
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void addHeader(String key, String value) {
		headers.put(key, value);
	}
	
	

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}
}
