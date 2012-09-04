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
 * CSocket.h
 *
 * Created on: 2011-7-4
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#ifndef CSOCKET_H_
#define CSOCKET_H_
#define ANET_OK 0
#define ANET_ERR -1
#define ANET_ERR_LEN 256
#define ANET_CONNECT_NONE 0
#define ANET_CONNECT_NONBLOCK 1
#include "CSocket.h"
#include "SocketPool.h"
#include "SocketPoolProfile.h"
#include "Log.h"
#include "WindowData.h"

#include <fcntl.h>
#include <string.h>
#include <errno.h>
#include <stdio.h>
#include <stdarg.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/tcp.h>
#include <arpa/inet.h>
#include <netdb.h>
#include <vector>
namespace gaea {
class CSocket {
public:
	static int createSocket(const char *hostName, int port, class SocketPool *socketPool, SocketPoolProfile *config);
	static int sendData(int fd, const char* data, int dataLen, int sessionId);
	static char* receive(int &dataLen, int sessionId, int timeout);
	static void frameHandle(int fd, char *data, int dataLen);
	static int anetRead(int fd, char *buf, int count);
	static void closeSocket(int fd);
	static int anetTcpConnect(const char *addr, int port, int timeout);
private:
	static void registerRec(int sessionId);
	static void unregisterRec(int sessionId);
	static WindowData **windowDataMap;

	static int anetTcpGenericConnect(const char *addr, int port, int flags, int timeout);

	static int anetTcpNonBlockConnect(char *addr, int port, int timeout);

	static int anetWrite(int fd, const char *buf, int count);
};
}
#endif /* CSOCKET_H_ */
