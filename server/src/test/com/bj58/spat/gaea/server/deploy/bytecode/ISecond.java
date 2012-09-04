package com.bj58.spat.gaea.server.deploy.bytecode;

import com.bj58.spat.gaea.server.contract.annotation.OperationContract;

public interface ISecond {
	@OperationContract
	public String loadByName(String name) throws Exception;
}
