package com.bj58.spat.gaea.server.secure;

import junit.framework.Assert;

import org.junit.Test;

import com.bj58.spat.gaea.server.secure.DESHelper;

public class DESHelperTest {

	@Test
	public void testEncrypt() throws Exception {
		DESHelper desHelper = DESHelper.getInstance("abcdefgh");
		String src = "q3rgtasdfgqet83tseflgasdf^@$FSGaweg";
		
		byte[] destBuf = desHelper.encrypt(src.getBytes("utf-8"));
		byte[] srcBuf = desHelper.decrypt(destBuf);
		Assert.assertEquals(src, new String(srcBuf, "utf-8"));
	}
	

	@Test
	public void testDecrypt() throws Exception {
		DESHelper desHelper = DESHelper.getInstance("11111111");
		String src = "23ra阿豆腐花确认嘎斯大户带回去东风史蒂夫核污染格俄哇凤凰sdgfasdfwef--------------^@$FSGaweg";
		
		byte[] destBuf = desHelper.encrypt(src.getBytes("utf-8"));
		byte[] srcBuf = desHelper.decrypt(destBuf);
		Assert.assertEquals(src, new String(srcBuf, "utf-8"));
	}

}