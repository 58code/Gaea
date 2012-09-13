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
package com.bj58.spat.gaea.serializer.component.helper;

/**
 * StrHelper
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class StrHelper {

	public static String EmptyString = "";

	public static int GetHashcode(String str) {
		int hash1 = 5381;
		int hash2 = hash1;
		int len = str.length();
		for (int i = 0; i < len; i++) {
			int c = str.charAt(i);
			hash1 = ((hash1 << 5) + hash1) ^ c;
			if (++i >= len) {
				break;
			}
			c = str.charAt(i);
			hash2 = ((hash2 << 5) + hash2) ^ c;
		}
		return hash1 + (hash2 * 1566083941);
	}

	public static boolean isEmptyOrNull(String str) {
		if (str == null || "".equals(str)) {
			return true;
		}
		return false;
	}
}
