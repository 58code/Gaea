package com.bj58.spat.gaea.server.deploy.bytecode;

import com.bj58.spat.gaea.server.contract.annotation.OperationContract;

public interface IFirst {
	@OperationContract
	public String loadByID(int id) throws Exception;
}
