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
 * threadpoolmacro.h
 *
 *  Created on: 2011-7-20
 *  Author: Service Platform Architecture Team (spat@58.com)
 */

#ifndef THREAD_POOL_MACRO_H_
#define THREAD_POOL_MACRO_H_

#define MAX_THREAD_NUM 256    //线程池允许创建的最大线程数
#define MIN_THREAD_NUM 1    //线程池中最小需要创建的线程数

#include <stdio.h>
#include <pthread.h>
#include <stdlib.h>
#include <string.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <unistd.h>
#include <time.h>

#define return_if_null(p) if((p) == NULL)\
        {printf("%s:%d Warning: failed.\n",\
            __func__, __LINE__); return;}

#define return_null_if_fail(p) if((p) == NULL)\
        {printf("%s:%d Warning: failed.\n",\
            __func__, __LINE__); return NULL;}
        
#define return_val_if_fail(p, ret) if((p) == NULL)\
        {printf("%s:%d Warning: failed.\n", \
            __func__, __LINE__); return (ret);}

#endif //end THREAD_POOL_MACRO_H_
