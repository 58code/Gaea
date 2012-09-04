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

import java.util.Set;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.jar.JarFile;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.io.BufferedReader;
import java.util.logging.Level;
import java.util.LinkedHashSet;
import java.util.logging.Logger;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import com.bj58.spat.gaea.serializer.component.helper.ClassHelper;
import com.bj58.spat.gaea.serializer.component.helper.StrHelper;

/**
 * ClassHelper
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ClassHelper {

    public static Set<Class> GetClassFromJar(String jarPath) throws IOException, ClassNotFoundException {
        JarFile jarFile = new JarFile(jarPath);
        return GetClassFromJar(jarFile, "", "");
    }

    public static Set<Class> GetClassFromJar(String jarPath, String keyword) throws IOException, ClassNotFoundException {
        JarFile jarFile = new JarFile(jarPath);
        return GetClassFromJar(jarFile, keyword, "");
    }

    public static Set<Class> GetClassFromJar2(JarFile jarFile, String keyword, String basePakage) {
        String packageDirName = basePakage.replace('.', '/');
        Enumeration<JarEntry> entries = jarFile.entries();
        Set<Class> classes = new LinkedHashSet<Class>();
        while (entries.hasMoreElements()) {
            try {
                JarEntry jarEntry = entries.nextElement();
                String name = jarEntry.getName();
                if (name.charAt(0) == '/') {
                    name = name.substring(1);
                }
                if (!StrHelper.isEmptyOrNull(packageDirName) && !name.startsWith(packageDirName)) {
                    continue;
                }
                if (name.endsWith(".class") && !name.contains("com/bj58/spat/gaea/serializer")) {
                    if (checkJarEntry(jarFile, jarEntry, keyword)) {
                        String className = name.replaceAll(".class", StrHelper.EmptyString).replaceAll("/", ".");
                        Class type = null;
                        type = Thread.currentThread().getContextClassLoader().loadClass(className);
                        if (type != null) {
                            classes.add(type);
                        }
                    }
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
        return classes;
    }

    public static Set<Class> GetClassFromJar(JarFile jarFile, String keyword, String basePakage) throws IOException {
        Boolean recursive = true;//是否递归
        String packageName = basePakage;
        String packageDirName = basePakage.replace('.', '/');
        Enumeration<JarEntry> entries = jarFile.entries();
        Set<Class> classes = new LinkedHashSet<Class>();
        while (entries.hasMoreElements()) {
            try {
                // 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                // 如果是以/开头的
                if (name.charAt(0) == '/') {
                    // 获取后面的字符串
                    name = name.substring(1);
                }
                // 如果前半部分和定义的包名相同
                if (name.startsWith(packageDirName)) {
                    int idx = name.lastIndexOf('/');
                    // 如果以"/"结尾 是一个包
                    if (idx != -1) {
                        // 获取包名 把"/"替换成"."
                        packageName = name.substring(0, idx).replace('/', '.');
                    }
                    // 如果可以迭代下去 并且是一个包
                    if ((idx != -1) || recursive) {
                        // 如果是一个.class文件 而且不是目录
                        if (name.endsWith(".class")
                                && !entry.isDirectory()) {
                            //检测entry是否符合要求
                            if (!ClassHelper.checkJarEntry(jarFile, entry, keyword)) {
                                continue;
                            }
                            // 去掉后面的".class" 获取真正的类名
                            String className = name.substring(
                                    packageName.length() + 1, name.length() - 6);
                            try {
                                // 添加到classes
                                Class c = Thread.currentThread().getContextClassLoader().loadClass(packageName + "." + className);
                                classes.add(c);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (NoClassDefFoundError e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    public static boolean checkJarEntry(JarFile jarFile, JarEntry entry, String keyWord) throws IOException {
        if (keyWord == null || keyWord.equals("")) {
            return true;
        }
        InputStream input = null;
        InputStreamReader isr = null;
        BufferedReader reader = null;

        try {
            input = jarFile.getInputStream(entry);
            isr = new InputStreamReader(input);
            reader = new BufferedReader(isr);
            StringBuffer sb = new StringBuffer();
            boolean result = false;
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
                if (sb.indexOf(keyWord) > -1) {
                    result = true;
                }
            }
            return result;
        } finally {
            if (input != null) {
                input.close();
            }
            if (isr != null) {
                isr.close();
            }
            if (reader != null) {
                reader.close();
            }
        }
    }

    public static boolean InterfaceOf(Class type, Class interfaceType) {
        if (type == null) {
            return false;
        }
        Class[] interfaces = type.getInterfaces();
        for (Class c : interfaces) {
            if (c.equals(interfaceType)) {
                return true;
            }
        }
        return false;
    }

    public static Class<?> GetClassForName(String name) throws ClassNotFoundException {
        if (name.equals("boolean")) {
            return Boolean.class;
        } else if (name.equals("char")) {
            return Character.class;
        } else if (name.equals("byte")) {
            return Byte.class;
        } else if (name.equals("short")) {
            return Short.class;
        } else if (name.equals("int")) {
            return Integer.class;
        } else if (name.equals("long")) {
            return Long.class;
        } else if (name.equals("float")) {
            return Float.class;
        } else if (name.equals("double")) {
            return Double.class;
        } else {
            return Class.forName(name);
        }
    }

    public static String getJarPath(Class type) {
        String path = type.getProtectionDomain().getCodeSource().getLocation().getPath();
        path = path.replaceFirst("file:/", "");
        path = path.replaceAll("!/", "");
        path = path.replaceAll("\\\\", "/");
        path = path.substring(0, path.lastIndexOf("/"));
        if (path.substring(0, 1).equalsIgnoreCase("/")) {
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.indexOf("window") >= 0) {
                path = path.substring(1);
            }
        }
        try {
            return URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClassHelper.class.getName()).log(Level.SEVERE, null, ex);
            return path;
        }
    }

    public static String getCurrJarName(Class c) {
        String filePath = c.getProtectionDomain().getCodeSource().getLocation().getFile();
        filePath = filePath.replaceFirst("file:/", "");
        filePath = filePath.replaceAll("!/", "");
        filePath = filePath.replaceAll("\\\\", "/");
        filePath = filePath.substring(filePath.lastIndexOf("/") + 1);
        return filePath;
    }
}
