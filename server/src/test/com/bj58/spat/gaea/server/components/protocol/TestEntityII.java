package com.bj58.spat.gaea.server.components.protocol;

public class TestEntityII {
	private int id;
	private String title;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public TestEntityII() {

	}

	public TestEntityII(int id, String title) {
		this.setId(id);
		this.setTitle(title);
	}
}
