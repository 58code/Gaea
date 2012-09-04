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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.bj58.spat.gaea.client.configuration.ServiceConfig;
import com.bj58.spat.gaea.client.configuration.commmunication.SocketPoolProfile;
import com.bj58.spat.gaea.client.loadbalance.Server;
import com.bj58.spat.gaea.client.proxy.ServiceProxy;
import com.bj58.spat.gaea.client.secure.SecureKey;
import com.bj58.spat.gaea.client.utility.logger.ILog;
import com.bj58.spat.gaea.client.utility.logger.LogFactory;
import com.bj58.spat.gaea.protocol.exception.RebootException;
import com.bj58.spat.gaea.protocol.exception.ThrowErrorHelper;
import com.bj58.spat.gaea.protocol.sdp.ExceptionProtocol;
import com.bj58.spat.gaea.protocol.sdp.HandclaspProtocol;
import com.bj58.spat.gaea.protocol.sfp.enumeration.SDPType;
import com.bj58.spat.gaea.protocol.sfp.v1.Protocol;

/**
 * scoket pool
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ScoketPool {

	private static final ILog logger = LogFactory.getLogger(ScoketPool.class);
    private InetSocketAddress endPoint;
    private SocketPoolProfile socketPoolConfig;
    private CQueueL queue = null;
    private Server server;
    private ServiceConfig serviceConfig;
    
    public ScoketPool(Server server, SocketPoolProfile config) {
        this.server = server;
        this.endPoint = new InetSocketAddress(server.getAddress(), server.getPort());
        this.socketPoolConfig = config;
        this.queue = new CQueueL(config.getShrinkInterval(), config.getMinPoolSize());
    }
    
    /**
     * 授权文件实例化
     * @param server
     * @param config
     * @param secureKey
     * @author HaoXB
     * @date 2011-09-01
     */
    public ScoketPool(Server server, ServiceConfig serviceconfig) {
        this.server = server;
        this.endPoint = new InetSocketAddress(server.getAddress(), server.getPort());
        this.socketPoolConfig = serviceconfig.getSocketPool();
        this.queue = new CQueueL(serviceconfig.getSocketPool().getShrinkInterval(), serviceconfig.getSocketPool().getMinPoolSize());
        this.serviceConfig = serviceconfig;
    }
    
    public int count() {
        return queue.size();
    }

    public synchronized CSocket getSocket() throws TimeoutException, IOException, InterruptedException, Throwable, Exception {
        CSocket rSocket = null;
        if (queue.size() > 0) {
            rSocket = queue.dequeue();
        } else if (queue.getTotal() < socketPoolConfig.getMaxPoolSize()) {
            CSocket socket = new CSocket(endPoint, this, socketPoolConfig);
            //如果启动权限认证则注册之前进行授权文件认证
            if(checkRights(socket)){
            	queue.register(socket);//注册到连接池中
                rSocket = socket;
            }else{
            	 logger.error("授权文件没有通过校验!");
            	 throw new Exception("授权文件没有通过校验!");
            }
        } else {        	
            if (queue.size() > 0) {            	
            	rSocket = queue.dequeue();
            } else {            	
            	rSocket = queue.dequeue(socketPoolConfig.getWaitTimeout());
            	if (rSocket == null) {
                    logger.error("socket connection pool is full!");
                    throw new TimeoutException("socket connection pool is full!");
                }
            }
        }
        if (rSocket == null) {
            throw new Exception("GetSocket socket is null!");
        }
        rSocket.setInPool(false);
        return rSocket;
    }

    public void release(CSocket socket) {
        if (socket == null) {
            logger.warn("socket is null when release(CSocket socket)");
        } else if (!socket.connecting()) {
            logger.warn("socket is closed when release(CSocket socket)--" + socket.toString());
            destroy(socket);
        } else if (socketPoolConfig.AutoShrink() && queue.shrink()) {
            socket.waitDestroy();
            logger.info("this socket is waitDestroy!");
        } else if (!socket.inPool()) {
            if (socket != null) {
                queue.enqueue(socket);
                socket.setInPool(true);
            }
        }
    }

    public void destroy(CSocket socket) {
        try {
            logger.warn("socket destroyed!--" + socket.toString());
            socket.disconnect();
        } catch (Throwable err) {
            logger.error("socket destroy error!--" + socket.toString(), err);
        } finally {
            queue.remove(socket);
        }
    }

    public void destroy() throws Exception {
        synchronized (this) {
            List<CSocket> csList = queue.getAllSocket();
            for (int i = 0; i < csList.size(); i++) {
                if (i < csList.size()) {
                    csList.get(i).dispose(true);
                }
            }
        }
    }
    
    /**
     * 权限认证
     * @param scoket
     * @return 是否校验成功
     * @author HaoXB
     * @throws Throwable 
     * @date 2010-09-01
     * 处理过程:
     * 1、建立连接
     * 2、客户端生成RSA公(CPublicKey)/私(CPrivateKey)钥,并将公钥(CPublicKey)传送给服务器端
     * 3、服务器端接收客户端提供的公钥(CPublicKey),并生成新的RSA公(SPublicKey)/私(SPrivateKey)钥,将公钥(SPublicKey)传送给客户端
     * 4、客户端用服务器端提供的公钥(SPublicKey)加密授权文件，并传送给服务器端
     * 5、服务器端通过服务器端私钥(SPrivateKey)解密、并校验授权文件是否正确，如果正确则返回通过客户端公钥(CPublicKey)加密的RSA密钥，否则返回null/false
     * 6、客户端通过客户端私钥(CPrivateKey)解密服务器端返回数据获得RSA密钥
     * 7、客户端、服务器端通过RSA加密数据进行交互
     */
    private boolean checkRights(CSocket scoket) throws Throwable{
    	
    	long startTime = System.currentTimeMillis();
        //如果没有启用权限认证，则直接返回true,直接注册socket
        if(serviceConfig.getSecureKey().getInfo() == null){
        	return true;
        }
        	
        //----发送客户端公钥去服务器端、并获取服务器端公钥--start---------------------
        SecureKey sk = new SecureKey();//生成客户端公/私 钥    	
        ServiceProxy proxy = ServiceProxy.getProxy(serviceConfig.getServicename());
        HandclaspProtocol handclaspProtocol = new HandclaspProtocol("1",sk.getStringPublicKey());
        Protocol publicKeyProtocol = proxy.createProtocol(handclaspProtocol);
        	
        try {
        	scoket.registerRec(publicKeyProtocol.getSessionID());
        	scoket.send(publicKeyProtocol.toBytes());//过程2
        	logger.info("send client publicKey sucess!");
        } finally {
        	//scoket.dispose();
        }
            
        handclaspProtocol = null;
        /**
        * 过程3,接收服务器端生成公钥
        */
        byte[] receivePublicBuffer = scoket.receive(publicKeyProtocol.getSessionID(), server.getCurrUserCount());
        if(null == receivePublicBuffer){
        	logger.warn("获取服务器公钥失败!");
            return false;
        }
            
        Protocol serverPublicProtocol = publicKeyProtocol.fromBytes(receivePublicBuffer);
        HandclaspProtocol _handclaspProtocol = null;
            
        if (serverPublicProtocol.getSDPType() == SDPType.Handclasp) {
        	_handclaspProtocol = (HandclaspProtocol)serverPublicProtocol.getSdpEntity();
            logger.debug("get server publicKey time:" + (System.currentTimeMillis() - startTime) + "ms");
                
        } else if (serverPublicProtocol.getSDPType() == SDPType.Exception) {
            ExceptionProtocol ep = (ExceptionProtocol)serverPublicProtocol.getSdpEntity();
            throw ThrowErrorHelper.throwServiceError(ep.getErrorCode(), ep.getErrorMsg());
        } else if(serverPublicProtocol.getSDPType() == SDPType.Reset){
        	throw new RebootException("this server is reboot!");
        } else {
            throw new Exception("userdatatype error!");
        }
            
        logger.info("receive server publicKey sucess!");
        publicKeyProtocol = null;
            
        String keyInfo = serviceConfig.getSecureKey().getInfo();//授权文件
        if(null == keyInfo || "".equals(keyInfo)){
        	logger.warn("获取授权文件失败!");
         	return false;
        }
            
        String ciphertext = sk.encryptByPublicKeyString(keyInfo, _handclaspProtocol.getData());
        _handclaspProtocol = null;
        serverPublicProtocol = null;
        //----发送客户端公钥去服务器端、并获取服务器端公钥--end---------------------
        //---发送授权文件到服务器端认证--------------------start-------------------------------
        HandclaspProtocol handclaspProtocol_ = new HandclaspProtocol("2",ciphertext);
        Protocol protocol_mw = proxy.createProtocol(handclaspProtocol_);
        	
        try {
        	scoket.registerRec(protocol_mw.getSessionID());
        	scoket.send(protocol_mw.toBytes());//过程4
        	logger.info("send keyInfo sucess!");
        } finally {
            //scoket.dispose();
        }
            
        handclaspProtocol_ = null;
        /**
         * 过程5
         * 获取由客户端公钥加密后的DES密钥
         */
        byte [] receiveDESKey = scoket.receive(protocol_mw.getSessionID(), server.getCurrUserCount());
        if(null == receiveDESKey){
        	logger.warn("获取DES密钥失败!");
        	return false;
        }
        logger.info("receive DESKey sucess!");
           
        HandclaspProtocol handclaspProtocol_mw = null;
        Protocol serverDesKeyProtocol = Protocol.fromBytes(receiveDESKey);
        if (serverDesKeyProtocol.getSDPType() == SDPType.Handclasp) {
        	handclaspProtocol_mw = (HandclaspProtocol)serverDesKeyProtocol.getSdpEntity();
        } else if (serverDesKeyProtocol.getSDPType() == SDPType.Exception) {
        	ExceptionProtocol ep = (ExceptionProtocol)serverDesKeyProtocol.getSdpEntity();
            throw ThrowErrorHelper.throwServiceError(ep.getErrorCode(), ep.getErrorMsg());
        } else if(serverDesKeyProtocol.getSDPType() == SDPType.Reset){
        	throw new RebootException("this server is reboot!");
        } else {
        	throw new Exception("userdatatype error!");
        }
        
        /**
        * 解密获取DES密钥
        */
        byte [] DESKeyStr = sk.decryptByPrivateKeyByte(handclaspProtocol_mw.getData(),sk.getStringPrivateKey());//过程6
        if(null == DESKeyStr){
        	logger.warn("解密DES密钥失败!");
            return false;
        }
        handclaspProtocol_mw = null;
        protocol_mw = null;
        //---发送授权文件到服务器端认证--------------------end-------------------------------
            
        scoket.setDESKey(DESKeyStr);
        scoket.setRights(true);
        logger.info("securekey use Time is "+String.valueOf(System.currentTimeMillis()-startTime)+" millisecond");
        return (scoket.getDESKey() != null && scoket.getDESKey().length >0) ? true : false;
    }
}


