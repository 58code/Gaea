package com.bj58.spat.gaea.server.deploy.bytecode;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.bj58.spat.gaea.server.deploy.bytecode.ProxyClassCreater;

public class ProxyClassCreaterTest {

	@Test
	public void testCreateMethods() {
		Class<?> implCls = TestClass.class;
		Method[] methodAry = implCls.getDeclaredMethods();
		List<String> uniqueNameList = new ArrayList<String>();
		List<Method> uniqueMethodList = new ArrayList<Method>();
		List<Method> allMethodList = new ArrayList<Method>();
		for (Method m : methodAry) {
			if (Modifier.isPublic(m.getModifiers())
					|| Modifier.isProtected(m.getModifiers())) {
				if (!uniqueNameList.contains(m.getName())) {
					uniqueNameList.add(m.getName());
					uniqueMethodList.add(m);
				}
				allMethodList.add(m);
			}
		}

		ProxyClassCreater pcc = new ProxyClassCreater();
		// method
		for (Method m : uniqueMethodList) {
			System.out.println("create method:" + m.getName());
			String methodStr = pcc.createMethods("TestClass", m.getName(),
					allMethodList, uniqueNameList);
			System.out.println("method(" + m.getName() + ") source code:"
					+ methodStr);

			TestEntity te = new TestEntity();
			te.toString();
		}
	}

	@Test
	public void testCodeSource() throws MalformedURLException {
		URL url = new URL("file", "", "E:/JavaWorkJar/gaea/gaea-server-2.0.0.jar");
		CodeSource cs = new CodeSource(url,
				(java.security.cert.Certificate[]) null);
		System.out.println(cs.getLocation().getPath());
	}
}
