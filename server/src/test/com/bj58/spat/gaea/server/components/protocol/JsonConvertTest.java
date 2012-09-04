package com.bj58.spat.gaea.server.components.protocol;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.bj58.sfft.json.JsonHelper;
import com.bj58.sfft.json.orgjson.JSONObject;
import com.bj58.spat.gaea.server.core.convert.JsonConvert;

public class JsonConvertTest extends TestCase {

	public void testConvertToBoolean() throws Exception {
		String jsonStr = JsonHelper.toJsonExt(new Boolean(true));

		Boolean actual = new JsonConvert().convertToBoolean(jsonStr);
		Boolean expected = new Boolean(true);
		junit.framework.Assert.assertEquals(expected, actual);

	}

	public void testConvertToByte() throws Exception {
		for (int i = 0; i < 256; i++) {
			String jsonStr = JsonHelper.toJsonExt(new Byte((byte) i));

			Byte actual = new JsonConvert().convertToByte(jsonStr);
			Byte expected = new Byte((byte) i);
			junit.framework.Assert.assertEquals(expected, actual);
		}

		TestEntity te = TestEntity.createInstrance();
		String jsonStr = JsonHelper.toJsonExt(te);
		JSONObject jsonObj = new JSONObject(jsonStr);

		Byte actual = new JsonConvert().convertToByte(jsonObj.get("fByte"));
		junit.framework.Assert.assertEquals(te.getFByte(), actual);
	}

	public void testConvertToCharacter() throws Exception {
		for (int i = 0; i < 1; i++) {
			String jsonStr = JsonHelper.toJsonExt(new Character((char) 'a'));
			Character actual = new JsonConvert().convertToCharacter(jsonStr);
			Character expected = new Character((char) 'a');
			junit.framework.Assert.assertEquals(expected, actual);
		}

		TestEntity te = TestEntity.createInstrance();
		String jsonStr = JsonHelper.toJsonExt(te);
		JSONObject jsonObj = new JSONObject(jsonStr);

		Character actual = new JsonConvert().convertToCharacter(jsonObj
				.get("fCharacter"));
		junit.framework.Assert.assertEquals(te.getFCharacter(), actual);
	}

	public void testConvertToDouble() throws Exception {
		String jsonStr = JsonHelper.toJsonExt(new Double(123.123D));

		Double actual = new JsonConvert().convertToDouble(jsonStr);
		Double expected = new Double(123.123D);
		junit.framework.Assert.assertEquals(expected, actual);
	}

	public void testConvertToFloat() throws Exception {
		String jsonStr = JsonHelper.toJsonExt(new Float(123.123F));

		Float actual = new JsonConvert().convertToFloat(jsonStr);
		Float expected = new Float(123.123F);
		junit.framework.Assert.assertEquals(expected, actual);
	}

	public void testConvertToInteger() throws Exception {
		String jsonStr = JsonHelper.toJsonExt(new Integer(123456));

		Integer actual = new JsonConvert().convertToInteger(jsonStr);
		Integer expected = new Integer(123456);
		junit.framework.Assert.assertEquals(expected, actual);
	}

	public void testConvertToLong() throws Exception {
		String jsonStr = JsonHelper.toJsonExt(new Long(123456789012345L));

		Long actual = new JsonConvert().convertToLong(jsonStr);
		Long expected = new Long(123456789012345L);
		junit.framework.Assert.assertEquals(expected, actual);
	}

	public void testConvertToShort() throws Exception {
		String jsonStr = JsonHelper.toJsonExt(new Short((short) 1234));

		Short actual = new JsonConvert().convertToShort(jsonStr);
		Short expected = new Short((short) 1234);
		junit.framework.Assert.assertEquals(expected, actual);
	}

	public void testConvertToString() throws Exception {
		TestEntity te = TestEntity.createInstrance();
		String jsonStr = JsonHelper.toJsonExt(te);
		JSONObject jsonObj = new JSONObject(jsonStr);

		String actual = new JsonConvert().convertToString(jsonObj
				.get("fString"));
		junit.framework.Assert.assertEquals(te.getFString(), actual);
	}

	public void testConvertToboolean() throws Exception {
		TestEntity te = TestEntity.createInstrance();
		String jsonStr = JsonHelper.toJsonExt(te);
		JSONObject jsonObj = new JSONObject(jsonStr);

		boolean actual = new JsonConvert().convertToboolean(jsonObj
				.get("fboolean"));
		junit.framework.Assert.assertEquals(te.isFboolean(), actual);
	}

