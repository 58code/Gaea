package com.bj58.spat.gaea.server.core.communication.http;

public class Entity {

	private int id;
	private String title;

	public Entity() {

	}

	public Entity(int id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

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
}
