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
 * Protocol.h
 *
 *  Created on: 2011-7-7
 *  Author: Service Platform Architecture Team (spat@58.com)
 */

#ifndef PROTOCOL_H_
#define PROTOCOL_H_
#define HEAD_STACK_LENGTH 14

namespace gaea {
typedef enum {
	Response = 1, Request, Exception, Config, Handclasp, RebootException
} SDPType;

typedef enum {
	JSON = 1, JAVABinary, XML, GAEABinary
} SerializeType;

typedef enum {
	Java = 1, Dotnet, C
} PlatformType;

typedef enum {
	UnCompress, SevenZip
} CompressType;

class Protocol {
public:
	Protocol(int sessionId, char serviceId, SDPType sdpType, CompressType compressType, SerializeType serializeType, PlatformType platformType, char* userData);
	Protocol(int sessionId, char serviceId, SDPType sdpType, CompressType compressType, SerializeType serializeType, PlatformType platformType, void *sdpEntity,
			const char *sdpEntityType);
	Protocol(int sessionId, char serviceId, SDPType sdpType, void* sdpEntity, const char *sdpEntityType);
	Protocol();
	virtual ~Protocol();
	char* getBytes(int &dataLen);
	int getSessionID();
	static Protocol* fromBytes(const char* data, int dataLen);
	CompressType getCompressType() const;
	PlatformType getPlatformType() const;
	void *getSdpEntity() const;
	char *getSdpEntityType() const;
	SDPType getSdpType() const;
	SerializeType getSerializeType() const;
	char getServiceId() const;
	int getSessionId() const;
	int getTotalLen() const;
	char *getUserData() const;
	void setCompressType(CompressType compressType);
	void setPlatformType(PlatformType platformType);
	void setSdpEntity(void *sdpEntity);
	void setSdpEntityType(char *sdpEntityType);
	void setSdpType(SDPType sdpType);
	void setSerializeType(SerializeType serializeType);
	void setServiceId(char serviceId);
	void setSessionId(int sessionId);
	void setTotalLen(int totalLen);
	void setUserData(char *userData);
private:
	int sessionId;
	int totalLen;
	char serviceId;
	SDPType sdpType;
	CompressType compressType;
	PlatformType platformType;
	SerializeType serializeType;
	char *userData;
	void *sdpEntity;
	char *sdpEntityType;
	static const int VERSION = 1;
	char *serData;
	static const char* getSDPClass(SDPType type);
};
}
#endif /* PROTOCOL_H_ */
