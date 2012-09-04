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
package com.bj58.spat.gaea.protocol.utility;

/**
 * ProtocolHelper
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ProtocolHelper {
	
	/**
	 * 获得协义的版本号
	 * @param buffer
	 * @return
	 */
	public static int getVersion(byte[] buffer) {
		return buffer[0];
	}

	/**
	 * 解析协义
	 * @param buffer
	 * @return
	 * @throws Exception
	 */
	public static Object fromBytes(byte[] buffer) throws Exception {
		if(buffer != null && buffer.length > 0) {
			int version = buffer[0];
			if(version == com.bj58.spat.gaea.protocol.sfp.v1.Protocol.VERSION) {
				return com.bj58.spat.gaea.protocol.sfp.v1.Protocol.fromBytes(buffer);
			}
		}
		
		throw new Exception("不完整的二进制流");
	}
	
	/**
	 * 
	 * @param buf
	 * @return
	 */
//	public static boolean checkHeadDelimiter(byte[] buf){
//		if(buf.length == ProtocolConst.P_START_TAG.length){
//			for(int i=0; i<buf.length; i++) {
//				if(buf[i] != ProtocolConst.P_START_TAG[i]) {
//					return false;
//				}
//			}
//			return true;
//		} else{
//			return false;
//		}
//	}
}
