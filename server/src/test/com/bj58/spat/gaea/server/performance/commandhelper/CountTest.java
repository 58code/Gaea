package com.bj58.spat.gaea.server.performance.commandhelper;

import junit.framework.Assert;

import org.junit.Test;

import com.bj58.spat.gaea.server.performance.Command;
import com.bj58.spat.gaea.server.performance.CommandType;
import com.bj58.spat.gaea.server.performance.commandhelper.Count;

public class CountTest {

	@Test
	public void testCreateCommand() {
		Count count = new Count();
		Command command1 = count.createCommand("count");
		Assert.assertEquals(CommandType.Count, command1.getCommandType());
		Assert.assertEquals("#all#", command1.getMethod());
		Assert.assertEquals(1, command1.getSecond());
		
		
		Command command2 = count.createCommand("count|second 2|method getInfo");
		Assert.assertEquals(CommandType.Count, command2.getCommandType());
		Assert.assertEquals("getInfo", command2.getMethod());
		Assert.assertEquals(2, command2.getSecond());
	}
}
