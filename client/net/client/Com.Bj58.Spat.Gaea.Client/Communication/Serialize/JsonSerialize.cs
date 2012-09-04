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
using System.IO;
using System.Xml;
using System.Runtime.Serialization.Json;
using Com.Bj58.Spat.Gaea.Client.Utility.Logger;

namespace Com.Bj58.Spat.Gaea.Client.Communication.Serialize
{
    /// <summary>
    /// Json序例化
    /// </summary>
    public class JsonSerialize : SerializeBase
    {
        private LoggerBase logger = LoggerBase.GetLogger();
        public override byte[] Serialize(object obj)
        {
            try
            {
                using (MemoryStream ms = new MemoryStream())
                {
                    DataContractJsonSerializer ser = new DataContractJsonSerializer(obj.GetType());
                    ser.WriteObject(ms, obj);
                    byte[] json = ms.ToArray();
                    return json;
                }
            }
            catch (Exception ex)
            {
                logger.Error("JsonSerialize Serialize Error! "+ex.Message);
                throw ex;
            }
        }

        public override object Deserialize(byte[] data,Type type)
        {
            try
            {
                using (XmlDictionaryReader jsonReader = JsonReaderWriterFactory.CreateJsonReader(data, XmlDictionaryReaderQuotas.Max))
                {
                    DataContractJsonSerializer dcjs = new DataContractJsonSerializer(type);
                    object obj = dcjs.ReadObject(jsonReader);
                    return obj;
                }
            }
            catch (Exception ex)
            {
                logger.Error("JsonSerialize Deserialize Error! " + ex.Message + " json:" + Encoder.GetString(data)+" type:"+type.FullName);
                throw ex;
            }
        }

        public override T Deserialize<T>(byte[] data)
        {
            return (T)Deserialize(data, typeof(T));
        }
    }
}
