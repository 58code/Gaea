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
package com.bj58.spat.gaea.client.configuration.commmunication;

import java.nio.charset.Charset;

import javax.naming.directory.NoSuchAttributeException;

import org.w3c.dom.Node;

import com.bj58.spat.gaea.protocol.serializer.SerializeBase;
import com.bj58.spat.gaea.protocol.sfp.enumeration.CompressType;
import com.bj58.spat.gaea.protocol.sfp.enumeration.SerializeType;

/**
 * ProtocolProfile
 *
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ProtocolProfile {

    private SerializeType serializerType;
    private SerializeBase serialize;
    public Charset Encoder;
    public byte serviceID;
    public CompressType compress;

    public ProtocolProfile(Node node) throws Exception {
        Node attrSer = node.getAttributes().getNamedItem("serialize");
        if (attrSer == null) {
            throw new ExceptionInInitializerError("Not find attrbuts:" + node.getNodeName() + "[@'serialize']");
        }
        String value = attrSer.getNodeValue().trim().toLowerCase();
        if (value.equalsIgnoreCase("binary")) {
            serializerType = SerializeType.JAVABinary;
        } else if (value.equalsIgnoreCase("json")) {
            serializerType = SerializeType.JSON;
        } else if (value.equalsIgnoreCase("xml")) {
            serializerType = SerializeType.XML;
        } else if (value.equalsIgnoreCase("gaea")) {
            serializerType = SerializeType.GAEABinary;
        } else {
            throw new NoSuchAttributeException("Protocol not supported " + value + "!");
        }
        this.serialize = SerializeBase.getInstance(serializerType);
        attrSer = node.getAttributes().getNamedItem("encoder");
        if (attrSer == null) {
            this.Encoder = Charset.forName("UTF-8");
        } else {
            this.Encoder = Charset.forName(attrSer.getNodeValue());
        }
        this.serialize.setEncoder(this.Encoder);
        serviceID = Byte.parseByte(node.getParentNode().getParentNode().getAttributes().getNamedItem("id").getNodeValue());//TODO 待检验
        compress = (CompressType) Enum.valueOf(CompressType.class, node.getAttributes().getNamedItem("compressType").getNodeValue());
    }

    public Charset getEncoder() {
        return Encoder;
    }

    public CompressType getCompress() {
        return compress;
    }

    public SerializeBase getSerializer() {
        return serialize;
    }

    public SerializeType getSerializerType() {
        return serializerType;
    }

    public byte getServiceID() {
        return serviceID;
    }
}
