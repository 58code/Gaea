package com.bj58.spat.gaea.server.core.communication.http;

public enum HttpMethod {
	
	GET(1),

	POST(2),

	DELETE(3),

	PUT(4),

	HEAD(5);

	private final int num;

	public int getNum() {
		return num;
	}

	private HttpMethod(int num) {
		this.num = num;
	}
}