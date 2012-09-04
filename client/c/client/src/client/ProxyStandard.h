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
 * ProxyStandard.h
   *
 * Created on: 2011-7-12
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#ifndef PROXYSTANDARD_H_
#define PROXYSTANDARD_H_
typedef void (*call_back_fun_)(int errorCode, void *retVal, void *context);
#include "Parameter.h"
#include "../threadpool/threadpool.h"
#include <string>
namespace gaea {

class ProxyStandard {
public:
	ProxyStandard(std::string serviceName, char *lookup);
	void *invoke(char *methodName, Parameter **para, int paraLen);
	int asyncInvoke(char *methodName, Parameter **para, int paraLen, call_back_fun_ callBackFun, void *context);
	virtual ~ProxyStandard();
private:
	std::string serviceName;
	char *lookup;
	static ThreadPool *tp;
	static pthread_mutex_t mutex;
	static void asyncInvoke(void *data);
};
class ProxStandardSession {
public:
	ProxStandardSession(char *methodName, Parameter **para, int paraLen, call_back_fun_ callBackFun, void *context,
			ProxyStandard *ps) {
		this->methodName = methodName;
		this->para = para;
		this->paraLen = paraLen;
		this->ps = ps;
		this->callBackFun = callBackFun;
		this->context = context;
	}
	call_back_fun_ getCallBackFun() const;
	void *getContext() const;
	char *getMethodName() const;
	Parameter **getPara() const;
	int getParaLen() const;
	ProxyStandard *getPs() const;

private:
	call_back_fun_ callBackFun;
	void *context;
	char *methodName;
	Parameter **para;
	int paraLen;
	ProxyStandard *ps;
};
} /* namespace gaea */
#endif /* PROXYSTANDARD_H_ */
