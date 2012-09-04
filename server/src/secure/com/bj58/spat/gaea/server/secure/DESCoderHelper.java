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

import java.io.UnsupportedEncodingException;
import java.security.Key;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
/**
 * DESCoderHelper
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DESCoderHelper {
	
	public static final String KEY_ALGORITHM = "DES";
	public static final String CIPHER_ALGORITHM_ECB = "DES/ECB/PKCS5Padding";
	public static final String CIPHER_ALGORITHM_CBC = "DES/CBC/PKCS5Padding";
	
	public static DESCoderHelper getInstance(){
		return new DESCoderHelper();
	}
	
	/**
	 * 转换密钥
	 * @param key byte类型密钥
	 * @return key 密钥
	 * @throws Exception
	 */
	private Key toKey(byte[] key)throws Exception{
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory skf = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		//生成密钥
		SecretKey secretKey = skf.generateSecret(dks);
		return secretKey;
	}
	
	/**
	 * 生成8位byte密钥(系统自动生成)
	 * java默认密钥长度为56位
	 * @return byte[] 8位byte
	 * @throws Exception
	 */
	private byte[] initkey()throws Exception{
		KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
		kg.init(56);
		SecretKey secretKey = kg.generateKey();	
		return secretKey.getEncoded();
	}
	
	/**
	 * 生成密钥(系统自动生成)
	 * 用于java环境,c#下不可使用
	 * @return String
	 * @throws Exception
	 */
	public String initkeyString()throws Exception{
		return Base64.encodeBase64String(initkey());
	}
	
	/**
	 * 生成密钥(系统自动生成)
	 * 用于java环境,c#下不可使用
	 * @return String
	 * @throws Exception
	 */
	public String initkeyString(String key)throws Exception{
		return Base64.encodeBase64String(initkey(key));
	}
	
	/**
	 * 生成密钥(自定义)
	 * 用于java环境c#下不可使用
	 * 使用 key 中的前 8 个字节作为 DES 密钥的密钥内容
	 * @return
	 * @throws Exception
	 */
	public byte[] initkey(String key)throws Exception{
		if(key == null || key.getBytes().length < 8) {
			throw new Exception("key不合法, 长度必须大于8个字节!");
		}
		byte[] bufKey = key.getBytes(CharEncoding.UTF_8);		
		DESKeySpec dks = new DESKeySpec(bufKey);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
		SecretKey securekey = keyFactory.generateSecret(dks);
		return securekey.getEncoded();
	}

	/**
	 * 加密
	 * @param data 原文byte类型
	 * @param key 密钥byte类型
	 * @return byte[] 密文byte类型
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] data,byte[] key) throws Exception{
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
		cipher.init(cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}
	
	/**
	 * 加密
	 * @param data 原文byte类型
	 * @param key 密钥byte类型
	 * @return String 密文
	 * @throws Exception
	 */
	public String encryptString(byte[] data,byte[] key) throws Exception{
		return Base64.encodeBase64String(encrypt(data,key));
	}
	
	/**
	 * 加密  
	 * @param data 原文
	 * @param key 密钥
	 * @return 密文
	 * @throws Exception
	 */
	public String encryptString(String data,String key) throws Exception{
		return encryptString(data.getBytes(CharEncoding.UTF_8),key.getBytes(CharEncoding.UTF_8));
	}
	
	/**
	 * 解密
	 * @param data 密文byte类型
	 * @param key 密钥
	 * @return byte[] 原文byte类型
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] data,byte[] key) throws Exception{
		Key k = toKey(key);
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);
		cipher.init(cipher.DECRYPT_MODE, k);
		return cipher.doFinal(data);
	}
	
	/**
	 * 解密  
	 * data数据为base64加密后的字符串
	 * @param data 密文
	 * @param key 密钥
	 * @return 原文
	 * @throws Exception
	 */
	private String decrypt(String data,String key) throws Exception{
		return new String(decrypt(Base64.decodeBase64(data), key.getBytes(CharEncoding.UTF_8)));
	}
	
	/**
	 * 加密 
	 * 该方法与c#互通
	 * @param data 原文
	 * @param key 密钥(IV length must be 8 bytes long)
	 * @return byte 密文byte类型
	 * @throws Exception
	 */
	public byte[] encryptNetByte(String data, String key) throws Exception {
		keyLength(key);
	    return encryptNet(data.getBytes(CharEncoding.UTF_8),key.getBytes(CharEncoding.UTF_8));
	}
	
	/**
	 * 加密 
	 * 该方法与c#互通 内容
	 * @param data 原文
	 * @param key 密钥
	 * @return String 密文(密文为base64加密后的字符串)
	 * @throws Exception
	 */
	public String encryptNetString(String data, String key) throws Exception {
		keyLength(key);
	    return Base64.encodeBase64String(encryptNetByte(data,key));
	}
	
	/**
	 * 加密 
	 * 该方法与c#互通
	 * @param data 原文byte类型
	 * @param key 密钥 byte类型
	 * @return byte 密文byte类型(IV length must be 8 bytes long)
	 * @throws Exception
	 */
	private byte[] encryptNet(byte[] data, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
	    SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
	    IvParameterSpec iv = new IvParameterSpec(key);
	    cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
	    return cipher.doFinal(data);
	}
	
	/**
	 * 解密  
	 * 该方法与c#互通
	 * 如果密文为base64加密后的字符串,则需要base64解密为byte进行解码,
	 * 否则会抛出 BadPaddingException: Given final block not properly padded 异常 
	 * @param data 密文byte类型
	 * @param key 密钥byte类型(IV length must be 8 bytes long)
	 * @return 原文byte类型
	 * @throws Exception
	 */
	private byte[] decryptNet(byte[] data, byte[] key) throws Exception {
		Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);
		DESKeySpec desKeySpec = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
	    SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
	    IvParameterSpec iv = new IvParameterSpec(key);
	    cipher.init(Cipher.DECRYPT_MODE,secretKey, iv);
	    return cipher.doFinal(data);
	}
	
	/**
	 * 解密
	 * 该方法与c#互通
	 * data密文类型为base64加密后的字符串
	 * key为密钥,本方法通过UTF-8方式获得key的byte类型
	 * @param data 密文
	 * @param key 密钥(IV length must be 8 bytes long)
	 * @return 原文
	 * @throws Exception
	 */
	public String decryptNetString(String data, String key) throws Exception {
		keyLength(key);
	    return new String(decryptNet(Base64.decodeBase64(data),key.getBytes(CharEncoding.UTF_8)));
	}
	
	/**
	 * 解密
	 * 该方法与c#互通
	 * data密文类型为原文加密后的byte类型,如果密文通过base64加密为字符串形式，解码请用方法decryptNetString(String data,String key)
	 * 如通过通过base64加密后字符串getByte方式调用该方法会抛出BadPaddingException: Given final block not properly padded 异常
	 * key为密钥byte类型,为防止乱码问题建议使用时通过getByte("UTF-8")获取byte类型
	 * @param data 密文byte类型
	 * @param key 密钥byte类型(IV length must be 8 bytes long)
	 * @return 原文
	 * @throws Exception
	 */
	public String decryptNetString(byte[] data, byte[] key) throws Exception {
		keyLength(key);
	    return new String(decryptNet(data,key));
	}
	
	/**
	 * 判断key length
	 * @param key
	 * @throws Exception 
	 * @throws UnsupportedEncodingException 
	 */
	private void keyLength(String key) throws Exception{
		keyLength(key.getBytes(CharEncoding.UTF_8));
	}
	
	private void keyLength(byte[] key) throws Exception{
		if(key.length != 8){
			throw new Exception("key length must be 8 bytes long!");
		}
	}
	
	/**main 测试
	 * @throws Exception */
	public static void main(String []args) throws Exception{
		DESCoderHelper descode = DESCoderHelper.getInstance();
		String key = descode.initkeyString();//cHZkc3V5dWg= cHZkc3V5dWg=
		System.out.println("密钥:"+key);
		System.out.println(descode.encryptNetString("58同城一个神奇的网站  very good! oh haha $&**&", "tsyg=af$"));
		//System.out.println(decryptNetString("ZHg25iEgaOarDRbIfHNnXqPuMaOXPRo6CBTLUW2s8BOecSdJIoFQeI/g07ViN+KkxpJdW/ewNCQ=", key));
	}
}
