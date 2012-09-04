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
package com.bj58.spat.gaea.server.core.convert;

import com.bj58.spat.gaea.protocol.sfp.enumeration.SerializeType;
import com.bj58.spat.gaea.protocol.sfp.v1.Protocol;
import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;

/**
 * A convert facotry for create converter
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ConvertFacotry {
	
	/**
	 * json
	 */
	private static JsonConvert jsonConvert = new JsonConvert();
	
	/**
	 * java
	 */
	private static JavaConvert javaConvert = new JavaConvert();
	
	/**
	 * GaeaBinary
	 */
	private static GaeaBinaryConvert gaeaBinaryConvert = new GaeaBinaryConvert();
	
	private static ILog logger = LogFactory.getLogger(ConvertFacotry.class);
	

	public static IConvert getConvert(Protocol p) {
		if(p.getSerializeType() == SerializeType.GAEABinary) {
			return gaeaBinaryConvert;
		} else if(p.getSerializeType() == SerializeType.JAVABinary) {
			return javaConvert;
		} else if(p.getSerializeType() == SerializeType.JSON) {
			return jsonConvert;
		}
		
		logger.error("can't get IConvert not : json ,java, customBinary ");
		return null;
	}
}