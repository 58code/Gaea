package com.bj58.spat.gaea.server.util;

import junit.framework.Assert;

import org.junit.Test;

import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.context.ServiceConfig;
import com.bj58.spat.gaea.server.util.IPTable;

public class IPTableTest {

	static {
		try {
			Global.getSingleton().setServiceConfig(ServiceConfig.getServiceConfig("D:/workspace/java/bj58.gaea.server/config/gaea_config.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testIsAllowI() {
		Global.getSingleton().getServiceConfig().set("gaea.iptable.allow.iplist", "192.168.9.*,192.168.11.*");
		Global.getSingleton().getServiceConfig().set("gaea.iptable.forbid.iplist", "192.168.9.199,192.168.9.241");
		IPTable.init();
		
		Assert.assertEquals(false, IPTable.isAllow("192.168.111.9"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.9.9"));
		Assert.assertEquals(false, IPTable.isAllow("192.168.110.123"));
		Assert.assertEquals(false, IPTable.isAllow("192.168.9.199"));
		Assert.assertEquals(false, IPTable.isAllow("192.168.9.241"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.11.9"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.11.129"));
	}
	
	@Test
	public void testIsAllowII() {
		Global.getSingleton().getServiceConfig().set("gaea.iptable.allow.iplist", "");
		Global.getSingleton().getServiceConfig().set("gaea.iptable.forbid.iplist", "192.168.9.199,192.168.9.241");
		IPTable.init();
		
		Assert.assertEquals(true, IPTable.isAllow("192.168.111.9"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.9.9"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.110.123"));
		Assert.assertEquals(false, IPTable.isAllow("192.168.9.199"));
		Assert.assertEquals(false, IPTable.isAllow("192.168.9.241"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.11.9"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.11.129"));
	}
	
	@Test
	public void testIsAllowIII() {
		Global.getSingleton().getServiceConfig().set("gaea.iptable.allow.iplist", "192.168.9.*,192.168.11.*");
		Global.getSingleton().getServiceConfig().set("gaea.iptable.forbid.iplist", "");
		IPTable.init();
		
		Assert.assertEquals(false, IPTable.isAllow("192.168.111.9"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.9.9"));
		Assert.assertEquals(false, IPTable.isAllow("192.168.110.123"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.9.199"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.9.241"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.11.9"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.11.129"));
	}
	
	@Test
	public void testIsAllowIV() {
		Global.getSingleton().getServiceConfig().set("gaea.iptable.allow.iplist", "");
		Global.getSingleton().getServiceConfig().set("gaea.iptable.forbid.iplist", "");
		IPTable.init();
		
		Assert.assertEquals(true, IPTable.isAllow("192.168.111.9"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.9.9"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.110.123"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.9.199"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.9.241"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.11.9"));
		Assert.assertEquals(true, IPTable.isAllow("192.168.11.129"));
	}
}