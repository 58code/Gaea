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
package com.bj58.spat.gaea.server.util;

import java.util.regex.Pattern;

import com.bj58.spat.gaea.server.contract.context.Global;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class IPTable {
	
	private static Pattern allowPattern;
	private static Pattern forbidPattern;
	
	static {
		init();
	}
	
	public static void init() {
		String allowIP = Global.getSingleton().getServiceConfig().getString("gaea.iptable.allow.iplist");
		String forbidIP = Global.getSingleton().getServiceConfig().getString("gaea.iptable.forbid.iplist");
		allowIP = allowIP.replaceAll("\\.", "\\\\.")
				   .replaceAll(",", "|")
				   .replaceAll("\\*", "\\.\\*");
		
		forbidIP = forbidIP.replaceAll("\\.", "\\\\.")
		  		   .replaceAll(",", "|")
		  		   .replaceAll("\\*", "\\.\\*");
		
		if(allowIP != null && !allowIP.equalsIgnoreCase("")) {
			allowPattern = Pattern.compile(allowIP);
		} else {
			allowPattern = null; //for unit test
		}
		if(forbidIP != null && !forbidIP.equalsIgnoreCase("")) {
			forbidPattern = Pattern.compile(forbidIP);
		} else {
			forbidPattern = null; //for unit test
		}
	}
	
	/**
	 * check ip is allow
	 * @param ip
	 * @return
	 */
	public static boolean isAllow(String ip) {
		if(ip != null && !ip.equalsIgnoreCase("")) {
			boolean allowMatch = true;
			boolean forbidMatch = false;
			
			if(allowPattern != null) {
				allowMatch = allowPattern.matcher(ip).find();
			}
			if(forbidPattern != null) {
				forbidMatch = forbidPattern.matcher(ip).find();
			}
			
			return (allowMatch && !forbidMatch); 
		}

		return false;
	}
	
	/**
	 * format ip
	 * @param ip
	 * @return
	 */
	public static String formatIP(String ip) {
		ip = ip.replaceAll("/", "");
		ip = ip.substring(0, ip.lastIndexOf(":"));
		return ip;
	}
}