package com.bj58.spat.gaea.client.configuration;

import java.util.List;

import com.bj58.spat.gaea.client.GaeaInit;
import com.bj58.spat.gaea.client.configuration.ServiceConfig;
import com.bj58.spat.gaea.client.configuration.commmunication.SocketPoolProfile;
import com.bj58.spat.gaea.client.configuration.loadbalance.ServerProfile;

public class ServiceConfigTest {
	
	public static void ServiceConfig(String serviceName) throws Exception{
		GaeaInit.init("e:/gaea.config");
		ServiceConfig serviceConfig = ServiceConfig.GetConfig(serviceName);
		System.out.println("servicename is "+serviceConfig.getServicename());
		System.out.println("serviceid is "+serviceConfig.getServiceid());
		
		SocketPoolProfile socketPoolProfile = serviceConfig.getSocketPool();
		System.out.println("bufferSize "+socketPoolProfile.getBufferSize());
		System.out.println("ConnectionTimeout "+socketPoolProfile.getConnectionTimeout());
		System.out.println("MaxPakageSize "+socketPoolProfile.getMaxPakageSize());
		System.out.println("MaxPoolSize "+socketPoolProfile.getMaxPoolSize());
		System.out.println("MinPoolSize "+socketPoolProfile.getMinPoolSize());
		System.out.println("ReceiveTimeout "+socketPoolProfile.getReceiveTimeout());
		System.out.println("RecvBufferSize "+socketPoolProfile.getRecvBufferSize());
		System.out.println("ReconnectTime "+socketPoolProfile.getReconnectTime());
		System.out.println("SendBufferSize "+socketPoolProfile.getSendBufferSize());
		System.out.println("SendTimeout "+socketPoolProfile.getSendTimeout());
		System.out.println("ShrinkInterval "+socketPoolProfile.getShrinkInterval());
		System.out.println("WaitTimeout "+socketPoolProfile.getWaitTimeout());
		
		List<ServerProfile> list = serviceConfig.getServers();
		for(ServerProfile serverProfile: list){
			System.out.println("name:"+serverProfile.getName()+"host:"+serverProfile.getHost()+"port"+serverProfile.getPort()+"DeadTimeout:"+serverProfile.getDeadTimeout());
		}
	}
	
	
	public static void main(String []args) throws Exception{
		ServiceConfigTest.ServiceConfig("imc");
	}
}
