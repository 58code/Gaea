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
using System.Xml;
using Com.Bj58.Spat.Gaea.Client.Communication.Protocol.SFP.Enum;
using Com.Bj58.Spat.Gaea.Client.Communication.Serialize;

namespace Com.Bj58.Spat.Gaea.Client.Configuration.Commmunication
{
    internal class ProtocolProfile
    {
        public ProtocolProfile(XmlNode node)
        { 
            XmlAttribute attrSer = node.Attributes["serialize"];
            if (attrSer == null)
                throw new XmlException("Not find attrbuts:" + node.Name + "[@'serialize']");
            string value = attrSer.Value.Trim().ToLower();
            if (value.Equals("json"))
            {
                this.SerializerType = SerializeType.JSON;
            }
            else if (value.Equals("binary"))
            {
                this.SerializerType = SerializeType.Binary;
            }
            else if (value.Equals("gaea"))
            {
                this.SerializerType = SerializeType.GAEA;
            }
            else
            {
                throw new NotSupportedException("Protocol not supported!");
            }
            XmlAttribute attrSerializer = node.Attributes["serializeType"];
            if (attrSerializer != null)
            {
                string serializeType = attrSerializer.Value;
                if (!string.IsNullOrEmpty(serializeType))
                {
                    string[] types = serializeType.Split(',');
                    if (types.Length == 2)
                    {
                       
                        this.Serializer = System.Reflection.Assembly.Load(types[0]).CreateInstance(types[1]) as SerializeBase;
                    }
                    else if (types.Length == 1)
                    {
                        this.Serializer = Activator.CreateInstance(Type.GetType(types[0])) as SerializeBase;
                    }
                }
            }
            if (Serializer == null)
            {
                this.Serializer = SerializeBase.GetInstance();
            }
            XmlAttribute attrEncoder = node.Attributes["encoder"];
            if (attrEncoder == null)
                this.Encoder = Encoding.UTF8;
            else
                this.Encoder = Encoding.GetEncoding(attrEncoder.Value);
            this.Serializer.Encoder = this.Encoder;
            ServiceID = byte.Parse(node.ParentNode.ParentNode.Attributes["id"].Value);
            Compress = (CompressType)Enum.Parse(typeof(CompressType), node.Attributes["compressType"].Value, true);
            
        }

        public SerializeType SerializerType
        {
            get;
            set;
        }
        public SerializeBase Serializer
        {
            get;
            set;
        }
        public Encoding Encoder
        {
            get;
            set;
        }
        public byte ServiceID
        {
            get;
            set;
        }
        public CompressType Compress
        {
            get;
            set;
        }
    }
}
