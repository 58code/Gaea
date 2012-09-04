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
package com.bj58.spat.gaea.serializer.component.helper;

import com.bj58.spat.gaea.serializer.component.exception.OutOfRangeException;

/**
 * ByteHelper
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ByteHelper {

    public static short ToInt16(byte[] buffer) throws OutOfRangeException {
        if (buffer.length < 2) {
            throw new OutOfRangeException();
        }
        short int16 = 0;
        int16 = (short) (buffer[0] & 0xff);
        int16 |= ((short) buffer[1] << 8) & 0xff00;
        return int16;
    }

    public static int ToInt32(byte[] buffer) throws OutOfRangeException {
        if (buffer.length < 4) {
            throw new OutOfRangeException();
        }
        int int32 = 0;
        int32 = buffer[0] & 0xff;
        int32 |= ((int) buffer[1] << 8) & 0xff00;
        int32 |= ((int) buffer[2] << 16) & 0xff0000;
        int32 |= ((int) buffer[3] << 24) & 0xff000000;
        return int32;
    }

    public static long ToInt64(byte[] buffer) throws OutOfRangeException {
        if (buffer.length < 8) {
            throw new OutOfRangeException();
        }
        long int64 = 0;
        int64 = buffer[0] & 0xffL;
        int64 |= ((long) buffer[1] << 8) & 0xff00L;
        int64 |= ((long) buffer[2] << 16) & 0xff0000L;
        int64 |= ((long) buffer[3] << 24) & 0xff000000L;
        int64 |= ((long) buffer[4] << 32) & 0xff00000000L;
        int64 |= ((long) buffer[5] << 40) & 0xff0000000000L;
        int64 |= ((long) buffer[6] << 48) & 0xff000000000000L;
        int64 |= ((long) buffer[7] << 56);
        return int64;
    }

    public static byte[] GetBytesFromInt16(short value) {
        byte[] buffer = new byte[2];
        buffer[0] = (byte) value;
        buffer[1] = (byte) (value >> 8);
        return buffer;
    }

    public static byte[] GetBytesFromInt32(int value) {
        byte[] buffer = new byte[4];
        for (int i = 0; i < 4; i++) {
            buffer[i] = (byte) (value >> (8 * i));
        }
        return buffer;
    }

    public static byte[] GetBytesFromInt64(long value) {
        byte[] buffer = new byte[8];
        for (int i = 0; i < 8; i++) {
            buffer[i] = (byte) (value >> (8 * i));
        }
        return buffer;
    }

    public static byte[] GetBytesFromChar(char ch) {
        int temp = (int) ch;
        byte[] b = new byte[2];
        for (int i = b.length - 1; i > -1; i--) {
        	//将最高位保存在最低位
            b[i] = new Integer(temp & 0xff).byteValue();
            temp = temp >> 8;
        }
        return b;
    }

    public static char getCharFromBytes(byte[] b) {
        int s = 0;
        if (b[0] > 0) {
            s += b[0];
        } else {
            s += 256 + b[0];
        }
        s *= 256;
        if (b[1] > 0) {
            s += b[1];
        } else {
            s += 256 + b[1];
        }
        char ch = (char) s;
        return ch;
    }
}
