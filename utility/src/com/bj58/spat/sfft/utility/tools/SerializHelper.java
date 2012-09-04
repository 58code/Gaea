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
package com.bj58.spat.sfft.utility.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializHelper {
	
	/**
	 * 将对象序列化为字符串
	 * @param obj 
	 * @param encode 编码
	 * @return
	 * @throws IOException
	 */
	public static String serializeToString(Object obj, String encode) throws IOException {
		return new String(serializeToByteArray(obj),encode);
	}
	
	/**
	 * 将对象序列化为字符串
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static String serializeToString(Object obj) throws IOException {
		return new String(serializeToByteArray(obj),"ISO-8859-1");
	}
	
	/**
	 * 将字符串反序列化为object
	 * @param str 字符串
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deSerialize(String str) throws IOException, ClassNotFoundException{
		byte[] byteFromStr = str.getBytes("ISO-8859-1");
		return deSerialize(byteFromStr);
	}
	
	/**
	 * 将字符串反序列化为object
	 * @param str 字符串
	 * @param encode 编码
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deSerialize(String str, String encode) throws IOException, ClassNotFoundException{
		byte[] byteFromStr = str.getBytes(encode);
		return deSerialize(byteFromStr);
	}
	
	/**
	 * 将对象序列化为byte[]数组
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public static byte[] serializeToByteArray(Object obj) throws IOException {
		ByteArrayOutputStream bis = null;
		ObjectOutputStream os = null;
		byte[] byteArray = null;
		try {
			bis = new ByteArrayOutputStream(1024);
			os = new ObjectOutputStream(bis);
			os.writeObject(obj);
			byteArray = bis.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return byteArray;
	}

	/**
	 * 将byte[]反序列化为对象
	 * @param byteArray byte[]
	 * @return 对象
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deSerialize(byte[] byteArray) throws IOException, ClassNotFoundException {
		ByteArrayInputStream bos = null;
		ObjectInputStream ois = null;
		Object obj = null;
		try {
			bos = new ByteArrayInputStream(byteArray);
			ois = new ObjectInputStream(bos);
			obj = ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ois != null) {
				try {
					ois.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		return obj;
	}
}