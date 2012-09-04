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
package com.bj58.spat.gaea.server.deploy.filemonitor;

import com.bj58.spat.gaea.serializer.component.helper.TypeHelper;
import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.context.IProxyFactory;
import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;
import com.bj58.spat.gaea.server.deploy.hotdeploy.DynamicClassLoader;
import com.bj58.spat.gaea.server.deploy.hotdeploy.ProxyFactoryLoader;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class HotDeployListener implements IListener {
	
	/**
	 * log
	 */
	private static ILog logger = LogFactory.getLogger(HotDeployListener.class);

	
	public void fileChanged(FileInfo fInfo) {
		logger.info("service file is change!!! ");
		try {
			logger.info("begin hot deploy gaea...");
			
			DynamicClassLoader classLoader = new DynamicClassLoader();
			classLoader.addFolder(
					Global.getSingleton().getRootPath() + "service/deploy/" + Global.getSingleton().getServiceConfig().getString("gaea.service.name") + "/",
					Global.getSingleton().getRootPath() + "service/lib/",
					Global.getSingleton().getRootPath() + "lib"
					);
			
			IProxyFactory proxyFactory = ProxyFactoryLoader.loadProxyFactory(classLoader);
			if(proxyFactory != null) {
				Global.getSingleton().setProxyFactory(proxyFactory);
				logger.info("change context class loader");
				Thread.currentThread().setContextClassLoader(proxyFactory.getClass().getClassLoader());
				logger.info("init serializer type map");
				TypeHelper.InitTypeMap();
				logger.info("notice gc");
				System.gc();
				logger.info("hot deploy service success!!!");
				
			} else {
				logger.error("IInvokerHandle is null when hotDeploy!!!");
			}

			logger.info("finish hot deploy!!!");
		} catch (Exception e) {
			logger.error("create proxy error" , e);
		}
	}
}