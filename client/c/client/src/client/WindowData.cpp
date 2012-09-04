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
 * WindowData.cpp
   *
 * Created on: 2011-7-8
 * Author: Service Platform Architecture Team (spat@58.com)
 */

#include "WindowData.h"
#include <stdexcept>
#include <time.h>
#include <errno.h>
#include <sys/time.h>
#include <stddef.h>
#include <sys/types.h>
#include <stdio.h>
WindowData::WindowData() {
	if (pthread_mutex_init(&mutex_, NULL) != 0) {
		errno = -3;
		throw std::runtime_error("pthread_mutex_init error");
	}
	if (pthread_cond_init(&cond_, NULL) != 0) {
		errno = -3;
		throw std::runtime_error("pthread_cond_init error");
	}
	data = NULL;
}
int WindowData::waitOne(int timeOut) {
	int i = 0;
	timespec mytime;
	timeval now;
	gettimeofday(&now, NULL);
	mytime.tv_sec = now.tv_sec + timeOut;
	mytime.tv_nsec = now.tv_usec * 1000;
	pthread_mutex_lock(&mutex_);
	i = pthread_cond_timedwait(&cond_, &mutex_, &mytime); //等待线程调度
	pthread_mutex_unlock(&mutex_);
	return i;
}
void WindowData::set() {
	pthread_mutex_lock(&mutex_);
	pthread_cond_signal(&cond_);
	pthread_mutex_unlock(&mutex_);
}

WindowData::~WindowData() {
	pthread_mutex_destroy(&mutex_);
	pthread_cond_destroy(&cond_);
}

char *WindowData::getData() const {
	return data;
}

int WindowData::getDataLen() const {
	return dataLen;
}

pthread_mutex_t WindowData::getMutex() const {
	return mutex_;
}

void WindowData::setDataLen(int dataLen) {
	this->dataLen = dataLen;
}

void WindowData::setData(char *data) {
	this->data = data;
}

int WindowData::getFd() const {
	return fd;
}

void WindowData::setFd(int fd) {
	this->fd = fd;
}

