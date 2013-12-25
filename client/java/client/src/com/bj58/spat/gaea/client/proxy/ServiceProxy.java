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
package com.bj58.spat.gaea.client.proxy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.bj58.spat.gaea.client.GaeaConst;
import com.bj58.spat.gaea.client.configuration.ServiceConfig;
import com.bj58.spat.gaea.client.loadbalance.Dispatcher;
import com.bj58.spat.gaea.client.loadbalance.Server;
import com.bj58.spat.gaea.client.proxy.builder.Parameter;
import com.bj58.spat.gaea.client.utility.logger.ILog;
import com.bj58.spat.gaea.client.utility.logger.LogFactory;
import com.bj58.spat.gaea.protocol.exception.RebootException;
import com.bj58.spat.gaea.protocol.exception.ThrowErrorHelper;
import com.bj58.spat.gaea.protocol.exception.TimeoutException;
import com.bj58.spat.gaea.protocol.sdp.ExceptionProtocol;
import com.bj58.spat.gaea.protocol.sdp.HandclaspProtocol;
import com.bj58.spat.gaea.protocol.sdp.RequestProtocol;
import com.bj58.spat.gaea.protocol.sdp.ResponseProtocol;
import com.bj58.spat.gaea.protocol.sfp.enumeration.CompressType;
import com.bj58.spat.gaea.protocol.sfp.enumeration.PlatformType;
import com.bj58.spat.gaea.protocol.sfp.enumeration.SDPType;
import com.bj58.spat.gaea.protocol.sfp.v1.Protocol;
import com.bj58.spat.gaea.protocol.utility.KeyValuePair;

