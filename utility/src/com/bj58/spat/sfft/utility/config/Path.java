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

import java.io.File;

public class Path {
	
	/**
	 * 获取当前jar包所在目录 / 程序bin所在目录
	 * @return
	 */
	public static String getCurrentPath() {
		String path = System.getProperty("serviceframe.config.path");
		System.out.println("serviceframe.config.path:" + path);

		if (path == null || path.equalsIgnoreCase("")) {
			Class<?> caller = getCaller();
			if (caller == null) {
				caller = Path.class;
			}
			path = getCurrentPath(caller);
		}

		System.out.println("utility path getCurrentPath:" + path);
		return path;
	}


	public static Class<?> getCaller() {
		StackTraceElement stack[] = (new Throwable()).getStackTrace();
		System.out.println("stack length:"+stack.length);
		if(stack.length < 3) {
			return Path.class;
		}
		String className = stack[2].getClassName();
		System.out.println("getCaller class name:" + className);
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取当前class父目录
	 * @param cls
	 * @return 当前class父目录 URL
	 */
	public static String getCurrentPath(Class<?> cls) {
		String path = cls.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = path.replaceFirst("file:/", "");
		path = path.replaceAll("!/", "");
		if(path.lastIndexOf(File.separator) >= 0){
			path = path.substring(0, path.lastIndexOf(File.separator));
		}
		if(path.substring(0,1).equalsIgnoreCase("/")){
			String osName = System.getProperty("os.name").toLowerCase();
			if(osName.indexOf("window") >= 0){
				path = path.substring(1);
			}
		}
		return path;
	}
}