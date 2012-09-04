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
 * ServerProfile.h
   *
 * Created on: 2011-7-8
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#ifndef SERVERPROFILE_H_
#define SERVERPROFILE_H_
#include "../tinyxpath/tinyxml.h"
namespace gaea {
class ServerProfile {
public:
	ServerProfile(TiXmlElement *element);
	virtual ~ServerProfile();
	char *getHostName() const;
	int getPort() const;
	void setHostName(char *hostName);
	void setPort(int port);
	int getWeithRage() const;
	void setWeithRage(int weithRate);
	int getDeadTimeout() const;
	void setDeadTimeout(int deadTimeout);
	char *getName() const;
	void setName(char *name);
private:
	char *hostName;
	char *name;
	int port;
	int weithRage;
	int deadTimeout;
};
}
#endif /* SERVERPROFILE_H_ */
