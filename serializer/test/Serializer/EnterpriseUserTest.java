package Serializer;

import com.bj58.enterprise.entity.SESUser;
import com.bj58.spat.gaea.serializer.serializer.Serializer;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
public class EnterpriseUserTest {

	@Test
	public void TestUser() throws Exception {
		SESUser user = new SESUser();
		user.setUserID(1L);
		user.setState(1);

		Serializer serializer = new Serializer();
		byte[] buffer = serializer.Serialize(user);
		assertNotNull(buffer);
		Object obj = serializer.Derialize(buffer, SimpleClass.class);
		Object expect = obj;
		assertNotNull(expect);
	}
}