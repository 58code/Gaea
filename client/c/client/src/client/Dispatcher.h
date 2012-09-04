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
 * Dispatcher.h
   *
 * Created on: 2011-7-8
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#ifndef DISPATCHER_H_
#define DISPATCHER_H_
#include "GaeaClientConfig.h"
#include "Server.h"
#include <vector>
#include <pthread.h>
namespace gaea {

class Dispatcher {
public:
    Dispatcher(GaeaClientConfig *gaeaClientConfig);
    virtual ~Dispatcher();
    Server* getServer();
    Server* getServer(char *hostName);
    std::vector<Server*>* getAllServer();
private:
    size_t count;
    pthread_mutex_t countMutex;
    pthread_mutex_t stateMutex;
    std::vector<Server*> *serverVector;
    int requestCount;
};

}

#endif /* DISPATCHER_H_ */
