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
package com.bj58.spat.gaea.client.utility.helper;

/**
 * ArrayHelper
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ArrayHelper {

    public static boolean equals(byte[] array1, byte[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(byte[] array1, int offset, byte[] array2) {
        if (array1 == array2 && offset == 0) {
            return true;
        }
        if (array1.length - offset < array2.length) {
            return false;
        }
        for (int i = 0; i < array2.length; i++) {
            if (array1[i + offset] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static byte[] subArray(byte[] source, int start, int len) {
        if (start < 0) {
            start = 0;
        }
        if (len < 0 || len > source.length) {
            return null;
        }
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = source[start + i];
        }
        return result;
    }

    public static void leftMove(byte[] array, int count) {
        for (int i = 0; i < array.length; i++) {
            int target = i - count;
            if (target < 0) {
                continue;
            }
            array[target] = array[i];
            array[i] = 0;
        }
    }
}
