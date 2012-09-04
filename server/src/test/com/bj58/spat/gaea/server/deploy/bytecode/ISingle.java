package com.bj58.spat.gaea.server.deploy.bytecode;

import com.bj58.spat.gaea.server.contract.annotation.OperationContract;
import com.bj58.spat.gaea.server.contract.annotation.ServiceContract;

@ServiceContract
public interface ISingle {
	@OperationContract
	public String loadByID(int id) throws Exception;
}
