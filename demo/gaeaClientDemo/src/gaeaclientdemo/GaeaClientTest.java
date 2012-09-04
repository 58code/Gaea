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
package gaeaclientdemo;

import entity.News;
import java.util.List;
import contract.INewsService;
import com.bj58.spat.gaea.client.GaeaInit;
import com.bj58.spat.gaea.client.proxy.builder.ProxyFactory;

public class GaeaClientTest {
	public static void main(String[] args) throws Exception {
		// 加载配置文件
		GaeaInit.init("e:/gaea.config");
		/**
		 * 调用URL 格式:tcp://服务名//接口实现类 
		 * 备注: 
		 * 服务名：需要与gaea.config中的服务名一一对应
		 * 接口实现类：具体调用接口的接口实现类
		 */

		final String url = "tcp://demo/NewsService";
		INewsService newsService = ProxyFactory.create(INewsService.class, url);
		List<News> list = newsService.getNewsByCateID();
		for (News news : list) {
			System.out.println("ID is " + news.getNewsID() + " title is "
					+ news.getTitle());
		}
	}
}
