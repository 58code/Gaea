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
 * ServiceProxy.h
   *
 * Created on: 2011-7-12
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#ifndef SERVICEPROXY_H_
#define SERVICEPROXY_H_
#include "Dispatcher.h"
#include "GaeaClientConfig.h"
#include "Parameter.h"
#include "../protocol/Protocol.h"
#include "../serialize/serializer.h"
#include <string>
#include <pthread.h>
#include <map>
namespace gaea {
class InvokeResult {
public:
	InvokeResult(void *result, array *outPara);
	virtual ~InvokeResult();
	array* getOutPara() const;
	void *getResult() const;
private:
	void *result;
	array *outPara;
	GaeaClientConfig *config;
};

class ServiceProxy {
public:
	virtual ~ServiceProxy();
	static ServiceProxy* getProxy(std::string &serviceName);
	InvokeResult* invoke(char *lookup, char *methodName, Parameter **paras, int parasLen);
protected:
	ServiceProxy(std::string &serviceName);
	int createSessionId();
	Dispatcher *dispatcher;
	GaeaClientConfig *config;
	static volatile int sessionId;
	static std::map<std::string, ServiceProxy*> proxys;
	static pthread_mutex_t mutex;
};
}
#endif /* SERVICEPROXY_H_ */
