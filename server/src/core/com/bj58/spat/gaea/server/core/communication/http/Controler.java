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

/**
 * ServerStateType
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Controler {
	
	private Action getAction;
	private Action postAction;
	private Action deleteAction;
	private Action putAction;
	private Action headAction;
	
	public Action getGetAction() {
		return getAction;
	}
	public void setGetAction(Action getAction) {
		this.getAction = getAction;
	}
	public Action getPostAction() {
		return postAction;
	}
	public void setPostAction(Action postAction) {
		this.postAction = postAction;
	}
	public Action getDeleteAction() {
		return deleteAction;
	}
	public void setDeleteAction(Action deleteAction) {
		this.deleteAction = deleteAction;
	}
	public Action getPutAction() {
		return putAction;
	}
	public void setPutAction(Action putAction) {
		this.putAction = putAction;
	}
	public Action getHeadAction() {
		return headAction;
	}
	public void setHeadAction(Action headAction) {
		this.headAction = headAction;
	}
}