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
using System.Collections.Generic;
using System.Text;

namespace Com.Bj58.Spat.Gaea.Client.Proxy.Component
{
    public class TypeMapping
    {
        private static List<KeyValuePair<string, string>> mapList = new List<KeyValuePair<string, string>>();

        static TypeMapping()
        {
            mapList.Add(new KeyValuePair<string, string>("Int16", "short"));
            mapList.Add(new KeyValuePair<string, string>("Int32", "int"));
            mapList.Add(new KeyValuePair<string, string>("Int64", "long"));
            mapList.Add(new KeyValuePair<string, string>("Single", "float"));
            mapList.Add(new KeyValuePair<string, string>("KeyValuePair", "Map"));
            mapList.Add(new KeyValuePair<string, string>("IList", "List"));
            mapList.Add(new KeyValuePair<string, string>("DateTime", "Date"));
            mapList.Add(new KeyValuePair<string, string>("Decimal", "BigDecimal"));
        }

        public static string ParseType(string type)
        {
            StringBuilder all = new StringBuilder();
            StringBuilder sbKey = new StringBuilder();
            bool isGeneric = false;
            int genericCount = 0;
            int arrayCount = 0;

            for (int i = 0; i < type.Length; i++)
            {
                if (type[i] == ',')
                {
                    all.Append(sbKey);
                    all.Append(",");
                    sbKey.Length = 0;
                }
                else if (type[i] == '.')
                {
                    sbKey.Length = 0;
                }
                else if (type[i] == '`')
                {
                    all.Append(sbKey);
                    sbKey.Length = 0;
                    isGeneric = true;
                }
                else if (type[i] == '[')
                {
                    all.Append(sbKey);
                    sbKey.Length = 0;
                    if (isGeneric)
                    {
                        genericCount++;
                        all.Append('<');
                        isGeneric = false;
                    }
                    else
                    {
                        arrayCount++;
                        all.Append('[');
                    }
                }
                else if (type[i] == ']')
                {
                    all.Append(sbKey);
                    sbKey.Length = 0;
                    if (arrayCount > 0)
                    {
                        all.Append(']');
                        arrayCount--;
                    }
                    else
                    {
                        all.Append('>');
                        genericCount--;
                    }
                }
                else
                {
                    if (!isGeneric)
                    {
                        sbKey.Append(type[i]);
                    }
                    if (i == type.Length - 1)
                    {
                        all.Append(sbKey);
                    }
                }
            }

            for (int i = 0; i < mapList.Count; i++)
            {
                all.Replace(mapList[i].Key, mapList[i].Value);
            }

            return all.ToString();
        }

    }
}