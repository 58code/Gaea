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
 * CSocket.cpp
 *
 * Created on: 2011-7-4
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#include "CSocket.h"
#include "SocketPool.h"
#include "WindowData.h"
#include "DataReceiver.h"
#include "../protocol/ProtocolConst.h"
#include "../protocol/SFPStruct.h"
#include "GaeaConst.h"
#include <string.h>
#include <stdlib.h>
#include <sys/ioctl.h>
#include <pthread.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/select.h>
#include <time.h>
#include <map>
#include <vector>
#include <stdexcept>
namespace gaea {
WindowData **CSocket::windowDataMap = new WindowData*[MAX_SESSIONID];
int CSocket::createSocket(const char *hostName, int port, SocketPool *_socketPool, SocketPoolProfile *config) {
	int fd = anetTcpConnect(hostName, port, config->getConnectionTimeout());
	if (fd <= 0) {
		return 0;
	}
	int i = config->getBufferSize();
	setsockopt(fd, SOL_SOCKET, SO_RCVBUF, &i, sizeof(int));
	i = config->getMaxPakageSize();
	setsockopt(fd, SOL_SOCKET, SO_SNDBUF, &i, sizeof(int));
	i = config->getSendTimeout();
	setsockopt(fd, SOL_SOCKET, SO_SNDTIMEO, &i, sizeof(int));
	i = config->getReceiveTimeout();
	setsockopt(fd, SOL_SOCKET, SO_RCVTIMEO, &i, sizeof(int));
	return fd;
}
char* CSocket::receive(int &dataLen, int sessionId, int timeout) {

	if (sessionId <= 0 || sessionId > MAX_SESSIONID || windowDataMap[sessionId - 1] == NULL) {
		errno = -3;
		throw std::runtime_error("Need invoke 'registerRec' method before invoke 'receive' method!");
	}
	WindowData *wd = windowDataMap[sessionId - 1];
	int i = wd->waitOne(timeout);
	if (i == ETIMEDOUT) {
		errno = -1;
		unregisterRec(sessionId);
		gaeaLog(GAEA_WARNING, "timeout session:%d\n", sessionId);
		throw std::runtime_error("Receive data timeout or error!");
	}

	dataLen = wd->getDataLen();
	char *data = wd->getData();
	unregisterRec(sessionId);
	return data;
}
int CSocket::anetRead(int fd, char *buf, int count) {
	int nread = 0;
	retry: nread = read(fd, buf, count);
	if (nread < 0) {
		if (errno == EAGAIN) {
			goto retry;
		}
	}
	return nread;
}

int CSocket::anetWrite(int fd, const char *buf, int count) {
	int nwritten, totlen = 0;
	while (totlen != count) {
		nwritten = write(fd, buf, count - totlen);
		if (nwritten == 0)
			return totlen;
		if (nwritten == -1)
			return -1;
		totlen += nwritten;
		buf += nwritten;
	}
	return totlen;
}

int CSocket::sendData(int fd, const char* data, int dataLen, int sessionId) {
	if (sessionId <= 0 || sessionId > MAX_SESSIONID) {
		errno = -3;
		gaeaLog(GAEA_WARNING, "sendData sessionId is error:%d\n", sessionId);
		return errno;
	}
	int err = anetWrite(fd, P_START_TAG, sizeof(P_START_TAG));
	if (err < 0) {
		return err;
	}
	err = anetWrite(fd, data, dataLen);
	if (err < 0) {
		return err;
	}
	err = anetWrite(fd, P_END_TAG, sizeof(P_END_TAG));
	if (err < 0) {
		return err;
	}
	registerRec(sessionId);
	return err;
}
void CSocket::registerRec(int sessionId) {
	WindowData *wd = new WindowData();
	windowDataMap[sessionId - 1] = wd;
}
void CSocket::unregisterRec(int sessionId) {
	WindowData *wd = NULL;
	wd = windowDataMap[sessionId - 1];
	windowDataMap[sessionId - 1] = NULL;
	if (wd)
		delete wd;
}

int CSocket::anetTcpGenericConnect(const char *addr, int port, int flags, int timeout) {
	int s = 0;
	struct sockaddr_in sa;

	if ((s = socket(AF_INET, SOCK_STREAM, 0)) == -1) {
		gaeaLog(GAEA_WARNING, "creating socket: %s\n", strerror(errno));
		return ANET_ERR;
	}
	sa.sin_family = AF_INET;
	sa.sin_port = htons(port);
	if (inet_aton(addr, &sa.sin_addr) == 0) {
		struct hostent *he;

		he = gethostbyname(addr);
		if (he == NULL) {
			gaeaLog(GAEA_WARNING, "can't resolve: %s\n", addr);
			close(s);
			return ANET_ERR;
		}
		memcpy(&sa.sin_addr, he->h_addr, sizeof(struct in_addr));
	}
	unsigned long ul = 1;
	ioctl(s, FIONBIO, &ul);
	if (connect(s, (struct sockaddr*) &sa, sizeof(sa)) >= 0) {
	} else {
		if (errno == EINPROGRESS) {
			fd_set rset, wset;
			FD_ZERO(&rset);
			FD_ZERO(&wset);
			timeval tv;
			int error = 0, len = sizeof(int);
			tv.tv_sec = timeout;
			tv.tv_usec = 0;
			FD_SET(s, &wset);
			if (select(s + 1, &rset, &wset, NULL, &tv) > 0) {
				getsockopt(s, SOL_SOCKET, SO_ERROR, &error, (socklen_t *) &len);
				if (error != 0) {
					goto err;
				}
			} else {
				goto err;
			}
		} else {
			goto err;
		}
	}
	if (ANET_CONNECT_NONE == flags) {
		ul = 0;
		ioctl(s, FIONBIO, &ul);
	}
	return s;
	err: gaeaLog(GAEA_WARNING, "server %s connect failed: %s\n", addr, strerror(errno));
	close(s);
	return ANET_ERR;
}

int CSocket::anetTcpConnect(const char *addr, int port, int timeout) {
	return anetTcpGenericConnect(addr, port, ANET_CONNECT_NONE, timeout);
}

int CSocket::anetTcpNonBlockConnect(char *addr, int port, int timeout) {
	return anetTcpGenericConnect(addr, port, ANET_CONNECT_NONBLOCK, timeout);
}

void CSocket::frameHandle(int fd, char *data, int dataLen) {
	if (dataLen == 0) {
		free(data);
		gaeaLog(GAEA_WARNING, "server returned an unknown error.\n");
		return;
	}
	int sessionId = *(int*) (data + SFPStruct::Version + SFPStruct::TotalLen);
	if (sessionId > 0 && sessionId <= MAX_SESSIONID && windowDataMap[sessionId - 1]) {
		WindowData *wd = windowDataMap[sessionId - 1];
		wd->setData(data);
		wd->setDataLen(dataLen);
		wd->set();
	} else {
		free(data);
		gaeaLog(GAEA_WARNING, "frameHandle %d\n", sessionId);
	}
}
void CSocket::closeSocket(int fd) {
	close(fd);
}
}
