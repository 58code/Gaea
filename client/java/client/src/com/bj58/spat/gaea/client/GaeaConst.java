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
package com.bj58.spat.gaea.client;

/**
 * GaeaConst
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class GaeaConst {

	/**
	 * 配置文件路径
	 */
	public static String CONFIG_PATH = GaeaInit.DEFAULT_CONFIG_PATH;

	/**
     *MAX_SESSIONID
     */
	public static final long MAX_SESSIONID = 1024 * 1024 * 1024;

	public static final int DEFAULT_MAX_CURRENT_USER_COUNT = 2000;
	// 1m
	public static final int DEFAULT_MAX_PAKAGE_SIZE = 1024 * 1024;
	// 10kb
	public static final int DEFAULT_BUFFER_SIZE = 10 * 1024;
	// 60s
	public static final int DEFAULT_DEAD_TIMEOUT = 60000;

	public static final boolean DEFAULT_PROTECTED = true;

	public static final String VERSION_FLAG = "Gaea Client v1.0.0:";
}
