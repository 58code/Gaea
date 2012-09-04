package com.bj58.spat.gaea.server.performance.commandhelper;

import static org.junit.Assert.*;

import org.junit.Test;

import com.bj58.spat.gaea.server.performance.Command;
import com.bj58.spat.gaea.server.performance.CommandType;
import com.bj58.spat.gaea.server.performance.ShowColumn;
import com.bj58.spat.gaea.server.performance.commandhelper.Time;

public class TimeTest {

	@Test
	public void testCreateCommand() {
		Time time = new Time();
		Command command1 = time.createCommand("time|grep abc|group 10|column -kd");
		assertEquals(CommandType.Time, command1.getCommandType());
		assertEquals("abc", command1.getGrep().get(0));
		assertEquals(ShowColumn.Key, command1.getColumnList().get(0));
		assertEquals(ShowColumn.Description, command1.getColumnList().get(1));
		assertEquals(2, command1.getColumnList().size());
		assertEquals(10, command1.getGroup());
		
		
		Command command2 = time.createCommand("time|grep 123");
		assertEquals(CommandType.Time, command2.getCommandType());
		assertEquals("123", command2.getGrep().get(0));
		assertEquals(ShowColumn.All, command2.getColumnList().get(0));
		assertEquals(1, command2.getColumnList().size());
		assertEquals(0, command2.getGroup());
	}

}
