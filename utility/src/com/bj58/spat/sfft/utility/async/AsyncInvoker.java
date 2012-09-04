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
package com.bj58.spat.sfft.utility.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bj58.spat.sfft.utility.tools.ThreadRenameFactory;

public class AsyncInvoker {
	
	/**
	 * Round Robin
	 */
	private int rr = 0;
	
	/**
	 * 工作线程
	 */
	private AsyncWorker[] workers = null;
	
	/**
	 * 获取AsyncInvoker实例
	 * @param workerCount 工作线程数
	 * @return
	 */
	public static AsyncInvoker getInstance(int workerCount) {
		return new AsyncInvoker(workerCount, true);
	}
	
	/**
	 * 获取AsyncInvoker实例(默认工作线程数为CPU的个数)
	 * @return
	 */
	public static AsyncInvoker getInstance() {
		//获取CPU个数
		int cpuCount = Runtime.getRuntime().availableProcessors();
		return new AsyncInvoker(cpuCount, true);
	}
	
	/**
	 * 获取AsyncInvoker实例
	 * @param workerCount 工作线程数
	 * @param timeoutEffect 是否启用调用超时
	 * @return
	 */
	public static AsyncInvoker getInstance(int workerCount, boolean timeoutEffect) {
		return new AsyncInvoker(workerCount, timeoutEffect);
	}
	
	
	/**
	 * 获取AsyncInvoker实例
	 * @param workerCount 工作线程个数
	 */
	private AsyncInvoker(int workerCount, boolean timeoutEffect) {
		workers = new AsyncWorker[workerCount];
		ExecutorService executor = Executors.newCachedThreadPool(new ThreadRenameFactory("async task thread"));
		
		for(int i=0; i<workers.length; i++) {
			workers[i] = new AsyncWorker(executor, timeoutEffect);
			workers[i].setDaemon(true);
			workers[i].setName("async task worker[" + i + "]");
			workers[i].start();
		}
	}

	/**
	 * 执行异步任务(无阻塞立即返回,当前版本只实现轮询分配,下个版本增加工作线程间的任务窃取)
	 * @param task
	 */
	@Deprecated
	public void run(AsyncTask task) {
		if(rr > 10000) {
			rr = 0;
		}
		int idx = rr % workers.length;
		workers[idx].addTask(task);
		++rr;
	}
	
	/**
	 * 执行异步任务(无阻塞立即返回,当前版本只实现轮询分配,下个版本增加工作线程间的任务窃取)
	 * @param timeOut 超时时间
	 * @param handler 任务handler
	 */
	public void run(int timeOut, IAsyncHandler handler) {
		AsyncTask task = new AsyncTask(timeOut, handler);
		if(rr > 10000) {
			rr = 0;
		}
		int idx = rr % workers.length;
		workers[idx].addTask(task);
		++rr;
	}
	
	/**
	 * 停止所有工作线程
	 */
	public void stop() {
		for(AsyncWorker worker : workers) {
			worker.end();
		}
	}
}