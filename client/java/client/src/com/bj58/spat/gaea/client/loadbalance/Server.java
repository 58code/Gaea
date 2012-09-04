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
package com.bj58.spat.gaea.client.loadbalance;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.bj58.spat.gaea.client.communication.socket.CSocket;
import com.bj58.spat.gaea.client.communication.socket.ScoketPool;
import com.bj58.spat.gaea.client.communication.socket.ThreadRenameFactory;
import com.bj58.spat.gaea.client.configuration.loadbalance.ServerProfile;
import com.bj58.spat.gaea.client.loadbalance.component.ServerState;
import com.bj58.spat.gaea.client.utility.logger.ILog;
import com.bj58.spat.gaea.client.utility.logger.LogFactory;
import com.bj58.spat.gaea.protocol.sfp.v1.Protocol;

/**
 * Server
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Server {

    private static ILog logger = LogFactory.getLogger(Server.class);
    private String name;
    private String address;
    private int port;
    private int weight;
    private float weightRage;
    private ServerState state;
    private ScoketPool scoketpool;
    private int currUserCount;
    private int deadTimeout;
    private long deadTime;
    private boolean testing = false;
    private final ScheduledExecutorService scheduler;

    protected Server(ServerProfile config) {
        this.name = config.getName();
        this.address = config.getHost();
        this.port = config.getPort();
        this.weightRage = config.getWeithtRate();
        this.deadTimeout = config.getDeadTimeout();
        if (this.weightRage >= 0) {
            this.state = ServerState.Normal;
        } else {
            this.state = ServerState.Disable;
        }
        scheduler = Executors.newScheduledThreadPool(2,new ThreadRenameFactory("Async "+this.getName()+"-Server Thread"));
    }

    public Protocol request(Protocol p) throws Exception, Throwable {
        if (state == ServerState.Dead) {
        	logger.warn("This proxy server is unavailable.state:" + state + "+host:" + address);
        	throw new Exception("This proxy server is unavailable.state:" + state + "+host:" + address);
        }
        
        increaseCU();
        CSocket socket = null;
        try {
            try {
            	socket = this.scoketpool.getSocket();
                byte[] data = p.toBytes(socket.isRights(),socket.getDESKey());
                socket.registerRec(p.getSessionID());
                socket.send(data);  
            } catch (Throwable ex) {
                logger.error("Server get socket Exception", ex);
                throw ex;
            }finally {
            	if(socket != null){
            		socket.dispose();
            	}
            }
            byte[] buffer = socket.receive(p.getSessionID(), currUserCount);
            Protocol result = Protocol.fromBytes(buffer,socket.isRights(),socket.getDESKey());
            if (this.state == ServerState.Testing) {
                relive();
            }
            return result;
        } catch (IOException ex) {
            logger.error("io exception", ex);
            if (socket == null || !socket.connecting()) {
                if (!test()) {
                    markAsDead();
                }
            }
            throw ex;
        } catch (Throwable ex) {
            logger.error("request other Exception", ex);
            throw ex;
        } finally {
            if (state == state.Testing) {
                markAsDead();
            }
            if (socket != null) {
                socket.unregisterRec(p.getSessionID());
            }
            decreaseCU();
        }
    }

    /**
     * Increase current user
     */
    private synchronized void increaseCU() {
        currUserCount++;
    }

    /**
     * Decrease current user
     */
    private synchronized void decreaseCU() {
        currUserCount--;
        if (currUserCount <= 0) {
            currUserCount = 0;
        }
    }

    /**
     * mark Server to dead
     * @throws IOException
     */
    private void markAsDead() throws Exception {
        logger.info("markAsDead server:" + this.state + "--server hashcode:" + this.hashCode() + "--conn count:" + this.scoketpool.count());
        if (this.state == ServerState.Dead) {
            logger.info("before markAsDead the server is dead!!!");
            return;
        }
        synchronized (this) {
            if (this.state == ServerState.Dead) {
                logger.info("before markAsDead the server is dead!!!");
                return;
            }
            logger.warn("this server is dead!host:" + address);
            this.setState(ServerState.Dead);
            this.deadTime = System.currentTimeMillis();
            this.scoketpool.destroy();
        }
    }
    
    /**
     * 设置当前重启服务
     * @param server
     * @throws Throwable 
     * @throws Exception 
     */
    public void createReboot() throws Exception, Throwable{
    	synchronized(this){
    		if (this.state == ServerState.Reboot) {
            	logger.info("before markAsReboot the server is Reboot!");
                return;
            }
    		
    		logger.warn("this server is reboot! host:" + address);
    		this.setState(ServerState.Reboot);//设置当前服务为重启状态
            this.setDeadTime(System.currentTimeMillis());
            /**
             * 如果当前连接处于重启状态则注销当前服务所有socket
             * 任务调度 3秒后执行
             */
            scheduler.schedule(new TimerJob(this), 3, TimeUnit.SECONDS);
    	}
    }
    
    /**
     * 设置当前服务为正常状态
     */
    public void markAsNormal(){
    	this.relive();
    }
    
    /**
     * relive Server if this Server is died
     */
    private void relive() {
        logger.info("this server is relive!host:" + address);
        if (this.state == ServerState.Normal) {
            return;
        }
        synchronized (this) {
            if (this.state == ServerState.Normal) {
                return;
            }
            logger.info("this server is relive!host:" + address);
            this.state = ServerState.Normal;
        }
    }

    private boolean test() {
        if (testing) {
            return true;
        }
        testing = true;
        boolean result = false;
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(this.address, this.port), 100);
            socket.close();
            result = true;
        } catch (Exception e) {
        } finally {
            logger.info("test server :" + this.address + ":" + this.port + "--alive:" + result);
            testing = false;
        }
        return result;
    }
    
    public boolean testing(){
    	return this.test();
    }
    
    public long getDeadTime() {
        return deadTime;
    }

    public void setDeadTime(long deadTime) {
        this.deadTime = deadTime;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
    
    public int getCurrUserCount() {
        return currUserCount;
    }

    public int getPort() {
        return port;
    }

    public ScoketPool getScoketpool() {
        return scoketpool;
    }

    protected void setScoketpool(ScoketPool scoketpool) {
        this.scoketpool = scoketpool;
    }

    public ServerState getState() {
        return state;
    }

    protected synchronized void setState(ServerState state) {
        this.state = state;
    }

    public int getWeight() {
        return weight;
    }

    public float getWeightRage() {
        return weightRage;
    }

    public int getDeadTimeout() {
        return deadTimeout;
    }

    protected void setDeadTimeout(int deadTimeout) {
        this.deadTimeout = deadTimeout;
    }
    
    @Override
    public String toString() {
        return "Name:" + name + ",Address:" + address + ",Port:" + port + ",Weight:" + weight + ",State:" + state.toString() + ",CurrUserCount:" + currUserCount + ",ScoketPool:" + scoketpool.count();
    }
}
