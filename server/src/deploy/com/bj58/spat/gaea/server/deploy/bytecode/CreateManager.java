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

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import com.bj58.spat.gaea.server.contract.context.IProxyFactory;
import com.bj58.spat.gaea.server.contract.context.IProxyStub;
import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;
import com.bj58.spat.gaea.server.deploy.hotdeploy.DynamicClassLoader;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class CreateManager {
	
	private static ILog logger = LogFactory.getLogger(CreateManager.class);

	public IProxyFactory careteProxy(String serviceRootPath, DynamicClassLoader classLoader) throws Exception {
		
		String configPath = serviceRootPath + "/" + Constant.SERVICE_CONTRACT;
		File file = new File(configPath);
		ContractInfo serviceContract = null;
		
		if (file != null && file.exists()) {
			serviceContract = ContractConfig.loadContractInfo(configPath, classLoader);
		} else {
			serviceContract = ScanClass.getContractInfo(serviceRootPath + "/", classLoader);
		}
		
		
		long time = System.currentTimeMillis();
		List<ClassFile> localProxyList = new ProxyClassCreater().createProxy(classLoader, serviceContract, time);
		logger.info("proxy class buffer creater finish!!!");
		ClassFile cfProxyFactory = new ProxyFactoryCreater().createProxy(classLoader, serviceContract, time);
		logger.info("proxy factory buffer creater finish!!!");
		
		List<IProxyStub> localProxyAry = new ArrayList<IProxyStub>();
		for(ClassFile cf : localProxyList) {
			Class<?> cls = classLoader.findClass(cf.getClsName(), cf.getClsByte(), null);
			logger.info("dynamic load class:" + cls.getName());
			localProxyAry.add((IProxyStub)cls.newInstance());
		}
		
		Class<?> proxyFactoryCls = classLoader.findClass(cfProxyFactory.getClsName(), cfProxyFactory.getClsByte(), null);
		Constructor<?> constructor = proxyFactoryCls.getConstructor(List.class);
		IProxyFactory pfInstance = (IProxyFactory)constructor.newInstance(localProxyAry);
		logger.info("crate ProxyFactory instance!!!");
		return pfInstance;
	}
}