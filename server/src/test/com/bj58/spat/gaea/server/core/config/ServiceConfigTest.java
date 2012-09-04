package com.bj58.spat.gaea.server.core.config;

import junit.framework.Assert;

import org.junit.Test;

import com.bj58.spat.gaea.server.RootPath;
import com.bj58.spat.gaea.server.contract.context.ServiceConfig;

public class ServiceConfigTest {
	
	@Test
	public void testCreateInstance() throws Exception {
		
		ServiceConfig sc = ServiceConfig.getServiceConfig(RootPath.projectRootPath + "config/gaea_config.xml");
		
		Assert.assertEquals("", sc.getString("gaea.service.name"));
		Assert.assertEquals("utf-8", sc.getString("gaea.encoding"));
		Assert.assertEquals("com.bj58.spat.gaea.server.core.init.SerializerInit", sc.getString("gaea.init"));

		//socket
		Assert.assertEquals("0.0.0.0", sc.getString("gaea.server.tcp.listenIP"));
		Assert.assertEquals(9090, sc.getInt("gaea.server.tcp.listenPort"));
		Assert.assertEquals(60, sc.getInt("gaea.server.tcp.workerCount"));
		Assert.assertEquals("com.bj58.spat.gaea.server.core.communication.tcp.SocketServer", sc.getString("gaea.server.tcp.implement"));
		Assert.assertEquals(true, sc.getBoolean("gaea.server.tcp.enable"));
		Assert.assertEquals(1, sc.getList("gaea.server.tcp.filter.request", ",").size());
		Assert.assertEquals("com.bj58.spat.gaea.server.core.filter.ProtocolParseFilter", sc.getList("gaea.server.tcp.filter.request", ",").get(0));
		Assert.assertEquals(1, sc.getList("gaea.server.tcp.filter.response", ",").size());
		Assert.assertEquals("com.bj58.spat.gaea.server.core.filter.ProtocolCreateFilter", sc.getList("gaea.server.tcp.filter.response", ",").get(0));
		Assert.assertEquals(65536, sc.getInt("gaea.server.tcp.receiveBufferSize"));
		Assert.assertEquals(65536, sc.getInt("gaea.server.tcp.sendBufferSize"));
		Assert.assertEquals(524288, sc.getInt("gaea.server.tcp.frameMaxLength"));
		
		//http
		Assert.assertEquals("0.0.0.0", sc.getString("gaea.server.http.listenIP"));
		Assert.assertEquals(8080, sc.getInt("gaea.server.http.listenPort"));
		Assert.assertEquals(200, sc.getInt("gaea.server.http.workerCount"));
		Assert.assertEquals("com.bj58.spat.gaea.server.core.communication.http.HttpServer", sc.getString("gaea.server.http.implement"));
		Assert.assertEquals(false, sc.getBoolean("gaea.server.http.enable"));
		Assert.assertEquals(null, sc.getList("gaea.server.http.filter.request", ","));
		Assert.assertEquals(null, sc.getList("gaea.server.http.filter.response", ","));
		Assert.assertEquals(65536, sc.getInt("gaea.server.http.receiveBufferSize"));
		Assert.assertEquals(65536, sc.getInt("gaea.server.http.sendBufferSize"));
		Assert.assertEquals(524288, sc.getInt("gaea.server.http.frameMaxLength"));
		
		//telnet
		Assert.assertEquals("0.0.0.0", sc.getString("gaea.server.telnet.listenIP"));
		Assert.assertEquals(7070, sc.getInt("gaea.server.telnet.listenPort"));
		Assert.assertEquals(1, sc.getInt("gaea.server.telnet.workerCount"));
		Assert.assertEquals("com.bj58.spat.gaea.server.core.communication.telnet.TelnetServer", sc.getString("gaea.server.telnet.implement"));
		Assert.assertEquals(true, sc.getBoolean("gaea.server.telnet.enable"));
		Assert.assertEquals(null, sc.getList("gaea.server.telnet.filter.request", ","));
		Assert.assertEquals(null, sc.getList("gaea.server.telnet.filter.response", ","));
		Assert.assertEquals(65536, sc.getInt("gaea.server.telnet.receiveBufferSize"));
		Assert.assertEquals(65536, sc.getInt("gaea.server.telnet.sendBufferSize"));
		Assert.assertEquals(524288, sc.getInt("gaea.server.telnet.frameMaxLength"));
	}
	
	
	
	
	@Test
	public void testCreateInstanceII() throws Exception {
		
		ServiceConfig sc = ServiceConfig.getServiceConfig(RootPath.projectRootPath + "config/gaea_config.xml",
														  RootPath.projectRootPath + "config/demo_config.xml");
		
		Assert.assertEquals(null, sc.getString("gaea.service.name.abc"));
		Assert.assertEquals("demo", sc.getString("gaea.service.name"));
		Assert.assertEquals("utf-8", sc.getString("gaea.encoding"));
		Assert.assertEquals("com.bj58.spat.gaea.server.core.init.SerializerInit,com.bj58.zhaopin.gaea.pusher.zpt.components.Global", sc.getString("gaea.init"));

		//socket
		Assert.assertEquals("192.168.1.123", sc.getString("gaea.server.tcp.listenIP"));
		Assert.assertEquals(10000, sc.getInt("gaea.server.tcp.listenPort"));
		Assert.assertEquals(60, sc.getInt("gaea.server.tcp.workerCount"));
		Assert.assertEquals("com.bj58.spat.gaea.server.core.communication.tcp.SocketServer", sc.getString("gaea.server.tcp.implement"));
		Assert.assertEquals(true, sc.getBoolean("gaea.server.tcp.enable"));
		Assert.assertEquals(1, sc.getList("gaea.server.tcp.filter.request", ",").size());
		Assert.assertEquals("com.bj58.spat.gaea.server.core.filter.ProtocolParseFilter", sc.getList("gaea.server.tcp.filter.request", ",").get(0));
		Assert.assertEquals(1, sc.getList("gaea.server.tcp.filter.response", ",").size());
		Assert.assertEquals("com.bj58.spat.gaea.server.core.filter.ProtocolCreateFilter", sc.getList("gaea.server.tcp.filter.response", ",").get(0));
		Assert.assertEquals(65536, sc.getInt("gaea.server.tcp.receiveBufferSize"));
		Assert.assertEquals(65536, sc.getInt("gaea.server.tcp.sendBufferSize"));
		Assert.assertEquals(524288, sc.getInt("gaea.server.tcp.frameMaxLength"));
		
		//http
		Assert.assertEquals("0.0.0.0", sc.getString("gaea.server.http.listenIP"));
		Assert.assertEquals(8080, sc.getInt("gaea.server.http.listenPort"));
		Assert.assertEquals(200, sc.getInt("gaea.server.http.workerCount"));
		Assert.assertEquals("com.bj58.spat.gaea.server.core.communication.http.HttpServer", sc.getString("gaea.server.http.implement"));
		Assert.assertEquals(true, sc.getBoolean("gaea.server.http.enable"));
		Assert.assertEquals(null, sc.getList("gaea.server.http.filter.request", ","));
		Assert.assertEquals(null, sc.getList("gaea.server.http.filter.response", ","));
		Assert.assertEquals(65536, sc.getInt("gaea.server.http.receiveBufferSize"));
		Assert.assertEquals(65536, sc.getInt("gaea.server.http.sendBufferSize"));
		Assert.assertEquals(524288, sc.getInt("gaea.server.http.frameMaxLength"));
		
		//telnet
		Assert.assertEquals("0.0.0.0", sc.getString("gaea.server.telnet.listenIP"));
		Assert.assertEquals(7070, sc.getInt("gaea.server.telnet.listenPort"));
		Assert.assertEquals(1, sc.getInt("gaea.server.telnet.workerCount"));
		Assert.assertEquals("com.bj58.spat.gaea.server.core.communication.telnet.TelnetServer", sc.getString("gaea.server.telnet.implement"));
		Assert.assertEquals(true, sc.getBoolean("gaea.server.telnet.enable"));
		Assert.assertEquals(null, sc.getList("gaea.server.telnet.filter.request", ","));
		Assert.assertEquals(null, sc.getList("gaea.server.telnet.filter.response", ","));
		Assert.assertEquals(65536, sc.getInt("gaea.server.telnet.receiveBufferSize"));
		Assert.assertEquals(65536, sc.getInt("gaea.server.telnet.sendBufferSize"));
		Assert.assertEquals(524288, sc.getInt("gaea.server.telnet.frameMaxLength"));
	}

}