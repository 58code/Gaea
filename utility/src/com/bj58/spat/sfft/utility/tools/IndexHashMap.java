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
package com.bj58.spat.sfft.utility.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 顺序敏感的HashMap
 * get(int idx)/remove(int idx) 为按加入hashMap的顺序号 (index) 来取得/删除 数据
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class IndexHashMap extends HashMap {

	private static final long serialVersionUID = 1L;

	private List list = new ArrayList();

	public Object put(Object key, Object value) {
		if (!containsKey(key)) {
			list.add(key);
		}
		return super.put(key, value);
	}

	public Object get(int idx) {
		return super.get(getKey(idx));
	}

	public int getIndex(Object key) {
		return list.indexOf(key);
	}

	public Object getKey(int idx) {
		if (idx >= list.size())
			return null;
		return list.get(idx);
	}

	public void remove(int idx) {
		Object key = getKey(idx);
		removeFromList(getIndex(key));
		super.remove(key);
	}

	public Object remove(Object key) {
		removeFromList(getIndex(key));
		return super.remove(key);
	}

	public void clear() {
		this.list = new ArrayList();
		super.clear();
	}

	private void removeFromList(int idx) {
		if (idx < list.size() && idx >= 0) {
			list.remove(idx);
		}
	}
}