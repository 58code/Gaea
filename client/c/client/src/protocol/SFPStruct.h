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
 * SFPStruct.h
 *
 *  Created on: 2011-7-12
 *  Author: Service Platform Architecture Team (spat@58.com)
 */

#ifndef SFPSTRUCT_H_
#define SFPSTRUCT_H_
namespace gaea {
class SFPStruct {
public:
	const static int Version = 1;
	const static int TotalLen = 4;
	const static int SessionId = 4;
	const static int ServerId = 1;
	const static int SDPType = 1;
	const static int CompressType = 1;
	const static int SerializeType = 1;
	const static int Platform = 1;
};
}
#endif /* SFPSTRUCT_H_ */
