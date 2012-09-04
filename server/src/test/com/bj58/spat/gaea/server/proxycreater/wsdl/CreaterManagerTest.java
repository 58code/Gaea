package com.bj58.spat.gaea.server.proxycreater.wsdl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CreaterManagerTest {

	@Test
	public void testMethod() throws Exception {

		Method[] methodAry = TestMethoInfo.class.getDeclaredMethods();
		TestMethoInfo mi = new TestMethoInfo();

		long start1 = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			methodAry[0].invoke(mi, 1, 2);
		}
		long end1 = System.currentTimeMillis();
		System.out.println("invoke:" + (end1 - start1));

		long start2 = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			mi.add(1, 2);
		}
		long end2 = System.currentTimeMillis();
		System.out.println("normal:" + (end2 - start2));

		long start3 = System.currentTimeMillis();
		for (int i = 0; i < 1000000; i++) {
			"aaaaaaaaaaaaaaaaaaaaaa".equalsIgnoreCase("aaaaaaaaaaaaaaaaaa");
		}
		long end3 = System.currentTimeMillis();
		System.out.println("equals:" + (end3 - start3));

		for (Method m : TestMethoInfo.class.getDeclaredMethods()) {
			System.out.println(m.getName());
		}
	}

	@Test
	public void testObject() {
		int aaa = 1;
		Object[] objAry = new Object[10];
		objAry[0] = aaa;

		List<Object> objList = new ArrayList<Object>();
		objList.add(aaa);

		System.out.println(objAry[0].getClass());
		System.out.println(objAry[0].getClass().getTypeParameters());

		Integer objInt = Integer.parseInt("1");
		int para = 1;

		fun(para);
		fun(objInt);

		fun2("a", para);
		fun2("a", objInt);

		// Method[] methodAry = CreaterManagerTest.class.getDeclaredMethods();
		// for(Method m : methodAry) {
		// System.out.println(m.getName());
		// Class<?>[] clsPara = m.getParameterTypes();
		// for(Class<?> cls : clsPara) {
		// System.out.println(cls.getName());
		// }
		// System.out.println("===============");
		// }
	}

	public void fun(Integer para) {
		System.out.println("Integer");
	}

	public void fun(int para) {
		System.out.println("int");
	}

	public void fun2(String para1, int para2) {
		System.out.println("fun2 int");
	}

	// public void fun2(String para1, Integer para2){
	// System.out.println("fun2 Integer");
	// }

	@Test
	public void testFun() throws Exception {
		Method[] methodAry = TestMethoInfo.class.getDeclaredMethods();
		for (Method m : methodAry) {
			Type[] typeAry = m.getGenericParameterTypes();
			Class<?>[] clsAry = m.getParameterTypes();
			
			int i=0;
			for (Type t : typeAry) {
				
				if(t.equals(clsAry[i])){
					Class<?> cls = (Class<?>)t;
					System.out.println("cls:"+cls.getName());
				} else {
					Class<?> cls = Class.forName( t.toString().replaceAll(clsAry[i].getCanonicalName(), "")
					.replaceAll("\\<", "")
					.replaceAll("\\>", ""));
					System.out.println("cls---------" + cls.getName());
				}
				
				System.out.println(t.equals(clsAry[i]));
				System.out.println("gt:" + t);
				System.out.println("nc:"+clsAry[i].getCanonicalName());
				System.out.println(clsAry[i].getSimpleName());
				System.out.println(clsAry[i].getName());
				//System.out.println("tc:" + Class.forName(t.toString()));
				i++;
			}
			System.out.println("g rv:" + m.getGenericReturnType());
			System.out.println("n rv:" + m.getReturnType().getName());
			System.out.println("===========");
		}
	}
}