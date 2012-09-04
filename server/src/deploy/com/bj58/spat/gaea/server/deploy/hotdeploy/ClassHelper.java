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
package com.bj58.spat.gaea.server.deploy.hotdeploy;

import java.io.IOException;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * A ClassHelper for get class from jar
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ClassHelper {

	public static Set<Class<?>> getClassFromJar(String jarPath, String... regex) throws IOException, ClassNotFoundException {
        JarFile jarFile = new JarFile(jarPath); // read jar file
        Enumeration<JarEntry> entries = jarFile.entries();
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            if (name.endsWith(".class")) {
                String className = name.replaceAll(".class", "").replaceAll("/", ".");
                Class<?> type = null;
                try {
                    type = Class.forName(className);
                } catch (Throwable ex) {
                	
                }
                if (type != null) {
                    classes.add(type);
                }
            }
        }
        jarFile.close();
        return classes;
    }
}