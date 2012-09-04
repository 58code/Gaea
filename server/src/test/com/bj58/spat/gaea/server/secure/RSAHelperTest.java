package com.bj58.spat.gaea.server.secure;

import junit.framework.Assert;

import org.junit.Test;

import com.bj58.spat.gaea.server.secure.RSAHelper;

public class RSAHelperTest {
	
	private static String src = "afh425yqdsf*$#^Q@#%HFGKSHFKG@#$%SFDGSFGK@$TDSF";


	@Test
	public void testDecryptByPubKey() throws Exception {
		RSAHelper helper = RSAHelper.getInstance();
		byte[] destBuf = helper.encryptByPrivateKey(src.getBytes("utf-8"));
		byte[] srcBuf = helper.decryptByPublicKey(destBuf);
		Assert.assertEquals(src, new String(srcBuf, "utf-8"));
	}

	@Test
	public void testDecryptByPriKey() throws Exception {
		RSAHelper helper = RSAHelper.getInstance();
		byte[] destBuf = helper.encryptByPublicKey(src.getBytes("utf-8"));
		byte[] srcBuf = helper.decryptByPrivateKey(destBuf);
		Assert.assertEquals(src, new String(srcBuf, "utf-8"));
	}
	
	@Test
	public void testPublicKey() throws Exception {
		RSAHelper helper1 = RSAHelper.getInstance();
		String exponent = helper1.getPubExponent();
		String modulus = helper1.getPubModulus();
		System.out.println("-----------------------------------");
		
		byte[] expBuf = exponent.getBytes("utf-8");
		byte[] modBuf = modulus.getBytes("utf-8");
		for(int i=0; i<expBuf.length; i++) {
			System.out.print(expBuf[i] + ",");
		}
		System.out.println("\n-----------------------------------");
		for(int i=0; i<modBuf.length; i++) {
			System.out.print(modBuf[i] + ",");
		}
		System.out.println("\n-----------------------------------");
		System.out.println(exponent);
		System.out.println("-----------------------------------");
		System.out.println(modulus);
		System.out.println("-----------------------------------");
		
		
		byte[] buf = helper1.encryptByPrivateKey(src.getBytes("utf-8"));
		for(int i=0; i<buf.length; i++) {
			System.out.print((buf[i] < 0 ? buf[i] + 256 : buf[i]) + ",");
		}
		
		
		
		RSAHelper helper2 = RSAHelper.getInstance(modulus, exponent);
		byte[] destBuf = helper2.encryptByPublicKey(src.getBytes("utf-8"));
		byte[] srcBuf = helper1.decryptByPrivateKey(destBuf);
		Assert.assertEquals(src, new String(srcBuf, "utf-8"));
	}
	
	
	
	@Test
	public void testCustomKey() throws Exception {
		String modulus = new String(new byte[]{65,76,66,70,47,121,82,48,116,99,82,104,118,102,89,81,43,120,114,67,77,66,76,90,103,120,98,119,77,53,74,50,88,71,73,68,121,71,82,67,108,56,86,84,122,66,105,52,116,83,88,116,76,102,86,83,73,77,114,87,119,114,84,102,100,90,83,70,52,80,87,106,121,79,50,65,13,10,97,81,57,66,115,108,65,122,74,71,114,56,110,51,76,104,113,110,114,120,117,102,78,98,47,85,69,56,54,109,50,66,69,51,102,112,83,116,98,109,71,57,66,110,98,97,52,78,120,56,73,50,69,85,84,121,110,101,104,49,98,72,70,121,87,67,85,111,121,79,73,103,106,98,120,79,13,10,111,47,81,49,73,121,104,87,53,116,52,98,98,105,80,109,74,104,71,72,13,10}, "utf-8");
		String exponent = new String(new byte[]{65,81,65,66,13,10}, "utf-8");
		
		RSAHelper helper = RSAHelper.getInstance(modulus, exponent);
		byte[] destBuf = helper.encryptByPrivateKey(src.getBytes("utf-8"));
		
		for(int i=0; i<destBuf.length; i++) {
			System.out.print(destBuf[i] + ",");
		}
	}
}