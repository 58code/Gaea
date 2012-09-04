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
package com.bj58.spat.gaea.client.configuration.commmunication;

import com.bj58.spat.gaea.client.GaeaConst;
import com.bj58.spat.gaea.client.utility.helper.TimeSpanHelper;
import org.w3c.dom.Node;

/**
 * SocketPoolProfile
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class SocketPoolProfile {

    private int minPoolSize;
    private int maxPoolSize;
    private int sendTimeout;
    private int receiveTimeout;
    private int waitTimeout;
    private boolean nagle;
    private int shrinkInterval;
    private int bufferSize;
    private int connectionTimeout = 3000;
    private int maxPakageSize;
    private boolean _protected;
	private int reconnectTime = 0;
	
	private int recvBufferSize;
	private int sendBufferSize;

    public SocketPoolProfile(Node node) {
        this.minPoolSize = Integer.parseInt(node.getAttributes().getNamedItem("minPoolSize").getNodeValue());
        this.maxPoolSize = Integer.parseInt(node.getAttributes().getNamedItem("maxPoolSize").getNodeValue());
        this.sendTimeout = TimeSpanHelper.getIntFromTimeSpan(node.getAttributes().getNamedItem("sendTimeout").getNodeValue().toString());
        this.receiveTimeout = TimeSpanHelper.getIntFromTimeMsSpan(node.getAttributes().getNamedItem("receiveTimeout").getNodeValue().toString());
        this.waitTimeout = TimeSpanHelper.getIntFromTimeSpan(node.getAttributes().getNamedItem("waitTimeout").getNodeValue().toString());
        this.nagle = Boolean.parseBoolean(node.getAttributes().getNamedItem("nagle").getNodeValue().toString());
        this.shrinkInterval = TimeSpanHelper.getIntFromTimeSpan(node.getAttributes().getNamedItem("autoShrink").getNodeValue().toString());
        this.bufferSize = Integer.parseInt(node.getAttributes().getNamedItem("bufferSize").getNodeValue());
        if (bufferSize < GaeaConst.DEFAULT_BUFFER_SIZE) {
            bufferSize = GaeaConst.DEFAULT_BUFFER_SIZE;
        }
        Node nProtected = node.getAttributes().getNamedItem("protected");
        if (nProtected == null) {
            this._protected = GaeaConst.DEFAULT_PROTECTED;
        } else {
            this._protected = Boolean.parseBoolean(nProtected.getNodeValue());
        }
        Node nPackage = node.getAttributes().getNamedItem("maxPakageSize");
        if (nPackage == null) {
            this.maxPakageSize = GaeaConst.DEFAULT_MAX_PAKAGE_SIZE;
        } else {
            this.maxPakageSize = Integer.parseInt(nPackage.getNodeValue());
        }
        
        Node nReconnectTime = node.getAttributes().getNamedItem("reconnectTime");
        if (nReconnectTime != null) {
            this.reconnectTime = Integer.parseInt(nReconnectTime.getNodeValue());
            if(reconnectTime < 0){
            	reconnectTime = 0;
            }
        }
        
        this.recvBufferSize = 1024 * 1024 * 8;
		this.sendBufferSize = 1024 * 1024 * 8;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public int getMinPoolSize() {
        return minPoolSize;
    }

    public boolean isNagle() {
        return nagle;
    }

    public int getReceiveTimeout() {
        return receiveTimeout;
    }

    public int getSendTimeout() {
        return sendTimeout;
    }

    public int getShrinkInterval() {
        return shrinkInterval;
    }

    public int getWaitTimeout() {
        return waitTimeout;
    }

    public boolean AutoShrink() {
        return shrinkInterval > 0;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public int getMaxPakageSize() {
        return maxPakageSize;
    }

    public boolean isProtected() {
        return _protected;
    }
    public int getReconnectTime() {
		return reconnectTime;
	}

	public int getRecvBufferSize() {
		return recvBufferSize;
	}

	public void setRecvBufferSize(int recvBufferSize) {
		this.recvBufferSize = recvBufferSize;
	}

	public int getSendBufferSize() {
		return sendBufferSize;
	}

	public void setSendBufferSize(int sendBufferSize) {
		this.sendBufferSize = sendBufferSize;
	}
}
