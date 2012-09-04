package com.bj58.spat.gaea.server.performance.commandhelper;

import static org.junit.Assert.*;

import org.junit.Test;

import com.bj58.spat.gaea.server.performance.Command;
import com.bj58.spat.gaea.server.performance.CommandType;
import com.bj58.spat.gaea.server.performance.commandhelper.Exec;

public class ExecTest {

	@Test
	public void testCreateCommand() {
		Exec exec = new Exec();
		Command command1 = exec.createCommand("exec|netstat -na");
		assertEquals(CommandType.Exec, command1.getCommandType());
		assertEquals("netstat -na", command1.getCommand());
		
		Command command2 = exec.createCommand("exec|killall java");
		assertEquals(CommandType.Illegal, command2.getCommandType());
	}

}
