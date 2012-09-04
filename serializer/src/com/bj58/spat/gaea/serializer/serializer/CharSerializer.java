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
import com.bj58.spat.gaea.serializer.component.helper.ByteHelper;
import com.bj58.spat.gaea.serializer.serializer.SerializerBase;

/**
 * CharSerializer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class CharSerializer extends SerializerBase {

    @Override
    public void WriteObject(Object obj, GaeaOutStream outStream) throws Exception {
        byte[] bs = ByteHelper.GetBytesFromChar((Character) obj);
        for (byte b : bs) {
            outStream.WriteByte(b);
        }
    }

    @Override
    public Object ReadObject(GaeaInStream inStream, Class defType) throws Exception {
        short data = inStream.ReadInt16();
        byte[] buffer = ByteHelper.GetBytesFromInt16(data);
        return ByteHelper.getCharFromBytes(buffer);
    }
}
