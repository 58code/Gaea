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
package com.bj58.spat.gaea.server.bootstrap.signal;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.context.ServerStateType;
import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;

/**
 * CheckNode
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class OperateSignal implements SignalHandler{
	
	private static ILog logger = LogFactory.getLogger(OperateSignal.class);

	@Override
	public void handle(Signal signalName) {
		//设置当前服务状态为重启
		Global.getSingleton().setServerState(ServerStateType.Reboot);
		logger.info(Global.getSingleton().getServiceConfig().getString("gaea.service.name")+" Server state is "+Global.getSingleton().getServerState());
		logger.info(Global.getSingleton().getServiceConfig().getString("gaea.service.name")+" Server will reboot!");
	}
}
