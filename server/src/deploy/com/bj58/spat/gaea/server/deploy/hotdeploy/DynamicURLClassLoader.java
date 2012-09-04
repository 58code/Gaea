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
package com.bj58.spat.gaea.server.deploy.hotdeploy;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import com.bj58.spat.gaea.server.util.FileHelper;

/**
 * A URLClassLoader for dynamic load class from jar
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class DynamicURLClassLoader {

	private static Method addURL;

	static {
		try {
			addURL = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
		} catch (Exception e) {
			e.printStackTrace();
		}
		addURL.setAccessible(true);
	}
	

	private URLClassLoader classLoader;
	
	public DynamicURLClassLoader() throws MalformedURLException {
		classLoader = new URLClassLoader(new URL[]{new URL("file", "", "")});
	}
 
	

	public void addURL(URL url) throws Exception {
		addURL.invoke(classLoader, new Object[] { url });
	}
	
	public void addURL(String path) throws Exception {
		URL url = new URL("file", "", path);
		addURL(url);
	}
	
	/**
	 * add folder jars
	 * @param path
	 * @throws Exception 
	 */
	public void addFolder(String... dirs) throws Exception {
		List<String> jarList = FileHelper.getUniqueLibPath(dirs);
		for(String jar : jarList) {
			addURL(jar);
		}
	}
	
	
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		return classLoader.loadClass(className);
	}
}