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
import java.util.Set;
import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import java.math.BigDecimal;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.net.MalformedURLException;

import com.bj58.spat.gaea.serializer.classes.DBNull;
import com.bj58.spat.gaea.serializer.classes.GKeyValuePair;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;
import com.bj58.spat.gaea.serializer.component.exception.DisallowedSerializeException;
import com.bj58.spat.gaea.serializer.component.helper.StrHelper;
import com.bj58.spat.gaea.serializer.component.ClassItem;

/**
 * TypeMap
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public final class TypeMap {

    private static Map<Class, ClassItem> TypeIdMap = new HashMap<Class, ClassItem>();
    private static Map<Integer, ClassItem> IdTypeMap = new HashMap<Integer, ClassItem>();

    public static void InitTypeMap() {
        TypeIdMap.clear();
        IdTypeMap.clear();
        ArrayList<ClassItem> ClassList = new ArrayList<ClassItem>();
        ClassList.add(new ClassItem(1, DBNull.class));
        ClassList.add(new ClassItem(2, Object.class));
        ClassList.add(new ClassItem(3, Boolean.class, boolean.class));
        ClassList.add(new ClassItem(4, Character.class, char.class));
        ClassList.add(new ClassItem(5, Byte.class, byte.class));
        ClassList.add(new ClassItem(7, Short.class, short.class));
        ClassList.add(new ClassItem(9, Integer.class, int.class));
        ClassList.add(new ClassItem(11, Long.class, long.class));
        ClassList.add(new ClassItem(13, Float.class, float.class));
        ClassList.add(new ClassItem(14, Double.class, double.class));
        ClassList.add(new ClassItem(15, BigDecimal.class));
        ClassList.add(new ClassItem(16, Date.class, java.sql.Date.class, java.sql.Time.class, java.sql.Timestamp.class));
        ClassList.add(new ClassItem(18, String.class));
        ClassList.add(new ClassItem(19, List.class));
        ClassList.add(new ClassItem(22, GKeyValuePair.class));
        ClassList.add(new ClassItem(23, Array.class));
        ClassList.add(new ClassItem(24, Map.class));
        for (ClassItem item : ClassList) {
            int id = item.getTypeId();
            Class[] types = item.getTypes();
            for (Class c : types) {
                TypeIdMap.put(c, item);
            }
            IdTypeMap.put(id, item);
        }

        String scanType = System.getProperty("gaea.serializer.scantype");
        if (scanType != null && scanType.equals("asyn")) {
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Scan jar files begin!");
                    try {
                        LoadCustmeType();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    System.out.println("Scan jar files completed!");
                }
            });
            th.start();
        } else {
            System.out.println("Scan jar files begin!");
            try {
                LoadCustmeType();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            System.out.println("Scan jar files completed!");
        }
    }

    private static void LoadCustmeType() throws URISyntaxException, IOException, MalformedURLException, ClassNotFoundException {
        ClassScaner cs = new ClassScaner();
        String basePakage = System.getProperty("gaea.serializer.basepakage");
        if (basePakage == null) {
            basePakage = StrHelper.EmptyString;
        }
        Set<Class> classes = cs.scan(basePakage.split(";"));
        for (Class c : classes) {
            System.out.println("scaning " + c.getPackage().getName() + "." + c.getName());
            try {
                GaeaSerializable ann = (GaeaSerializable) c.getAnnotation(GaeaSerializable.class);
                if (ann != null) {
                    String name = ann.name();
                    if (name.equals(StrHelper.EmptyString)) {
                        name = c.getSimpleName();
                    }
                    int typeId = StrHelper.GetHashcode(name);
                    TypeIdMap.put(c, new ClassItem(typeId, c));
                    IdTypeMap.put(typeId, new ClassItem(typeId, c));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static Class getClass(int typeId) {
        /*****************兼容之前版本***********************/
        if(typeId==6){
            typeId =5;
        }else if(typeId==20 || typeId == 21){
            typeId = 19;
        }else if(typeId == 25){
            typeId = 24;
        }
        /**************************************************/
        ClassItem ci = IdTypeMap.get(typeId);
        if (ci != null) {
            return ci.getType();
        }
        return null;
    }

    public static int getTypeId(Class type) throws DisallowedSerializeException {
        int typeId = 0;
        if (type.isArray()) {
            type = Array.class;
        } else if (Map.class.isAssignableFrom(type)) {
            type = Map.class;
        } else if (List.class.isAssignableFrom(type)) {
            type = List.class;
        }
        ClassItem ci = TypeIdMap.get(type);
        if (ci != null) {
            typeId = ci.getTypeId();
        } else {
            GaeaSerializable ann = (GaeaSerializable) type.getAnnotation(GaeaSerializable.class);
            if (ann == null) {
                throw new DisallowedSerializeException(type);
            }
            String name = ann.name();
            if (name.equals(StrHelper.EmptyString)) {
                name = type.getSimpleName();
            }
            typeId = StrHelper.GetHashcode(name);
            setTypeMap(type, typeId);
        }
        return typeId;
    }

    public static void setTypeMap(Class type, int typeId) {
        ClassItem ci = new ClassItem(typeId, type);
        TypeIdMap.put(type, ci);
        IdTypeMap.put(typeId, ci);
    }
}

class ClassItem {

    private Class[] Types;
    private int TypeId;

    public ClassItem(int typeids, Class... types) {
        Types = types;
        TypeId = typeids;
    }

    public Class getType() {
        return Types[0];
    }

    public Class[] getTypes() {
        return Types;
    }

    public int getTypeId() {
        return TypeId;
    }
}
