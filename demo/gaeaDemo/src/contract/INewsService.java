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
package contract;

import java.util.List;
import com.bj58.spat.gaea.server.contract.annotation.OperationContract;
import com.bj58.spat.gaea.server.contract.annotation.ServiceContract;

import entity.News;

/**
 * 对外提供服务接口类
 * 
 * @ServiceContract 标记该接口对外提供服务
 * @OperationContract 标记该方法对外暴露
 * 
 * @author @author Service Platform Architecture Team (spat@58.com)
 */
@ServiceContract
public interface INewsService {
	@OperationContract
	public News getNewsByID(int newsID) throws Exception;

	@OperationContract
	public List<News> getNewsByCateID() throws Exception;
}
