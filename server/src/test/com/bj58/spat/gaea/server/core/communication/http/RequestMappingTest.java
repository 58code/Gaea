package com.bj58.spat.gaea.server.core.communication.http;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Assert;

import org.junit.Test;

import com.bj58.spat.gaea.server.contract.annotation.HttpParameterType;
import com.bj58.spat.gaea.server.contract.annotation.HttpRequestMethod;
import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.context.ServiceConfig;
import com.bj58.spat.gaea.server.core.communication.http.Action;
import com.bj58.spat.gaea.server.core.communication.http.Controler;
import com.bj58.spat.gaea.server.core.communication.http.HttpContext;
import com.bj58.spat.gaea.server.core.communication.http.RequestMapping;
import com.bj58.spat.gaea.server.core.communication.http.Action.Parameter;
import com.bj58.spat.gaea.server.deploy.bytecode.ClassInfo.ParamInfo;

public class RequestMappingTest {

	@SuppressWarnings("unchecked")
	private Map<String, Controler> init() throws Exception {
		ServiceConfig sc = ServiceConfig
				.getServiceConfig("E:/javaproject/bj58.gaea.server/config/demo_config_all.xml");
		Global.getSingleton().setServiceConfig(sc);
		Global.getSingleton().setRootPath("D:/gaea/server/");

		Class<?> cls = RequestMapping.class;
		Method m = cls.getDeclaredMethod("init");
		m.setAccessible(true);
		Field f = cls.getDeclaredField("urlMap");
		f.setAccessible(true);
		m.invoke(cls);

		return (Map<String, Controler>) f.get(cls);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testInit() throws Exception {

		Map<String, Controler> urlMap = init();

		Iterator<?> it = urlMap.entrySet().iterator();

		List<Map.Entry<String, Controler>> entryList = new ArrayList<Map.Entry<String, Controler>>();

		while (it.hasNext()) {
			entryList.add((Entry) it.next());
		}

		Assert.assertEquals("/news/(\\d+)", entryList.get(1).getKey());
		Assert.assertEquals("RestService", entryList.get(1).getValue()
				.getGetAction().getLookup());
		Assert.assertEquals("getNews", entryList.get(1).getValue()
				.getGetAction().getMethodName());
		Assert.assertEquals("newsID", entryList.get(1).getValue()
				.getGetAction().getParamList().get(0).getMapping());
		Assert.assertEquals(0, entryList.get(1).getValue().getGetAction()
				.getParamList().get(0).getMethodParaIndex());
		Assert.assertEquals(0, entryList.get(1).getValue().getGetAction()
				.getParamList().get(0).getUrlParaIndex());
		Assert.assertEquals(HttpParameterType.PathParameter, entryList.get(1)
				.getValue().getGetAction().getParamList().get(0).getParaType());

		Assert.assertEquals("/newslist/(\\d+)/(\\d+)", entryList.get(0)
				.getKey());
		Assert.assertEquals("RestService", entryList.get(0).getValue()
				.getGetAction().getLookup());
		Assert.assertEquals("getNews", entryList.get(0).getValue()
				.getGetAction().getMethodName());

		Assert.assertEquals("cateID", entryList.get(0).getValue()
				.getGetAction().getParamList().get(0).getMapping());
		Assert.assertEquals(0, entryList.get(0).getValue().getGetAction()
				.getParamList().get(0).getMethodParaIndex());
		Assert.assertEquals(0, entryList.get(0).getValue().getGetAction()
				.getParamList().get(0).getUrlParaIndex());
		Assert.assertEquals(HttpParameterType.PathParameter, entryList.get(0)
				.getValue().getGetAction().getParamList().get(0).getParaType());

		Assert.assertEquals("userID", entryList.get(0).getValue()
				.getGetAction().getParamList().get(1).getMapping());
		Assert.assertEquals(1, entryList.get(0).getValue().getGetAction()
				.getParamList().get(1).getMethodParaIndex());
		Assert.assertEquals(1, entryList.get(0).getValue().getGetAction()
				.getParamList().get(1).getUrlParaIndex());
		Assert.assertEquals(HttpParameterType.PathParameter, entryList.get(0)
				.getValue().getGetAction().getParamList().get(1).getParaType());

	}

	@Test
	public void testGetUrlParaIndex() throws Exception {
		int expected = 0;
		int actual = RequestMapping.getUrlParaIndex("/news/{newsID}/{cateID}",
				"newsID");
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testGetActionHttpContext() throws Exception {
		init();

		HttpContext hc = new HttpContext();
		hc.setUri("/news/101001");
		hc.setMethod(HttpRequestMethod.GET);

		Action action = RequestMapping.getAction(hc);
		List<Parameter> paraList = action.getParamList();

		Assert.assertEquals("RestService", action.getLookup());
		Assert.assertEquals("getNews", action.getMethodName());
		Assert.assertEquals(1, paraList.size());
		Assert.assertEquals("newsID", paraList.get(0).getMapping());
		Assert.assertEquals(0, paraList.get(0).getMethodParaIndex());
		Assert.assertEquals(0, paraList.get(0).getUrlParaIndex());
		Assert.assertEquals(101001, paraList.get(0).getValue());
		Assert.assertEquals(HttpParameterType.PathParameter, paraList.get(0)
				.getParaType());

		hc.setUri("/newslist/101002/123");
		action = RequestMapping.getAction(hc);

		Assert.assertEquals("RestService", action.getLookup());
		Assert.assertEquals("getNews", action.getMethodName());
		Assert.assertEquals(2, action.getParamList().size());

		Assert.assertEquals("cateID", action.getParamList().get(0).getMapping());
		Assert.assertEquals(0, action.getParamList().get(0)
				.getMethodParaIndex());
		Assert.assertEquals(0, action.getParamList().get(0).getUrlParaIndex());
		Assert.assertEquals(101002, action.getParamList().get(0).getValue());
		Assert.assertEquals(HttpParameterType.PathParameter, action
				.getParamList().get(0).getParaType());

		Assert.assertEquals("userID", action.getParamList().get(1).getMapping());
		Assert.assertEquals(1, action.getParamList().get(1)
				.getMethodParaIndex());
		Assert.assertEquals(1, action.getParamList().get(1).getUrlParaIndex());
		Assert.assertEquals(123, action.getParamList().get(1).getValue());
		Assert.assertEquals(HttpParameterType.PathParameter, action
				.getParamList().get(1).getParaType());
	}

	@Test
	public void testCreateRegexUrl() {
		String expected = "http://bj.58.com/infolist/(\\d+)/(\\w+)";
		String actual;

		String sourceUrl = "http://bj.58.com/infolist/{userID}/{cateName}";
		ParamInfo[] piAry = new ParamInfo[2];
		piAry[0] = new ParamInfo(0, int.class, null, "uid", "userID", null);
		piAry[1] = new ParamInfo(1, String.class, null, "cn", "cateName", null);

		actual = RequestMapping.createRegexUrl(sourceUrl, piAry);

		Assert.assertEquals(expected, actual);
	}

	@Test
	public void testRegex() {
		String regUrl = "/news/(\\d+)";
		String sourceUrl = "/news/101001";

		Pattern pattern = Pattern.compile(regUrl);
		Matcher matcher = pattern.matcher(sourceUrl);
		if (matcher.find()) {
			Assert.assertEquals("101001", matcher.group(1));
		}

		regUrl = "/news/(\\d+)/(\\w+)/(\\d+)";
		sourceUrl = "/news/101002/abc123/23456";

		pattern = Pattern.compile(regUrl);
		matcher = pattern.matcher(sourceUrl);
		if (matcher.find()) {
			Assert.assertEquals("101002", matcher.group(1));
			Assert.assertEquals("abc123", matcher.group(2));
			Assert.assertEquals("23456", matcher.group(3));
		}
	}

	@Test
	public void testConvertValue() throws Exception {
		Object expected = 101001;
		Object actual;

		String type = "int";
		String value = "101001";
		actual = RequestMapping.convertValue(type, value);
		Assert.assertEquals(expected, actual);

		expected = new Entity(101001, "hello");
		type = "com.bj58.spat.gaea.server.core.communication.http.Entity";
		value = "{\"id\":101001,\"title\":\"hello\"}";
		actual = RequestMapping.convertValue(type, value);

		Assert.assertEquals(((Entity) expected).getId(),
				((Entity) actual).getId());
		Assert.assertEquals(((Entity) expected).getTitle(),
				((Entity) actual).getTitle());
	}
}