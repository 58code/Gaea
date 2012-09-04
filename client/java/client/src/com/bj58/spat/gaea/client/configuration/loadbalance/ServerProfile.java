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
package com.bj58.spat.gaea.client.configuration.loadbalance;

import com.bj58.spat.gaea.client.GaeaConst;
import com.bj58.spat.gaea.client.utility.helper.TimeSpanHelper;
import org.w3c.dom.*;

/**
 * ServerProfile
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ServerProfile {

    private String name;
    private String host;
    private int port;
    private int deadTimeout;
    private float weithtRate;

    public ServerProfile(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        this.name = attributes.getNamedItem("name").getNodeValue();
        this.host = attributes.getNamedItem("host").getNodeValue();
        this.port = Integer.parseInt(attributes.getNamedItem("port").getNodeValue());
        Node atribute = attributes.getNamedItem("weithtRate");
        if (atribute != null) {
            this.weithtRate = Float.parseFloat(atribute.getNodeValue().toString());
        } else {
            this.weithtRate = 1;
        }
        atribute = node.getParentNode().getAttributes().getNamedItem("deadTimeout");
        if (atribute != null) {
        	//设置最小值为30s
        	int dtime = TimeSpanHelper.getIntFromTimeSpan(atribute.getNodeValue().toString());        	
        	if(dtime < 30000){
        		dtime = 30000;
        	}
            this.deadTimeout = dtime;
        } else {
            this.deadTimeout = GaeaConst.DEFAULT_DEAD_TIMEOUT;
        }
    }

    /*
     * Unit is ms
     */
    public int getDeadTimeout() {
        return deadTimeout;
    }

    public String getHost() {
        return host;
    }

    public String getName() {
        return name;
    }

    public int getPort() {
        return port;
    }

    public float getWeithtRate() {
        return weithtRate;
    }
}
