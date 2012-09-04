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

import java.util.ArrayList;
import java.util.List;

/**
 * Util
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Util {
	
	/**
	 * get simple para name
	 * @param paraName
	 * @return
	 */
	public static String getSimpleParaName(String paraName) {
		paraName = paraName.replaceAll(" ",	"");
		if(paraName.indexOf(".") > 0) {
			String[] pnAry = paraName.split("");
			List<String> originalityList = new ArrayList<String>();
			List<String> replaceList = new ArrayList<String>();
			
			String tempP = "";
			for(int i=0; i<pnAry.length; i++) {
				if(pnAry[i].equalsIgnoreCase("<")) {
					originalityList.add(tempP);
					replaceList.add(tempP.substring(tempP.lastIndexOf(".") + 1));
					tempP = "";
				} else if(pnAry[i].equalsIgnoreCase(">")) {
					originalityList.add(tempP);
					replaceList.add(tempP.substring(tempP.lastIndexOf(".") + 1));
					tempP = "";
				} else if(pnAry[i].equalsIgnoreCase(",")){
					originalityList.add(tempP);
					replaceList.add(tempP.substring(tempP.lastIndexOf(".") + 1));
					tempP = "";
				} else if(i == pnAry.length - 1){
					originalityList.add(tempP);
					replaceList.add(tempP.substring(tempP.lastIndexOf(".") + 1));
					tempP = "";
				} else {
					if(!pnAry[i].equalsIgnoreCase("[") && !pnAry[i].equalsIgnoreCase("]")) {
						tempP += pnAry[i];
					}
				}
			}
			
			for(int i=0; i<replaceList.size(); i++) {
				paraName = paraName.replaceAll(originalityList.get(i), replaceList.get(i));
			}
			return paraName;
		} else {
			return paraName;
		}
	}
}
