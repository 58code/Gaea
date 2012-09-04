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
 * GaeaClientConfig.h
   *
 * Created on: 2011-7-8
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#ifndef GAEACLIENTCONFIG_H_
#define GAEACLIENTCONFIG_H_
#include "SocketPoolProfile.h"
#include "ServerProfile.h"
#include <pthread.h>
#include <map>
#include <string>
#include <list>
namespace gaea {

class GaeaClientConfig {
public:
	static GaeaClientConfig* getConfig(std::string &serviceName);
	static int init(std::string &confFile, std::string &structFile, std::string &logFile);
	GaeaClientConfig();
	int getMaxThreadCount() const;
	int getServiceId() const;
	char *getServiceName() const;
	SocketPoolProfile *getSocketPoolProfile() const;
	void setMaxThreadCount(int maxThreadCount);
	void setServiceId(int serviceId);
	void setServiceName(char *serviceName);
	void setSocketPoolProfile(SocketPoolProfile *socketPoolProfile);
	std::list<ServerProfile*>* getServerList() const;
	void setServerList(std::list<ServerProfile*>* serverList);
private:
	static std::map<std::string, GaeaClientConfig*> *configsMap;
	static pthread_mutex_t mutex;
	SocketPoolProfile *socketPoolProfile;
	char *serviceName;
	int serviceId;
	int maxThreadCount;
	std::list<ServerProfile*> *serverList;
};
}
#endif /* GAEACLIENTCONFIG_H_ */
