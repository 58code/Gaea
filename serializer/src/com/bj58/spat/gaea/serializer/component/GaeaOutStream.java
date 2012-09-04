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
package com.bj58.spat.gaea.serializer.component;

import com.bj58.spat.gaea.serializer.component.helper.ByteHelper;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * GaeaOutStream
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class GaeaOutStream extends ByteArrayOutputStream {

    public Charset Encoder = Charset.forName("UTF-8");
    private Map<Integer, Object> _RefPool = new HashMap<Integer, Object>();

    public boolean WriteRef(Object obj) throws IOException {
        if (obj == null) {
            this.WriteByte((byte) 1);
            this.WriteInt32((int) 0);
            return true;
        }
        int objHashcode = getHashCode(obj);
        if (_RefPool.containsKey(objHashcode)) {
            WriteByte((byte) 1);
            WriteInt32(objHashcode);
            return true;
        } else {
            _RefPool.put(objHashcode, obj);
            WriteByte((byte) 0);
            WriteInt32(objHashcode);
            return false;
        }
    }

    public void WriteByte(byte value) throws IOException {
        this.write(new byte[]{value});
    }

    public void WriteInt16(short value) throws IOException {
        byte[] buffer = ByteHelper.GetBytesFromInt16(value);
        this.write(buffer);
    }

    public void WriteInt32(int value) throws IOException {
        byte[] buffer = ByteHelper.GetBytesFromInt32(value);
        this.write(buffer);
    }

    public void WriteInt64(long value) throws IOException {
        byte[] buffer = ByteHelper.GetBytesFromInt64(value);
        this.write(buffer);
    }
    
    private int hashCode = 1000;
    private Map<Object, Integer> _objMap = new HashMap<Object, Integer>();
    
    private int getHashCode(Object obj) {
    	if (obj == null) {
            return 0;
        }
        if (_objMap.containsKey(obj) && obj == _objMap.get(obj)) {
        	return _objMap.get(obj);
        } else {
        	_objMap.put(obj, ++hashCode);
            return hashCode;
        }
    }
}
