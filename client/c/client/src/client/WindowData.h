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
 * WindowData.h
   *
 * Created on: 2011-7-8
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#ifndef WINDOWDATA_H_
#define WINDOWDATA_H_
#include <pthread.h>
class WindowData {
public:
	WindowData();
	virtual ~WindowData();
	int waitOne(int time);
	void set();
    char *getData() const;
    int getDataLen() const;
    pthread_mutex_t getMutex() const;
    void setDataLen(int dataLen);
    void setData(char *data);
    int getFd() const;
    void setFd(int fd);
private:
    int fd;
    char *data;
    int dataLen;
    pthread_mutex_t mutex_; //线程同步锁
    pthread_cond_t cond_; //线程同步的条件变量
};

#endif /* WINDOWDATA_H_ */
