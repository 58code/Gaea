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
#include "SocketPoolProfile.h"
#include "../tinyxpath/tinyxml.h"
#include "Log.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stddef.h>

namespace gaea {

SocketPoolProfile::SocketPoolProfile(TiXmlElement *clientElement) {
	TiXmlAttribute* attrib = clientElement->FirstAttribute(); //获得student的name属性
	while (attrib) {
		if (attrib->NameTStr() == "bufferSize") {
			this->bufferSize = atoi(attrib->Value());
		} else if (attrib->NameTStr() == "minPoolSize") {
			this->minPoolSize = atoi(attrib->Value());
		} else if (attrib->NameTStr() == "nagle") {
			this->nagle = strcmp(attrib->Value(), "true") == 0 || strcmp(attrib->Value(), "on") == 0;
		} else if (attrib->NameTStr() == "autoShrink") {
			this->shrinkInterval = atoi(attrib->Value());
		} else if (attrib->NameTStr() == "sendTimeout") {
			this->sendTimeout = atoi(attrib->Value());
		} else if (attrib->NameTStr() == "receiveTimeout") {
			this->receiveTimeout = atoi(attrib->Value());
		} else if (attrib->NameTStr() == "waitTimeout") {
			this->waitTimeout = atoi(attrib->Value());
		} else if (attrib->NameTStr() == "connectionTimeout") {
			this->connectionTimeout = atoi(attrib->Value());
		} else if (attrib->NameTStr() == "maxPakageSize") {
			this->maxPakageSize = atoi(attrib->Value());
		} else if (attrib->NameTStr() == "maxPoolSize") {
			this->maxPoolSize = atoi(attrib->Value());
		}
		attrib = attrib->Next();
	}
}

SocketPoolProfile::~SocketPoolProfile() {
}

int SocketPoolProfile::getBufferSize() const {
	return bufferSize;
}

int SocketPoolProfile::getConnectionTimeout() const {
	return connectionTimeout;
}

int SocketPoolProfile::getMaxPakageSize() const {
	return maxPakageSize;
}

int SocketPoolProfile::getMaxPoolSize() const {
	return maxPoolSize;
}

int SocketPoolProfile::getMinPoolSize() const {
	return minPoolSize;
}

bool SocketPoolProfile::getNagle() const {
	return nagle;
}

bool SocketPoolProfile::getProtected() const {
	return _protected;
}

int SocketPoolProfile::getReceiveTimeout() const {
	return receiveTimeout;
}

int SocketPoolProfile::getSendTimeout() const {
	return sendTimeout;
}

int SocketPoolProfile::getShrinkInterval() const {
	return shrinkInterval;
}

int SocketPoolProfile::getWaitTimeout() const {
	return waitTimeout;
}

}

