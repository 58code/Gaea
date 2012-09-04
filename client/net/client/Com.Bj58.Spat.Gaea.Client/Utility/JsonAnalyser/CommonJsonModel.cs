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
    internal class CommonJsonModel : CommonJsonModelAnalyzer
    {
        private string rawjson;

        private bool isValue = false;

        private bool isModel = false;

        private bool isCollection = false;

        public CommonJsonModel() { }
        public CommonJsonModel(string rawjson)
        {
            Rawjson = rawjson;
        }

        public string Rawjson
        {
            get { return rawjson; }
            set {

                this.rawjson = value;
                if (string.IsNullOrEmpty(rawjson))
                    throw new Exception("missing rawjson");
                rawjson = rawjson.Trim();
                if (rawjson.StartsWith("{"))
                {
                    isModel = true;
                }
                else if (rawjson.StartsWith("["))
                {
                    isCollection = true;
                }
                else
                {
                    isValue = true;
                }
                
            }
        }

        public bool IsValue()
        {
            return isValue;
        }
        public bool IsValue(string key)
        {
            if (!isModel)
                return false;

            if (string.IsNullOrEmpty(key))
                return false;

            foreach (string subjson in base._GetCollection(this.rawjson))
            {
                CommonJsonModel model = new CommonJsonModel(subjson);

                if (!model.IsValue())
                    continue;

                if (model.Key == key)
                {
                    CommonJsonModel submodel = new CommonJsonModel(model.Value);

                    return submodel.IsValue();
                }
            }

            return false;
        }
        public bool IsModel()
        {
            return isModel;
        }
        public bool IsModel(string key)
        {
            if (!isModel)
                return false;

            if (string.IsNullOrEmpty(key))
                return false;

            foreach (string subjson in base._GetCollection(this.rawjson))
            {
                CommonJsonModel model = new CommonJsonModel(subjson);

                if (!model.IsValue())
                    continue;

                if (model.Key == key)
                {
                    CommonJsonModel submodel = new CommonJsonModel(model.Value);

                    return submodel.IsModel();
                }
            }

            return false;
        }
        public bool IsCollection()
        {
            return isCollection;
        }
        public bool IsCollection(string key)
        {
            if (!isModel)
                return false;

            if (string.IsNullOrEmpty(key))
                return false;

            foreach (string subjson in base._GetCollection(this.rawjson))
            {
                CommonJsonModel model = new CommonJsonModel(subjson);

                if (!model.IsValue())
                    continue;

                if (model.Key == key)
                {
                    CommonJsonModel submodel = new CommonJsonModel(model.Value);

                    return submodel.IsCollection();
                }
            }

            return false;
        }


        /// <summary>
        /// 当模型是对象，返回拥有的key
        /// </summary>
        /// <returns></returns>
        public List<string> GetKeys()
        {
            if (!isModel)
                return null;

            List<string> list = new List<string>();

            foreach (string subjson in base._GetCollection(this.rawjson))
            {
                string key = new CommonJsonModel(subjson).Key;

                if (!string.IsNullOrEmpty(key))
                    list.Add(key);
            }

            return list;
        }

        /// <summary>
        /// 当模型是对象，key对应是值，则返回key对应的值
        /// </summary>
        /// <param name="key"></param>
        /// <returns></returns>
        public string GetValue(string key)
        {
            if (!isModel)
                return null;

            if (string.IsNullOrEmpty(key))
                return null;

            foreach (string subjson in base._GetCollection(this.rawjson))
            {
                CommonJsonModel model = new CommonJsonModel(subjson);

                if (!model.IsValue())
                    continue;

                if (model.Key == key)
                    return model.Value;
            }

            return null;
        }

        /// <summary>
        /// 模型是对象，key对应是对象，返回key对应的对象
        /// </summary>
        /// <param name="key"></param>
        /// <returns></returns>
        public CommonJsonModel GetModel(string key)
        {
            if (!isModel)
                return null;

            if (string.IsNullOrEmpty(key))
                return null;

            foreach (string subjson in base._GetCollection(this.rawjson))
            {
                CommonJsonModel model = new CommonJsonModel(subjson);

                if (!model.IsValue())
                    continue;

                if (model.Key == key)
                {
                    CommonJsonModel submodel = new CommonJsonModel(model.Value);

                    if (!submodel.IsModel())
                        return null;
                    else
                        return submodel;
                }
            }

            return null;
        }

        /// <summary>
        /// 模型是对象，key对应是集合，返回集合
        /// </summary>
        /// <param name="key"></param>
        /// <returns></returns>
        public CommonJsonModel GetCollection(string key)
        {
            if (!isModel)
                return null;

            if (string.IsNullOrEmpty(key))
                return null;

            foreach (string subjson in base._GetCollection(this.rawjson))
            {
                CommonJsonModel model = new CommonJsonModel(subjson);

                if (!model.IsValue())
                    continue;

                if (model.Key == key)
                {
                    CommonJsonModel submodel = new CommonJsonModel(model.Value);

                    if (!submodel.IsCollection())
                        return null;
                    else
                        return submodel;
                }
            }

            return null;
        }

        /// <summary>
        /// 模型是集合，返回自身
        /// </summary>
        /// <returns></returns>
        public List<CommonJsonModel> GetCollection()
        {
            List<CommonJsonModel> list = new List<CommonJsonModel>();

            if (IsValue())
                return list;

            foreach (string subjson in base._GetCollection(rawjson))
            {
                list.Add(new CommonJsonModel(subjson));
            }

            return list;
        }




        /// <summary>
        /// 当模型是值对象，返回key
        /// </summary>
        private string Key
        {
            get
            {
                if (IsValue())
                    return base._GetKey(rawjson);

                return null;
            }
        }
        /// <summary>
        /// 当模型是值对象，返回value
        /// </summary>
        private string Value
        {
            get
            {
                if (!IsValue())
                    return null;

                return base._GetValue(rawjson);
            }
        }
    }
}



