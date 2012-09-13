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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jboss.netty.channel.Channel;

import com.bj58.spat.gaea.server.contract.filter.IFilter;
import com.bj58.spat.gaea.server.contract.init.IInit;
import com.bj58.spat.gaea.server.contract.server.IServer;

/**
 * A class contains global variable
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Global {

	private ServiceConfig serviceConfig = null;
	
	private IProxyFactory proxyFactory = null;
	
	private List<IServer> serverList = new ArrayList<IServer>();
	
	private List<IFilter> globalRequestFilterList = new ArrayList<IFilter>();
	
	private List<IFilter> globalResponseFilterList = new ArrayList<IFilter>();
	
	private List<IFilter> connectionFilterList = new ArrayList<IFilter>();
	
	private List<IInit> initList = new ArrayList<IInit>();
	
	private String rootPath;
	
	private ThreadLocal<GaeaContext> threadLocal = new ThreadLocal<GaeaContext>();
	
	private static final Object lockHelper = new Object();
	
	private static volatile Global m_global = null;

	/**
	 * 授权文件、对应方法
	 * @author HaoXB
	 * @date 2011-09-05
	 */
	private Map<String,List<String>> secureMap = new HashMap<String,List<String>>();
	/**
	 * 各channel对应SecureContext
	 */
	private ConcurrentMap<Channel,SecureContext> channelMap = new ConcurrentHashMap<Channel,SecureContext>();
	/**
	 * 服务器运行状态Nomarl正常、Reboot重启
	 */
	private ServerStateType serverState = ServerStateType.Nomarl;
	
	/**
	 * 获取单例Global
	 * @return
	 */
	public static Global getSingleton() {
		if(m_global == null) {
			synchronized (lockHelper) {
				if(m_global == null) {
					m_global = new Global();
				}
			}
		}
		
		return m_global;
	}
	
	private Global() {
		
	}
	
	public ServiceConfig getServiceConfig() {
		return this.serviceConfig;
	}

	public void setServiceConfig(ServiceConfig serviceConfig) {
		this.serviceConfig = serviceConfig;
	}

	public List<IInit> getInitList() {
		return this.initList;
	}
	
	public List<IFilter> getGlobalRequestFilterList() {
		return this.globalRequestFilterList;
	}

	public List<IFilter> getGlobalResponseFilterList() {
		return this.globalResponseFilterList;
	}
	
	public List<IFilter> getConnectionFilterList() {
		return connectionFilterList;
	}

	public void setConnectionFilterList(List<IFilter> connectionFilterList) {
		this.connectionFilterList = connectionFilterList;
	}

	public void setProxyFactory(IProxyFactory proxyFactory) {
		this.proxyFactory = proxyFactory;
	}

	public IProxyFactory getProxyFactory() {
		return this.proxyFactory;
	}

	public List<IServer> getServerList() {
		return this.serverList;
	}
	
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getRootPath() {
		return this.rootPath;
	}

	public void addServer(IServer server) {
		synchronized (serverList) {
			serverList.add(server);
		}
	}
	
	public void addInit(IInit init) {
		synchronized(initList) {
			initList.add(init);
		}
	}
	
	public void addGlobalRequestFilter(IFilter filter) {
		synchronized (globalRequestFilterList) {
			globalRequestFilterList.add(filter);
		}
	}
	
	public void removeGlobalRequestFilter(IFilter filter) {
		synchronized (globalRequestFilterList) {
			globalRequestFilterList.remove(filter);
		}
	}
	
	public void addGlobalResponseFilter(IFilter filter) {
		synchronized (globalResponseFilterList) {
			globalResponseFilterList.add(filter);
		}
	}
	
	public void removeGlobalResponseFilter(IFilter filter) {
		synchronized (globalResponseFilterList) {
			globalResponseFilterList.remove(filter);
		}
	}
	
	
	public void addConnectionFilter(IFilter filter) {
		synchronized (connectionFilterList) {
			connectionFilterList.add(filter);
		}
	}

	public void removeConnectionFilter(IFilter filter) {
		synchronized (connectionFilterList) {
			connectionFilterList.remove(filter);
		}
	}

	public ThreadLocal<GaeaContext> getThreadLocal() {
		return threadLocal;
	}
	
	public void removeSecureMap(String key) {
		secureMap.remove(key);
	}
	public boolean containsSecureMap(String key) {
		return secureMap.containsKey(key);
	}
	
	public Map getSecureMap(){
		return secureMap;
	}
	public void addSecureMap(String key,List<String> list) {
		secureMap.put(key, list);
	}
	
	public void addChannelMap(Channel channel,SecureContext context){
		channelMap.put(channel, context);
	}
	
	public void removeChannelMap(Channel channel){
		channelMap.remove(channel);
	}
	
	public Map getChannelMap(){
		return channelMap;
	}
	
	public ServerStateType getServerState() {
		return serverState;
	}

	public void setServerState(ServerStateType serverState) {
		this.serverState = serverState;
	}
	
	/**
	 *获的当前channel对应密钥类 
	 * @return SecureContext
	 * @author HaoXB
	 * @date 2010-09-01
	 */
	public SecureContext getGlobalSecureContext(Channel channel){
		if(null != this.channelMap){
			return this.channelMap.get(channel);
		}
		return null;
	}
	
	/**
	 * 获得是否启用权限认证 
	 * @return boolean
	 * @author HaoXB
	 * @date 2010-09-01
	 */
	public boolean getGlobalSecureIsRights(){
		if(null != this.serviceConfig){
			return ("true".equalsIgnoreCase(this.serviceConfig.getString("gaea.secure")))?true:false;
		}
		return false;
	}
}