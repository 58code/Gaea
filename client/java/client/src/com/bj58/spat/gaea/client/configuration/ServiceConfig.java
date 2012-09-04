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
package com.bj58.spat.gaea.client.configuration;

import com.bj58.spat.gaea.client.GaeaConst;
import com.bj58.spat.gaea.client.configuration.commmunication.ProtocolProfile;
import com.bj58.spat.gaea.client.configuration.commmunication.SocketPoolProfile;
import com.bj58.spat.gaea.client.configuration.loadbalance.ServerProfile;
import com.bj58.spat.gaea.client.configuration.secure.KeyProfile;
import com.bj58.spat.gaea.client.utility.helper.XMLHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.xpath.*;
import org.w3c.dom.*;

/**
 * ServiceConfig
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public final class ServiceConfig {

    private String servicename;
    private int serviceid;
    private SocketPoolProfile SocketPool;
    private ProtocolProfile protocol;
    private List<ServerProfile> servers;
    private KeyProfile SecureKey;//授权配置文件
    
    public KeyProfile getSecureKey() {
		return SecureKey;
	}

	public void setSecureKey(KeyProfile secureKey) {
		SecureKey = secureKey;
	}

    private ServiceConfig() {
    }

    public SocketPoolProfile getSocketPool() {
        return SocketPool;
    }

    public ProtocolProfile getProtocol() {
        return protocol;
    }

    public List<ServerProfile> getServers() {
        return servers;
    }

    public int getServiceid() {
        return serviceid;
    }

    public String getServicename() {
        return servicename;
    }

    public static ServiceConfig GetConfig(String serviceName) throws Exception {
        File f = new File(GaeaConst.CONFIG_PATH);
        if (!f.exists()) {
            throw new Exception("gaea.config not fond:" + GaeaConst.CONFIG_PATH);
        }

        Element xmlDoc = XMLHelper.GetXmlDoc(GaeaConst.CONFIG_PATH);
        XPathFactory xpathFactory = XPathFactory.newInstance();
        XPath xpath = xpathFactory.newXPath();
        Node serviceNode = (Node) xpath.evaluate("//Service[@name='" + serviceName + "']", xmlDoc, XPathConstants.NODE);
        if(serviceNode == null){
        	printExceprion(0,serviceName);
        }
        
        ServiceConfig config = new ServiceConfig();
        config.servicename = serviceNode.getAttributes().getNamedItem("name").getNodeValue();
        Node idNode = serviceNode.getAttributes().getNamedItem("id");
        if(idNode == null){
        	printExceprion(4,serviceName);
        }
        config.serviceid = Integer.parseInt(idNode.getNodeValue());
        Node xnSocketPool = (Node) xpath.evaluate("Commmunication/SocketPool", serviceNode, XPathConstants.NODE);
        if(xnSocketPool == null){
        	printExceprion(1,serviceName);
        }
        config.SocketPool = new SocketPoolProfile(xnSocketPool);

        Node xnProtocol = (Node) xpath.evaluate("Commmunication/Protocol", serviceNode, XPathConstants.NODE);
        if(xnProtocol == null){
        	printExceprion(2,serviceName);
        }
        config.protocol = new ProtocolProfile(xnProtocol);
        
        /**
         * 加载授权文件key
         */
        Node xnKey = (Node) xpath.evaluate("Secure/Key", serviceNode, XPathConstants.NODE);
        config.SecureKey = new KeyProfile(xnKey);
        
        NodeList xnServers = (NodeList) xpath.evaluate("Loadbalance/Server/add", serviceNode, XPathConstants.NODESET);
        if(xnServers == null || xnServers.getLength() == 0){
        	printExceprion(3,serviceName);
        }
        
        List<ServerProfile> servers = new ArrayList<ServerProfile>();
        for (int i = 0; i < xnServers.getLength(); i++) {
            servers.add(new ServerProfile(xnServers.item(i)));
        }
        config.servers = servers;
        config.servicename = serviceName;
        return config;
    }
    
    
    private static void printExceprion(int i, String serviceName) throws Exception{
    	switch (i) {
			case 0:
				throw new Exception("gaea.config中没有发现" + serviceName + "服务节点!");
			case 1:
				throw new Exception("gaea.config服务节点" + serviceName + "没有发现Commmunication/SocketPool配置!");
			case 2:
				throw new Exception("gaea.config服务节点" + serviceName + "没有发现Commmunication/Protocol配置!");
			case 3:
				throw new Exception("gaea.config服务节点" + serviceName + "没有发现Loadbalance/Server/add配置!");
			case 4:
				throw new Exception("gaea.config服务节点" + serviceName + "没有发现Service/id配置!");
			default:
				break;
		}
    }
}
