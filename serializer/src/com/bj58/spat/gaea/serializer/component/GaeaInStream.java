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

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.nio.charset.Charset;
import java.io.ByteArrayInputStream;

import com.bj58.spat.gaea.serializer.component.exception.StreamException;
import com.bj58.spat.gaea.serializer.component.helper.ByteHelper;

/**
 * GaeaInStream
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class GaeaInStream extends ByteArrayInputStream {

    public final int MAX_DATA_LEN = 1024 * 1024 * 10;
    public Charset Encoder = Charset.forName("UTF-8");
    private Map<Integer, Object> _RefPool = new HashMap<Integer, Object>();

    public GaeaInStream(byte[] buffer) {
        super(buffer);
    }

    public GaeaInStream(byte[] buffer, int offset, int length) {
        super(buffer, offset, length);
    }

    public void SafeRead(byte[] buffer) throws StreamException, IOException {
        if (this.read(buffer) != buffer.length) {
            throw new StreamException();
        }
    }

    public Object GetRef(int hashcode) {
        if (hashcode == 0) {
            return null;
        }
        return _RefPool.get(hashcode);
    }

    public void SetRef(int hashcode, Object obj) {
        _RefPool.put(hashcode, obj);
    }

    public short ReadInt16() throws Exception {
        byte[] buffer = new byte[2];
        if (this.read(buffer) != 2) {
            throw new StreamException();
        }
        return ByteHelper.ToInt16(buffer);
    }

    public int ReadInt32() throws Exception {
        byte[] buffer = new byte[4];
        if (this.read(buffer) != 4) {
            throw new StreamException();
        }
        return ByteHelper.ToInt32(buffer);
    }

    public long ReadInt64() throws Exception {
        byte[] buffer = new byte[8];
        if (this.read(buffer) != 8) {
            throw new StreamException();
        }
        return ByteHelper.ToInt64(buffer);
    }
}
