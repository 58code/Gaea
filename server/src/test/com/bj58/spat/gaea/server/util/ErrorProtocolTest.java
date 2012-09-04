package com.bj58.spat.gaea.server.util;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.bj58.spat.gaea.server.util.ExceptionHelper;

public class ErrorProtocolTest {

//	static {
//		try {
//			ServiceConfig.createInstance("D:/serviceframe_v2_II/bin/imc_config.xml");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	@Test
	public void testCreateErrorServiceFrameException() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateErrorErrorStateStringString() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateErrorErrorStateStringStringException() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateErrorException() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateErrorProtocol() {
		byte[] buf = ExceptionHelper.createErrorProtocol();
		System.out.println("length:"+buf.length);
		for(byte b : buf) {
			System.out.println(b);
		}
	}
}
