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
internal static class CONST
{
    /// <summary>
    /// SFP start tag
    /// </summary>
    public static readonly byte[] P_START_TAG = new byte[] {};
    /// <summary>
    /// SFP end tag
    /// </summary>
    public static readonly byte[] P_END_TAG = new byte[] { 9, 11, 13, 17, 18 };
    /// <summary>
    /// config file path
    /// </summary>
    public static readonly string CONFIG_PATH = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "gaea.config");
    /// <summary>
    /// max sessionid
    /// </summary>
    public static readonly long MAX_SESSIONID = 1024 * 1024 * 1024;
    /// <summary>
    /// default max thread count;
    /// </summary>
    public static readonly int DEFAULT_MAX_THREAD_COUNT = 50;

}
