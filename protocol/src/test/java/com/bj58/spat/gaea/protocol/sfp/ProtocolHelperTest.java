package com.bj58.spat.gaea.protocol.sfp;

import junit.framework.Assert;

import org.junit.Test;

import com.bj58.spat.gaea.protocol.sdp.RequestProtocol;
import com.bj58.spat.gaea.protocol.sfp.enumeration.SDPType;
import com.bj58.spat.gaea.protocol.sfp.v1.Protocol;
import com.bj58.spat.gaea.protocol.utility.ProtocolHelper;

public class ProtocolHelperTest {

	@Test
	public void testFromBytes() throws Exception {
		RequestProtocol rp = new RequestProtocol("lookup", "methodname", null);
		Protocol p = new Protocol(101001, (byte)1, SDPType.Request, rp);
		
		byte[] buffer = p.toBytes();
		
		Protocol p2 = (Protocol)ProtocolHelper.fromBytes(buffer);
		Assert.assertEquals(rp.getLookup(), ((RequestProtocol)p2.getSdpEntity()).getLookup());
		Assert.assertEquals(rp.getMethodName(), ((RequestProtocol)p2.getSdpEntity()).getMethodName());
	}
	
	@Test
	public void testProtocol() throws Exception {
		byte[] buffer = new byte[]{1,-79,0,0,0,1,0,0,0,101,2,0,4,1,-5,107,-6,25,0,-23,3,0,0,19,0,0,0,19,0,0,0,0,-22,3,0,0,1,0,0,0,39,5,-10,-65,39,5,-10,-65,0,-21,3,0,0,18,0,0,0,0,-20,3,0,0,4,0,0,0,85,115,101,114,17,-116,74,-65,17,-116,74,-65,0,-19,3,0,0,24,0,0,0,24,0,0,0,0,-18,3,0,0,1,0,0,0,5,0,0,0,29,18,0,0,0,0,-17,3,0,0,19,0,0,0,49,107,99,54,54,52,53,56,56,64,107,117,99,104,101,46,99,111,109,11,0,0,0,116,72,-59,0,0,0,0,0,18,0,0,0,1,-20,3,0,0,18,0,0,0,0,-16,3,0,0,6,0,0,0,117,112,100,97,116,101};
		Protocol p = (Protocol)ProtocolHelper.fromBytes(buffer);
		System.out.println(((RequestProtocol)p.getSdpEntity()).getLookup());
		System.out.println(((RequestProtocol)p.getSdpEntity()).getMethodName());
		
//		Assert.assertEquals(rp.getLookup(), ((RequestProtocol)p2.getSdpEntity()).getLookup());
//		Assert.assertEquals(rp.getMethodName(), ((RequestProtocol)p2.getSdpEntity()).getMethodName());
	}
	
	
	@Test
	public void testToBytes() throws Exception {
		RequestProtocol rp = new RequestProtocol("lookup", "methodname", null);
		Protocol p = new Protocol(101001, (byte)1, SDPType.Request, rp);
		
		byte[] buffer = p.toBytes();
		StringBuilder sbBuf = new StringBuilder();
		for(byte b : buffer) {
			sbBuf.append(b);
			sbBuf.append(" ");
		}
		System.out.println(sbBuf.toString());
	}

}