/**
 * ServiceProxy
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ServiceProxy {

    private Dispatcher dispatcher;
    private ServiceConfig config;
    private int sessionId = 1;
    private int requestTime = 0;//超时重连次数
    private int ioreconnect = 0;//IO服务切换次数
    private int count = 0; //根据requestTime和ioreconnect确定
    private static final ILog logger = LogFactory.getLogger(ServiceProxy.class);
    private static final Object locker = new Object();
    private static final HashMap<String, ServiceProxy> Proxys = new HashMap<String, ServiceProxy>();

    private ServiceProxy(String serviceName) throws Exception {
        config = ServiceConfig.GetConfig(serviceName);
        dispatcher = new Dispatcher(config);
        
        requestTime = config.getSocketPool().getReconnectTime();
    	int serverCount = 1;
    	if(dispatcher.GetAllServer() != null && dispatcher.GetAllServer().size() > 0){
    		serverCount = dispatcher.GetAllServer().size();
    	}
    	
    	ioreconnect = serverCount - 1;
    	
    //	count = max {ioreconnect, requestTime}
    	count = requestTime;
    	
    	if(ioreconnect > requestTime){
    		count = ioreconnect;
    	}
    }
    
    private void destroy() {
    	List<Server> serverList = dispatcher.GetAllServer();
		if(serverList != null) {
			for(Server server : serverList) {
				try {
					server.getScoketpool().destroy();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
    }

    public static ServiceProxy getProxy(String serviceName) throws Exception {
        ServiceProxy p = Proxys.get(serviceName.toLowerCase());
        if (p == null) {
	        synchronized(locker) {
	        	p = Proxys.get(serviceName.toLowerCase());
	        	if (p == null) {
	        		p = new ServiceProxy(serviceName);
	        		Proxys.put(serviceName.toLowerCase(), p);
	        	}
	        }
        }
        return p;
    }

    public InvokeResult invoke(Parameter returnType, String typeName, String methodName, Parameter[] paras) throws Exception, Throwable {
        long watcher = System.currentTimeMillis();
        List<KeyValuePair> listPara = new ArrayList<KeyValuePair>();
        for (Parameter p : paras) {
            listPara.add(new KeyValuePair(p.getSimpleName(), p.getValue()));
        }
        RequestProtocol requestProtocol = new RequestProtocol(typeName, methodName, listPara);
        Protocol sendP = new Protocol(createSessionId(), 
        		(byte) config.getServiceid(),
        		SDPType.Request,
        		CompressType.UnCompress,
        		config.getProtocol().getSerializerType(),
        		PlatformType.Java,
        		requestProtocol);
        
        Protocol receiveP = null;
        Server server = null;
        
        for(int i = 0; i <= count; i++){
        	server = dispatcher.GetServer();
            if (server == null) {
                logger.error("cannot get server");
                throw new Exception("cannot get server");
            }
            try{
            	receiveP = server.request(sendP);
            	break;
            } catch(IOException io){
            	if(count == 0 || i == ioreconnect){
            		throw io;
            	}
            	if(i < count && i < ioreconnect) {
            		logger.error(server.getName()+" server has IOException,system will change normal server!");
            		continue;
            	}
            } catch(RebootException rb){
            	this.createReboot(server);
            	if(count == 0 || i == ioreconnect){
            		throw new IOException("connect fail!");
            	}
            	if(i < count && i < ioreconnect) {
            		logger.error(server.getName()+" server has reboot,system will change normal server!");
            		continue;
            	}
            }catch(TimeoutException te){
            	if(count == 0 || i == requestTime){
            		throw new TimeoutException("Receive data timeout or error!");
            	}
            	if(i < count && i < requestTime) {
            		logger.error(server.getName()+" server has TimeoutException,system will change normal server!");
            		continue;
            	}
            } catch (Throwable ex){
            	logger.error("invoke other Exception", ex);
            	throw ex;
            }
    	}
        
        if(receiveP == null){
        	throw new Exception("userdatatype error!");
        }
        
        if (receiveP.getSDPType() == SDPType.Response) {
            ResponseProtocol rp = (ResponseProtocol)receiveP.getSdpEntity();
            logger.debug("invoke time:" + (System.currentTimeMillis() - watcher) + "ms");
            return new InvokeResult(rp.getResult(), rp.getOutpara());
        } else if(receiveP.getSDPType() == SDPType.Reset){ //服务重启
        	logger.info(server.getName()+" server is reboot,system will change normal server!");
        	this.createReboot(server);
        	return invoke(returnType, typeName, methodName, paras);
        }else if (receiveP.getSDPType() == SDPType.Exception) {
            ExceptionProtocol ep = (ExceptionProtocol)receiveP.getSdpEntity();
            throw ThrowErrorHelper.throwServiceError(ep.getErrorCode(), ep.getErrorMsg());
        } else {
            throw new Exception("userdatatype error!");
        }
    }
    
    /**
     * 设置当前重启服务
     * @param server
     * @throws Throwable 
     * @throws Exception 
     */
    private void createReboot(Server server) throws Exception, Throwable{
    	server.createReboot();
    }
    
    /**
     * 权限协议
     * @param data 
     * @return Protocol
     * @throws Exception 
     */
    public Protocol createProtocol(HandclaspProtocol hp) throws Exception{
    	Protocol sendRightsProtocol = new Protocol(createSessionId(), 
        		(byte) config.getServiceid(),
        		SDPType.Request,
        		CompressType.UnCompress,
        		config.getProtocol().getSerializerType(),
        		PlatformType.Java,
        		hp);
    	return sendRightsProtocol;
    }
    
    /**
     *  get Server info
     * @param name Server name
     * @return if Server exist return Server info else return empty
     */
    public String getServer(String name) {
        Server server = dispatcher.GetServer(name);
        if (server == null) {
            return "";
        }
        return server.toString();
    }
    
    public static void destroyAll() {
    	Collection<ServiceProxy> spList = Proxys.values();
		if(spList != null) {
			for(ServiceProxy sp : spList) {
				sp.destroy();
			}
		}
    }
    

    private int createSessionId() {
        synchronized (this) {
            if (sessionId > GaeaConst.MAX_SESSIONID) {
                sessionId = 1;
            }
            return sessionId++;
        }
    }
    
    /**
     * invoke result
     * <typeparam name="T">result data type</typeparam>
     */
    public class InvokeResult<T> {

        InvokeResult(Object result, Object[] outPara) {
            Result = (T) result;
            OutPara = outPara;
        }
        private T Result;
        private Object[] OutPara;

        public Object[] getOutPara() {
            return OutPara;
        }

        public void setOutPara(Object[] OutPara) {
            this.OutPara = OutPara;
        }

        public T getResult() {
            return Result;
        }

        public void setResult(T Result) {
            this.Result = Result;
        }
    }
}
