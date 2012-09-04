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

import java.net.Socket;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.channels.NotYetConnectedException;

import com.bj58.spat.gaea.client.configuration.commmunication.SocketPoolProfile;
import com.bj58.spat.gaea.client.utility.AutoResetEvent;
import com.bj58.spat.gaea.client.utility.logger.ILog;
import com.bj58.spat.gaea.client.utility.logger.LogFactory;
import com.bj58.spat.gaea.protocol.exception.DataOverFlowException;
import com.bj58.spat.gaea.protocol.exception.ProtocolException;
import com.bj58.spat.gaea.protocol.exception.TimeoutException;
import com.bj58.spat.gaea.protocol.sfp.v1.SFPStruct;
import com.bj58.spat.gaea.protocol.utility.ByteConverter;
import com.bj58.spat.gaea.protocol.utility.ProtocolConst;

/**
 * CSocket
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class CSocket {

    private static final ILog logger = LogFactory.getLogger(CSocket.class);
   
    private byte[] DESKey;//DES密钥
    private boolean rights;//是否启用认证
    private Socket socket;
    private ScoketPool pool;
    private SocketChannel channel;
    private ByteBuffer receiveBuffer, sendBuffer;
    private SocketPoolProfile socketConfig;
    private boolean _inPool = false;
    private boolean _connecting = false;
    private DataReceiver dataReceiver = null;
    private boolean waitDestroy = false;
    private final Object sendLockHelper = new Object();
    private final Object receiveLockHelper = new Object();
    private CByteArrayOutputStream receiveData = new CByteArrayOutputStream();
    private ConcurrentHashMap<Integer, WindowData> WaitWindows = new ConcurrentHashMap<Integer, WindowData>();

    protected CSocket(InetSocketAddress endpoint, ScoketPool _pool, SocketPoolProfile config) throws IOException {
        this.socketConfig = config;
        this.pool = _pool;
        channel = SocketChannel.open(endpoint);
        channel.configureBlocking(false);
        socket = channel.socket();
        channel.socket().setSendBufferSize(config.getSendBufferSize());
        channel.socket().setReceiveBufferSize(config.getRecvBufferSize());
        receiveBuffer = ByteBuffer.allocate(config.getBufferSize());
        sendBuffer = ByteBuffer.allocate(config.getMaxPakageSize());
        _connecting = true;
        dataReceiver = DataReceiver.instance();
        dataReceiver.RegSocketChannel(this);
        
        logger.info("MaxPakageSize:" + config.getMaxPakageSize());
		logger.info("SendBufferSize:" + config.getSendBufferSize());
		logger.info("RecvBufferSize:" + config.getRecvBufferSize());
        logger.info("create a new connection :" + this.toString());
    }
    
    public int send(byte[] data) throws IOException, Throwable {
        try {
            synchronized (sendLockHelper) {
                int pakageSize = data.length + ProtocolConst.P_END_TAG.length;
                if (sendBuffer.capacity() < pakageSize) {
                    throw new DataOverFlowException("数据包(size:" + pakageSize + ")超过最大限制,请修改或增加配置文件中的<SocketPool maxPakageSize=\"" + socketConfig.getMaxPakageSize() + "\"/>节点属性！");
                }
          
	            int count = 0;
	    		sendBuffer.clear();
	    		sendBuffer.put(data);
	    		sendBuffer.put(ProtocolConst.P_END_TAG);
	    		sendBuffer.flip();
	    		
	    		int retryCount = 0;
	    		while(sendBuffer.hasRemaining()) {
	    			count += channel.write(sendBuffer);
	    			
	    			if(retryCount++ > 10) {
	    				throw new Exception("retry write count(" + retryCount + ") above 10");
	    			}
	    		}
	    		return count;
            }
        } catch (IOException ex) {
            _connecting = false;
            throw ex;
        } catch (NotYetConnectedException ex) {
            _connecting = false;
            throw ex;
        }
    }

    public byte[] receive(int sessionId, int queueLen) throws IOException, TimeoutException, Exception {
        WindowData wd = WaitWindows.get(sessionId);
        if (wd == null) {
            throw new Exception("Need invoke 'registerRec' method before invoke 'receive' method!");
        }
        AutoResetEvent event = wd.getEvent();
        int timeout = getReadTimeout(socketConfig.getReceiveTimeout(), queueLen);
        if (!event.waitOne(timeout)) {
            throw new TimeoutException("Receive data timeout or error!timeout:" + timeout + "ms,queue length:" + queueLen);
        }
        byte[] data = wd.getData();
        int offset = SFPStruct.Version;
        int len = ByteConverter.bytesToIntLittleEndian(data, offset);
        if (len != data.length) {
            throw new ProtocolException("The data length inconsistent!datalen:" + data.length + ",check len:" + len);
        }
        return data;
    }

    public void registerRec(int sessionId) {
        AutoResetEvent event = new AutoResetEvent();
        WindowData wd = new WindowData(event);
        WaitWindows.put(sessionId, wd);
    }

    public void unregisterRec(int sessionId) {
        WaitWindows.remove(sessionId);
    }

    public void close() {
        pool.release(this);
    }

    public void dispose() throws Exception {
        dispose(false);
    }

    public void dispose(boolean flag) {
        if (flag) {
            logger.warning("destory a connection");
            try {
                dataReceiver.UnRegSocketChannel(this);
            } finally {
                pool.destroy(this);
            }
        } else {
            close();
        }
    }

    protected void disconnect() throws IOException {
        if (channel != null) {
            channel.close();
        }
        _connecting = false;
    }

    private int getReadTimeout(int baseReadTimeout, int queueLen) {
        if (!socketConfig.isProtected()) {
            return baseReadTimeout;
        }
        if (queueLen <= 0) {
            queueLen = 1;
        }
        int result = baseReadTimeout;
        int flag = (queueLen - 100) / 10;
        if (flag >= 0) {
            if (flag == 0) {
                flag = 1;
            }
            result = baseReadTimeout / (2 * flag);
        } else if (flag < -7) {
            result = baseReadTimeout - ((flag) * (baseReadTimeout / 10));
        }

        if (result > 2 * baseReadTimeout) {
            result = baseReadTimeout;
        } else if (result < 5) {
            result = 5; //min timeout is 5ms
        }
        if (queueLen > 50) {
            logger.warn("-----IsProtected:true,queueLen:" + queueLen + ",timeout:" + result + ",baseReadTimeout:" + baseReadTimeout);
        }
        return result;
    }
    
    private boolean handling = false;

    protected void frameHandle() throws IOException, InterruptedException {
        if (handling) {
            return;
        }
        synchronized (receiveLockHelper) {
            handling = true;
            try {
                if (waitDestroy && isIdle()) {
                    logger.info("Shrinking the connection:" + this.toString());
                    dispose(true);
                    return;
                }
                int index = 0;
                receiveBuffer.clear();
                try {
                    channel.read(receiveBuffer);
                } catch (IOException ex) {
                    _connecting = false;
                    throw ex;
                } catch (NotYetConnectedException e) {
                    _connecting = false;
                    throw e;
                }
                receiveBuffer.flip();
                if (receiveBuffer.remaining() == 0) {
                    return;
                }
                
                while (receiveBuffer.remaining() > 0) {
                    byte b = receiveBuffer.get();
                    receiveData.write(b);
                    if (b == ProtocolConst.P_END_TAG[index]) {
                        index++;
                        if (index == ProtocolConst.P_END_TAG.length) {
                            byte[] pak = receiveData.toByteArray(0, receiveData.size() - ProtocolConst.P_END_TAG.length);
                            int pSessionId = ByteConverter.bytesToIntLittleEndian(pak, SFPStruct.Version + SFPStruct.TotalLen);
                            WindowData wd = WaitWindows.get(pSessionId);
                            if (wd != null) {
                                wd.setData(pak);
                                wd.getEvent().set();
                            }
                            index = 0;
                            receiveData.reset();
                            continue;
                        }
                    } else if (index != 0) {
                    	if(b == ProtocolConst.P_END_TAG[0]) {
                    		index = 1;
                    	} else {
                    		index = 0;
                    	}
                    }
                }
            } finally {
                handling = false;
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            if (_connecting || (channel != null && channel.isOpen())) {
                dispose(true);
            }
        } catch (Throwable t) {
            logger.error("Pool Release Error!:", t);
        } finally {
            super.finalize();
        }
    }

    public boolean connecting() {
        return _connecting;
    }

    protected boolean inPool() {
        return _inPool;
    }

    protected void setInPool(boolean inPool) {
        _inPool = inPool;
    }

    protected SocketChannel getChannle() {
        return channel;
    }

    /*
     * 该链接是否是空闲状态
     */
    protected boolean isIdle() {
        return !(WaitWindows.size() > 0);
    }

    protected void waitDestroy() {
        this.waitDestroy = true;
    }
    
    public boolean isRights() {
		return rights;
	}

	public void setRights(boolean rights) {
		this.rights = rights;
	}
	
	public byte[] getDESKey() {
		return DESKey;
	}

	public void setDESKey(byte[] dESKey) {
		DESKey = dESKey;
	}
    
    @Override
    public String toString() {
        try {
            return (socket == null) ? "" : socket.toString();
        } catch (Throwable ex) {
            return "Socket[error:" + ex.getMessage() + "]";
        }
    }
}
