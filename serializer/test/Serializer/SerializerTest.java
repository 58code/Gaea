package Serializer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.bj58.spat.gaea.serializer.serializer.Serializer;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class SerializerTest {

	@Test
	public void testByte() throws Exception {
		Serializer serializer = new Serializer();
		Byte[] b = null;
		byte[] buffer = serializer.Serialize(b);

		Object obj1 = serializer.Derialize(buffer, Byte[].class);

		Short[] s = new Short[] { 1, 2, 3 };
		byte[] bs = serializer.Serialize(s);

		Map<Byte, Object> map = new HashMap<Byte, Object>();
		map.put((byte) 1, "abc");
		map.put((byte) 2, 123456L);
		map.put((byte) 3, s);
		map.put((byte) 4, 123);
		map.put((byte) 5, null);

		byte[] buf3 = serializer.Serialize(map);
		Map<Byte, Object> obj3 = (Map<Byte, Object>) serializer.Derialize(buf3,
				Map.class);
		System.out.println(obj3.get(5));
	}

	/**
	 * Test of WriteObject method, of class NullSerializer.
	 */
	@Test
	public void TestObject() throws Exception {
		SimpleClass sc = SimpleClass.Get();
		Object data = sc;
		Serializer serializer = new Serializer();
		byte[] buffer = serializer.Serialize(data);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, SimpleClass.class);

		Object expect = obj;
		assertNotNull(expect);
	}

	public void TestEqualObject() throws Exception {
		Serializer serializer = new Serializer();
		List<SimpleClass> scs = new ArrayList<SimpleClass>();
		SimpleClass sc = SimpleClass.Get();
		scs.add(sc);
		scs.add(sc);
		byte[] buffer = serializer.Serialize(scs);
		Object obj = serializer.Derialize(buffer, ArrayList.class);
		List<SimpleClass> scs2 = (List<SimpleClass>) obj;
		assertNotNull(scs2);
		assertTrue(scs2.get(0) == scs2.get(1));
	}

	@Test
	public void TesteInt32() throws Exception {
		Serializer serializer = new Serializer();
		byte[] buffer = serializer.Serialize(123);
		assertNotNull(buffer);
		int num = (Integer) serializer.Derialize(buffer, Integer.class);
		assertEquals(123, num);
	}

	@Test
	public void TestDate() throws Exception {
		Serializer serializer = new Serializer();
		Object data = new Date();
		byte[] buffer = serializer.Serialize(data);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, Date.class);
		Object expect = data;
		assertEquals(expect, obj);
	}

	@Test
	public void TestInt() throws Exception {
		Serializer serializer = new Serializer();
		Object data = (int) 32;
		byte[] buffer = serializer.Serialize(data);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, int.class);
		Object expect = data;
		assertEquals(expect, obj);
	}

	@Test
	public void SerizlizeSqlDate() throws Exception {
		Serializer serializer = new Serializer();
		Object data = new java.sql.Date((new Date()).getTime());
		byte[] buffer = serializer.Serialize(data);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, java.sql.Date.class);
		Object expect = data;
		assertEquals(expect, obj);
	}

	@Test
	public void SerizlizeSqlTimestamp() throws Exception {
		Serializer serializer = new Serializer();
		Object data = new java.sql.Timestamp((new Date()).getTime());
		byte[] buffer = serializer.Serialize(data);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, java.sql.Timestamp.class);
		Object expect = data;
		assertEquals(expect, obj);
	}

	@Test
	public void SerizlizeSqlTime() throws Exception {
		Serializer serializer = new Serializer();
		Object data = new java.sql.Time((new Date()).getTime());
		byte[] buffer = serializer.Serialize(data);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, java.sql.Time.class);
		Object expect = data;
		assertEquals(expect, obj);
	}

	@Test
	public void TestIntArray() throws Exception {
		Integer[] data = new Integer[] { 12, 32, 44 };
		Serializer serializer = new Serializer();
		byte[] buffer = serializer.Serialize(data);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, data.getClass());
		Object expect = data;
		assertArrayEquals((Integer[]) expect, (Integer[]) obj);
	}

	@Test
	public void TestMap() throws Exception {
		Map data = new HashMap();
		data.put("123", "asdfasd");
		data.put(234, 444);
		Serializer serializer = new Serializer();
		byte[] buffer = serializer.Serialize(data);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, HashMap.class);
		Object expect = data;
		assertEquals(expect, obj);
	}

	@Test
	public void TestList() throws Exception {
		List data = new ArrayList();
		data.add("234");
		data.add(1234);
		Serializer serializer = new Serializer();
		byte[] buffer = serializer.Serialize(data);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, data.getClass());
		Object expect = data;
		assertEquals(expect, obj);
	}

	@Test
	public void TestString() throws Exception {
		String data = "$^%&^*&([新闻]智慧的星球 Sun的力量可强化？serializer.Derialize(buffer, data.getClass())";
		Serializer serializer = new Serializer();
		byte[] buffer = serializer.Serialize(data);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, data.getClass());
		Object expect = data;
		assertEquals(expect, obj);
	}

	@Test
	public void TestBoolean() throws Exception {
		Boolean data = false;
		Serializer serializer = new Serializer();
		byte[] buffer = serializer.Serialize(data);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, data.getClass());
		Object expect = data;
		assertEquals(expect, obj);

		Boolean data2 = true;
		byte[] buffer2 = serializer.Serialize(data2);
		assertNotNull(buffer2);
		Object obj2 = serializer.Derialize(buffer2, data2.getClass());
		Object expect2 = data2;
		assertEquals(expect2, obj2);
	}

	@Test
	public void TestEnum() throws Exception {
		Serializer serializer = new Serializer();
		Object data = EnmReadState.UnRead;
		byte[] buffer = serializer.Serialize(data);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, data.getClass());
		Object expect = data;
		assertEquals(expect, obj);
	}

	@Test
	public void TestChar() throws Exception {
		Serializer serializer = new Serializer();
		char data = '中';
		byte[] buffer = serializer.Serialize(data);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, char.class);
		Object expect = data;
		assertEquals(expect, obj);
	}

	@Test
	public void TestByte() throws Exception {
		Serializer serializer = new Serializer();
		Object data = 120;
		byte[] buffer = serializer.Serialize(data);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, data.getClass());
		Object expect = data;
		assertEquals(expect, obj);
	}

	@Test
	public void TestListArray() throws Exception {
		Serializer serializer = new Serializer();
		ArrayList a1 = new ArrayList();
		a1.add(1);
		ArrayList a2 = new ArrayList();
		a2.add(2);
		Object data = new ArrayList[] { a1, a2 };

		byte[] buffer = serializer.Serialize(data);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, data.getClass());
		Object expect = data;

		Object a = Array.newInstance(List.class, 3);

		assertEquals(expect, obj);

		ArrayList[] abc = (ArrayList[]) obj;
	}
}
