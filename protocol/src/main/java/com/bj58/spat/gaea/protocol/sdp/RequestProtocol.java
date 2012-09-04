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

import java.util.List;

import com.bj58.spat.gaea.protocol.utility.KeyValuePair;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaMember;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;

/**
 * RequestProtocol
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
@GaeaSerializable(name = "RequestProtocol")
public class RequestProtocol {

	@GaeaMember
	private String lookup;
	@GaeaMember
	private String methodName;
	@GaeaMember
	private List<KeyValuePair> paraKVList;

	public RequestProtocol() {
	}

	public RequestProtocol(String lookup, String methodName,
			List<KeyValuePair> paraKVList) {
		this.lookup = lookup;
		this.methodName = methodName;
		this.paraKVList = paraKVList;
	}

	public String getLookup() {
		return lookup;
	}

	public void setLookup(String lookup) {
		this.lookup = lookup;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public List<KeyValuePair> getParaKVList() {
		return paraKVList;
	}

	public void setParaKVList(List<KeyValuePair> paraKVList) {
		this.paraKVList = paraKVList;
	}
}
