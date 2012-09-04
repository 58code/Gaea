package com.bj58.spat.gaea.server.core.communication.http;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Test;

import com.bj58.sfft.json.JsonHelper;
import com.bj58.spat.gaea.server.core.communication.http.KeyValue;

public class PostTest {

	@Test
	public void testDate() {
		java.util.Date d1 = new java.util.Date();
		java.sql.Date d2 = new java.sql.Date(d1.getTime());
		java.util.Date d3 = new java.util.Date(d2.getTime());

		Assert.assertEquals(d1.getTime(), d2.getTime());
		Assert.assertEquals(d1.getTime(), d3.getTime());
		Assert.assertEquals(d2.getTime(), d3.getTime());
	}

	@Test
	public void testDel() throws Exception {
		String url = "http://127.0.0.1:8099/del/";
		KeyValue[] kv = new KeyValue[] { new KeyValue("newsID", "101001") };
		String content = JsonHelper.toJsonExt(kv);
		System.out.println(content);

		byte[] buffer = content.getBytes("utf-8");

		String response = post(url, buffer);
		System.out.println(response);
	}

	@Test
	public void testAdd() throws Exception {
		String url = "http://127.0.0.1:8099/add/";

		News news = new News();
		news.setAddTime(new Date());
		news.setContent("content");
		news.setNewsID(101001);
		news.setTitle("title");
		String jsonStr = JsonHelper.toJsonExt(news);

		KeyValue[] kv = new KeyValue[] { new KeyValue("news", jsonStr) };
		String content = JsonHelper.toJsonExt(kv);
		System.out.println(content);

		byte[] buffer = content.getBytes("utf-8");

		String response = post(url, buffer);
		System.out.println(response);
	}

	public String post(String url, byte[] rb) throws Exception {

		HttpWebRequest request = null;
		HttpWebResponse response = null;
		try {
			request = HttpWebRequest.create(url);
			request.setMethod(HttpMethod.POST);
			request.setContentType("multipart/form-data;");
			request.setContentLength(rb.length);
			request.setTimeOut(3000);

			request.write(rb);

			response = request.getResponse();

			int status = 0;
			String statusStr = response.getHeaders().get("Status");
			if (statusStr != null && !statusStr.equalsIgnoreCase("")) {
				status = Integer.parseInt(statusStr);
			}
			if (response.getStatusCode() == 200) {
				if (status == 0) {
					byte[] buffer = response.getContentBuffer();
					return new String(buffer, "utf-8");
				} else {
					throw new Exception("请求被禁止:" + status);
				}
			} else {
				byte[] buffer = response.getContentBuffer();
				String msg = new String(buffer, "utf-8");
				throw new Exception("HttpStatusCode="
						+ response.getStatusCode() + " 内部服务器错误:" + msg);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (response != null) {
					response.close();
					response = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (request != null) {
					request.close();
					request = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}