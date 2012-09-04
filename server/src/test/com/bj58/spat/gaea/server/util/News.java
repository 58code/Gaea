package com.bj58.spat.gaea.server.util;

import java.util.Date;

import com.bj58.spat.gaea.serializer.component.annotation.GaeaMember;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;

@GaeaSerializable
public class News {

	private static final long serialVersionUID = 1L;
	
	@GaeaMember //标记该字段为需要序列化字段
	private int newsID;
	
	@GaeaMember
	private String title;
	
	@GaeaMember
	private String content;
	
	@GaeaMember
	private Date addTime;
	
	
	public News() {
		
	}
	
	public News(int newsID, String title, String content, Date addTime) {
		super();
		this.newsID = newsID;
		this.title = title;
		this.content = content;
		this.addTime = addTime;
	}





	public int getNewsID() {
		return newsID;
	}
	public void setNewsID(int newsID) {
		this.newsID = newsID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getAddTime() {
		return addTime;
	}
	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}
}