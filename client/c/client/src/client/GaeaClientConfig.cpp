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
 * GaeaClientConfig.cpp
   *
 * Created on: 2011-7-8
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#include "GaeaClientConfig.h"
#include "SocketPoolProfile.h"
#include "DataReceiver.h"
#include "../tinyxpath/tinyxml.h"
#include "../serialize/structHelper.h"
#include "GaeaConst.h"
#include "Log.h"
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <string>
#include <iostream>
#include <pthread.h>
#include <stdexcept>
#include <errno.h>
namespace gaea {
pthread_mutex_t GaeaClientConfig::mutex = PTHREAD_MUTEX_INITIALIZER;
std::map<std::string, GaeaClientConfig*>* GaeaClientConfig::configsMap=new std::map<std::string, GaeaClientConfig*>;
GaeaClientConfig::GaeaClientConfig() {

}
int GaeaClientConfig::init(std::string &confFile, std::string &structFile, std::string &logFile) {

	TiXmlDocument* myDocument = new TiXmlDocument();
	if (!myDocument->LoadFile(confFile.c_str())) {
		std::string errorMsg = "load config failed:";
		return -1;
	}
	TiXmlElement* rootElement = myDocument->RootElement(); //Class
	TiXmlElement* servicesElement = rootElement->FirstChildElement(); //Students
	while (servicesElement) {
		if (servicesElement->ValueTStr() == "Service") {
			GaeaClientConfig *config = new GaeaClientConfig();
			TiXmlAttribute* servicesAttrib = servicesElement->FirstAttribute(); //获得student的name属性
			while (servicesAttrib) {
				if (servicesAttrib->NameTStr() == "name") {
					config->setServiceName((char*) servicesAttrib->Value());
				} else if (servicesAttrib->NameTStr() == "id") {
					config->setServiceId(atoi(servicesAttrib->Value()));
				} else if (servicesAttrib->NameTStr() == "maxThreadCount") {
					config->setMaxThreadCount(atoi(servicesAttrib->Value()));
				}
				servicesAttrib = servicesAttrib->Next();
			}
			TiXmlElement* clientElement = servicesElement->FirstChildElement(); //获得student的phone元素
			while (clientElement) {
				if (clientElement->ValueTStr() == "Commmunication") {
					TiXmlElement* commmunicationElement = clientElement->FirstChildElement(); //获得student的phone元素
					while (commmunicationElement) {
						if (commmunicationElement->ValueTStr() == "SocketPool") {
							SocketPoolProfile *socketPool = new SocketPoolProfile(commmunicationElement);
							config->setSocketPoolProfile(socketPool);
						}
						commmunicationElement = commmunicationElement->NextSiblingElement(); //获得student的phone元素
					}
				} else if (clientElement->ValueTStr() == "Loadbalance") {
					TiXmlElement* serversElement = clientElement->FirstChildElement();
					TiXmlAttribute*serversAttrib = serversElement->FirstAttribute();
					int deadTimeout = 0;
					while (serversAttrib) {
						if (serversAttrib->NameTStr() == "deadTimeout") {
							deadTimeout = atoi(serversAttrib->Value());
						}
						serversAttrib = serversAttrib->Next();
					}
					TiXmlElement* serverElement = serversElement->FirstChildElement();
					config->setServerList(new std::list<ServerProfile*>);
					while (serverElement) {
						ServerProfile *sp = new ServerProfile(serverElement);
						sp->setDeadTimeout(deadTimeout);
						config->getServerList()->push_back(sp);
						serverElement = serverElement->NextSiblingElement();

					}
				}
				clientElement = clientElement->NextSiblingElement();
			}
			std::string *s = new std::string(config->getServiceName());
			configsMap->insert(std::pair<std::string, GaeaClientConfig*>(*s, config));
		}
		servicesElement = servicesElement->NextSiblingElement();
	}
	myDocument->Clear();
	delete myDocument;
	DataReceiver::GetInstance();
	if (registerStruct(structFile.c_str()) == -1) {
		return -1;
	}
	char *slf = (char*) malloc(logFile.length() + 1);
	strcpy(slf, logFile.c_str());
	setLogFilePath(slf);
	return 0;
}
GaeaClientConfig* GaeaClientConfig::getConfig(std::string &serviceName) {
	std::map<std::string, GaeaClientConfig*>::iterator it = configsMap->find(serviceName);
	if (it == configsMap->end()) {
		pthread_mutex_lock(&mutex);
		it = configsMap->find(serviceName);
		if (it == configsMap->end()) {
			std::string cf = CONFIG_PATH;
			std::string gaea = STURCT_CONFIG_PATH;
			std::string lf = GAEA_LOG_PATH;
			if (init(cf, gaea, lf) == -1) {
				pthread_mutex_unlock(&mutex);
				throw std::runtime_error("gaea init error");
			}
			it = configsMap->find(serviceName);
		}
		pthread_mutex_unlock(&mutex);
	}

	return it->second;
}

int GaeaClientConfig::getMaxThreadCount() const {
	return maxThreadCount;
}

int GaeaClientConfig::getServiceId() const {
	return serviceId;
}

char *GaeaClientConfig::getServiceName() const {
	return serviceName;
}

SocketPoolProfile *GaeaClientConfig::getSocketPoolProfile() const {
	return socketPoolProfile;
}

void GaeaClientConfig::setMaxThreadCount(int maxThreadCount) {
	this->maxThreadCount = maxThreadCount;
}

void GaeaClientConfig::setServiceId(int serviceId) {
	this->serviceId = serviceId;
}

void GaeaClientConfig::setServiceName(char *serviceName) {
	int len = strlen(serviceName) + 1;
	this->serviceName = (char*) malloc(len);
	memcpy(this->serviceName, serviceName, len);
	this->serviceName[len - 1] = '\0';
}

void GaeaClientConfig::setSocketPoolProfile(SocketPoolProfile *socketPoolProfile) {
	this->socketPoolProfile = socketPoolProfile;
}

std::list<ServerProfile*>* GaeaClientConfig::getServerList() const {
	return serverList;
}

void GaeaClientConfig::setServerList(std::list<ServerProfile*>* serverList) {
	this->serverList = serverList;
}

}
