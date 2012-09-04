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
package com.bj58.spat.gaea.serializer.serializer;

import com.bj58.spat.gaea.serializer.component.GaeaInStream;
import com.bj58.spat.gaea.serializer.component.GaeaOutStream;
import com.bj58.spat.gaea.serializer.component.exception.StreamException;
import com.bj58.spat.gaea.serializer.component.helper.ByteHelper;
import com.bj58.spat.gaea.serializer.component.helper.StrHelper;
import com.bj58.spat.gaea.serializer.serializer.SerializerBase;

import java.io.IOException;

/**
 * StringSerializer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class StringSerializer extends SerializerBase {

    @Override
    public void WriteObject(Object obj, GaeaOutStream outStream) throws IOException {
        if (outStream.WriteRef(obj)) {
            return;
        }
        byte[] buffer = obj.toString().getBytes(outStream.Encoder);
        byte[] bLen = ByteHelper.GetBytesFromInt32(buffer.length);
        byte[] bytes = new byte[buffer.length + 4];
        System.arraycopy(bLen, 0, bytes, 0, 4);
        System.arraycopy(buffer, 0, bytes, 4, buffer.length);
        outStream.write(bytes);
    }

    @Override
    public Object ReadObject(GaeaInStream inStream, Class defType) throws Exception {
        int isRef = (byte) inStream.read();
        int hashcode = inStream.ReadInt32();
        if (isRef > 0) {
            Object obj = inStream.GetRef(hashcode);
            if (obj == null) {
                return StrHelper.EmptyString;
            }
            return obj;
        }
        int len = inStream.ReadInt32();
        if (len > inStream.MAX_DATA_LEN) {
            throw new StreamException("Data length overflow.");
        }
        if (len == 0) {
            inStream.SetRef(hashcode, StrHelper.EmptyString);
            return StrHelper.EmptyString;
        }
        byte[] buffer = new byte[len];
        inStream.SafeRead(buffer);
        String str = new String(buffer, inStream.Encoder);
        inStream.SetRef(hashcode, str);
        return str;
    }
}
