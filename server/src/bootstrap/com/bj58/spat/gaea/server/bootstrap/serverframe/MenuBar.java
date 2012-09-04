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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.bj58.spat.gaea.server.bootstrap.FrameMain;

/**
 * CheckNode
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class MenuBar {
	
	FrameMain framemain;

	/**Build MenuBar method*/
	public JMenuBar bulidMenuBar(FrameMain framemain) {
		this.framemain = framemain;
		return createMenuBar();
	}
	/**create MenuBar*/
	private JMenuBar createMenuBar(){
		JMenuBar mb = new JMenuBar();
		/**file*/
		JMenu m_file = new JMenu("File");
		JMenuItem mi_exit = new JMenuItem("exit");
		mi_exit.setActionCommand("exit");
		/**window*/
		JMenu m_advanced = new JMenu("Winodw");
		JMenuItem mi_deploy = new JMenuItem("preferences");
		mi_deploy.setActionCommand("deploy");
		/**help*/
		JMenu m_help = new JMenu("Help");
		JMenuItem mi_help = new JMenuItem("help");
		mi_help.setActionCommand("help");
		JMenuItem mi_about = new JMenuItem("about");
		mi_about.setActionCommand("about");
		/**add menu listener*/
		ActionListener ac_listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processMenuEvent(e);
			}
		};
		/**增加按钮监听*/
		mi_exit.addActionListener(ac_listener);
		mi_deploy.addActionListener(ac_listener);
		mi_about.addActionListener(ac_listener);
		mi_help.addActionListener(ac_listener);
		/**构建菜单*/
		m_file.add(mi_exit);
		m_advanced.add(mi_deploy);
		m_help.add(mi_help);
		m_help.add(mi_about);
		mb.add(m_file);
		mb.add(m_advanced);
		mb.add(m_help);
		return mb;
	}
	
	
	/**菜单事件的方法*/
	private void processMenuEvent(ActionEvent e) {
		String command = e.getActionCommand();		
		if ("deploy".equals(command)) {
			String fileUrl = JOptionPane.showInputDialog("Please input file path:");
			if(fileUrl==null){
				showMessage(null, "Please input file path", "Message ",JOptionPane.INFORMATION_MESSAGE);
                return; 
			}
			if("".equals(fileUrl)){
				showMessage(null, "Please input the correct file path", "Message ",JOptionPane.ERROR_MESSAGE);
                return; 
			}
			AssistUtils.setPath(fileUrl);/**设置路径为全局变量*/
			/**生成主窗体*/
			framemain.createGUI();
		} 
		if ("exit".equals(command)) {
			System.exit(0);
		}
		if ("about".equals(command)) {
			showMessage(null, " author: Service Platform Architecture Team\r\n version: 1.0\r\n mail: spat@58.com", "About...", JOptionPane.PLAIN_MESSAGE);
			return;
		}
	}
	
	/**ShowMessage*/
	private static void showMessage(Component component,Object messgae,String title,int messageType){
		JOptionPane.showMessageDialog(component, messgae,title, messageType);
	}
}
