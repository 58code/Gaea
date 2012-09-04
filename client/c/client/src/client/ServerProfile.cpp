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
 * ServerProfile.cpp
   *
 * Created on: 2011-7-8
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#include "ServerProfile.h"
#include "../tinyxpath/tinyxml.h"
namespace gaea {
ServerProfile::ServerProfile(TiXmlElement *element) {
	TiXmlAttribute*serverAttrib = element->FirstAttribute();
	while (serverAttrib) {
		if (serverAttrib->NameTStr() == "name") {
			setName((char*) serverAttrib->Value());
		} else if (serverAttrib->NameTStr() == "host") {
			setHostName((char*) serverAttrib->Value());
		} else if (serverAttrib->NameTStr() == "port") {
			this->port = atoi(serverAttrib->Value());
		} else if (serverAttrib->NameTStr() == "maxCurrentUser") {
			this->weithRage = atoi(serverAttrib->Value());
		}
		serverAttrib = serverAttrib->Next();
	}
}

ServerProfile::~ServerProfile() {
	// TODO Auto-generated destructor stub
}

char *ServerProfile::getHostName() const {
	return hostName;
}

int ServerProfile::getPort() const {
	return port;
}

void ServerProfile::setHostName(char *hostName) {
	int len = strlen(hostName) + 1;
	this->hostName = (char*) malloc(len);
	memcpy(this->hostName, hostName, len);
	this->hostName[len - 1] = '\0';
}

void ServerProfile::setPort(int port) {
	this->port = port;
}

int ServerProfile::getWeithRage() const {
	return weithRage;
}

void ServerProfile::setWeithRage(int weithRage) {
	this->weithRage = weithRage;
}

int ServerProfile::getDeadTimeout() const {
	return deadTimeout;
}

void ServerProfile::setDeadTimeout(int deadTimeout) {
	this->deadTimeout = deadTimeout;
}

char *ServerProfile::getName() const {
	return name;
}

void ServerProfile::setName(char *name) {
	int len = strlen(name) + 1;
	this->name = (char*) malloc(len);
	memcpy(this->name, name, len);
	this->name[len - 1] = '\0';
}

}
