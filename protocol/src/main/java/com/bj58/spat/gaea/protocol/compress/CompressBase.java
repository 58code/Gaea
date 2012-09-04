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
package com.bj58.spat.gaea.protocol.compress;

import com.bj58.spat.gaea.protocol.sfp.enumeration.CompressType;

/**
 * CompressBase
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class CompressBase {

	private static CompressBase sevenZip = new SevenZip();
	private static CompressBase unCompress = new UnCompress();
	
    public static CompressBase getInstance(CompressType ct) throws Exception {
        if (ct == CompressType.UnCompress) {
            return unCompress;
        } else if(ct == CompressType.SevenZip){
            return sevenZip;
        }
        
        throw new Exception("末知的压缩格式");
    }
	
	public abstract byte[] unzip(byte[] buffer) throws Exception;

	public abstract byte[] zip(byte[] buffer) throws Exception;
}
