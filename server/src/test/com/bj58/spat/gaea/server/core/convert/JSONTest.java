package com.bj58.spat.gaea.server.core.convert;

import org.junit.Test;

import com.bj58.sfft.json.JsonHelper;
import com.bj58.spat.gaea.protocol.utility.KeyValuePair;

public class JSONTest {

	@Test
	public void createJSON() throws Exception {

		KV[] aryKV = new KV[] {
				new KV("k1", JsonHelper.toJsonExt(new KV("k1", "v1"))),
				new KV("k2", "v2"), new KV("k3", "v3") };

		System.out.println(JsonHelper.toJsonExt(aryKV));

		KeyValuePair[] aryKVP = new KeyValuePair[] {
				new KeyValuePair("k1", "v1"), new KeyValuePair("k2", "v2"),
				new KeyValuePair("k3", "v3") };
		System.out.println(JsonHelper.toJsonExt(aryKVP));
	}

	@Test
	public void parseJSON() throws Exception {
		KV[] aryKV = new KV[] {
				new KV("k1", JsonHelper.toJsonExt(new KV("k1", JsonHelper
						.toJsonExt(new KV("k1", "v1"))))), new KV("k2", "v2"),
				new KV("k3", "v3") };
		String jsonStr = JsonHelper.toJsonExt(aryKV);

		KV[] kvs = (KV[]) JsonHelper.toJava(jsonStr, KV[].class);
		System.out.println(kvs[0].getValue());

		KeyValuePair[] aryKVP = new KeyValuePair[] {
				new KeyValuePair("k1", "v1"), new KeyValuePair("k2", "v2"),
				new KeyValuePair("k3", "v3") };
		jsonStr = JsonHelper.toJsonExt(aryKVP);
		KeyValuePair[] kvps = (KeyValuePair[]) JsonHelper.toJava(jsonStr,
				KeyValuePair[].class);
		System.out.println(kvps[0].getValue());
	}

	public static class KV {
		private String key;
		private String value;

		public KV() {

		}

		public KV(String k, String v) {
			this.key = k;
			this.value = v;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
