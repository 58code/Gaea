package com.bj58.spat.gaea.server.util;

import junit.framework.Assert;

import org.junit.Test;

import com.bj58.spat.gaea.server.util.EndiannessHelper;

public class EndiannessTest {

	@Test
	public void testBytesToIntLittleEndian() {
		byte[] buf = new byte[] { 10, 0, 0, 0 };
		int n = EndiannessHelper.bytesToIntLittleEndian(buf);
		Assert.assertEquals(10, n);

		byte[] buf2 = new byte[] { -97, 0, 0, 10 };
		int n2 = EndiannessHelper.bytesToIntLittleEndian(buf2);
		Assert.assertEquals(167772319, n2);
	}

	@Test
	public void testIntToBytesLittleEndian() {
		byte[] buf = EndiannessHelper.intToBytesLittleEndian(1);
		Assert.assertEquals(1, buf[0]);
		Assert.assertEquals(0, buf[1]);
		Assert.assertEquals(0, buf[2]);
		Assert.assertEquals(0, buf[3]);

		byte[] buf2 = EndiannessHelper.intToBytesLittleEndian(1891);
		Assert.assertEquals(99, buf2[0]);
		Assert.assertEquals(7, buf2[1]);
		Assert.assertEquals(0, buf2[2]);
		Assert.assertEquals(0, buf2[3]);

		Assert.assertEquals(1891, EndiannessHelper.bytesToIntLittleEndian(buf2));
	}

	@Test
	public void testBytesToIntBigEndian() {
		byte[] buf2 = new byte[] { -97, 0, 0, 10 };
		int n2 = EndiannessHelper.bytesToIntBigEndian(buf2);
		Assert.assertEquals(-1627389942, n2);
	}

	@Test
	public void testIntToBytesBigEndian() {

		byte[] buf = EndiannessHelper.intToBytesBigEndian(10);
		Assert.assertEquals(0, buf[0]);
		Assert.assertEquals(0, buf[1]);
		Assert.assertEquals(0, buf[2]);
		Assert.assertEquals(10, buf[3]);

		byte[] buf2 = EndiannessHelper.intToBytesBigEndian(1891);
		Assert.assertEquals(0, buf2[0]);
		Assert.assertEquals(0, buf2[1]);
		Assert.assertEquals(7, buf2[2]);
		Assert.assertEquals(99, buf2[3]);

		Assert.assertEquals(1891, EndiannessHelper.bytesToIntBigEndian(buf2));
	}
}