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
package com.bj58.spat.gaea.client.communication.socket;

import com.bj58.spat.gaea.client.utility.logger.ILog;
import com.bj58.spat.gaea.client.utility.logger.LogFactory;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * DataReceiver
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class DataReceiver {

    private static DataReceiver _DataReceiver = null;
    List<CSocket> sockets = new ArrayList<CSocket>();
    private final static Object LockHelper = new Object();

    private DataReceiver() {
        Worker worker = new Worker(sockets);
        Thread thread = new Thread(worker);
        thread.setDaemon(true);
        thread.start();
    }

    public static DataReceiver instance() throws ClosedChannelException, IOException {
        if (_DataReceiver == null) {
            synchronized (LockHelper) {
                if (_DataReceiver == null) {
                    _DataReceiver = new DataReceiver();
                }
            }
        }
        return _DataReceiver;
    }

    public synchronized void RegSocketChannel(final CSocket socket) throws ClosedChannelException, IOException {
        sockets.add(socket);
    }

    public synchronized void UnRegSocketChannel(CSocket socket) {
        sockets.remove(socket);
        Handle.RemoveInstance(socket);
    }
}

class Worker implements Runnable {

    private final static int T_COUNT = Runtime.getRuntime().availableProcessors();//CPU核数
    private ExecutorService pool = Executors.newFixedThreadPool(T_COUNT, new ThreadRenameFactory("Async DataReceiver Thread"));
    private static ILog logger = LogFactory.getLogger(Worker.class);
    List<CSocket> sockets = null;

    public Worker(List<CSocket> sockets) {
        this.sockets = sockets;
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (final CSocket socket : sockets) {
                    try {
                        pool.execute(Handle.getInstance(socket));
                    } catch (Throwable ex) {
                        logger.error(ex);
                    }
                }
                Thread.sleep(1);
            } catch (Throwable ex) {
                logger.error(ex);
            }
        }
    }
}

class Handle implements Runnable {

    private static ConcurrentHashMap<CSocket, Handle> mapInstance = new ConcurrentHashMap<CSocket, Handle>();
    private static ILog logger = LogFactory.getLogger(Handle.class);
    private final static Object lockHelper = new Object();
    private CSocket socket = null;

    protected static Handle getInstance(CSocket socket) {
        Handle handle = mapInstance.get(socket);
        if (handle == null) {
            synchronized (lockHelper) {
                if (handle == null) {
                    handle = new Handle(socket);
                    mapInstance.put(socket, handle);
                }
            }
        }
        return handle;

    }

    private Handle(CSocket socket) {
        this.socket = socket;
    }

    protected static void RemoveInstance(CSocket socket) {
        synchronized (lockHelper) {
            mapInstance.remove(socket);
        }
    }

    @Override
    public void run() {
        try {
            socket.frameHandle();
        } catch (Throwable ex) {
            logger.error("socket frameHandle error "+ex);
            if (!socket.connecting()) {
                socket.dispose(true);
            }
        }
    }
}
