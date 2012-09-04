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
package com.bj58.spat.gaea.client;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bj58.spat.gaea.client.proxy.ServiceProxy;
import com.bj58.spat.gaea.serializer.serializer.Serializer;

/**
 * Init
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class GaeaInit {
	
	/**
	 * 有的老系统没有调用GaeaInit的init方法, 因此在GaeaConst中增加对GaeaInit类的引用,从而保证静态构造函数会被执行
	 */
    protected static String DEFAULT_CONFIG_PATH = null;
	
    static {
    	DEFAULT_CONFIG_PATH = System.getProperty("gaea.client.config.path");
    	if(DEFAULT_CONFIG_PATH == null) {
    		DEFAULT_CONFIG_PATH = System.getProperty("gaea.config.path");
    	}
    	if(DEFAULT_CONFIG_PATH == null) {
    		DEFAULT_CONFIG_PATH = getJarPath(GaeaConst.class) + "/gaea.config";
    	}
    	
    	registerExcetEven();
    }
    

    @Deprecated
    public static void init(String configPath, String[] jarPaths) {
        GaeaConst.CONFIG_PATH = DEFAULT_CONFIG_PATH = configPath;
        Serializer.SetJarPath(jarPaths);
    }


    public static void init(String configPath) {
        GaeaConst.CONFIG_PATH = DEFAULT_CONFIG_PATH = configPath;
    }

    private static String getJarPath(Class<?> type) {
        String path = type.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.replaceFirst("file:/", "");
        path = path.replaceAll("!/", "");
        path = path.replaceAll("\\\\", "/");
        path = path.substring(0, path.lastIndexOf("/"));
        if (path.substring(0, 1).equalsIgnoreCase("/")) {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.indexOf("window") >= 0) {
                path = path.substring(1);
            }
        }
        try {
            return URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GaeaConst.class.getName()).log(Level.SEVERE, null, ex);
            return path;
        }
    }
    
	/**
	 * when shutdown server destroyed all socket connection
	 */
	private static void registerExcetEven() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				ServiceProxy.destroyAll();
			}
		});
	}
}