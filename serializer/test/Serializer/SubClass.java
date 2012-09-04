package Serializer;

import com.bj58.spat.gaea.serializer.component.annotation.GaeaMember;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
@GaeaSerializable(name = "SubClass")
public class SubClass {
	@GaeaMember
	public String Name;
}
