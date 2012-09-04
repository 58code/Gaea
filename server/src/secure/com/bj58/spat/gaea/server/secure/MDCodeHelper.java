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

import java.security.MessageDigest;

/**
 * MDCodeHelper
 * Java 消息摘要算法(MD2,MD5,SHA) 128位
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class MDCodeHelper {
	/**
	 * MD2 消息摘要
	 * @param data 待做摘要处理的数据
	 * @return byte [] 消息摘要
	 * @throws Exception
	 */
	public static byte[] encodeMD2(byte [] data)throws Exception{
		//初始化MessageDigest
		MessageDigest md = MessageDigest.getInstance("MD2");
		//执行消息摘要
		return md.digest(data);
	}
	
	/**
	 * MD2 消息摘要
	 * @param data 待做摘要处理的数据
	 * @return String 消息摘要
	 * @throws Exception
	 */
	public static String encodeMD2(String data)throws Exception{
		return Base64.encodeBase64String(encodeMD2(data.getBytes(CharEncoding.UTF_8)));
	}
	
	/**
	 * MD5 消息摘要
	 * @param data 待做摘要处理的数据
	 * @return byte [] 消息摘要
	 * @throws Exception
	 */
	public static byte[] encodeMD5(byte [] data)throws Exception{
		//初始化MessageDigest
		MessageDigest md = MessageDigest.getInstance("MD5");
		//执行消息摘要
		return md.digest(data);
	}
	
	/**
	 * MD5 消息摘要
	 * @param data 待做摘要处理的数据
	 * @return String 消息摘要
	 * @throws Exception
	 */
	public static String encodeMD5(String data)throws Exception{
		return Base64.encodeBase64String(encodeMD5(data.getBytes(CharEncoding.UTF_8)));
	}
	
	/**
	 * SHA-1 消息摘要
	 * @param data 待做摘要处理的数据
	 * @return byte [] 消息摘要
	 * @throws Exception
	 */
	public static byte[] encodeSHA(byte [] data)throws Exception{
		//初始化MessageDigest
		MessageDigest md = MessageDigest.getInstance("SHA");
		//执行消息摘要
		return md.digest(data);
	}
	
	/**
	 * SHA-1 消息摘要
	 * @param data 待做摘要处理的数据
	 * @return String 消息摘要
	 * @throws Exception
	 */
	public static String encodeSHA(String data)throws Exception{
		return Base64.encodeBase64String(encodeSHA(data.getBytes(CharEncoding.UTF_8)));
	}
	
	/**
	 * SHA-256 消息摘要
	 * @param data 待做摘要处理的数据
	 * @return byte [] 消息摘要
	 * @throws Exception
	 */
	public static byte[] encodeSHA256(byte [] data)throws Exception{
		//初始化MessageDigest
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		//执行消息摘要
		return md.digest(data);
	}
	
	/**
	 * SHA-256 消息摘要
	 * @param data 待做摘要处理的数据
	 * @return String 消息摘要
	 * @throws Exception
	 */
	public static String encodeSHA256(String data)throws Exception{
		return Base64.encodeBase64URLSafeString(encodeSHA256(data.getBytes(CharEncoding.UTF_8)));
	}
	
	/**
	 * SHA-384 消息摘要
	 * @param data 待做摘要处理的数据
	 * @return byte [] 消息摘要
	 * @throws Exception
	 */
	public static byte[] encodeSHA384(byte [] data)throws Exception{
		//初始化MessageDigest
		MessageDigest md = MessageDigest.getInstance("SHA-384");
		//执行消息摘要
		return md.digest(data);
	}
	
	/**
	 * SHA-384 消息摘要
	 * @param data 待做摘要处理的数据
	 * @return String 消息摘要
	 * @throws Exception
	 */
	public static String encodeSHA384(String data)throws Exception{
		return Base64.encodeBase64String(encodeSHA384(data.getBytes(CharEncoding.UTF_8)));
	}
	
	/**
	 * SHA-512 消息摘要
	 * @param data 待做摘要处理的数据
	 * @return byte [] 消息摘要
	 * @throws Exception
	 */
	public static byte[] encodeSHA512(byte [] data)throws Exception{
		//初始化MessageDigest
		MessageDigest md = MessageDigest.getInstance("SHA-512");
		//执行消息摘要
		return md.digest(data);
	}
	/**
	 * SHA-512 消息摘要
	 * @param data 待做摘要处理的数据
	 * @return String 消息摘要
	 * @throws Exception
	 */
	public static String encodeSHA512(String data)throws Exception{
		return Base64.encodeBase64String(encodeSHA512(data.getBytes(CharEncoding.UTF_8)));
	}
}
