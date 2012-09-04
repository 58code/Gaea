package com.bj58.spat.gaea.client.communication.protocol.sfp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class userdatatypeTest implements Serializable {

	public userdatatypeTest() {
	}

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testSomeMethod() throws FileNotFoundException, IOException,
			ClassNotFoundException {
		// ObjectOutputStream out = new ObjectOutputStream(new
		// FileOutputStream("D:/objectFile.obj"));
		// Customer customer = new Customer("阿蜜果", 24);
		// out.writeObject(customer);

		ObjectInputStream in = new ObjectInputStream(new FileInputStream("D:/objectFile.obj"));
		Customer obj3 = (Customer) in.readObject();
		System.out.println(obj3.toString());
	}

	class Customer implements Serializable {

		private String name;
		private int age;

		public Customer(String name, int age) {
			this.name = name;
			this.age = age;
		}

		public String toString() {
			return "name=" + name + ", age=" + age;
		}
	}
}