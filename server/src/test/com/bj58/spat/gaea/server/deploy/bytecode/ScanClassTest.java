package com.bj58.spat.gaea.server.deploy.bytecode;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.bj58.spat.gaea.server.deploy.bytecode.ClassInfo;
import com.bj58.spat.gaea.server.deploy.bytecode.ContractInfo;
import com.bj58.spat.gaea.server.deploy.bytecode.ScanClass;
import com.bj58.spat.gaea.server.deploy.bytecode.ClassInfo.MethodInfo;
import com.bj58.spat.gaea.server.deploy.bytecode.ClassInfo.ParamInfo;
import com.bj58.spat.gaea.server.deploy.bytecode.ContractInfo.SessionBean;
import com.bj58.spat.gaea.server.deploy.hotdeploy.DynamicClassLoader;

public class ScanClassTest {
	
	
	private static String jarPath = "E:\\javaproject\\bj58.gaea.server\\lib";
	
	@Test
	public void testContract() {
		ClassInfo ciMulti = ScanClass.contract(IMulti.class);
		List<MethodInfo> miList1 = ciMulti.getMethodList();
		Assert.assertEquals("loadByID", miList1.get(0).getMethod().getName());
		Assert.assertEquals("loadByName", miList1.get(1).getMethod().getName());

		
		ClassInfo ciSingle = ScanClass.contract(ISingle.class);
		List<MethodInfo> miList2 = ciSingle.getMethodList();
		Assert.assertEquals("loadByID", miList2.get(0).getMethod().getName());
	}

	
	@SuppressWarnings("rawtypes")
	@Test
	public void testGetContractInfo() throws Exception {
		DynamicClassLoader classLoader = new DynamicClassLoader();
		classLoader.addFolder(jarPath);
		ContractInfo ci = ScanClass.getContractInfo(jarPath, classLoader);
		for(SessionBean sb : ci.getSessionBeanList()) {
			
			Assert.assertEquals("com.bj58.spat.servicedemo.contract.INewsService", sb.getInterfaceName());
			
			Map<String, String> map = sb.getInstanceMap();
			Iterator it = map.entrySet().iterator();

			int i = 0;
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				if(i == 0) {
					Assert.assertEquals("RestService", entry.getKey());
					Assert.assertEquals("com.bj58.spat.servicedemo.components.RestService", 
										entry.getValue());
				} else if(i == 1) {
					Assert.assertEquals("NewsService", entry.getKey());
					Assert.assertEquals("com.bj58.spat.servicedemo.components.NewsService", 
										entry.getValue());
				}
				i++;
			}
		}
	}
	
	
	

	@Test
	public void testGetContractClassInfos() throws Exception {
		DynamicClassLoader classLoader = new DynamicClassLoader();
		classLoader.addFolder(jarPath);
		List<ClassInfo> ciList = ScanClass.getContractClassInfos(jarPath, classLoader);
		
		for(int i=0; i<ciList.size(); i++) {
			if(i == 0) {
				Assert.assertEquals("com.bj58.spat.servicedemo.contract.INewsService", 
									ciList.get(i).getCls().getName());
			}
		}
	}
	
	
	

	@Test
	public void testGetBehaviorClassInfos() throws Exception {
		DynamicClassLoader classLoader = new DynamicClassLoader();
		classLoader.addFolder(jarPath);
		List<ClassInfo> ciList = ScanClass.getBehaviorClassInfos(jarPath, classLoader);
		
		for(int i=0; i<ciList.size(); i++) {
			if(i == 0) {
				Assert.assertEquals("NewsService", 
									ciList.get(i).getLookUP());
				
				Assert.assertEquals("com.bj58.spat.servicedemo.components.NewsService", 
									ciList.get(i).getCls().getName());
			} else if(i == 1) {
				Assert.assertEquals("RestService", 
									ciList.get(i).getLookUP());
				
				Assert.assertEquals("com.bj58.spat.servicedemo.components.RestService", 
									ciList.get(i).getCls().getName());
			}
			
			List<MethodInfo> miList = ciList.get(i).getMethodList();
			for(MethodInfo mi : miList) {
				if(mi.getHttpRequestMapping() != null) {
					System.out.println("methodName: " + mi.getMethod().getName());
					
					System.out.println("uri: "+mi.getHttpRequestMapping().uri());
					
					ParamInfo[] piAry = mi.getParamInfoAry();
					for(ParamInfo pi : piAry) {
						System.out.println("index:"+pi.getIndex());
						System.out.println("mapping:"+pi.getMapping());
						System.out.println("name:"+pi.getName());
						System.out.println("type:"+pi.getCls().getName());
						if(pi.getHttpPathParameter() != null) {
							System.out.println("paramType:"+pi.getHttpPathParameter().paramType());
						}
						System.out.println("-----");
					}
					System.out.println("\n\n\n");
				}
			}
		}
	}

}