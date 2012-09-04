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
package com.bj58.spat.gaea.server.performance;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.MessageEvent;

import com.bj58.spat.gaea.server.contract.context.GaeaContext;
import com.bj58.spat.gaea.server.performance.commandhelper.CRLF;
import com.bj58.spat.gaea.server.performance.commandhelper.Control;
import com.bj58.spat.gaea.server.performance.commandhelper.Count;
import com.bj58.spat.gaea.server.performance.commandhelper.Exec;
import com.bj58.spat.gaea.server.performance.commandhelper.Help;
import com.bj58.spat.gaea.server.performance.commandhelper.ICommandHelper;
import com.bj58.spat.gaea.server.performance.commandhelper.Illegal;
import com.bj58.spat.gaea.server.performance.commandhelper.Quit;
import com.bj58.spat.gaea.server.performance.commandhelper.Time;

/**
 * 
 * count[|second num|method methodName]
 *         * show method call times in num seconds
 *         * second  : in num seconds statistics once (num default 1)
 *         * method  : for statistics method
 *         * example : count
 *         * example : count|second 3
 *         * example : count|second 3|method getInfo
 * 
 * time|grep abc[|group num|column -tkda]
 *         * show method execute time
 *         * grep   : condition
 *         * group  : method called num times show statistics once
 *         * column : show column a->all t->time k->key d->description
 *         * example: time|grep getInfo
 *         * example: time|grep getInfo|group 10|column -tk
 * 
 * exec|top
 *     |netstat -na
 *         * exec command  (at present only allow:top or netstat)
 *         * example: exec|top
 * 
 * control * use for control gaea-server
 * 
 * help    * show help
 * 
 * quit    * quit monitor
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Command {
	
	private CommandType commandType;
	
	private String command;
	
	private List<String> grep;
	
	private int group;
	
	private List<ShowColumn> columnList;
	
	private int second;
	
	private String method;
	

	/**
	 * 
	 */
	private static List<ICommandHelper> helperList = new ArrayList<ICommandHelper>();
	
	
	static {
		helperList.add(new CRLF());
		helperList.add(new Quit());
		helperList.add(new Count());
		helperList.add(new Exec());
		helperList.add(new Time());
		helperList.add(new Help());
		helperList.add(new Control());
		helperList.add(new Illegal());
	}
	
	
	/**
	 * 
	 * @param command
	 * @return
	 */
	public static Command create(String command) {
		Command entity = null;
		command = command.trim();
		for(ICommandHelper cc : helperList) {
			entity = cc.createCommand(command);
			if(entity != null) {
				break;
			}
		}
		if(entity == null) {
			entity = new Command();
			entity.setCommandType(CommandType.Illegal);
		}
		return entity;
	}
	
	
	/**
	 * exec command
	 * @return
	 * @throws Exception
	 */
	public void exec(MessageEvent event) throws Exception {
		for(ICommandHelper cc : helperList) {
			cc.execCommand(this, event);
		}
	}

 
	/**
	 * 
	 * @param channel
	 */
	public void removeChannel(Channel channel){
		for(ICommandHelper cc : helperList) {
			cc.removeChannel(this, channel);
		}
	}
	
	/**
	 * 
	 * @param context
	 */
	public void messageReceived(GaeaContext context) {
		for(ICommandHelper cc : helperList) {
			cc.messageReceived(context);
		}
	}
	
	/**
	 * get channel count
	 * @return
	 */
	public int getChannelCount(){
		int count = 0;
		for(ICommandHelper cc : helperList) {
			count += cc.getChannelCount();
		}
		return count;
	}
	
	
	
	
	

	public CommandType getCommandType() {
		return commandType;
	}

	public void setCommandType(CommandType commandType) {
		this.commandType = commandType;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public List<ShowColumn> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ShowColumn> columnList) {
		this.columnList = columnList;
	}
	
	public List<String> getGrep() {
		return grep;
	}

	public void setGrep(List<String> grep) {
		this.grep = grep;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public int getGroup() {
		return group;
	}
	
	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
}