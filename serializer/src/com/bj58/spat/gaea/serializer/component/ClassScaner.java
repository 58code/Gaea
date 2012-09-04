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

import java.io.File;
import java.net.URL;
import java.util.Set;
import java.util.List;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.jar.JarFile;
import java.util.Enumeration;
import java.io.BufferedReader;
import java.util.LinkedHashSet;
import java.net.URLClassLoader;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import com.bj58.spat.gaea.serializer.component.helper.ClassHelper;
import com.bj58.spat.gaea.serializer.component.helper.FileHelper;
import com.bj58.spat.gaea.serializer.component.helper.StrHelper;
import com.bj58.spat.gaea.serializer.serializer.Serializer;

/**
 * ClassScaner
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
class ClassScaner {
    private ClassLoader cl = Thread.currentThread().getContextClassLoader();
    //类文件中包含的关键字
    private final String KEY_WORD = "GaeaSerializable";

    public Set<Class> scan(String... basePackages) throws URISyntaxException, IOException, ClassNotFoundException {
        Set<Class> classes = new LinkedHashSet<Class>();
        if (basePackages != null && basePackages.length > 0 && (!StrHelper.isEmptyOrNull(basePackages[0]))) {
            for (String pack : basePackages) {
                classes.addAll(scanByPakage(pack));
            }
        } else if (Serializer.JarPath != null && Serializer.JarPath.length > 0) {
            System.err.println("指定JarPath路径扫描Jar包模式已经过时，请在启动vm参数中设置gaea.serializer.basepakage。");
            for (String path : Serializer.JarPath) {
                classes.addAll(scanByJarPath(path));
            }
        } else {
            System.err.println("开始扫描全部引用jar包，如果扫描过程过长请在启动vm参数中设置gaea.serializer.basepakage或者设置gaea.serializer.scantype=asyn使用异步模式扫描。");
            classes.addAll(scanByURLClassLoader());
            if (classes.size() == 0) {
                classes.addAll(scanByJarPath(ClassHelper.getJarPath(ClassScaner.class)));
            }
        }
        return classes;
    }

    /**
     * 从包package中获取所有的Class
     * @param pack
     * @return
     */
    public Set<Class> scanByPakage(String pack) throws URISyntaxException, MalformedURLException, FileNotFoundException, ClassNotFoundException {
        // 第一个class类的集合   
        Set<Class> classes = new LinkedHashSet<Class>();
        // 获取包的名字 并进行替换
        String packageName = pack;
        String packageDirName = packageName.replace('.', '/');
        // 定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs;
        try {
            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            while (dirs.hasMoreElements()) {
                URL url = dirs.nextElement();
                // 得到协议的名称
                String protocol = url.getProtocol();
                // 如果是以文件的形式保存在服务器上
                if ("file".equals(protocol)) {
                    // 获取包的物理路径
                    getClassFromURL(url, pack, classes);
                } else if ("jar".equals(protocol)) {
                    try {
                        // 获取jar
                        JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
                        classes.addAll(ClassHelper.GetClassFromJar(jar, KEY_WORD, pack));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return classes;
    }

    private Set<Class> scanByJarPath(String jarPath) throws IOException, ClassNotFoundException {
    	System.out.println("jarPath:" + jarPath);
        Set<Class> classes = new LinkedHashSet<Class>();
        List<File> jarFiles = FileHelper.getFiles(jarPath, "jar");
        if (jarFiles == null) {
            System.err.println("No find jar from path:" + jarPath);
        } else {
	        for (File f : jarFiles) {
	            classes.addAll(ClassHelper.GetClassFromJar(f.getPath(), KEY_WORD));
	        }
        }
        return classes;
    }

    private Set<Class> scanByURLClassLoader() throws URISyntaxException, IOException, ClassNotFoundException {
        Set<Class> classes = new LinkedHashSet<Class>();
        URL[] urlAry = ((URLClassLoader) Thread.currentThread().getContextClassLoader()).getURLs();
        for (URL url : urlAry) {
        	if(!url.getPath().equalsIgnoreCase("/")) {
	        	System.out.println("scanByURLClassLoader:" + URLDecoder.decode(url.getPath(), "utf-8"));
	            if (url.getPath().endsWith(".jar")) {
	                classes.addAll(ClassHelper.GetClassFromJar(URLDecoder.decode(url.getPath(), "utf-8"), KEY_WORD));
	            } else {
	                getClassFromURL(url, "", classes);
	            }
        	}
        }
        return classes;
    }

    private void getClassFromURL(URL url, String basePak, Set<Class> classes) throws MalformedURLException, URISyntaxException, FileNotFoundException, IOException, ClassNotFoundException {
        if(url == null) {
        	System.err.println("url is null when getClassFromURL");
        	return;
        }
        String path = URLDecoder.decode(url.getPath(), "utf-8");
        if(path == null || path.equalsIgnoreCase("")) {
        	System.err.println("path is null when getClassFromURL (url:" + url + ")");
        	return;
        }
        
    	File f = new File(path);
        if (f.isDirectory()) {
            List<File> files = FileHelper.getFiles(f.getAbsolutePath(), "class");
            for (File file : files) {
                Class c = getClassFromFile(file, url, basePak);
                if (c != null) {
                    classes.add(c);
                }
            }
        } else if (f.getName().endsWith(".class")) {
            Class c = getClassFromFile(f, url, basePak);
            if (c != null) {
                classes.add(c);
            }
        }
    }

    private Class getClassFromFile(File f, URL baseURL, String basePak) throws ClassNotFoundException, URISyntaxException, FileNotFoundException, IOException {
        if (!isSerializable(f)) {
            return null;
        }
        String filePath = f.getAbsolutePath();
        filePath = filePath.replace("\\", ".");
        String dirPath = baseURL.toURI().getPath();
        if (dirPath.startsWith("/")) {
            dirPath = dirPath.substring(1);
        }
        dirPath = dirPath.replace("/", ".");
        filePath = filePath.replace(dirPath, "");
        if (filePath.endsWith(".class")) {
            filePath = filePath.substring(0, filePath.length() - ".class".length());
        }
        Class c = cl.loadClass(basePak + filePath);
        return c;
    }

    private static boolean isSerializable(File f) throws FileNotFoundException, IOException {
        if (!f.getAbsolutePath().endsWith(".class")) {
            return false;
        }
        boolean result = false;
        StringBuffer sb = new StringBuffer();
        FileReader fr = null;
        BufferedReader br = null;
        try {
            fr = new FileReader(f);
            br = new BufferedReader(fr);
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
                if (sb.indexOf("GaeaSerializable") > -1) {
                    result = true;
                    break;
                }
            }
        } finally {
            if (fr != null) {
                fr.close();
            }
            if (br != null) {
                br.close();
            }
        }
        return result;
    }
}
