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
 * Server.cpp
 *
 * Created on: 2011-7-5
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#include "Server.h"
#include "../protocol/Protocol.h"
#include "SocketPool.h"
#include "CSocket.h"
#include "ServerProfile.h"
#include "Log.h"
#include <stdexcept>
#include <bits/signum.h>
#include <signal.h>
namespace gaea {
Server::Server(ServerProfile *serverProfile, SocketPoolProfile *spp) {
	socketPool = new SocketPool(serverProfile->getHostName(), serverProfile->getPort(), spp);
	this->serverProfile = serverProfile;
	this->name = serverProfile->getName();
	this->spp = spp;
	this->state = SERVER_NORMAL;
	deadTime = 0;
	pthread_mutex_init(&mutex, NULL);
	testing = false;
}

Server::~Server() {
	delete socketPool;
}
Protocol* Server::request(Protocol &p) {
	int dataLen = 0;
	int fd = socketPool->getSocket();
	if (fd < 0) {
		markAsDead();
		return NULL;
	} else if (fd == 0) {
		return NULL;
	}
	char *data = p.getBytes(dataLen);
	int sessionId = p.getSessionID();
	int err = CSocket::sendData(fd, data, dataLen, sessionId);
	if (err < 0) {
		socketPool->closeSocket(fd);
		delete[] data;
		if (!test()) {
			markAsDead();
		}
		throw std::runtime_error("socket send data error\n");
	}
	socketPool->releaseSocket(fd);
	delete[] data;
	data = CSocket::receive(dataLen, sessionId, spp->getReceiveTimeout());
	Protocol *retP = Protocol::fromBytes(data, dataLen);
	if (retP == NULL) {
		errno = -2;
		throw std::runtime_error("Protocol is null\n");
	}
	if (this->state == SERVER_TESTING) {
		relive();
	}
	free(data);
	return retP;
}

char *Server::getName() const {
	return name;
}

void Server::setName(char *name) {
	this->name = name;
}

ServerState Server::getState() const {
	return state;
}

void Server::setState(ServerState state) {
	this->state = state;
}

time_t Server::getDeadTime() const {
	return deadTime;
}

void Server::setDeadTime(time_t deadTime) {
	this->deadTime = deadTime;
}
int Server::getDeadTimeout() {
	return serverProfile->getDeadTimeout();
}

void Server::markAsDead() {
	gaeaLog(GAEA_WARNING, "markAsDead server:%s\n", this->name);
	if (this->state == SERVER_DEAD) {
		return;
	}
	pthread_mutex_lock(&mutex);
	if (this->state == SERVER_DEAD) {
		pthread_mutex_unlock(&mutex);
		return;
	}
	this->setState(SERVER_DEAD);
	this->deadTime = time(NULL);
	socketPool->closeAllSocket();
	pthread_mutex_unlock(&mutex);
}

void Server::markAsReboot() {
	if (this->state == SERVER_REBOOT) {
		return;
	}
	pthread_mutex_lock(&mutex);
	if (this->state == SERVER_REBOOT) {
		pthread_mutex_unlock(&mutex);
		return;
	}
	this->setState(SERVER_REBOOT);
	this->deadTime = time(NULL) + 5;
	pthread_mutex_unlock(&mutex);
}
/**
 * 设置当前服务为正常状态
 */
void Server::markAsNormal() {
	this->relive();
}

/**
 * relive Server if this Server is died
 */
void Server::relive() {
	if (this->state == SERVER_NORMAL) {
		return;
	}
	pthread_mutex_lock(&mutex);
	if (this->state == SERVER_NORMAL) {
		pthread_mutex_unlock(&mutex);
		return;
	}
	gaeaLog(GAEA_WARNING, "this server is relive!host:%s\n", name);
	this->state = SERVER_NORMAL;
	pthread_mutex_unlock(&mutex);
}

bool Server::test() {
	if (testing) {
		return true;
	}
	testing = true;
	bool result = false;
	int fd = CSocket::anetTcpConnect(serverProfile->getHostName(), serverProfile->getPort(), 100);
	if (fd > 0) {
		result = true;
	} else {
		result = false;
	}
	testing = false;
	return result;
}

}
