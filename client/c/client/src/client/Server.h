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
 * Server.h
   *
 * Created on: 2011-7-5
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#ifndef SERVER_H_
#define SERVER_H_
#include "../protocol/Protocol.h"
#include "SocketPool.h"
#include "ServerProfile.h"
#include "SocketPoolProfile.h"
#include <time.h>
#include <pthread.h>
namespace gaea {
typedef enum {
	SERVER_DEAD, SERVER_NORMAL, SERVER_BUSY, SERVER_DISABLE, SERVER_REBOOT, SERVER_TESTING
} ServerState;
class Server {
public:
	Server(ServerProfile *serverProfile, SocketPoolProfile *spp);
	virtual ~Server();
	Protocol* request(Protocol &p);
	char *getName() const;
	void setName(char *name);
	ServerState getState() const;
	void setState(ServerState state);
	time_t getDeadTime() const;
	void setDeadTime(time_t deadTime);
	int getDeadTimeout();

	void markAsDead();

	void markAsReboot();

	void markAsNormal();

	void relive();

	bool test();

private:
	pthread_mutex_t mutex;
	ServerState state;
	class SocketPool *socketPool;
	ServerProfile *serverProfile;
	char *name;
	SocketPoolProfile *spp;
	time_t deadTime;
	bool testing;
};
}
#endif /* SERVER_H_ */
