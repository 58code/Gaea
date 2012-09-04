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
package com.bj58.spat.gaea.protocol.serializer;

import java.nio.charset.Charset;

import com.bj58.spat.gaea.protocol.sfp.enumeration.SerializeType;

/**
 * SerializeBase
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public abstract class SerializeBase {

    private static GaeaSerialize gaeaSerialize = new GaeaSerialize();
    
    private static JSONSerialize jsonSerialize = new JSONSerialize();
    
    private Charset encoder;

    public Charset getEncoder() {
        return encoder;
    }

    public void setEncoder(Charset encoder) {
        this.encoder = encoder;
    }

    public static SerializeBase getInstance(SerializeType serializeType) throws Exception {
        if (serializeType == SerializeType.GAEABinary) {
            return gaeaSerialize;
        } else if(serializeType == SerializeType.JSON){
            return jsonSerialize;
        }
        
        throw new Exception("末知的序列化算法");
    }

    
    public abstract byte[] serialize(Object obj) throws Exception;

    public abstract Object deserialize(byte[] data, Class<?> cls) throws Exception;
}