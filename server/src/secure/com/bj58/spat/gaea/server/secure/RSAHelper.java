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

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * RSAHelper
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
@SuppressWarnings("restriction")
public class RSAHelper {

	private PublicKey pubKey;
	private PrivateKey priKey;
	private Cipher pubDecryptCipher;
	private Cipher priDecryptCipher;
	private Cipher pubEncryptCipher;
	private Cipher priEncryptCipher;
	private String pubModulus;
	private String pubExponent;

	/**
	 * 获取实例
	 * 
	 * @return
	 * @throws Exception
	 */
	public static RSAHelper getInstance() throws Exception {
		return new RSAHelper();
	}
	
	/**
	 * 
	 * @param pubModulus
	 * @param pubExponent
	 * @return
	 * @throws Exception
	 */
	public static RSAHelper getInstance(String pubModulus, String pubExponent) throws Exception {
		return new RSAHelper(pubModulus, pubExponent);
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	private RSAHelper() throws Exception {
		// 生成公私钥对
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		this.pubKey = keyPair.getPublic();
		this.priKey = keyPair.getPrivate();
		
		this.pubEncryptCipher = Cipher.getInstance("RSA");
		this.pubEncryptCipher.init(Cipher.ENCRYPT_MODE, this.pubKey);
		this.priEncryptCipher = Cipher.getInstance("RSA");
		this.priEncryptCipher.init(Cipher.ENCRYPT_MODE, this.priKey);

		this.pubDecryptCipher = Cipher.getInstance("RSA");
		this.pubDecryptCipher.init(Cipher.DECRYPT_MODE, this.pubKey);
		this.priDecryptCipher = Cipher.getInstance("RSA");
		this.priDecryptCipher.init(Cipher.DECRYPT_MODE, this.priKey);
		
		// 将公钥和模进行Base64编码
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec publicSpec = keyFactory.getKeySpec(this.pubKey, RSAPublicKeySpec.class);
		BigInteger modulus = publicSpec.getModulus();
		BigInteger exponent = publicSpec.getPublicExponent();
		BASE64Encoder base64Encoder = new BASE64Encoder();
		this.pubModulus = base64Encoder.encodeBuffer(modulus.toByteArray());
		this.pubExponent = base64Encoder.encodeBuffer(exponent.toByteArray());
	}

	/**
	 * 
	 * @param pubModulus
	 * @param pubExponent
	 * @throws Exception
	 */
	private RSAHelper(String pubModulus, String pubExponent) throws Exception {
		BASE64Decoder base64Decoder = new BASE64Decoder();
		
		// 创建公钥
		byte[] exponentBuf = base64Decoder.decodeBuffer(pubExponent);
		byte[] modulusBuf = base64Decoder.decodeBuffer(pubModulus);
		BigInteger big_exponent = new BigInteger(1, exponentBuf);
		BigInteger big_modulus = new BigInteger(1, modulusBuf);
		RSAPublicKeySpec keyspec = new RSAPublicKeySpec(big_modulus, big_exponent);
		
		KeyFactory keyfac = KeyFactory.getInstance("RSA");
		this.pubKey = keyfac.generatePublic(keyspec);
		
		this.pubEncryptCipher = Cipher.getInstance("RSA");
		this.pubEncryptCipher.init(Cipher.ENCRYPT_MODE, this.pubKey);
		this.pubDecryptCipher = Cipher.getInstance("RSA");
		this.pubDecryptCipher.init(Cipher.DECRYPT_MODE, this.pubKey);
		
		this.pubModulus = pubModulus;
		this.pubExponent = pubExponent;
	}

	/**
	 * 公钥加密
	 * 
	 * @param key 密钥
	 * @param source 原文
	 * @return 密文
	 * @throws Exception
	 */
	public byte[] encryptByPublicKey(byte[] source) throws Exception {
		return pubEncryptCipher.doFinal(source);
	}

	/**
	 * 公钥解密
	 * 
	 * @param key 密钥
	 * @param dest 密文
	 * @return 原文
	 * @throws Exception
	 */
	public byte[] decryptByPublicKey(byte[] dest) throws Exception {
		return pubDecryptCipher.doFinal(dest);
	}
	
	/**
	 * 私钥加密
	 * 
	 * @param key 密钥
	 * @param source 原文
	 * @return 密文
	 * @throws Exception
	 */
	public byte[] encryptByPrivateKey(byte[] source) throws Exception {
		if(priEncryptCipher == null) {
			throw new Exception("私钥不存在,加密失败");
		}
		return priEncryptCipher.doFinal(source);
	}

	/**
	 * 私钥解密
	 * 
	 * @param key 密钥
	 * @param dest 密文
	 * @return 原文
	 * @throws Exception
	 */
	public byte[] decryptByPrivateKey(byte[] dest) throws Exception {
		if(priDecryptCipher == null) {
			throw new Exception("私钥不存在,解密失败");
		}
		return priDecryptCipher.doFinal(dest);
	}

	
	public String getPubModulus() {
		return pubModulus;
	}

	public String getPubExponent() {
		return pubExponent;
	}
}