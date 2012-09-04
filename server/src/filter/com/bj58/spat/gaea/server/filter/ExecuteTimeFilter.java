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
package com.bj58.spat.gaea.server.filter;

import java.util.Collection;

import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.context.GaeaContext;
import com.bj58.spat.gaea.server.contract.context.StopWatch;
import com.bj58.spat.gaea.server.contract.context.StopWatch.PerformanceCounter;
import com.bj58.spat.gaea.server.contract.filter.IFilter;
import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;
import com.bj58.spat.gaea.server.util.UDPClient;

/**
 * A filter for record execute time
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ExecuteTimeFilter implements IFilter {
	
	private static int minRecordTime = 0;
	
	private static String serviceName;
	
	private static UDPClient udpClient = null;
	
	private static ILog logger = LogFactory.getLogger(ExecuteTimeFilter.class);
	
	static {
		try {
			String ip = Global.getSingleton().getServiceConfig().getString("gaea.log.udpserver.ip");
			int port = Global.getSingleton().getServiceConfig().getInt("gaea.log.udpserver.port");
			minRecordTime = Global.getSingleton().getServiceConfig().getInt("gaea.log.exectime.limit");
			serviceName = Global.getSingleton().getServiceConfig().getString("gaea.service.name");
			
			if(ip == null || port <= 0) {
				logger.error("upd ip is null or port is null");
			} else {
				udpClient = UDPClient.getInstrance(ip, port, "utf-8");
			}
		} catch(Exception ex) {
			logger.error("init ExecuteTimeFilter error", ex);
		}
	}

	@Override
	public void filter(GaeaContext context) throws Exception {
		StopWatch sw = context.getStopWatch();
		Collection<PerformanceCounter> pcList = sw.getMapCounter().values();
		for(PerformanceCounter pc : pcList) {
			if(pc.getExecuteTime() > minRecordTime) {
				StringBuilder sbMsg = new StringBuilder();
				sbMsg.append(serviceName);
				sbMsg.append("--");
				sbMsg.append(pc.getKey());
				sbMsg.append("--time: ");
				sbMsg.append(pc.getExecuteTime());
				
				sbMsg.append(" [fromIP: ");
				sbMsg.append(sw.getFromIP());
				sbMsg.append(";localIP: ");
				sbMsg.append(sw.getLocalIP()+"]");
				
				udpClient.send(sbMsg.toString());
			}
		}
	}

	@Override
	public int getPriority() {
		return 0;
	}
}