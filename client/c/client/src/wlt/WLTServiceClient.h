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
/**	
 *  @file     WLTServiceClient.h
 *  @brief    
 *  @version  0.0.1
 *  @author   Service Platform Architecture Team (spat@58.com)
 *  @date     2012-02-21 Created it
 *  @note     
 */

#ifndef   WLTSERVICECLIENT_H
#define   WLTSERVICECLIENT_H

#include  "../client/ProxyStandard.h"

class WLTServiceClient {
public:
    static WLTServiceClient *singleton;
    virtual ~WLTServiceClient();
    bool isVipUser(long long uid);
private:
    WLTServiceClient();
    gaea::ProxyStandard *ps;
};
#endif /* WLTSERVICECLIENT_H*/
