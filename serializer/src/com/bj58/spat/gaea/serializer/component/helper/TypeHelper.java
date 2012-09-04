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

import com.bj58.spat.gaea.serializer.component.TypeMap;
import com.bj58.spat.gaea.serializer.component.exception.DisallowedSerializeException;

/**
 * TypeHelper
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class TypeHelper {

    public static void InitTypeMap() {
        TypeMap.InitTypeMap();
    }

    public static int GetTypeId(Class type) throws DisallowedSerializeException {
        return TypeMap.getTypeId(type);
    }

    public static Class GetType(int typeId) {
        return TypeMap.getClass(typeId);
    }

    public static boolean IsPrimitive(Class type) {
        if (type.isPrimitive()) {
            return true;
        } else if (type == Long.class || type == long.class) {
            return true;
        } else if (type == Integer.class || type == int.class) {
            return true;
        } else if (type == Byte.class || type == byte.class) {
            return true;
        } else if (type == Short.class || type == short.class) {
            return true;
        } else if (type == Character.class || type == char.class) {
            return true;
        } else if (type == Double.class || type == double.class) {
            return true;
        } else if (type == Float.class || type == float.class) {
            return true;
        } else if (type == Boolean.class || type == boolean.class) {
            return true;
        }
        return false;
    }
}
