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
package com.bj58.spat.gaea.protocol.sfp.v1;

import com.bj58.spat.gaea.protocol.compress.CompressBase;
import com.bj58.spat.gaea.protocol.serializer.SerializeBase;
import com.bj58.spat.gaea.protocol.sfp.enumeration.CompressType;
import com.bj58.spat.gaea.protocol.sfp.enumeration.PlatformType;
import com.bj58.spat.gaea.protocol.sfp.enumeration.SDPType;
import com.bj58.spat.gaea.protocol.sfp.enumeration.SerializeType;
import com.bj58.spat.gaea.protocol.utility.ByteConverter;
import com.bj58.spat.gaea.secure.DESCoderHelper;

/**
 * 
 * 版本一协义定义
 * 
 * 1byte(版本号) | 4byte(协义总长度) | 4byte(序列号) | 1byte(服务编号) | 1byte(消息体类型) | 1byte 所采用的压缩算法 | 1byte 序列化规则 | 1byte 平台(.net java ...) | n byte消息体 | 5byte(分界符)
 *    0                1~4              5~8              9                   10                   11                      12                     13
 * 消息头总长度:14byte 
 * 
 * 协义总长度 = 消息头总长度 + 消息体总长度 (不包括分界符)
 * 
 * 尾分界符: 9, 11, 13, 17, 18
 * 
 * 版本号从ASCII > 48 开始标识
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Protocol {

    public static final byte VERSION = 1;
    private int totalLen;
    private int sessionID;
    private byte serviceID;
    private SDPType sdpType;
    private CompressType compressType = CompressType.UnCompress;
    private SerializeType serializeType = SerializeType.GAEABinary;
    private PlatformType platformType = PlatformType.Java;
    private byte[] userData;
    private Object sdpEntity;

	/**
	 * 协义头14个byte
	 */
	private final static int HEAD_STACK_LENGTH = 14;

    public Protocol(int sessionId, 
    		byte serviceId,
    		SDPType sdpType, 
    		CompressType compressType,
    		SerializeType serializeType,
    		PlatformType platformType,
    		Object sdpEntity) {
    	
            this.sdpEntity = sdpEntity;
            this.sessionID = sessionId;
            this.serviceID = serviceId;
            this.sdpType = sdpType;
            this.compressType = compressType;
            this.serializeType = serializeType;
            this.platformType = platformType;
    }
    
    public Protocol(int sessionId, 
    		byte serviceId,
    		SDPType sdpType, 
    		CompressType compressType,
    		SerializeType serializeType,
    		PlatformType platformType,
    		byte[] userData) {
    	
            this.userData = userData;
            this.sessionID = sessionId;
            this.serviceID = serviceId;
            this.sdpType = sdpType;
            this.compressType = compressType;
            this.serializeType = serializeType;
            this.platformType = platformType;
    }
    
    public Protocol(int sessionId, 
    		byte serviceId,
    		SDPType sdpType, 
    		Object sdpEntity) {
    	
            this.sdpEntity = sdpEntity;
            this.sessionID = sessionId;
            this.sdpType = sdpType;
            this.serviceID = serviceId;
    }

    public Protocol() {
    	
    }
    
    public int getVersion() {
    	return VERSION;
    }

    public int getTotalLen() {
        return totalLen;
    }

    public void setTotalLen(int totalLen) {
        this.totalLen = totalLen;
    }

    public CompressType getCompressType() {
        return compressType;
    }

    public void setCompressType(CompressType compressType) {
        this.compressType = compressType;
    }

    public PlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(PlatformType platform) {
        this.platformType = platform;
    }

    public SerializeType getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(SerializeType serializeType) {
        this.serializeType = serializeType;
    }

    public byte getServiceID() {
        return serviceID;
    }

    public void setServiceID(byte serviceID) {
        this.serviceID = serviceID;
    }

    public int getSessionID() {
        return sessionID;
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public byte[] getUserData() {
        return userData;
    }

    public void setUserData(byte[] userData) {
        this.userData = userData;
    }

    public SDPType getSDPType() {
        return sdpType;
    }

    public void setSDPType(SDPType sdpType) {
        this.sdpType = sdpType;
    }

    public void setSdpEntity(Object sdpEntity) {
		this.sdpEntity = sdpEntity;
	}

	public Object getSdpEntity() {
		return sdpEntity;
	}

	public byte[] toBytes() throws Exception{
        int startIndex = 0;
        SerializeBase serialize = SerializeBase.getInstance(this.getSerializeType());
        CompressBase compress = CompressBase.getInstance(this.getCompressType());
        
        this.sdpType = SDPType.getSDPType(this.sdpEntity);
        byte[] sdpData = serialize.serialize(this.sdpEntity);
        
        sdpData = compress.zip(sdpData);
        int protocolLen = HEAD_STACK_LENGTH + sdpData.length;
        this.setTotalLen(protocolLen);
        
        byte[] data = new byte[protocolLen];
        data[0] = Protocol.VERSION;

        startIndex += SFPStruct.Version;
        System.arraycopy(ByteConverter.intToBytesLittleEndian(this.getTotalLen()), 0, data, startIndex, SFPStruct.TotalLen);

        startIndex += SFPStruct.TotalLen;
        System.arraycopy(ByteConverter.intToBytesLittleEndian(this.getSessionID()), 0, data, startIndex, SFPStruct.SessionId);

        startIndex += SFPStruct.SessionId;
        data[startIndex] = this.getServiceID();

        startIndex += SFPStruct.ServerId;
        data[startIndex] = (byte) this.getSDPType().getNum();

        startIndex += SFPStruct.SDPType;
        data[startIndex] = (byte) this.getCompressType().getNum();

        startIndex += SFPStruct.CompressType;
        data[startIndex] = (byte) this.getSerializeType().getNum();

        startIndex += SFPStruct.SerializeType;
        data[startIndex] = (byte) this.getPlatformType().getNum();

        startIndex += SFPStruct.Platform;
        System.arraycopy(sdpData, 0, data, startIndex, protocolLen - startIndex);

        return data;
    }
	
    public static Protocol fromBytes(byte[] data) throws Exception {
        Protocol p = new Protocol();
        int startIndex = 0;
        if(data[startIndex] != Protocol.VERSION) {
        	throw new Exception("协义版本错误");
        }

        startIndex += SFPStruct.Version;//1
        byte[] totalLengthByte = new byte[SFPStruct.TotalLen];
        for (int i = 0; i < SFPStruct.TotalLen; i++) {
            totalLengthByte[i] = data[startIndex + i];
        }
        p.setTotalLen(ByteConverter.bytesToIntLittleEndian(totalLengthByte));

        startIndex += SFPStruct.TotalLen;//5
        byte[] sessionIDByte = new byte[SFPStruct.SessionId];
        for (int i = 0; i < SFPStruct.SessionId; i++) {
            sessionIDByte[i] = data[startIndex + i];
        }
        p.setSessionID(ByteConverter.bytesToIntLittleEndian(sessionIDByte));

        startIndex += SFPStruct.SessionId;//9
        p.setServiceID(data[startIndex]);

        startIndex += SFPStruct.ServerId;//10
        p.setSDPType(SDPType.getSDPType(data[startIndex]));

        startIndex += SFPStruct.SDPType;
        CompressType ct = CompressType.getCompressType(data[startIndex]);
        p.setCompressType(ct);

        startIndex += SFPStruct.CompressType;
        SerializeType st = SerializeType.getSerializeType(data[startIndex]);
        p.setSerializeType(st);

        startIndex += SFPStruct.SerializeType;
        p.setPlatformType(PlatformType.getPlatformType(data[startIndex]));

        startIndex += SFPStruct.Platform;

        byte[] sdpData = new byte[data.length - startIndex];
        System.arraycopy(data, startIndex, sdpData, 0, data.length - startIndex);
        sdpData = CompressBase.getInstance(ct).unzip(sdpData);
        p.setUserData(sdpData);
        
        SerializeBase serialize = SerializeBase.getInstance(st);
        p.setSdpEntity(serialize.deserialize(sdpData, SDPType.getSDPClass(p.getSDPType())));
        return p;
    }
    
    /**
     * desKey 解密重载方法
     * @param data 密文
     * @param desKey DES密钥
     * @return
     * @throws Exception
     */
    public static Protocol fromBytes(byte[] data, boolean rights, byte[] desKey) throws Exception {
        Protocol p = new Protocol();
        int startIndex = 0;
        if(data[startIndex] != Protocol.VERSION) {
        	throw new Exception("协义版本错误");
        }

        startIndex += SFPStruct.Version;//1
        byte[] totalLengthByte = new byte[SFPStruct.TotalLen];
        for (int i = 0; i < SFPStruct.TotalLen; i++) {
            totalLengthByte[i] = data[startIndex + i];
        }
        p.setTotalLen(ByteConverter.bytesToIntLittleEndian(totalLengthByte));

        startIndex += SFPStruct.TotalLen;//5
        byte[] sessionIDByte = new byte[SFPStruct.SessionId];
        for (int i = 0; i < SFPStruct.SessionId; i++) {
            sessionIDByte[i] = data[startIndex + i];
        }
        p.setSessionID(ByteConverter.bytesToIntLittleEndian(sessionIDByte));

        startIndex += SFPStruct.SessionId;//9
        p.setServiceID(data[startIndex]);

        startIndex += SFPStruct.ServerId;//10
        p.setSDPType(SDPType.getSDPType(data[startIndex]));

        startIndex += SFPStruct.SDPType;
        CompressType ct = CompressType.getCompressType(data[startIndex]);
        p.setCompressType(ct);

        startIndex += SFPStruct.CompressType;
        SerializeType st = SerializeType.getSerializeType(data[startIndex]);
        p.setSerializeType(st);

        startIndex += SFPStruct.SerializeType;
        p.setPlatformType(PlatformType.getPlatformType(data[startIndex]));

        startIndex += SFPStruct.Platform;

        byte[] sdpData = new byte[data.length - startIndex];
        System.arraycopy(data, startIndex, sdpData, 0, data.length - startIndex);
        sdpData = CompressBase.getInstance(ct).unzip(sdpData);
        
        //数据解密
        if(p.getSDPType().getNum() != 5 && rights && desKey !=null){
        	sdpData = DESCoderHelper.getInstance().decrypt(sdpData, desKey);//DES解密数据
        }
        
        p.setUserData(sdpData);
        SerializeBase serialize = SerializeBase.getInstance(st);        
        p.setSdpEntity(serialize.deserialize(sdpData, SDPType.getSDPClass(p.getSDPType())));
        return p;
    }
    /**
     * 加密重载方法
     * @param rights 是否启用权限认证
     * @param desKey DES密钥
     * @return
     * @throws Exception
     */
    public byte[] toBytes(boolean rights, byte [] desKey) throws Exception{
        int startIndex = 0;
        SerializeBase serialize = SerializeBase.getInstance(this.getSerializeType());
        CompressBase compress = CompressBase.getInstance(this.getCompressType());
        
        this.sdpType = SDPType.getSDPType(this.sdpEntity);
        byte[] sdpData = serialize.serialize(this.sdpEntity);
        
        //数据加密
        if(this.getSDPType().getNum() != 5 && rights && desKey != null){
        	sdpData = DESCoderHelper.getInstance().encrypt(sdpData, desKey);//DES加密数据
        }
        
        sdpData = compress.zip(sdpData);
        int protocolLen = HEAD_STACK_LENGTH + sdpData.length;
        this.setTotalLen(protocolLen);
        
        byte[] data = new byte[protocolLen];
        data[0] = Protocol.VERSION;

        startIndex += SFPStruct.Version;
        System.arraycopy(ByteConverter.intToBytesLittleEndian(this.getTotalLen()), 0, data, startIndex, SFPStruct.TotalLen);

        startIndex += SFPStruct.TotalLen;
        System.arraycopy(ByteConverter.intToBytesLittleEndian(this.getSessionID()), 0, data, startIndex, SFPStruct.SessionId);

        startIndex += SFPStruct.SessionId;
        data[startIndex] = this.getServiceID();

        startIndex += SFPStruct.ServerId;
        data[startIndex] = (byte) this.getSDPType().getNum();

        startIndex += SFPStruct.SDPType;
        data[startIndex] = (byte) this.getCompressType().getNum();

        startIndex += SFPStruct.CompressType;
        data[startIndex] = (byte) this.getSerializeType().getNum();

        startIndex += SFPStruct.SerializeType;
        data[startIndex] = (byte) this.getPlatformType().getNum();

        startIndex += SFPStruct.Platform;
        System.arraycopy(sdpData, 0, data, startIndex, protocolLen - startIndex);

        return data;
    }
}