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
 * SdpStruct.h
 *
 *  Created on: 2011-7-13
 *  Author: Service Platform Architecture Team (spat@58.com)
 */

#ifndef SDPSTRUCT_H_
#define SDPSTRUCT_H_
#include "../serialize/serializeList.h"
#include "../serialize/serializer.h"
typedef struct {
	char *key;
	void *value;
	int valueTypeId;
} KeyValuePair, RpParameter;

typedef struct {
	int errorCode;
	char *toIP;
	char *fromIP;
	char *ErrorMsg;
} ExceptionProtocol;

typedef struct {
	char *msg;
} ResetProtocol;

typedef struct {
	int hand;
} HandclaspProtocol;

typedef struct {
	char *lookup;
	char *methodName;
	struct serialize_list *paraList;
} RequestProtocol;

typedef struct {
	void *result;
	int resultTypeId;
	array *outpara;
} ResponseProtocol;

#endif /* SDPSTRUCT_H_ */
