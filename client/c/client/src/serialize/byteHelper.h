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
#ifndef __byteHelper
#define __byteHelper
void byteArrayPutData(array *outArray, const void *data, int dataLen);
void structPutData(array *outArray, const void *data, int dataLen);
void *byteArrayPopData(array *outArray, int dataLen);
void writeInt16(short data, array *outArray);
void writeInt32(int data, array *outArray);
void writeInt64(long long data, array *outArray);
char IsPrimitive(int typeId);
int getObjectSize(int typeId);
#endif
