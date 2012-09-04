package com.bj58.spat.gaea.server.util;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;

import org.junit.Test;

import com.bj58.spat.gaea.server.contract.annotation.HttpPathParameter;
import com.bj58.spat.gaea.server.core.communication.http.HttpServer;
import com.bj58.spat.gaea.server.util.ClassHelper;

public class ClassHelperTest {

	@Test
	public void testGetClassFromJar() {
		fail("Not yet implemented");
	}

	@Test
	public void testInterfaceOf() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetParamNames() throws Exception {
		Class<?> cls  = HttpServer.class;
		Method[] methods = cls.getDeclaredMethods();
		for(Method m : methods) {
			if(Modifier.isPublic(m.getModifiers()) || Modifier.isProtected(m.getModifiers())){
				String[] paramNames = ClassHelper.getParamNames(cls, m);
				for(String pn : paramNames) {
					System.out.println(pn);
				}
			}
		}
	}
	
	@Test
	public void testGetParamAnnotations() throws Exception {
		Class<?> cls = RestService.class;
		Method[] methods = cls.getDeclaredMethods();
		for(Method m : methods) {
			if(Modifier.isPublic(m.getModifiers()) || Modifier.isProtected(m.getModifiers())) {
				Object[][] paramNames = ClassHelper.getParamAnnotations(cls, m);
				
				System.out.println("paramNames.length: "+paramNames.length);
				for(int i=0; i<paramNames.length; i++) {
					System.out.println("paramNames[i].length: " + paramNames[i].length);
					for(int j=0; j<paramNames[i].length; j++) {
						System.out.println("mapping: " + ((HttpPathParameter)paramNames[i][j]).mapping());
						
						System.out.println("cls: " + ((Annotation)paramNames[i][j]).getClass().getName());
						System.out.println(paramNames[i][j]);
					}
					
				}
			}
			System.out.println("\n\n\n");
		}
	}
	
	@Test
	public void testPath() throws IOException {
		Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("");

		while(urls.hasMoreElements()) {
			URL url = urls.nextElement();
			System.out.println(url);
		}
	}

}