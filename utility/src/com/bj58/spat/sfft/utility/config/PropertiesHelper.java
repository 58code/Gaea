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
package com.bj58.spat.sfft.utility.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class PropertiesHelper {

	private Properties pro = null;

	public PropertiesHelper(String path) throws Exception {
		pro = loadProperty(path);
	}

	public PropertiesHelper(InputStream inputStream) {
		pro = new Properties();
		try {
			pro.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getString(String key) throws Exception {
		try {
			return pro.getProperty(key);
		} catch (Exception e) {
			throw new Exception("key:" + key);
		}
	}

	public int getInt(String key) throws Exception {
		try {
			return Integer.parseInt(pro.getProperty(key));
		} catch (Exception e) {
			throw new Exception("key:" + key);
		}
	}

	public double getDouble(String key) throws Exception {
		try {
			return Double.parseDouble(pro.getProperty(key));
		} catch (Exception e) {
			throw new Exception("key:" + key);
		}
	}

	public long getLong(String key) throws Exception {
		try {
			return Long.parseLong(pro.getProperty(key));
		} catch (Exception e) {
			throw new Exception("key:" + key);
		}
	}

	public float getFloat(String key) throws Exception {
		try {
			return Float.parseFloat(pro.getProperty(key));
		} catch (Exception e) {
			throw new Exception("key:" + key);
		}
	}
	
	public boolean getBoolean(String key) throws Exception {
		try {
			return Boolean.parseBoolean(pro.getProperty(key));
		} catch (Exception e) {
			throw new Exception("key:" + key);
		}
	}
	
	public Set<Object> getAllKey(){
		return pro.keySet();
	}
	
	public Collection<Object> getAllValue(){
		return pro.values();
	}
	
	public Map<String,Object> getAllKeyValue(){
		Map<String,Object> mapAll = new HashMap<String,Object>();
		Set<Object> keys = getAllKey();
		
		Iterator<Object> it = keys.iterator();
		while(it.hasNext()){
			String key = it.next().toString();
			mapAll.put(key, pro.get(key));
		}
		return mapAll;
	}

	private Properties loadProperty(String filePath) throws Exception {
		FileInputStream fin = null;
		Properties pro = new Properties();
		try {
			fin = new FileInputStream(filePath);
			pro.load(fin);
		} catch (IOException e) {
			throw e;
		} finally {
			if(fin != null) {
				fin.close();
			}
		}
		return pro;
	}
}