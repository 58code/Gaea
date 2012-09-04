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
package com.bj58.spat.gaea.protocol.sdp;

import com.bj58.spat.gaea.serializer.component.annotation.GaeaMember;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;

/**
 * ResponseProtocol
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
@GaeaSerializable(name="ResponseProtocol")
public class ResponseProtocol {

    @GaeaMember
    private Object result;
    @GaeaMember
    private Object[] outpara;
    
    public ResponseProtocol() {
    	
    }

    public ResponseProtocol(Object result, Object[] outpara) {
		super();
		this.result = result;
		this.outpara = outpara;
	}
    
	public Object[] getOutpara() {
        return outpara;
    }

    public void setOutpara(Object[] outpara) {
        this.outpara = outpara;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
