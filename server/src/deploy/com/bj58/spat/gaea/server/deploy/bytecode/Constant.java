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
package com.bj58.spat.gaea.server.deploy.bytecode;

import com.bj58.spat.gaea.protocol.sdp.RequestProtocol;
import com.bj58.spat.gaea.protocol.utility.KeyValuePair;
import com.bj58.spat.gaea.server.contract.annotation.OperationContract;
import com.bj58.spat.gaea.server.contract.annotation.ServiceBehavior;
import com.bj58.spat.gaea.server.contract.context.IProxyFactory;
import com.bj58.spat.gaea.server.contract.context.IProxyStub;
import com.bj58.spat.gaea.server.contract.context.GaeaContext;
import com.bj58.spat.gaea.server.contract.context.GaeaResponse;
import com.bj58.spat.gaea.server.contract.entity.Out;
import com.bj58.spat.gaea.server.core.convert.ConvertFacotry;
import com.bj58.spat.gaea.server.core.convert.IConvert;
import com.bj58.spat.gaea.server.util.ErrorState;
import com.bj58.spat.gaea.server.util.ServiceFrameException;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Constant {

	/**
	 * service contract config xml
	 */
	public static final String SERVICE_CONTRACT = "serviceframe.xml";

	/**
	 * out parameter name
	 */
	public static final String OUT_PARAM = Out.class.getName();

	/**
	 * IProxyStub class name
	 */
	public static final String IPROXYSTUB_CLASS_NAME = IProxyStub.class.getName();

	/**
	 * GaeaContext class name
	 */
	public static final String GAEACONTEXT_CLASS_NAME = GaeaContext.class.getName();

	/**
	 * GaeaRequest class name
	 */
	public static final String GAEARESPONSE_CLASS_NAME = GaeaResponse.class.getName();

	/**
	 * ServiceFrameException class name
	 */
	public static final String SERVICEFRAMEEXCEPTION_CLASS_NAME = ServiceFrameException.class.getName();

	/**
	 * Request protocol class name
	 */
	public static final String REQUEST_PROTOCOL_CLASS_NAME = RequestProtocol.class.getName();

	/**
	 * IConvert class name
	 */
	public static final String ICONVERT_CLASS_NAME = IConvert.class.getName();

	/**
	 * ConvertFactory class name
	 */
	public static final String CONVERT_FACTORY_CLASS_NAME = ConvertFacotry.class.getName();

	/**
	 * KeyValuePair protocol class name
	 */
	public static final String KEYVALUEPAIR_PROTOCOL_CLASS_NAME = KeyValuePair.class.getName();

	/**
	 * ErrorState class name
	 */
	public static final String ERRORSTATE_CLASS_NAME = ErrorState.class.getName();

	/**
	 * IProxyFactory class name
	 */
	public static final String IPROXYFACTORY_CLASS_NAME = IProxyFactory.class
			.getName();

	/**
	 * OperationContract class name
	 */
	public static final String OPERATIONCONTRACT_CLASS_NAME = OperationContract.class.getName();

	/**
	 * ServiceBehavior class name
	 */
	public static final String SERVICEBEHAVIOR_CLASS_NAME = ServiceBehavior.class.getName();

	/**
	 * ServiceContract class name
	 */
	public static final String SERVICECONTRACT_CLASS_NAME = ContractInfo.class.getName();
}