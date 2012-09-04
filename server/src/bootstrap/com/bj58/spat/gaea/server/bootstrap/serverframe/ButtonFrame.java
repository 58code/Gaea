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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.tree.TreePath;

import com.bj58.spat.gaea.server.secure.MDCodeHelper;

/**
 * ButtonFrame
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class ButtonFrame extends JPanel {
	
	private static final long serialVersionUID = 0L;
	/**
	 * 根据选择树内容生成对应key文件
	 */
	JTextArea keyjta;
	/**
	 * add button | button listener
	 */
	public ButtonFrame(JTextArea keyjta) {
		this.keyjta = keyjta;
		buildButton();
	}
	/**build button*/
	private void buildButton(){
		JButton jbSure = new JButton("确定");
		jbSure.setActionCommand("sure");
		ActionListener jb_listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processButtonEvent(e);
			}
		};
		jbSure.addActionListener(jb_listener);
		add(jbSure);
	}

	/** 处理按钮事件 */
	private void processButtonEvent(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("sure")) {
			Set<TreePath> set = TreeFrame.getCheckingPaths();			
			StringBuffer tempSpot = new StringBuffer("<?xml version=\"1.0\"?>\r\n<secure>\r\n<key>");
			
			try {
				tempSpot.append(MDCodeHelper.encodeSHA256(String.valueOf(System.nanoTime())+String.valueOf(new Random().nextLong())));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			tempSpot.append("</key>\r\n");
			tempSpot.append("<method>");
			
			int num = 0;
			for(TreePath tp:set){
				++num;
				if(tp != null){
					for(int counter = 1, maxCounter = tp.getPathCount();counter < maxCounter;counter++) {
				    	if(maxCounter<3){
				     		break;
				       	}
				    	String nameStr = tp.getPathComponent(counter).toString();
				    	if(counter == 1){
				    		nameStr = nameStr.substring(nameStr.lastIndexOf(".")+1);
				    	}
				        if(counter > 1){
				        	tempSpot.append(".");
				        }
				    	
				        tempSpot.append(nameStr);
				    	nameStr = null;
				    }
				    if(tp.getPathCount()>2 && num < set.size()){
				    	tempSpot.append(":");
				    }
				}
			}
			tempSpot.append("</method>\r\n");
			tempSpot.append("</secure>");
			//tempSpot.append("\r\n");
			
			keyjta.setText(tempSpot.toString());
		}
	}
}
