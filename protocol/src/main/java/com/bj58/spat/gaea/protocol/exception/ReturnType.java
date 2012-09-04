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
package com.bj58.spat.gaea.protocol.exception;

/**
 * ReturnType
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ReturnType {
	public static final int SUCCESS = 0;
	
	public static final int DB = 1;
	
	public static final int NET = 2;
	
	public static final int TIME_OUT = 3;
	
	public static final int PROTOCOL = 4;
	
	public static final int JSON_EXCEPTION = 5;
	
	public static final int PARA_EXCEPTION = 6;
	
	public static final int NOT_FOUND_METHOD_EXCEPTION = 7;
	
	public static final int NOT_FOUND_SERVICE_EXCEPTION = 8;
	
	public static final int JSON_SERIALIZE_EXCEPTION = 9;
	
	public static final int SERVICE_EXCEPTION = 10;
	
	public static final int DATA_OVER_FLOW_EXCEPTION = 11;
	
	public static final int REBOOT_EXCEPTION = 12;//服务重启异常
	
	public static final int OTHER_EXCEPTION = 99;
	
}
