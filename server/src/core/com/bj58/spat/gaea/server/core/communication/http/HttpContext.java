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
package com.bj58.spat.gaea.server.core.communication.http;

import java.util.List;
import java.util.Map;

import com.bj58.spat.gaea.server.contract.annotation.HttpRequestMethod;

/**
 * ServerStateType
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class HttpContext {

	public HttpContext() {

	}

	private String uri;
	private HttpRequestMethod method;
	private byte[] contentBuffer;
	private String fromIP;
	private String toIP;
	private Map<String, List<String>> headers;
	private Map<String, List<String>> params;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	public HttpRequestMethod getMethod() {
		return method;
	}

	public void setMethod(HttpRequestMethod method) {
		this.method = method;
	}

	public void setParams(Map<String, List<String>> params) {
		this.params = params;
	}

	public Map<String, List<String>> getParams() {
		return params;
	}

	public byte[] getContentBuffer() {
		return contentBuffer;
	}

	public void setContentBuffer(byte[] contentBuffer) {
		this.contentBuffer = contentBuffer;
	}

	public String getFromIP() {
		return fromIP;
	}

	public void setFromIP(String fromIP) {
		this.fromIP = fromIP;
	}

	public String getToIP() {
		return toIP;
	}

	public void setToIP(String toIP) {
		this.toIP = toIP;
	}
}