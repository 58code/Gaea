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
 * SocketPoolProfile.h
   *
 * Created on: 2011-7-5
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#ifndef SOCKETPOOLPROFILE_H_
#define SOCKETPOOLPROFILE_H_
#define CONFIGLINE_MAX    1024
#include "../tinyxpath/tinyxml.h"
namespace gaea {

class SocketPoolProfile {
private:
	int minPoolSize;
	int maxPoolSize;
	int sendTimeout;
	int receiveTimeout;
	int waitTimeout;
	bool nagle;
	int shrinkInterval;
	int bufferSize;
	int connectionTimeout;
	int maxPakageSize;
	bool _protected;

public:
	SocketPoolProfile(TiXmlElement *clientElement);
	virtual ~SocketPoolProfile();
	int getBufferSize() const;
	int getConnectionTimeout() const;
	int getMaxPakageSize() const;
	int getMaxPoolSize() const;
	int getMinPoolSize() const;
	bool getNagle() const;
	bool getProtected() const;
	int getReceiveTimeout() const;
	int getSendTimeout() const;
	int getShrinkInterval() const;
	int getWaitTimeout() const;

};
}

#endif /* SOCKETPOOLPROFILE_H_ */
