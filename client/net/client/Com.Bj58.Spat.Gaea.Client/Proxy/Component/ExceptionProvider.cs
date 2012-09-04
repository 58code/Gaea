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
using Com.Bj58.Spat.Gaea.Client.Exceptions;
using Com.Bj58.Spat.Gaea.Client.Proxy.Enum;

namespace Com.Bj58.Spat.Gaea.Client.Proxy.Component
{
    internal class ExceptionProvider
    {
        public static Exception ThrowServiceError(int errorcode, string exception)
        {
            switch (errorcode)
            {
                case (int)Errorcode.DB:
                    return new DBException(exception);
                case (int)Errorcode.Net:
                    return new NetException(exception);
                case (int)Errorcode.TimeOut:
                    return new Com.Bj58.Spat.Gaea.Client.Exceptions.TimeoutException(exception);
                case (int)Errorcode.Protocol:
                    return new ProtocolException(exception);
                case (int)Errorcode.JsonException:
                    return new JSONException(exception);
                case (int)Errorcode.ParaException:
                    return new ParaException(exception);
                case (int)Errorcode.NotFoundMethodException:
                    return new NotFoundMethodException(exception);
                case (int)Errorcode.NotFoundServiceException:
                    return new NotFoundServiceException(exception);
                case (int)Errorcode.JSONSerializeException:
                    return new JSONSerializeException(exception);
                case (int)Errorcode.ServiceException:
                    return new ServiceException(exception);
                case (int)Errorcode.DataOverFlowException:
                    return new DataOverFlowException(exception);
                case (int)Errorcode.OtherException:
                    return new OtherException(exception);
                default:
                    return new Exception("返回状态不可识别!");
            }
        }
    }
}
