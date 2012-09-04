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
import java.util.Random;
/**
 * StringUtils
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class StringUtils {
	/**
     * 使用ISO_8859_1字符集将此 String 编码为 byte序列
     * @param string 
     * @return
     */
    public static byte[] getBytesIso8859_1(String string) {
        return StringUtils.getBytesUnchecked(string, CharEncoding.ISO_8859_1);
    }
    /**
     * 使用UTF_16字符集将此 String 编码为 byte序列
     * @param string 
     * @return
     */
    public static byte[] getBytesUtf16(String string) {
        return StringUtils.getBytesUnchecked(string, CharEncoding.UTF_16);
    }
    /**
     * 使用UTF_8字符集将此 String 编码为 byte序列
     * @param string 
     * @return
     */
    public static byte[] getBytesUtf8(String string) {
        return StringUtils.getBytesUnchecked(string, CharEncoding.UTF_8);
    }
    /**
     * 使用指定的字符集将此 String 编码为 byte序列
     * @param string 
     * @param charsetName 编码格式
     * @return
     */
    public static byte[] getBytesUnchecked(String string, String charsetName) {
        if (string == null) {
            return null;
        }
        try {
            return string.getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            throw StringUtils.newIllegalStateException(charsetName, e);
        }
    }

    private static IllegalStateException newIllegalStateException(String charsetName, UnsupportedEncodingException e) {
        return new IllegalStateException(charsetName + ": " + e);
    }
    
    /**
     * 通过使用指定的 charset 解码指定的 byte 数组，构造一个新的 String
     * @param bytes
     * @param charsetName
     * @return
     */
    public static String newString(byte[] bytes, String charsetName) {
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw StringUtils.newIllegalStateException(charsetName, e);
        }
    }
    /**
     * 通过ISO-8859-1 解码指定的 byte 数组,构造一个新的 String
     * @param bytes
     * @return
     */
    public static String newStringIso8859_1(byte[] bytes) {
        return StringUtils.newString(bytes, CharEncoding.ISO_8859_1);
    }
    /**
     * 通过UTF_16 解码指定的 byte 数组,构造一个新的 String
     * @param bytes
     * @return
     */
    public static String newStringUtf16(byte[] bytes) {
        return StringUtils.newString(bytes, CharEncoding.UTF_16);
    }
    /**
     * 通过utf-8 解码指定的 byte 数组,构造一个新的 String
     * @param bytes
     * @return
     */
    public static String newStringUtf8(byte[] bytes) {
        return StringUtils.newString(bytes, CharEncoding.UTF_8);
    }
    
    private static final String[] ENCODE_TABLE = {
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
        "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z",
        "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
        "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
        "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    
    /**
     * 随机返回8位长度的字符串
     * @return
     */
    public static String getRandomNumAndStr(int length){
    	StringBuffer sbuffer = new StringBuffer("");
    	Random r = new Random();
    	for(int i=0;i<length;i++){
    		sbuffer.append(ENCODE_TABLE[r.nextInt(62)]);
    	}
    	return sbuffer.toString();
    }
}
