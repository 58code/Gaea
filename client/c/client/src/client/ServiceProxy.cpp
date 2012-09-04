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
/*
 * ServiceProxy.cpp
   *
 * Created on: 2011-7-12
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#include "ServiceProxy.h"
#include "Dispatcher.h"
#include "GaeaClientConfig.h"
#include "Parameter.h"
#include "../protocol/SdpStruct.h"
#include "../serialize/serializeList.h"
#include "../serialize/serializer.h"
#include "../serialize/derializer.h"
#include "../serialize/strHelper.h"
#include "GaeaConst.h"
#include "../protocol/Protocol.h"
#include "Log.h"
#include <stdexcept>
#include <string>
#include <pthread.h>
#include <errno.h>
#include <signal.h>
namespace gaea {
pthread_mutex_t ServiceProxy::mutex = PTHREAD_MUTEX_INITIALIZER;
std::map<std::string, ServiceProxy*> ServiceProxy::proxys;
volatile int ServiceProxy::sessionId = 1;
ServiceProxy::ServiceProxy(std::string &serviceName) {
	config = GaeaClientConfig::getConfig(serviceName);
	dispatcher = new Dispatcher(config);
}
ServiceProxy* ServiceProxy::getProxy(std::string &serviceName) {
	std::map<std::string, ServiceProxy*>::iterator it = proxys.find(serviceName);
	ServiceProxy *proxy = NULL;
	if (it == proxys.end()) {
		pthread_mutex_lock(&mutex);
		it = proxys.find(serviceName);
		if (it == proxys.end()) {
			proxy = new ServiceProxy(serviceName);
			proxys.insert(std::pair<std::string, ServiceProxy*>(serviceName, proxy));
		} else {
			proxy = it->second;
		}
		pthread_mutex_unlock(&mutex);
	} else {
		proxy = it->second;
	}
	return proxy;
}
ServiceProxy::~ServiceProxy() {
	delete dispatcher;
}
InvokeResult* ServiceProxy::invoke(char *lookup, char *methodName, Parameter **paras, int parasLen) {
	serialize_list *paraList;
	paraList = NULL;
	InvokeResult *is = NULL;
	Protocol *receiveP;
	KeyValuePair kvp[parasLen];
	std::string s = "RpParameter";
	for (int i = parasLen - 1; i >= 0; --i) {
		kvp[i].key = paras[i]->getSimpleName();
		kvp[i].value = paras[i]->getValue();
		kvp[i].valueTypeId = paras[i]->getTypeId();
		paraList = list_cons(&(kvp[i]), GetTypeId((char*) s.c_str()), paraList);
	}
	RequestProtocol requestProtocol;
	requestProtocol.lookup = lookup;
	requestProtocol.methodName = methodName;
	requestProtocol.paraList = paraList;
	std::string sdpEntityType = "RequestProtocol";
	int sid = createSessionId();
	Protocol sendP(sid, config->getServiceId(), Request, UnCompress, GAEABinary, Java, &requestProtocol, sdpEntityType.c_str());
	for (int i = 0; i < 3; ++i) {
		Server *server = dispatcher->getServer();
		if (server == NULL) {
			errno = -3;
			list_free(paraList);
			throw std::runtime_error("cannot get server");
		}
		try {
			receiveP = server->request(sendP);
		} catch (std::exception &e) {
			gaeaLog(GAEA_WARNING,"server request error:hostName:%s   ,error msg:%s\n",server->getName(), e.what());
			continue;
		}
		if (!receiveP) {
			continue;
		} else if (receiveP->getSdpType() == Response) {
			ResponseProtocol *rp = (ResponseProtocol*) receiveP->getSdpEntity();
			is = new InvokeResult(rp->result, rp->outpara);
			free(rp);
			delete receiveP;
			break;
		} else if (receiveP->getSdpType() == Exception) {
			ExceptionProtocol *ep = (ExceptionProtocol*) receiveP->getSdpEntity();
			std::string errorMsg(ep->ErrorMsg);
			DerializeFree((char*) "ExceptionProtocol", ep);
			free(receiveP);
			errno = -2;
			gaeaLog(GAEA_WARNING, errorMsg.c_str());
			break;
		} else if (receiveP->getSdpType() == RebootException) {
			ResetProtocol *rp = (ResetProtocol*) receiveP->getSdpEntity();
			DerializeFree((char*) "ResetProtocol", rp);
			free(receiveP);
			server->markAsReboot();
			errno = -2;
			printf("%d\n", receiveP->getSessionID());
			gaeaLog(GAEA_WARNING, "%s server has reboot,system will change normal server!\n", server->getName());
		} else {
			errno = -2;
			gaeaLog(GAEA_WARNING, ("userdatatype error!"));
		}
	}
	list_free(paraList);
	if (!is) {
		throw std::runtime_error("operation failed!\n");
	}
	return is;
}
int ServiceProxy::createSessionId() {
	int sid = 0;
	pthread_mutex_lock(&mutex);
	sessionId++;
	if (sessionId > MAX_SESSIONID) {
		sessionId = 1;
	}
	sid = sessionId;
	pthread_mutex_unlock(&mutex);
	return sid;
}
InvokeResult::InvokeResult(void *result, array *outPara) {
	this->result = result;
	this->outPara = outPara;
}
InvokeResult::~InvokeResult() {
}

array* InvokeResult::getOutPara() const {
	return outPara;
}

void *InvokeResult::getResult() const {
	return result;
}
}
