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
 * Dispatcher.cpp
   *
 * Created on: 2011-7-8
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#include "Dispatcher.h"
#include "GaeaClientConfig.h"
#include "ServerProfile.h"
#include "Log.h"
#include <list>
#include <time.h>
namespace gaea {

Dispatcher::Dispatcher(GaeaClientConfig *gaeaClientConfig) {
	serverVector = new std::vector<Server*>();
	pthread_mutex_init(&countMutex, NULL);
	pthread_mutex_init(&stateMutex, NULL);
	requestCount = 0;
	ServerProfile *spf = NULL;
	Server *server = NULL;
	std::list<ServerProfile*> *serverList = gaeaClientConfig->getServerList();
	std::_List_iterator<ServerProfile*> ite = serverList->begin();
	while (ite != serverList->end()) {
		spf = *ite;
		if (spf->getWeithRage() > 0) {
			server = new Server(spf, gaeaClientConfig->getSocketPoolProfile());
			serverVector->push_back(server);
		}
		++ite;
	}
	count = serverVector->size();
}
Server* Dispatcher::getServer() {
	pthread_mutex_lock(&countMutex);
	int start = requestCount % count;
	if (requestCount > 10000) {
		requestCount = 0;
	} else {
		++requestCount;
	}
	pthread_mutex_unlock(&countMutex);
	Server *server;
	Server *result = NULL;
	for (size_t i = start; i < start + count; ++i) {
		int index = i % count;
		server = serverVector->operator [](index);
		if ((server->getState() == SERVER_DEAD || server->getState() == SERVER_REBOOT) && time(NULL) - server->getDeadTime() > server->getDeadTimeout()) {
			pthread_mutex_lock(&stateMutex);
			if ((server->getState() == SERVER_DEAD || server->getState() == SERVER_REBOOT) && time(NULL) - server->getDeadTime() > server->getDeadTimeout()) {
				server->setDeadTime(0);
				server->setState(SERVER_TESTING);
				result = server;
				pthread_mutex_unlock(&stateMutex);
				break;
			}
			pthread_mutex_unlock(&stateMutex);
		} else if (server->getState() == SERVER_NORMAL) {
			result = server;
			break;
		}
	}
	if (!result) {
		for (size_t i = 0; i < count; i++) {
			server = serverVector->operator [](i);
			gaeaLog(GAEA_WARNING, "Not get server,This server is %d DeadTime:%ld DeadTimeout:%d\n", server->getState(), server->getDeadTime(), server->getDeadTimeout());
		}
		result = serverVector->operator [](start);
	}
	return result;
}
Server* Dispatcher::getServer(char *hostName) {
	Server *rets = NULL;
	Server *ts;
	for (size_t i = 0; i < serverVector->size(); ++i) {
		ts = serverVector->operator [](i);
		if (strcmp(hostName, ts->getName())) {
			rets = ts;
			break;
		}
	}
	return rets;
}
std::vector<Server*>* Dispatcher::getAllServer() {
	return serverVector;
}
Dispatcher::~Dispatcher() {
	for (size_t i = 0; i < serverVector->size(); ++i) {
		delete serverVector->operator [](i);
	}
	delete serverVector;
}

}
