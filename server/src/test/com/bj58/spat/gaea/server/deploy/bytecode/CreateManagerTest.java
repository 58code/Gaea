package com.bj58.spat.gaea.server.deploy.bytecode;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.junit.Test;

import com.bj58.spat.gaea.server.RootPath;
import com.bj58.spat.gaea.server.contract.context.IProxyFactory;
import com.bj58.spat.gaea.server.contract.log.Log4jConfig;
import com.bj58.spat.gaea.server.deploy.hotdeploy.DynamicURLClassLoader;

public class CreateManagerTest {
	
	static {
		Log4jConfig.configure(RootPath.projectRootPath + "config/gaea_log4j.xml");
	}

	@Test
	public void testCareteProxy() throws Exception {
		
	}
	
	@Test
	public void hotDeploy() throws Exception {
		
		DynamicURLClassLoader classLoader = new DynamicURLClassLoader();
		classLoader.addURL("D:/serviceframe_v2_online/lib/serviceframe/serviceframe-2.0.1.beta.jar");
		classLoader.addFolder("D:/serviceframe_v2_online/service/deploy/imc/");
		Class<?> cmCls = classLoader.loadClass("com.bj58.sfft.serviceframe.deploy.bytecode.CreateManager");
		
		Method createProxy = cmCls.getDeclaredMethod("careteProxy", new Class[] { String.class });
		IProxyFactory pf = (IProxyFactory)createProxy.invoke(cmCls.newInstance(), "D:/serviceframe_v2_online/service/deploy/imc/");
		System.out.println("pf:" + pf);
	}
	
	
	@Test
	public void fun() throws IOException {
		JarFile jarFile = new JarFile("D:/serviceframe_v2_online/lib/serviceframe/serviceframe-2.0.1.beta.jar"); // ����jar�ļ�
		Enumeration<JarEntry> entry = jarFile.entries();
		JarEntry jarEntry = null;
		while(entry.hasMoreElements()) {
			jarEntry = entry.nextElement();
			System.out.println(jarEntry.getName());
		}
	}
}