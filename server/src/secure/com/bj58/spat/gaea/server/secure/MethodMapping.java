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
package com.bj58.spat.gaea.server.secure;

import java.util.HashMap;
import java.util.Map;

import com.bj58.spat.gaea.server.contract.context.GaeaChannel;
import com.bj58.spat.gaea.server.contract.init.IInit;

/**
 * MethodMapping
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class MethodMapping implements IInit {

	private static final Map<GaeaChannel, HashMap<String, String>> channelMap = new HashMap<GaeaChannel, HashMap<String, String>>();

	@Override
	public void init() {
		// TODO 从授权文件中加载方法名到methodMap
	}
	
	public static boolean check(GaeaChannel channel, String methodName) {
		HashMap<String, String> map = channelMap.get(channel);
		if(map != null) {
			return map.containsKey(methodName);
		}
		
		return false;
	}
}