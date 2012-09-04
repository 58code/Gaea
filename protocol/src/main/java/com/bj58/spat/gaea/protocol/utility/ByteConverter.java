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

/**
 * ByteConverter
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ByteConverter {

    /**
     * byte array to int (little endian)
     * @param buf
     * @return
     */
    public static int bytesToIntLittleEndian(byte buf[]) {
        return buf[0] & 0xff
                | ((buf[1] << 8) & 0xff00)
                | ((buf[2] << 16) & 0xff0000)
                | ((buf[3] << 24) & 0xff000000);
    }

    /**
     * int to byte array (little endian)
     * @param n
     * @return
     */
    public static byte[] intToBytesLittleEndian(int n) {
        byte[] buf = new byte[4];
        buf[0] = (byte) (0xff & n);
        buf[1] = (byte) ((0xff00 & n) >> 8);
        buf[2] = (byte) ((0xff0000 & n) >> 16);
        buf[3] = (byte) ((0xff000000 & n) >> 24);
        return buf;
    }

    /**
     * byte array to int (big endian)
     * @param buf
     * @return
     */
    public static int bytesToIntBigEndian(byte[] buf) {
        return ((buf[0] << 24) & 0xff000000)
                | ((buf[1] << 16) & 0xff0000)
                | ((buf[2] << 8) & 0xff00)
                | (buf[3] & 0xff);
    }

    /**
     * int to byte array (big endian)
     * @param n
     * @return
     */
    public static byte[] intToBytesBigEndian(int n) {
        byte[] buf = new byte[4];
        buf[0] = (byte) ((0xff000000 & n) >> 24);
        buf[1] = (byte) ((0xff0000 & n) >> 16);
        buf[2] = (byte) ((0xff00 & n) >> 8);
        buf[3] = (byte) (0xff & n);
        return buf;
    }
    
   
    public static int bytesToIntLittleEndian(byte buf[], int offset) {
        return buf[offset] & 0xff
                | ((buf[offset + 1] << 8) & 0xff00)
                | ((buf[offset + 2] << 16) & 0xff0000)
                | ((buf[offset + 3] << 24) & 0xff000000);
    }

    public static int bytesToIntBigEndian(byte[] buf, int offset) {
        return ((buf[offset] << 24) & 0xff000000)
                | ((buf[offset + 1] << 16) & 0xff0000)
                | ((buf[offset + 2] << 8) & 0xff00)
                | (buf[offset + 3] & 0xff);
    }
}