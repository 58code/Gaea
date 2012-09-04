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
package com.bj58.spat.gaea.serializer.serializer;

import com.bj58.spat.gaea.serializer.component.GaeaInStream;
import com.bj58.spat.gaea.serializer.component.GaeaOutStream;
import com.bj58.spat.gaea.serializer.component.TypeMap;
import com.bj58.spat.gaea.serializer.component.helper.ClassHelper;
import com.bj58.spat.gaea.serializer.serializer.IGaeaSerializer;

import java.nio.charset.Charset;

/**
 * Serializer
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Serializer {

    static {
        TypeMap.InitTypeMap();
    }

    /*
     * @deprecated  使用指定vm参数gaea.serializer.basepakage取代了指定jarpath
     */
    @Deprecated
    public static String[] JarPath = null;

    /*
     * @param   指定扫描多个目录下的jar包文件
     * @deprecated  使用指定vm参数gaea.serializer.basepakage取代了指定jarpath
     */
    @Deprecated
    public static void SetJarPath(String... jarPath) {
        System.err.println("------------------------------------注意!!!------------------------------------------------------");
        System.err.println("注意!!!指定了JarPath重新扫描jar文件，强烈建议不使用指定JarPath方式扫描jar文件，请及时纠正，以免版本升级后带来系统错误！");
        System.err.println("-----------------------------------------------------------------------------------------------------");
        JarPath = jarPath;
        TypeMap.InitTypeMap();
    }
    private Charset _Encoder = Charset.forName("UTF-8");


    public Serializer() {
        
    }

    public Serializer(Charset encoder) {
        _Encoder = encoder;
    }

    /*
     * @return  对象序列化后的字节数组
     * @param   obj     要序列化的对象
     */
    public byte[] Serialize(Object obj) throws Exception {
        GaeaOutStream stream = null;
        try {
            stream = new GaeaOutStream();
            stream.Encoder = _Encoder;
            if (obj == null) {
                SerializerFactory.GetSerializer(null).WriteObject(obj, stream);
            } else {
                Class type = obj.getClass();
                if (obj instanceof IGaeaSerializer) {
                    ((IGaeaSerializer) obj).Serialize(stream);
                } else {
                    SerializerFactory.GetSerializer(type).WriteObject(obj, stream);
                }
            }
            byte[] result = stream.toByteArray();
            return result;
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

      /*
     * @return  对象序列化后的字节数组
     * @param   obj     要序列化的对象
     */
    public Object Derialize(byte[] buffer, Class type) throws Exception {
        GaeaInStream stream = null;
        try {
            stream = new GaeaInStream(buffer);
            stream.Encoder = _Encoder;
            if (ClassHelper.InterfaceOf(type, IGaeaSerializer.class)) {
                IGaeaSerializer obj = (IGaeaSerializer) type.newInstance();
                obj.Derialize(stream);
                return obj;
            }
            return SerializerFactory.GetSerializer(type).ReadObject(stream, type);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
}
