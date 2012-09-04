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
package com.bj58.spat.gaea.server.util;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import com.bj58.spat.gaea.server.deploy.hotdeploy.DynamicClassLoader;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ClassHelper {

	/**
	 * get all class from jar
	 * @param jarPath
	 * @param classLoader
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    public static Set<Class<?>> getClassFromJar(String jarPath, 
    											DynamicClassLoader classLoader) throws IOException, ClassNotFoundException {
        JarFile jarFile = new JarFile(jarPath);
        Enumeration<JarEntry> entries = jarFile.entries();
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String name = jarEntry.getName();
            if (name.endsWith(".class")) {
                String className = name.replaceAll(".class", "").replaceAll("/", ".");
                Class<?> cls = null;
                try {
//    				cls = classLoader.findClass(className);
                	cls = classLoader.loadClass(className);
                } catch (Throwable ex) {
                	
                }
                if (cls != null) {
                    classes.add(cls);
                }
            }
        }
        return classes;
    }

    /**
     * check class is implements the interface
     * @param type
     * @param interfaceType
     * @return
     */
    public static boolean interfaceOf(Class<?> type, Class<?> interfaceType) {
        if (type == null) {
            return false;
        }
        Class<?>[] interfaces = type.getInterfaces();
        for (Class<?> c : interfaces) {
            if (c.equals(interfaceType)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * get method parameter names
     * @param cls
     * @param method
     * @return
     * @throws Exception
     */
    public static String[] getParamNames(Class<?> cls, Method method) throws Exception {
    	ClassPool pool = ClassPool.getDefault();  
    	CtClass cc = pool.get(cls.getName());

    	Class<?>[] paramAry = method.getParameterTypes();
    	String[] paramTypeNames = new String[paramAry.length];
    	for(int i=0; i<paramAry.length; i++) {
    		paramTypeNames[i] = paramAry[i].getName();
    	}
    	
    	CtMethod cm = cc.getDeclaredMethod(method.getName(), pool.get(paramTypeNames));

    	MethodInfo methodInfo = cm.getMethodInfo();  
    	CodeAttribute codeAttribute = methodInfo.getCodeAttribute();  
    	LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);  
    	if (attr == null)  {
    	    throw new Exception("class:"+cls.getName()+", have no LocalVariableTable, please use javac -g:{vars} to compile the source file");
    	}
    	String[] paramNames = new String[cm.getParameterTypes().length];  
    	int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;  
    	for (int i = 0; i < paramNames.length; i++) {
    	    paramNames[i] = attr.variableName(i + pos);
    	}
    	return paramNames;
    }
    
    
    /**
     * get method parameters annotations
     * @param cls
     * @param method
     * @return
     * @throws Exception
     */
    public static Object[][] getParamAnnotations(Class<?> cls, Method method) throws Exception {
    	ClassPool pool = ClassPool.getDefault();  
    	CtClass cc = pool.get(cls.getName());

    	Class<?>[] paramAry = method.getParameterTypes();
    	String[] paramTypeNames = new String[paramAry.length];
    	for(int i=0; i<paramAry.length; i++) {
    		paramTypeNames[i] = paramAry[i].getName();
    	}
    	
    	CtMethod cm = cc.getDeclaredMethod(method.getName(), pool.get(paramTypeNames));
    	return cm.getParameterAnnotations();
    }
    
}