	public void testConvertTobyte() throws Exception {
		TestEntity te = TestEntity.createInstrance();
		String jsonStr = JsonHelper.toJsonExt(te);
		JSONObject jsonObj = new JSONObject(jsonStr);

		byte actual = new JsonConvert().convertTobyte(jsonObj.get("fbyte"));
		junit.framework.Assert.assertEquals(te.getFbyte(), actual);
	}

	public void testConvertTochar() throws Exception {
		TestEntity te = TestEntity.createInstrance();
		String jsonStr = JsonHelper.toJsonExt(te);
		JSONObject jsonObj = new JSONObject(jsonStr);

		char actual = new JsonConvert().convertTochar(jsonObj.get("fchar"));
		junit.framework.Assert.assertEquals(te.getFchar(), actual);
	}

	public void testConvertTodouble() throws Exception {
		TestEntity te = TestEntity.createInstrance();
		String jsonStr = JsonHelper.toJsonExt(te);
		JSONObject jsonObj = new JSONObject(jsonStr);

		double actual = new JsonConvert().convertTodouble(jsonObj
				.get("fdouble"));
		junit.framework.Assert.assertEquals(te.getFdouble(), actual);
	}

	public void testConvertTofloat() throws Exception {
		TestEntity te = TestEntity.createInstrance();
		String jsonStr = JsonHelper.toJsonExt(te);
		JSONObject jsonObj = new JSONObject(jsonStr);

		float actual = new JsonConvert().convertTofloat(jsonObj.get("ffloat"));
		junit.framework.Assert.assertEquals(te.getFfloat(), actual);
	}

	public void testConvertToint() throws Exception {
		TestEntity te = TestEntity.createInstrance();
		String jsonStr = JsonHelper.toJsonExt(te);
		JSONObject jsonObj = new JSONObject(jsonStr);

		int actual = new JsonConvert().convertToint(jsonObj.get("fint"));
		junit.framework.Assert.assertEquals(te.getFint(), actual);
	}

	public void testConvertTolong() throws Exception {
		TestEntity te = TestEntity.createInstrance();
		String jsonStr = JsonHelper.toJsonExt(te);
		JSONObject jsonObj = new JSONObject(jsonStr);

		long actual = new JsonConvert().convertTolong(jsonObj.get("flong"));
		junit.framework.Assert.assertEquals(te.getFlong(), actual);
	}

	public void testConvertToshort() throws Exception {
		TestEntity te = TestEntity.createInstrance();
		String jsonStr = JsonHelper.toJsonExt(te);
		JSONObject jsonObj = new JSONObject(jsonStr);

		short actual = new JsonConvert().convertToshort(jsonObj.get("fshort"));
		junit.framework.Assert.assertEquals(te.getFshort(), actual);
	}

	public void testConvertToT() throws Exception {
		TestEntity te = TestEntity.createInstrance();
		String jsonStr = JsonHelper.toJsonExt(te);

		JSONObject jsonObj = new JSONObject(jsonStr);

		TestEntityII actual = (TestEntityII) new JsonConvert().convertToT(
				jsonObj.get("fObject"), TestEntityII.class);

		junit.framework.Assert.assertEquals(
				((TestEntityII) te.getFObject()).getId(), actual.getId());
		junit.framework.Assert.assertEquals(
				((TestEntityII) te.getFObject()).getTitle(), actual.getTitle());
	}

	@SuppressWarnings("unchecked")
	public void testJSON() throws Exception {
		int[] intAry = new int[] { 1, 2, 3, 4, 5, 6 };
		String jsonStr = JsonHelper.toJsonExt(intAry);
		System.out.println(jsonStr);

		int[] intAryII = (int[]) JsonHelper.toJava(jsonStr, int[].class);
		for (int item : intAryII) {
			System.out.println(item);
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put("k1", "v1");
		map.put("k2", "v2");
		map.put("k3", "v3");
		jsonStr = JsonHelper.toJsonExt(map);
		System.out.println(jsonStr);

		Map<String, String> mapII = (Map<String, String>) JsonHelper.toJava(
				jsonStr, Map.class);
		System.out.println(mapII.get("k1"));
		System.out.println(mapII.get("k2"));
		System.out.println(mapII.get("k3"));

		TestEntityII[] teAry = new TestEntityII[] {
				new TestEntityII(1, "title1"), new TestEntityII(2, "title2") };
		jsonStr = JsonHelper.toJsonExt(teAry);
		System.out.println(jsonStr);

		teAry = (TestEntityII[]) JsonHelper.toJava(jsonStr,
				TestEntityII[].class);
		for (TestEntityII item : teAry) {
			System.out.println(item.getTitle());
		}

		jsonStr = JsonHelper.toJsonExt(null);
		System.out.println(jsonStr);
		Object obj = JsonHelper.toJava("null", TestEntityII.class);
		System.out.println(obj == null);
	}
}