class CQueueL extends LinkedBlockingQueue<CSocket> {

    private int _duration;
    private int _minConn;
    private final Object shrinkLockHelper = new Object();
    private CopyOnWriteArrayList<CSocket> _AllSocket = new CopyOnWriteArrayList<CSocket>();
    
    public CQueueL(int _duration, int _minConn) {
        this._duration = _duration;
        this._minConn = _minConn;
    }

    public CSocket enqueue(CSocket element) {
        offer(element);
        return element;
    }

    public CSocket dequeue() {
        CSocket csocket = (CSocket) poll();
        return csocket;
    }
    
    public CSocket dequeue(long time) throws InterruptedException {
        CSocket csocket = (CSocket) poll(time,TimeUnit.MILLISECONDS);
        return csocket;
    }

    public void register(CSocket socket) {
        _AllSocket.add(socket);
    }

    public synchronized boolean remove(CSocket socket) {
        _AllSocket.remove(socket);
        return super.remove(socket);
    }

    public int getTotal() {
        return _AllSocket.size();
    }

    public List<CSocket> getAllSocket() {
        return _AllSocket;
    }
    private long lastCheckTime = System.currentTimeMillis();
    private int freeCount = -1;
    private int shrinkCount = 0;

    public boolean shrink() {
        synchronized (shrinkLockHelper) {
            if (shrinkCount > 0) {
                shrinkCount--;
                return true;
            }
            if ((System.currentTimeMillis() - lastCheckTime) > _duration) {
                lastCheckTime = System.currentTimeMillis();
                boolean b = (freeCount > 0) && (getTotal() > _minConn);
                if (b) {
                    shrinkCount = Math.min((getTotal() - _minConn), freeCount);
                    if (shrinkCount < 0) {
                        shrinkCount = 0;
                    }
                }
                return false;
            }
            int currFreeCount = this.size();
            if (currFreeCount < freeCount || freeCount < 0) {
                freeCount = currFreeCount;
            }
            return false;
        }
    }
}
