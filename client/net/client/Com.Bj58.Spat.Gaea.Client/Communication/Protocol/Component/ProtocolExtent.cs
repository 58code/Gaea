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
//----------------------------------------------------------------------
//<Copyright company="58.com">
//      Team:SPAT
//      Blog:http://blog.58.com/spat/  
//      Website:http://www.58.com
//</Copyright>
//-----------------------------------------------------------------------
using System;
using System.Text;
using Com.Bj58.Spat.Gaea.Client.Exceptions;
using Com.Bj58.Spat.Gaea.Client.Utility.Helper;
using Com.Bj58.Spat.Gaea.Client.Proxy.Component;
using Com.Bj58.Spat.Gaea.Client.Utility.JsonAnalyser;
using Com.Bj58.Spat.Gaea.Client.Communication.Serialize;
using Com.Bj58.Spat.Gaea.Client.Communication.Protocol.SDP;
using Com.Bj58.Spat.Gaea.Client.Communication.Protocol.SFP;
using Com.Bj58.Spat.Gaea.Client.Configuration.Commmunication;
using Com.Bj58.Spat.Gaea.Client.Communication.Protocol.SFP.Enum;


/// <summary>
/// Binary序列化扩展方法
/// 
internal static class ProtocolExtent
{   
    /// <summary>
    /// Covert Protocol To Arrray
    /// </summary>
    /// <param name="p">Protocol</param>
    /// <returns></returns>
    public static byte[] ToBytes(this Protocol p)
    {
        long startIndex = 0;
        byte[] data = new byte[p.TotalLen];
        data[0] = p.Version;

        startIndex += SFPStruct.Version;
        Array.Copy(BitConverter.GetBytes(p.TotalLen), 0, data, startIndex, SFPStruct.TotalLen);

        startIndex += SFPStruct.TotalLen;
        Array.Copy(BitConverter.GetBytes(p.SessionID), 0, data, startIndex, SFPStruct.SessionId);
        
        startIndex += SFPStruct.SessionId;
        data[startIndex] = p.ServiceID;
        
        startIndex += SFPStruct.ServerId;
        data[startIndex] = (byte)p.UserDataType;
        
        startIndex += SFPStruct.SDPType;
        data[startIndex] = (byte)p.CompressType;
        
        startIndex += SFPStruct.CompressType;
        data[startIndex] = (byte)p.SerializeType;
        
        startIndex += SFPStruct.SerializeType;
        data[startIndex] = (byte)p.Platform;
        
        startIndex += SFPStruct.Platform;
        Array.Copy(p.UserData, 0, data,startIndex,p.TotalLen-startIndex);
       
        return data;
    }

    /// <summary>
    /// get protocol from bytes
    /// </summary>
    /// <param name="p">current porotocol object</param>
    /// <param name="data">bytes of data</param>
    public static void FromBytes(this Protocol p, byte[] data)
    {
        long startIndex = 0;
        p.Version = data[0];
        
        startIndex += SFPStruct.Version;
        p.TotalLen = BitConverter.ToInt32(data, (int)startIndex);
        
        startIndex += SFPStruct.TotalLen;
        p.SessionID = BitConverter.ToInt32(data, (int)startIndex);
        
        startIndex += SFPStruct.SessionId;
        p.ServiceID = data[startIndex];
        
        startIndex += SFPStruct.ServerId;
        p.UserDataType=(UserDataType)data[startIndex];
        
        startIndex += SFPStruct.SDPType;
        p.CompressType = (CompressType)data[startIndex];
        
        startIndex += SFPStruct.CompressType;
        p.SerializeType = (SerializeType)data[startIndex];
        
        startIndex += SFPStruct.SerializeType;
        p.Platform = (PlatformType)data[startIndex];
        
        startIndex += SFPStruct.Platform;
        p.UserData = new byte[data.Length - startIndex];
        Array.Copy(data, startIndex, p.UserData, 0, data.Length - startIndex);
    }
}