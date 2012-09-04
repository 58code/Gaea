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

import java.util.Map;
import java.util.HashMap;
import java.math.BigInteger;
import java.security.Key;
import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSACoderHelper
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class RSACoderHelper {
	private static final String KEY_ALGORITHM = "RSA";
	private static final String PUBLIC_KEY = "RSAPublicKey";
	private static final String PRIVATE_KEY = "RSAPrivateKey";
	private static final String CHAR_ENCODING = "utf-8";
	/*RSA密钥长度 必须为64的倍数 默认为1024*/
	private static int KEY_SIZE = 1024;
	
	/**
	 * 获取实例
	 * @param 密钥长度(默认为1024)
	 * @return
	 * @throws Exception
	 */
	public static RSACoderHelper getInstance(){
		return new RSACoderHelper();
	}
	
	/**
	 * 密钥初始化(系统自动生成)
	 * @return 密钥对
	 * @throws Exception
	 */
	public Map<String,Object> initKey()throws Exception{
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(KEY_SIZE);
		KeyPair keypair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey)keypair.getPublic();//公钥
		RSAPrivateKey privateKey = (RSAPrivateKey)keypair.getPrivate(); //私钥
		Map<String,Object> keyMap = new HashMap<String,Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}
	
	/**
	 * 获取公钥
	 * @param map 密钥对Map
	 * @return 公钥byte[]
	 */
	public byte[] getPublicKey(Map<String,Object> map){
		Key key = (Key)map.get(PUBLIC_KEY);
		return key.getEncoded();
	}
	
	/**
	 * 获取公钥
	 * @param map 密钥对Map
	 * @return String 公钥
	 */
	public String getStringPublicKey(Map<String,Object> map){
		return Base64.encodeBase64String(getPublicKey(map));
	}
	
	/**
	 * 获取私钥
	 * @param map 密钥对Map
	 * @return 私钥byte[]
	 */
	public byte[] getPrivateKey(Map<String,Object> map){
		Key key = (Key)map.get(PRIVATE_KEY);
		return key.getEncoded();
	}
	
	/**
	 * 获取私钥
	 * @param map 密钥对Map
	 * @return String 私钥
	 */
	public String getStringPrivateKey(Map<String,Object> map){
		return Base64.encodeBase64String(getPrivateKey(map));
	}
	
	/**
	 * 私钥加密
	 * @param data 原文byte[]
	 * @param key 密钥byte[]
	 * @return 密文
	 * @throws Exception
	 */
	private byte[] encryptByPrivateKey(byte[] data,byte[] key) throws Exception{
		PKCS8EncodedKeySpec pkcs8ks = new PKCS8EncodedKeySpec(key);
		KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey pk = kf.generatePrivate(pkcs8ks);
		Cipher cipher = Cipher.getInstance(kf.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, pk);
		return cipher.doFinal(data);
	}
	
	/**
	 * 私钥加密
	 * @param data 原文
	 * @param key 密钥
	 * @return 密文
	 * @throws Exception
	 */
	public String encryptByPrivateKeyString(String data,String key) throws Exception{
		return Base64.encodeBase64String(encryptByPrivateKey(data.getBytes(CHAR_ENCODING),Base64.decodeBase64(key)));
	}
	
	/**
	 * 私钥解密
	 * @param data 密文byte[]
	 * @param key 密钥byte[]
	 * @return 原文byte[]
	 * @throws Exception
	 */
	public byte[] decryptByPrivateKey(byte[] data,byte[] key) throws Exception{
		PKCS8EncodedKeySpec pkcs8ks = new PKCS8EncodedKeySpec(key);
		KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM);
		PrivateKey pk = kf.generatePrivate(pkcs8ks);
		Cipher cipher = Cipher.getInstance(kf.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, pk);
		return cipher.doFinal(data);
	}
	
	/**
	 * 私钥解密
	 * @param data 密文
	 * @param key 密钥
	 * @return 原文
	 * @throws Exception
	 */
	public String decryptByPrivateKey(String data,String key) throws Exception{
		return new String(decryptByPrivateKey(Base64.decodeBase64(data),Base64.decodeBase64(key)));
	}
	
	/**
	 * 私钥解密
	 * @param data 密文
	 * @param key 密钥
	 * @return 原文
	 * @throws Exception
	 */
	public byte[] decryptByPrivateKeyByte(String data,String key) throws Exception{
		return decryptByPrivateKey(Base64.decodeBase64(data),Base64.decodeBase64(key));
	}
	
	/**
	 * 公钥解密
	 * @param data 密文byte[]
	 * @param key 密钥byte[]
	 * @return 原文byte[]
	 * @throws Exception
	 */
	private byte[] decryptByPublicKey(byte[] data,byte[] key) throws Exception{
		X509EncodedKeySpec x509ks = new X509EncodedKeySpec(key);
		KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey pk = kf.generatePublic(x509ks);
		Cipher cipher = Cipher.getInstance(kf.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, pk);
		return cipher.doFinal(data);
	}
	
	/**
	 * 公钥解密
	 * @param data 密文
	 * @param key 密钥
	 * @return 原文
	 * @throws Exception
	 */
	public String decryptByPublicKey(String data,String key) throws Exception{
		return new String(decryptByPublicKey(Base64.decodeBase64(data),Base64.decodeBase64(key)));
	}
	
	/**
	 * 公钥加密
	 * @param data 原文byte[]
	 * @param key 密钥byte[]
	 * @return 密文byte[]
	 * @throws Exception
	 */
	public byte[] encryptByPublicKey(byte[] data,byte[] key) throws Exception{
		X509EncodedKeySpec x509ks = new X509EncodedKeySpec(key);
		KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM);
		PublicKey pk = kf.generatePublic(x509ks);
		Cipher cipher = Cipher.getInstance(kf.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, pk);
		return cipher.doFinal(data);
	}
	
	/**
	 * 公钥加密
	 * @param data 原文byte[]
	 * @param key 密钥byte[]
	 * @return 密文
	 * @throws Exception
	 */
	public String encryptByPublicKeyString(byte[] data,byte[] key) throws Exception{
		return Base64.encodeBase64String(encryptByPublicKey(data,key));
	} 
	
	/**
	 * 公钥加密
	 * @param data 原文
	 * @param key 密钥
	 * @return 密文
	 * @throws Exception
	 */
	public String encryptByPublicKeyString(String data,String key) throws Exception{
		return encryptByPublicKeyString(data.getBytes(CHAR_ENCODING),Base64.decodeBase64(key));
	}
	
	/**
	 * 加密(java与c#互通)
	 * java通过c#传来的公钥进行加密
	 * @param data 原文
	 * @param module 模块
	 * @param exponent 指数
	 * @return 密文
	 * @throws Exception
	 */
	public String encryptNet(String data,String module,String exponent) throws Exception {
		return Base64.encodeBase64String(encryptNet(data.getBytes(CHAR_ENCODING), Base64.decodeBase64(module), Base64.decodeBase64(exponent)));
	}
	
	/**
	 * 加密(java与c#互通)
	 * java通过c#传来的公钥进行加密
	 * @param data 原文byte[]
	 * @param module 模块byte[]
	 * @param exponent 指数byte[]
	 * @return 密文byte[]
	 * @throws Exception
	 */
    private byte[] encryptNet(byte[] data,byte[] module,byte[] exponent) throws Exception {
        BigInteger modulus = new BigInteger(1, module);  
        BigInteger exponents = new BigInteger(1, exponent);  

        RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, exponents);
        KeyFactory fact = KeyFactory.getInstance(KEY_ALGORITHM);  
        PublicKey pubKey = fact.generatePublic(rsaPubKey);  

        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);  
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return cipher.doFinal(data); 
    }  
    
    /**
     * 解密(java与c#互通)
     * java通过c#传来的私钥进行解密
     * @param data 密文
     * @param delement 专用指数
     * @param module 模块
     * @return 原文
     * @throws Exception
     */
    public String dencryptNet(String data,String delement,String module) throws Exception{ 
        return new String(dencryptNet(Base64.decodeBase64(data), Base64.decodeBase64(delement), Base64.decodeBase64((module))));  
    }
    
    /**
     * 解密(java与c#互通)
     * java通过c#传来私钥进行解密
     * @param data 密文byte[]
     * @param delement 专用指数
     * @param module 模块byte[]
     * @return 原文byte[]
     * @throws Exception
     */
    public byte[] dencryptNet(byte[] data,byte[] delement,byte[] module) throws Exception {  
    	BigInteger modules = new BigInteger(1, module);  
        BigInteger d = new BigInteger(1, delement);  

        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);  
        Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);  

        RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(modules, d);  
        PrivateKey privKey = factory.generatePrivate(privSpec);  
        cipher.init(Cipher.DECRYPT_MODE, privKey);
        return cipher.doFinal(data); 
    }
	
    /**
	 * java 私钥转换为c#私钥
	 * @param encodedPrivkey java私钥
	 * @return
	 */
    public String getRSAPrivateKeyAsNetFormat(String encodedPrivatekey){
    	return getRSAPrivateKeyAsNetFormat(Base64.decodeBase64(encodedPrivatekey));
    }
    
    /**
	 * java 私钥转换为c#私钥
	 * @param key java私钥
	 * @return
	 */
	private String getRSAPrivateKeyAsNetFormat(byte[] key) {
		try {
	    	StringBuffer buff = new StringBuffer(1024);
	    	RSAPrivateCrtKey pvkKey = getRSAPrivateCrtKey(key);
	        buff.append("<RSAKeyValue>");
	        buff.append("<Modulus>"+ b64encode(removeMSZero(pvkKey.getModulus().toByteArray()))+ "</Modulus>");
	        buff.append("<Exponent>"+ b64encode(removeMSZero(pvkKey.getPublicExponent().toByteArray())) + "</Exponent>");//公用指数
	        buff.append("<P>"+ b64encode(removeMSZero(pvkKey.getPrimeP().toByteArray()))+ "</P>");
	        buff.append("<Q>"+ b64encode(removeMSZero(pvkKey.getPrimeQ().toByteArray()))+ "</Q>");
	        buff.append("<DP>"+ b64encode(removeMSZero(pvkKey.getPrimeExponentP().toByteArray())) + "</DP>");
	        buff.append("<DQ>"+ b64encode(removeMSZero(pvkKey.getPrimeExponentQ().toByteArray())) + "</DQ>");
	        buff.append("<InverseQ>"+ b64encode(removeMSZero(pvkKey.getCrtCoefficient().toByteArray())) + "</InverseQ>");
	        buff.append("<D>"+ b64encode(removeMSZero(pvkKey.getPrivateExponent().toByteArray())) + "</D>");
	        buff.append("</RSAKeyValue>");
	        return buff.toString().replaceAll("[\t\n\r]", "");
	    } catch (Exception e) {
	        System.err.println(e);
	        return null;
	    }
	}
	
	/**
	 * java 私钥转换为c#公钥
	 * @param encodedPrivatekey java私钥
	 * @return
	 */
	public String getRSAPublicKeyAsNetFormat(String encodedPrivatekey){
		return getRSAPublicKeyAsNetFormat(Base64.decodeBase64(encodedPrivatekey));
	}
	
	/**
	 * java私钥转换为c#公钥Modulus系数
	 * @param key java端生成的私钥
	 * @return
	 * @throws Exception 
	 */
	private String getRSAPublicKeyModulus(String key) throws Exception{
		return b64encode(removeMSZero(getRSAPrivateCrtKey(Base64.decodeBase64(key)).getModulus().toByteArray()));
	}
	
	/**
	 * java私钥转换为c#公钥Exponent系数
	 * @param key java端生成的私钥
	 * @return
	 * @throws Exception 
	 */
	private String getRSAPublicKeyExponent(String key) throws Exception{
		return b64encode(removeMSZero(getRSAPrivateCrtKey(Base64.decodeBase64(key)).getPublicExponent().toByteArray()));
	}
	
	/**
	 * 获取私钥 RSAPrivateCrtKey
	 * @param key私钥
	 * @return RSAPrivateCrtKey
	 * @throws Exception
	 */
	public RSAPrivateCrtKey getRSAPrivateCrtKey(byte [] key) throws Exception{
		PKCS8EncodedKeySpec pvkKeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		RSAPrivateCrtKey pvkKey = (RSAPrivateCrtKey) keyFactory.generatePrivate(pvkKeySpec);
		return pvkKey;
	}
	
	/**
	 * java 私钥转换为c#公钥
	 * @param key java私钥byte[]
	 * @return
	 */
	private String getRSAPublicKeyAsNetFormat(byte[] key) {
		try {
			StringBuffer buff = new StringBuffer(1024);
			RSAPrivateCrtKey pvkKey = getRSAPrivateCrtKey(key);
			buff.append("<RSAKeyValue>");
			buff.append("<Modulus>"+ b64encode(removeMSZero(pvkKey.getModulus().toByteArray()))+ "</Modulus>");
			buff.append("<Exponent>"+ b64encode(removeMSZero(pvkKey.getPublicExponent().toByteArray())) + "</Exponent>");
			buff.append("</RSAKeyValue>");
			return buff.toString().replaceAll("[ \t\n\r]", "");
		} catch (Exception e) {
			System.err.println(e);
			return null;
		}
	}
    
	/**
	 * Base64 加密
	 * @param data 原文byte[]
	 * @return 密文
	 */
	private String b64encode(byte[] data) {
		return Base64.encodeBase64String(data);
	}
	
	private byte[] removeMSZero(byte[] data) {
		byte[] dataNew;
	    int len = data.length;
	    if (data[0] == 0) {
	    	dataNew = new byte[data.length - 1];
	        System.arraycopy(data, 1, dataNew, 0, len - 1);
	    } else{
	    	dataNew = data;
	    }
	    return dataNew;
	}
    
	/**main test*/
	public static void main(String []args) throws Exception{
		RSACoderHelper rsa = RSACoderHelper.getInstance();
		//java公钥加密 私钥解密 S--------------------------------------------------------------------------
//		String inputstr = "58 同城一个神奇的网站 oh good!";
//		System.out.println("原文为:"+inputstr);
//		byte []istr = inputstr.getBytes(CHAR_ENCODING);
//		Map map = rsa.initKey();
//		String publickey = rsa.getStringPublicKey(map);
//		System.out.println("公钥为:"+publickey);
//		String privatekey = rsa.getStringPrivateKey(map);
//		System.out.println("私钥为:"+privatekey);
//		String miwen = rsa.encryptByPublicKeyString(inputstr, publickey);
//		System.out.println("密文为:"+miwen);
//		String yuanwen = rsa.decryptByPrivateKey(miwen, privatekey);
//		System.out.println("原文为:"+yuanwen);
		//java公钥加密 私钥解密 E--------------------------------------------------------------------------
		
		//java私钥加密 公钥解密 S--------------------------------------------------------------------------
//		String inputstr = "58 同城一个神奇的网站 oh good!";
//		System.out.println("原文为:"+inputstr);
//		byte []istr = inputstr.getBytes(CHAR_ENCODING);
//		Map map = initKey();
//		String publickey = getStringPublicKey(map);
//		System.out.println("公钥为:"+publickey);
//		String privatekey = getStringPrivateKey(map);
//		System.out.println("私钥为:"+privatekey);
//		String miwen = encryptByPrivateKeyString(inputstr, privatekey);
//		System.out.println("密文为:"+miwen);
//		String yuanwen = decryptByPublicKey(miwen, publickey);
//		System.out.println("原文为:"+yuanwen);
		//java私钥加密 公钥解密 E--------------------------------------------------------------------------
		
//		String inputstr = "58 同城一个神奇的网站 oh good!";
//		System.out.println("原文为:"+inputstr);
//		byte []istr = inputstr.getBytes(CHAR_ENCODING);
//		Map map = initKey();
//		String publickey = getStringPublicKey(map);
//		System.out.println("公钥为:"+publickey);
//		String privatekey = getStringPrivateKey(map);
//		System.out.println("私钥为:"+privatekey);
//		String miwen = encryptByPublicKeyString(inputstr, publickey);
//		System.out.println("密文为:"+miwen);
//		String yuanwen = decryptByPrivateKey(miwen, privatekey);
//		System.out.println("解密后-原文为:"+yuanwen);
//		
//		System.out.println("net私钥为");
//		System.out.println(getRSAPrivateKeyAsNetFormat(privatekey));
//		System.out.println("net公钥为");
//		System.out.println(getRSAPublicKeyAsNetFormat(privatekey));
		
		//-----------------------c#公钥加密 java私钥解密 S(c#端公钥由java生成后传送给c#端)------------------------------------------------------------
		//java端生成公钥传送给c#端,c#端根据该公钥进行加密,密文传送给java端,java端解密恢复原文
		//加密后密文为:NU9hzvaZiceKLIjb6PDMJ7Zt91+90HuWMKOljQb8kubUms+S59pJSbTEuHltEEBOrfrueChm03v6CjwYn8Ye3zXdmcoOLhEHcUwD5AB3aqzcHkBPp2UbGt/HYMkp+f4XfN0wl7tnzfM2jBguwqS5xrYGRHX27vrrVnjhUJo+2lg=
		//java解密
		//Map map = initKey();
		//String publickey = getStringPublicKey(map);
		//System.out.println("公钥为:"+publickey);
		//String privatekey = getStringPrivateKey(map);
		//System.out.println("私钥为:"+privatekey);
		//net加密公钥(该公钥传送给net端)
		//String publicKeyNet = getRSAPublicKeyAsNetFormat(privatekey);
		//net 用公钥加密、java用私钥解密
		//decryptByPrivateKey(data,key) data为net加密后的密文,key为java解密私钥(privatekey)
		//String yuanwen2 = decryptByPrivateKey("plOziOzcv/s1DPEMxrvwnGm6bnPKvdtj/WWJsCgbIL8h934BaCPidxY7Va2W2bsACt4MwJXwj1fRxf7HB1iR6Qzynl8O/vzKeSEX7ROqc/p1He+4E8+7xqltBEpSSUoMS9i6u9HUbKVWVKsmkn8cdjaFIC08NH/dagOfj8b5qfE=", "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBANRD/Ws2GWSBaNvWMnF+RCDobBVitsRaN9w91OjWi1Duyhz4asDBgTiKndz14kr6zMKRWhFnTBEKoNtsdBuToFjCMG5unBlyrbhXKTHIeBtty2KP3jAISrF46omKcdfGC9vgKn59j1gdOV0BMJ+KcP6ARmH76MLGnTyEz08YDj3jAgMBAAECgYB9Ad2I+j9SFXRvo2HpvPbaeobMwWnpkUg8iJ2O0msRqs+U6CfWhR9Y8SmvK4+toK5eF8P3Y+JEVwIMt35Zj0Nqb3BE0/WiMYUkSpDYTMPCR5Dy66G32pr3Ztmsrsmj8RP8YgovseSIdsM1rcFhmM/7ykJ5LP6Z0Sit23FIgtj4QQJBAPSKwc98GlPmcSsBFYZ8Q6WgWC2y3cYqUj3rX3EhdMx1FMOxGXupRAPMNgtldxVT8AuvY1Jo962ulIqwLBUnbGkCQQDeNhUFQQOA5ThDjLlx2IJ2Pqg9lBHxxWjZaF0rIKpTc2blPuG0KSJFjM0Q3Jfmk8DWLuqbpr/FKBOIrgIBbr5rAkBPTH+0zMWs11lzXJKiXcGCABEYIJySGj85HQ6CHRtHAeCN5Owika0gniQYZfrLy8BXybQnkSpgA/DfWGmzImmhAkAvwcXQxEFBgYIPGn58lkCrsxmralhU8s1u9DwZ2cJhfwrdOB9//dsswW8NMau/1X97SMgJPdQ3Rj60gFlT/34TAkBFTv5RjfRccSs5PzKQtpx8tswvS9tjy7ep8a7JA//yEWlstqaZFBsGa8YNpwwsgGUSjXrJNIepwEVSU/NJjr0u");
		//System.out.println("解密后-原文为:"+yuanwen2);
		//-----------------------c#公钥加密 java私钥解密 End------------------------------------------------------------
		
		
		//---------------------java公钥加密c#私钥解密(java端公钥由c#端生成后传送到java端)-----------------------------
		//System.out.println(encryptNet("this is a good web !@#$%^&*()", "uTR2TBde0ceScVVHYqlwDA8QEKHDS6e1JmNpciqiFOgtWIxPcUGPS/qfkNOw/Z+yqWKPrRLLsifiz1qf9Myz5WIqxSCSAzzr6g3HPZFM8bCnFKJbenv4g5OSbCy1/2txmngIOYNI7zzHuTSkzba56HFDbnd4mqVTMgwRTEAgZ3E=", "AQAB"));
		//System.out.println(dencryptNet("jb4EqNA6ErZkAWmVyXbKZ+weLfMpmzeeFmt3HklBaPJ5vO/Geieq9ghgxbdfHfmGih9o1iuH61gAFkbIUi2GjZsirNAuJtO+TN4vf9XnDmeU3Kk1AW0/qJccaQzRykQCXdKzPyigBmkp+a7TfBqW6ST6gTpE9Bz3bOH0KTwaeBM=","PKAUvVHFIyyOHnVI+d33JKtlmdwp/sD2RouyP8H+7pMWDO9ZLfzE+J0g0JrLTCqTpGj0GTaW4Um3MCM9SzgbBsKlIEFT5qGq3hfamdhBOQGziEDuF5k3oYeRWbXeWIvzmwMMADg+jrNz2UXQ/eAefdvD6iWfozMcxwEOmnkTtuU=","uTR2TBde0ceScVVHYqlwDA8QEKHDS6e1JmNpciqiFOgtWIxPcUGPS/qfkNOw/Z+yqWKPrRLLsifiz1qf9Myz5WIqxSCSAzzr6g3HPZFM8bCnFKJbenv4g5OSbCy1/2txmngIOYNI7zzHuTSkzba56HFDbnd4mqVTMgwRTEAgZ3E="));
	}
}
