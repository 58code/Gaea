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
package com.bj58.spat.gaea.protocol.utility;

import com.bj58.spat.gaea.serializer.component.annotation.GaeaMember;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;
import com.bj58.spat.gaea.server.contract.entity.Out;
import java.io.Serializable;

/**
 * KeyValuePair
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
@SuppressWarnings("serial")
@GaeaSerializable(name="RpParameter")
public class KeyValuePair implements Serializable {

    @GaeaMember(name="name")
    private String key;

    @GaeaMember
    private Object value;
    

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        if( value instanceof Out){
            this.value = ((Out<?>)value).getOutPara();
        } else {
            this.value = value;
        }
    }

    public KeyValuePair() {
    }

    public KeyValuePair(String key, Object value) {
        this.setKey(key);
        this.setValue(value);
    }
}