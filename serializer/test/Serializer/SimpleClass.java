package Serializer;

import com.bj58.spat.gaea.serializer.component.annotation.GaeaMember;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
@GaeaSerializable(name = "SimpleClass")
public class SimpleClass {

	public SimpleClass() {
	}

	@GaeaMember
	public int UserId;
	@GaeaMember
	public Date PostDate;
	@GaeaMember
	public SubClass[] SubClasses;
	@GaeaMember
	public ArrayList List;
	@GaeaMember
	public Long[] arr;
	@GaeaMember
	public State state;
	@GaeaMember
	public BigDecimal num;
	@GaeaMember
	public Hashtable myMap;

	public static SimpleClass Get() {
		SimpleClass sc = new SimpleClass();
		sc.UserId = 123;
		sc.PostDate = new Date();
		sc.SubClasses = new SubClass[2];
		sc.SubClasses[0] = new SubClass();
		sc.SubClasses[1] = sc.SubClasses[0];
		sc.SubClasses[0].Name = "lxsfg";
		sc.List = new ArrayList();
		sc.List.add("123456");
		sc.List.add("rtertr");
		sc.arr = new Long[] { 1L, 2L, 3L };
		sc.state = State.Open;
		sc.num = new BigDecimal("1.43434234523452345");
		sc.myMap = new Hashtable();
		sc.myMap.put(1, 123);
		return sc;
	}
}
