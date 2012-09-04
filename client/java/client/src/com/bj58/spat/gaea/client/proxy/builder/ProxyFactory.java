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
package com.bj58.spat.gaea.client.proxy.builder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ProxyFactory
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ProxyFactory {

    private static Map cache = new ConcurrentHashMap();

    /**
     * Factory for Proxy - creation.
     * @param type the class of type
     * @param strUrl request URL
     * @return
     * @throws MalformedURLException
     */
    public static <T> T create(Class type, String strUrl) {
        String key = strUrl.toLowerCase();
        Object proxy = null;
        if (cache.containsKey(key)) {
            proxy = cache.get(key);
        }
        if (proxy == null) {
            proxy = createStandardProxy(strUrl, type);
            if (proxy != null) {
                cache.put(key, proxy);
            }
        }
        return (T)proxy;
    }
    
    /**
     * 
     * @param strUrl ex: tcp://***//***
     * @param type 接口类
     * @return
     */
    private static Object createStandardProxy(String strUrl, Class<?> type) {
        String serviceName = "";
        String lookup = "";//接口实现类
        strUrl = strUrl.replace("tcp://", "");
        String[] splits = strUrl.split("/");
        if (splits.length == 2) {
            serviceName = splits[0];
            lookup = splits[1];
        }
        InvocationHandler handler = new ProxyStandard(type, serviceName, lookup);
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{type},
                handler);
    }
}