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

import com.bj58.spat.gaea.server.contract.annotation.HttpParameterType;
import com.bj58.spat.gaea.server.contract.annotation.HttpRequestMethod;

/**
 * ServerStateType
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Action {

	private HttpRequestMethod mothod;
	private String lookup;
	private String methodName;
	private List<Parameter> paramList;

	public static class Parameter {
		private String type;
		private String mapping;
		private Object value;
		private int urlParaIndex;
		private int contentParaIndex;
		private int methodParaIndex;
		private HttpParameterType paraType;

		public Parameter() {

		}

		public Parameter(String type, String mapping, Object value,
				int urlParaIndex, int contentParaIndex, int methodParaIndex,
				HttpParameterType paraType) {
			super();
			this.type = type;
			this.mapping = mapping;
			this.value = value;
			this.urlParaIndex = urlParaIndex;
			this.contentParaIndex = contentParaIndex;
			this.methodParaIndex = methodParaIndex;
			this.paraType = paraType;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getMapping() {
			return mapping;
		}

		public void setMapping(String mapping) {
			this.mapping = mapping;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public void setUrlParaIndex(int urlParaIndex) {
			this.urlParaIndex = urlParaIndex;
		}

		public int getUrlParaIndex() {
			return urlParaIndex;
		}

		public void setMethodParaIndex(int methodParaIndex) {
			this.methodParaIndex = methodParaIndex;
		}

		public int getMethodParaIndex() {
			return methodParaIndex;
		}

		public void setParaType(HttpParameterType paraType) {
			this.paraType = paraType;
		}

		public HttpParameterType getParaType() {
			return paraType;
		}

		public void setContentParaIndex(int contentParaIndex) {
			this.contentParaIndex = contentParaIndex;
		}

		public int getContentParaIndex() {
			return contentParaIndex;
		}
	}

	public HttpRequestMethod getMothod() {
		return mothod;
	}

	public void setMothod(HttpRequestMethod mothod) {
		this.mothod = mothod;
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

	public void setParamList(List<Parameter> paramList) {
		this.paramList = paramList;
	}

	public List<Parameter> getParamList() {
		return paramList;
	}
}