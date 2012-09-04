package com.bj58.spat.gaea.client.communication.protocol.sfp;

import org.junit.Test;
import java.lang.reflect.Type;
import java.lang.reflect.Method;

/**
 * JsonTest
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class JsonTest {

	@Test
	public void testFun() throws Exception {
		Method[] methodAry = TestMethoInfo.class.getDeclaredMethods();
		for (Method m : methodAry) {
			Type[] typeAry = m.getGenericParameterTypes();
			Class<?>[] clsAry = m.getParameterTypes();
			for (Type t : typeAry) {
				System.out.println("gt" + t);
				System.out.println("tc:" + Class.forName(t.toString()));
			}
			System.out.println("g rv:" + m.getGenericReturnType());
			System.out.println("n rv:" + m.getReturnType().getName());
		}
	}
}
