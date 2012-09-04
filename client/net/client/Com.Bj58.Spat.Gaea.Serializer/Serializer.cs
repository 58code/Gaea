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
using System.Linq;
using System.Text;
using System.IO;
using System.Collections.Generic;
using Com.Bj58.Spat.Gaea.Serializer.Component;
using Com.Bj58.Spat.Gaea.Serializer.Component.Exceptions;

namespace Com.Bj58.Spat.Gaea.Serializer
{
    public class Serializer
    {
        private Encoding _encoder = Encoding.UTF8;
        public Serializer()
        { 
            
        }
        public Serializer(Encoding encoder)
        {
            if (encoder != null)
                _encoder = encoder;
        }

        public byte[] Serialize(object obj)
        {
            using (GaeaStream stream = new GaeaStream())
            {
                stream.Encoder = _encoder;
                if (obj == null)
                {
                    SerializerFactory.GetSerializer(null).WriteObject(obj,stream);
                }
                else
                {
                    if (obj is IGaeaSerializer)
                    {
                        ((IGaeaSerializer)obj).Serialize(stream);
                    }
                    else
                    {
                        SerializerFactory.GetSerializer(obj.GetType()).WriteObject(obj, stream);
                    }
                }
                return stream.ToArray();
            }
        }

        public object Derialize(byte[] buffer,Type type)
        {
            using (GaeaStream stream = new GaeaStream(buffer))
            {
                stream.Encoder = _encoder;

                if (type.GetInterface("IGaeaSerializer") != null)
                {
                     var obj = ((IGaeaSerializer)Activator.CreateInstance(type));
                     obj.Derialize(stream);
                     return obj;
                }
                else
                {
                    object obj = SerializerFactory.GetSerializer(type).ReadObject(stream,type);
                    return obj;
                }
            }
        }
    }
}
