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
using Com.Bj58.Spat.Gaea.Client.Proxy.Enum;

namespace Com.Bj58.Spat.Gaea.Client.Proxy.Component
{
    public class Parameter
    {
        public Parameter(object para,Type dataType, ParaType pType)
        {
            Para = para;
            ParaType = pType;
            DataType = dataType;
        }
        public Parameter(object para, Type dataType)
        {
            Para = para;
            DataType = dataType;
        }
        public Type DataType
        {
            get;
            set;
        }
        public object Para
        {
            get;
            set;
        }
        private ParaType _ParaType = ParaType.In;
        internal ParaType ParaType
        {
            get { return _ParaType; }
            set { _ParaType = value; }
        }

    }
}
