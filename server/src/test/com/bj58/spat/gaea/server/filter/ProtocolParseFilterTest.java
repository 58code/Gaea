package com.bj58.spat.gaea.server.filter;

import org.junit.Test;

import com.bj58.spat.gaea.server.filter.ProtocolParseFilter;

public class ProtocolParseFilterTest {

	@Test
	public void testFilter() {
		byte[] buf = new byte[]{1,-79,0,0,0,1,0,0,0,101,2,0,4,1,-5,107,-6,25,0,-23,3,0,0,19,0,0,0,19,0,0,0,0,-22,3,0,0,1,0,0,0,39,5,-10,-65,39,5,-10,-65,0,-21,3,0,0,18,0,0,0,0,-20,3,0,0,4,0,0,0,85,115,101,114,17,-116,74,-65,17,-116,74,-65,0,-19,3,0,0,24,0,0,0,24,0,0,0,0,-18,3,0,0,1,0,0,0,5,0,0,0,29,18,0,0,0,0,-17,3,0,0,19,0,0,0,49,107,99,54,54,52,53,56,56,64,107,117,99,104,101,46,99,111,109,11,0,0,0,116,72,-59,0,0,0,0,0,18,0,0,0,1,-20,3,0,0,18,0,0,0,0,-16,3,0,0,6,0,0,0,117,112,100,97,116,101};
		ProtocolParseFilter filter = new ProtocolParseFilter();
	}

}
