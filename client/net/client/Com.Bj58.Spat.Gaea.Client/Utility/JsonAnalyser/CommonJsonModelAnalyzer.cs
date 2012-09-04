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
using System.Collections.Generic;

namespace Com.Bj58.Spat.Gaea.Client.Utility.JsonAnalyser
{
    internal class CommonJsonModelAnalyzer
    {
        protected string _GetKey(string rawjson)
        {
            if (string.IsNullOrEmpty(rawjson))
                return rawjson;

            rawjson = rawjson.Trim();

            string[] jsons = rawjson.Split(new char[] { ':' });

            if (jsons.Length < 2)
                return rawjson;

            return jsons[0].Replace("\"", "").Trim();
        }

        protected string _GetValue(string rawjson)
        {
            if (string.IsNullOrEmpty(rawjson))
                return rawjson;

            rawjson = rawjson.Trim();

            string[] jsons = rawjson.Split(new char[] { ':' }, StringSplitOptions.RemoveEmptyEntries);

            if (jsons.Length < 2)
                return rawjson;

            StringBuilder builder = new StringBuilder();

            for (int i = 1; i < jsons.Length; i++)
            {
                builder.Append(jsons[i]);

                builder.Append(":");
            }

            if (builder.Length > 0)
                builder.Remove(builder.Length - 1, 1);

            string value = builder.ToString();

            if (value.StartsWith("\""))
                value = value.Substring(1);

            if (value.EndsWith("\""))
                value = value.Substring(0, value.Length - 1);

            return value;
        }

        protected List<string> _GetCollection(string rawjson)
        {
            //[{},{}]

            List<string> list = new List<string>();

            if (string.IsNullOrEmpty(rawjson))
                return list;

            rawjson = rawjson.Trim();

            StringBuilder builder = new StringBuilder();

            int nestlevel = -1;

            int mnestlevel = -1;

            for (int i = 0; i < rawjson.Length; i++)
            {
                if (i == 0)
                    continue;
                else if (i == rawjson.Length - 1)
                    continue;

                char jsonchar = rawjson[i];

                if (jsonchar == '{')
                {
                    nestlevel++;
                }

                if (jsonchar == '}')
                {
                    nestlevel--;
                }

                if (jsonchar == '[')
                {
                    mnestlevel++;
                }

                if (jsonchar == ']')
                {
                    mnestlevel--;
                }

                if (jsonchar == ',' && nestlevel == -1 && mnestlevel == -1)
                {
                    list.Add(builder.ToString());

                    builder = new StringBuilder();
                }
                else
                {
                    builder.Append(jsonchar);
                }
            }

            if (builder.Length > 0)
                list.Add(builder.ToString());

            return list;
        }
    }
}
