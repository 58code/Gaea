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
package com.bj58.spat.gaea.server.bootstrap.serverframe;

import java.util.Enumeration;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * CheckNode
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class CheckNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1395199707680929698L;
	/**
	 * 是否选择
	 */
	protected boolean isSelected;

	public CheckNode() {
		this(null);
	}

	public CheckNode(Object userObject) {
		this(userObject, true, false);
	}

	public CheckNode(Object userObject, boolean allowsChildren,boolean isSelected) {
		super(userObject, allowsChildren);
		this.isSelected = isSelected;
	}

	/**
	 * 设置子目录全选/取消
	 * @param isSelected
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		if ((children != null)) {
			Enumeration enumOne = children.elements();
			while (enumOne.hasMoreElements()) {
				CheckNode node = (CheckNode) enumOne.nextElement();
				node.setSelected(isSelected);
			}
		}
	}
	/**
	 * 判断当前节点是否已选择
	 * @return 是否选择
	 */
	public boolean isSelected() {
		return isSelected;
	}
}
