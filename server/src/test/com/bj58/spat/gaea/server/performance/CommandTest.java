package com.bj58.spat.gaea.server.performance;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.bj58.spat.gaea.server.performance.Command;
import com.bj58.spat.gaea.server.performance.CommandType;
import com.bj58.spat.gaea.server.performance.ShowColumn;

public class CommandTest {

	@Test
	public void testGetCommandString() {
		Command command1 = Command.create("time|grep abc|group 10|column -kd");
		assertEquals(CommandType.Time, command1.getCommandType());
		assertEquals("abc", command1.getGrep().get(0));
		assertEquals(ShowColumn.Key, command1.getColumnList().get(0));
		assertEquals(ShowColumn.Description, command1.getColumnList().get(1));
		assertEquals(2, command1.getColumnList().size());
		assertEquals(10, command1.getGroup());
		
		
		Command command2 = Command.create("exec|netstat -na");
		assertEquals(CommandType.Exec, command2.getCommandType());
		assertEquals("netstat -na", command2.getCommand());
		
		
		Command command3 = Command.create("time|grep abc");
		assertEquals(CommandType.Time, command3.getCommandType());
		assertEquals("abc", command3.getGrep().get(0));
		assertEquals(ShowColumn.All, command3.getColumnList().get(0));
		assertEquals(1, command3.getColumnList().size());
		assertEquals(0, command3.getGroup());
		
		
		Command command4 = Command.create("count");
		assertEquals(CommandType.Count, command4.getCommandType());
		assertEquals("#all#", command4.getMethod());
		assertEquals(1, command4.getSecond());
	}
}
