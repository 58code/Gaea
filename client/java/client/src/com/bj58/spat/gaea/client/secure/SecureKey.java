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
package com.bj58.spat.gaea.client.secure;

import java.util.HashMap;
import java.util.Map;

/**
 * SecureKey
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class SecureKey {
	RSACoderHelper rsaHelper = RSACoderHelper.getInstance();
	Map<String,Object> map  = new HashMap<String,Object>();
	
	public SecureKey() throws Exception{
		this.map = rsaHelper.initKey();
	}
	
	public String getRSAPrivateKeyAsNetFormat(String encodedPrivatekey){
		return rsaHelper.getRSAPrivateKeyAsNetFormat(encodedPrivatekey);
	}
	
	public String getRSAPublicKeyAsNetFormat(String encodedPrivatekey){
		return rsaHelper.getRSAPublicKeyAsNetFormat(encodedPrivatekey);
	}
	
	/**
	 * DES 加密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByDesKey(byte[] data, byte[] key) throws Exception{
		DESCoderHelper desHelper = DESCoderHelper.getInstance();
		return desHelper.encrypt(data, key);
	}
	
	/**
	 * DES 解密
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByDesKey(byte[] data, byte[] key) throws Exception{
		DESCoderHelper desHelper = DESCoderHelper.getInstance();
		return desHelper.decrypt(data, key);
	}
	
	/**
	 * 公钥加密
	 * @param data原文
	 * @param key 公钥
	 * @return 密文byte[]
	 * @throws Exception
	 */
	public byte[] encryptByPublicKey(byte[] data,byte[] key) throws Exception{
		return rsaHelper.encryptByPublicKey(data, key);
	}
	
	/**
	 * 公钥加密
	 * @param data原文
	 * @param key 公钥
	 * @return 密文byte[]
	 * @throws Exception
	 */
	public String encryptByPublicKeyString(byte[] data,byte[] key) throws Exception{
		return rsaHelper.encryptByPublicKeyString(data, key);
	}
	
	/**
	 * 公钥加密
	 * @param data原文
	 * @param key 公钥
	 * @return 密文byte[]
	 * @throws Exception
	 */
	public String encryptByPublicKeyString(String data,String key) throws Exception{
		return rsaHelper.encryptByPublicKeyString(data, key);
	}
	
	/**
	 * 私钥解密
	 * @param data 密文
	 * @param key 私钥
	 * @return 原文byte[]
	 * @throws Exception
	 */
	public byte[] decryptByPrivateKey(byte[] data,byte[] key) throws Exception{
		return rsaHelper.decryptByPrivateKey(data, key);
	}
	
	/**
	 * 私钥解密
	 * @param data 密文
	 * @param key 私钥
	 * @return 原文byte[]
	 * @throws Exception
	 */
	public String decryptByPrivateKey(String data,String key) throws Exception{
		return rsaHelper.decryptByPrivateKey(data, key);
	}
	
	/**
	 * 私钥解密
	 * @param data 密文
	 * @param key 私钥
	 * @return 原文byte[]
	 * @throws Exception
	 */
	public byte[] decryptByPrivateKeyByte(String data,String key) throws Exception{
		return rsaHelper.decryptByPrivateKeyByte(data, key);
	}
	
	/**
	 * 获取公钥
	 * @return byte公钥
	 */
	public byte[] getPublicKey(){
		return (map == null)? null : rsaHelper.getPublicKey(map);
	}
	
	/**
	 * 获取私钥
	 * @return byte私钥
	 */
	public byte[] getPrivateKey(){
		return (map == null)? null : rsaHelper.getPrivateKey(map);
	}
	/**
	 * 获取公钥
	 * @return String公钥
	 */
	public String getStringPublicKey(){
		return (map == null)? null : rsaHelper.getStringPublicKey(map);
	}
	
	/**
	 * 获取私钥
	 * @return String私钥
	 */
	public String getStringPrivateKey(){
		return (map == null)? null : rsaHelper.getStringPrivateKey(map);
	}
}
