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

import com.bj58.spat.gaea.client.communication.socket.ScoketPool;
import com.bj58.spat.gaea.client.configuration.ServiceConfig;
import com.bj58.spat.gaea.client.configuration.loadbalance.ServerProfile;
import com.bj58.spat.gaea.client.loadbalance.component.ServerState;
import com.bj58.spat.gaea.client.utility.logger.ILog;
import com.bj58.spat.gaea.client.utility.logger.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Dispatcher
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Dispatcher {

    private static ILog logger = LogFactory.getLogger(Server.class);
    private List<Server> ServerPool = new ArrayList<Server>();
    private AtomicInteger requestCount = new AtomicInteger(0);
    

    /**
     * Constructor
     * @param Configuration.Configuration object
     */
    public Dispatcher(ServiceConfig config) {
        logger.info("starting init servers");
        logger.debug("init connection begin:" + System.currentTimeMillis());
        for (ServerProfile ser : config.getServers()) {
            if (ser.getWeithtRate() > 0) {
                Server s = new Server(ser);
                if (s.getState() != ServerState.Disable) {
                    ScoketPool sp = new ScoketPool(s, config);
                    s.setScoketpool(sp);
                    ServerPool.add(s);
                }
            }
        }
        logger.debug("init connection end:" + System.currentTimeMillis());
        logger.info("init servers end");
    }

    /**
     * get Server from Server pool
     * @return return a Server minimum of current user
     */
    public Server GetServer() {
        if (ServerPool == null || ServerPool.size() == 0) {
            return null;
        }

        Server result = null;
        int currUserCount = -1;
        int count = ServerPool.size();
        int start = requestCount.get() % count;
        if (requestCount.get() > 10) {
            requestCount.set(0);
        } else {
            requestCount.getAndIncrement();
        }

        for (int i = start; i < start + count; i++) {
            int index = i % count;
            Server server = ServerPool.get(index);
  
            if (server.getState() == ServerState.Dead && (System.currentTimeMillis() - server.getDeadTime()) > server.getDeadTimeout()) {
                synchronized (this) {
                    if (server.getState() == ServerState.Dead && (System.currentTimeMillis() - server.getDeadTime()) > server.getDeadTimeout()) {
                        result = server;
                        server.setState(ServerState.Testing);
                        server.setDeadTime(0);
                        logger.warning("find server that need to test!host:" + server.getAddress());
                        break;
                    }
                }
            }
            
            if(server.getState() == ServerState.Reboot && (System.currentTimeMillis() - server.getDeadTime()) > server.getDeadTimeout()){
       		 	synchronized (this) {
                    if (server.getState() == ServerState.Reboot && (System.currentTimeMillis() - server.getDeadTime()) > server.getDeadTimeout()) {
                    	result = server;
	                   	server.setState(ServerState.Testing);
                        server.setDeadTime(0);
                        requestCount.getAndDecrement();
                        logger.warning("find server that need to test!host:" + server.getAddress());
                        break;
                    }
                }
            }
          
            //? Find the least load
            if ((server.getCurrUserCount() < currUserCount * server.getWeightRage() || currUserCount < 0)
                    && server.getState() == ServerState.Normal) {
                currUserCount = server.getCurrUserCount();
                result = server;
            }
        }

        if (result == null) {
        	result = ServerPool.get(new Random().nextInt(count));
        }
        return result;
    }

    public Server GetServer(String name) {
        for (Server s : ServerPool) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }
    
    public List<Server> GetAllServer() {
    	return ServerPool;
    }
}
