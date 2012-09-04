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
package com.bj58.spat.gaea.server.performance.commandhelper;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.MessageEvent;

import com.bj58.spat.gaea.server.contract.context.GaeaContext;
import com.bj58.spat.gaea.server.performance.Command;
import com.bj58.spat.gaea.server.performance.CommandType;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class Help extends CommandHelperBase {

	@Override
	public Command createCommand(String commandStr) {
		if(commandStr != null && !commandStr.equalsIgnoreCase("")) {
			if(commandStr.equalsIgnoreCase("help")) {
				Command entity = new Command();
				entity.setCommandType(CommandType.Help);
				return entity;
			}
		}
		return null;
	}

	
	@Override
	public void execCommand(Command command, MessageEvent event) throws Exception {
		if(command.getCommandType() == CommandType.Help) {
			StringBuilder sbMsg = new StringBuilder();
			sbMsg.append("*******************************************************************\r\n\n");
			
			sbMsg.append("count[|second num|method methodName]\r\n");
			sbMsg.append("\t* show method call times in num seconds\r\n");
			sbMsg.append("\t* second  : in num seconds statistics once (num default 1)\r\n");
			sbMsg.append("\t* method  : for statistics method\r\n");
			sbMsg.append("\t* example : count\r\n");
			sbMsg.append("\t* example : count|second 3\r\n");
			sbMsg.append("\t* example : count|second 3|method getInfo\r\n\n");
			
			sbMsg.append("time|grep abc[|group num|column -tkda]\r\n");
			sbMsg.append("\t* show method execute time\r\n");
			sbMsg.append("\t* grep   : condition\r\n");
			sbMsg.append("\t* group  : method called num times show statistics once\r\n");
			sbMsg.append("\t* column : show column a->all t->time k->key d->description\r\n");
			sbMsg.append("\t* example: time|grep getInfo\r\n");
			sbMsg.append("\t* example: time|grep getInfo|group 10|column -tk\r\n\n");
			
			sbMsg.append("exec|top\r\n");
			sbMsg.append("    |netstat -na\r\n");
			sbMsg.append("\t* exec command  (at present only allow:top or netstat)\r\n");
			sbMsg.append("\t* example: exec|top\r\n\n");
			
			sbMsg.append("control * use for control gaea-server\r\n\n");
			
			sbMsg.append("help    * show help\r\n\n");
			
			sbMsg.append("quit    * quit monitor\r\n\n");
			
			sbMsg.append("*******************************************************************\r\n\n");
			byte[] responseByte = sbMsg.toString().getBytes("utf-8");
			event.getChannel().write(ChannelBuffers.copiedBuffer(responseByte));
		}
	}
	
	@Override
	public void messageReceived(GaeaContext context) {
		// do nothing
	}
	
	@Override
	public void removeChannel(Command command, Channel channel) {
		// do nothing
	}
	
	@Override
	public int getChannelCount() {
		return 0;
	}
}