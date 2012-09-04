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

import java.util.List;

import com.bj58.spat.gaea.server.contract.entity.Out;

/**
 * Gaea response entity
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class GaeaResponse {
	
	private Object returnValue;

	private List<Out<?>> outParaList;
	
	private byte[] responseBuffer;
	
	
	public GaeaResponse(){
		
	}
	
	
	public GaeaResponse(String rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	
	public GaeaResponse(int rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	public GaeaResponse(Integer rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	public GaeaResponse(long rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	public GaeaResponse(Long rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	public GaeaResponse(short rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	public GaeaResponse(Short rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	public GaeaResponse(float rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	public GaeaResponse(Float rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	public GaeaResponse(boolean rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	public GaeaResponse(Boolean rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	public GaeaResponse(double rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	public GaeaResponse(Double rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	
	public GaeaResponse(char rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	public GaeaResponse(Character rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	public GaeaResponse(byte rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	public GaeaResponse(Byte rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	
	public GaeaResponse(Object rv, List<Out<?>> op) {
		setValue(rv, op);
	}
	
	
	
	public void setValue(Object rv, List<Out<?>> op) {
		this.setOutParaList(op);
		this.setReturnValue(rv);
	}

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}

	public List<Out<?>> getOutParaList() {
		return outParaList;
	}

	public void setOutParaList(List<Out<?>> outParaList) {
		this.outParaList = outParaList;
	}

	public void setResponseBuffer(byte[] responseBuffer) {
		this.responseBuffer = responseBuffer;
	}

	public byte[] getResponseBuffer() {
		return responseBuffer;
	}
}