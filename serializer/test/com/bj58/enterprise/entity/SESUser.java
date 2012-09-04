package com.bj58.enterprise.entity;

import com.bj58.spat.gaea.serializer.component.annotation.GaeaMember;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;

@SuppressWarnings("serial")
@GaeaSerializable
public class SESUser extends User {

	@GaeaMember
	private Enterprise enterprise;

	public void setEnterprise(Enterprise enterprise) {
		this.enterprise = enterprise;
	}

	public Enterprise getEnterprise() {
		return enterprise;
	}
	
}
