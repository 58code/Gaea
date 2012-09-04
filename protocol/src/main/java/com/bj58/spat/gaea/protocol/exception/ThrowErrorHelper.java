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
package com.bj58.spat.gaea.protocol.exception;

/**
 * ThrowErrorHelper
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ThrowErrorHelper {

	public static Exception throwServiceError(int errorcode, String exception) {
		switch (errorcode) {
			case ReturnType.DB:
				return new DBException(exception);
			case ReturnType.NET:
				return new NetException(exception);
			case ReturnType.TIME_OUT:
				return new TimeoutException(exception);
			case ReturnType.PROTOCOL:
				return new ProtocolException(exception);
			case ReturnType.JSON_EXCEPTION:
				return new JSONException(exception);
			case ReturnType.PARA_EXCEPTION:
				return new ParaException(exception);
			case ReturnType.NOT_FOUND_METHOD_EXCEPTION:
				return new NotFoundMethodException(exception);
			case ReturnType.NOT_FOUND_SERVICE_EXCEPTION:
				return new NotFoundServiceException(exception);
			case ReturnType.JSON_SERIALIZE_EXCEPTION:
				return new JSONSerializeException(exception);
			case ReturnType.SERVICE_EXCEPTION:
				return new ServiceException(exception);
			case ReturnType.DATA_OVER_FLOW_EXCEPTION:
				return new DataOverFlowException(exception);
			case ReturnType.OTHER_EXCEPTION:
				return new OtherException(exception);
			default:
				return (RemoteException) new Exception("返回状态不可识别!");
		}
	}
}
