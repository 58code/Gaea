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
#include "serializer.h"
#include <string.h>
int GetHashcode(char *str, int len) {

	int hash1 = 5381;
	int hash2 = hash1;
	int i = 0;
	for (i = 0; i < len; i++) {
		hash1 = ((hash1 << 5) + hash1) ^ *str;
		if (++i >= len) {
			break;
		}
		str += 1;
		hash2 = ((hash2 << 5) + hash2) ^ *str;
		str += 1;
	}
	return hash1 + (hash2 * 1566083941);

}

int GetTypeId(char *type) {

	int id;
	if (strcmp(type, SERIALIZE_NULL) == 0) {
		id = SERIALIZE_NULL_N;
	} else if (strcmp(type, SERIALIZE_CHAR) == 0) {
		id = SERIALIZE_CHAR_N;
	} else if (strcmp(type, SERIALIZE_BOOL) == 0) {
		id = SERIALIZE_BOOL_N;
	} else if (strcmp(type, SERIALIZE_SHORT_INT) == 0) {
		id = SERIALIZE_SHORT_INT_N;
	} else if (strcmp(type, SERIALIZE_INT) == 0) {
		id = SERIALIZE_INT_N;
	} else if (strcmp(type, SERIALIZE_TIME) == 0) {
		id = SERIALIZE_TIME_N;
	} else if (strcmp(type, SERIALIZE_FLOAT) == 0) {
		id = SERIALIZE_FLOAT_N;
	} else if (strcmp(type, SERIALIZE_DOUBLE) == 0) {
		id = SERIALIZE_DOUBLE_N;
	} else if (strcmp(type, SERIALIZE_LONG_LONG) == 0) {
		id = SERIALIZE_LONG_LONG_N;
	} else if (strcmp(type, SERIALIZE_LONG) == 0) {
		if (sizeof(long) == sizeof(int)) {
			id = SERIALIZE_INT_N;
		} else {
			id = SERIALIZE_LONG_LONG_N;
		}
	} else if (strcmp(type, SERIALIZE_ARRAY) == 0) {
		id = SERIALIZE_ARRAY_N;
	} else if (strcmp(type, SERIALIZE_LIST) == 0) {
		id = SERIALIZE_LIST_N;
	} else if (strcmp(type, SERIALIZE_MAP) == 0) {
		id = SERIALIZE_MAP_N;
	} else if (strcmp(type, SERIALIZE_STRING) == 0) {
		id = SERIALIZE_STRING_N;
	} else if (strcmp(type, SERIALIZE_VOID) == 0) {
		id = SERIALIZE_VOID_N;
	} else if (strcmp(type, SERIALIZE_ENUM) == 0) {
		id = SERIALIZE_ENUM_N;
	} else {
		id = GetHashcode(type, strlen(type));
	}

	return id;

}
