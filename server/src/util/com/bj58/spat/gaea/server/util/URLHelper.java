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
package com.bj58.spat.gaea.server.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class URLHelper {
	
	/**
	 * url decode
	 * @param url
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(String url, String encoding) throws UnsupportedEncodingException{
		if(url != null && !url.equals("")) {
			return URLDecoder.decode(url, encoding);
		}
		return "";
	}
	
	/**
	 * url decode
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(String url) throws UnsupportedEncodingException{
		return decode(url, "utf-8");
	}
	
	/**
	 * get url without para
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getOnlyUrl(String url) throws UnsupportedEncodingException{
		url = URLDecoder.decode(url, "utf-8");
		return url.split("\\?")[0];
	}
	
	/**
	 * get paras
	 * @param url
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String,String> getParas(String url) throws UnsupportedEncodingException {
		url = URLDecoder.decode(url, "utf-8");
		String[] urlAry = url.split("\\?"); 
		Map<String, String> mapParas = new HashMap<String, String>();
		if(urlAry.length > 1) {
			for(int i=1; i<urlAry.length; i++) {
				String[] paras = urlAry[i].split("&");
				for(String para : paras) {
					String[] kv = para.split("=");
					if(kv.length == 2) {
						mapParas.put(kv[0], kv[1]);
					}
				}
			}
		}
		return mapParas;
	}
}