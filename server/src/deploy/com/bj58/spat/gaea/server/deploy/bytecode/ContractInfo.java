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
package com.bj58.spat.gaea.server.deploy.bytecode;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ContractInfo {

	private List<SessionBean> sessionBeanList;

	public ContractInfo() {

	}

	public ContractInfo(List<SessionBean> sessionBeanList) {
		super();
		this.sessionBeanList = sessionBeanList;
	}

	public void setSessionBeanList(List<SessionBean> sessionBeanList) {
		this.sessionBeanList = sessionBeanList;
	}

	public List<SessionBean> getSessionBeanList() {
		return sessionBeanList;
	}

	public static class SessionBean {
		private String interfaceName;
		private Map<String, String> instanceMap;
		private ClassInfo interfaceClass;

		public SessionBean() {

		}

		public SessionBean(String interfaceName,
				Map<String, String> instanceMap, ClassInfo interfaceClass) {
			super();
			this.interfaceName = interfaceName;
			this.instanceMap = instanceMap;
			this.interfaceClass = interfaceClass;
		}

		public String getInterfaceName() {
			return interfaceName;
		}

		public void setInterfaceName(String interfaceName) {
			this.interfaceName = interfaceName;
		}

		public void setInstanceMap(Map<String, String> instanceMap) {
			this.instanceMap = instanceMap;
		}

		public Map<String, String> getInstanceMap() {
			return instanceMap;
		}

		public void setInterfaceClass(ClassInfo interfaceClass) {
			this.interfaceClass = interfaceClass;
		}

		public ClassInfo getInterfaceClass() {
			return interfaceClass;
		}
	}
}