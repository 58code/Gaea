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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;

import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;
import com.bj58.spat.gaea.server.deploy.hotdeploy.DynamicClassLoader;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ProxyFactoryCreater {

	private static ILog logger = LogFactory.getLogger(ProxyFactoryCreater.class);
	
	@SuppressWarnings("rawtypes")
	public ClassFile createProxy(DynamicClassLoader classLoader, ContractInfo serviceContract, long time) throws Exception {
		String pfClsName = "ProxyFactory" + time;
		logger.info("begin create ProxyFactory:" + pfClsName);
		ClassPool pool = ClassPool.getDefault();
		List<String> jarList = classLoader.getJarList();
		for(String jar : jarList) {
			pool.appendClassPath(jar);
		}
		
		CtClass ctProxyClass = pool.makeClass(pfClsName, null);

		CtClass proxyFactory = pool.getCtClass(Constant.IPROXYFACTORY_CLASS_NAME);
		ctProxyClass.addInterface(proxyFactory);
		
		//createProxy
		StringBuilder sbBody = new StringBuilder();
		sbBody.append("public " + Constant.IPROXYSTUB_CLASS_NAME + " getProxy(String lookup) {");
		StringBuilder sbConstructor = new StringBuilder();
		sbConstructor.append("{");
		int proxyCount = 0;
		for (ContractInfo.SessionBean sessionBean : serviceContract.getSessionBeanList()) {
			Iterator it = sessionBean.getInstanceMap().entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				String lookup = entry.getKey().toString();

				sbBody.append("if(lookup.equalsIgnoreCase(\"" + lookup + "\")){");
				sbBody.append("return proxy");
				sbBody.append(lookup);
				sbBody.append(Global.getSingleton().getServiceConfig().getString("gaea.service.name"));
				sbBody.append(";}");
				
				sbConstructor.append("proxy");
				sbConstructor.append(lookup);
				sbConstructor.append(Global.getSingleton().getServiceConfig().getString("gaea.service.name"));
				sbConstructor.append("=(");
				sbConstructor.append(Constant.IPROXYSTUB_CLASS_NAME);
				sbConstructor.append(")$1.get(");
				sbConstructor.append(proxyCount);
				sbConstructor.append(");");
				
				CtField proxyField = CtField.make(
						"private "
						 + Constant.IPROXYSTUB_CLASS_NAME+" proxy"
						 + lookup + Global.getSingleton().getServiceConfig().getString("gaea.service.name")
						 + " = null;",
						ctProxyClass);
				
				ctProxyClass.addField(proxyField);
				
				proxyCount++;
			}
		}
		sbBody.append("return null;}}");
		sbConstructor.append("}");

		CtMethod methodItem = CtMethod.make(sbBody.toString(), ctProxyClass);
		ctProxyClass.addMethod(methodItem);
		
	    CtConstructor cc = new CtConstructor(new CtClass[]{pool.get("java.util.List")}, ctProxyClass);
	    cc.setBody(sbConstructor.toString());
	    ctProxyClass.addConstructor(cc);

		logger.debug("ProxyFactory source code:"+sbBody.toString());

	    logger.info("create ProxyFactory success!!!");
	    
		return new ClassFile(pfClsName, ctProxyClass.toBytecode());
	}
}