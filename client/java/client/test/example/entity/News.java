package example.entity;

import com.bj58.spat.gaea.serializer.component.annotation.GaeaMember;
import com.bj58.spat.gaea.serializer.component.annotation.GaeaSerializable;

/**
 * 实体类
 * 
 * @GaeaSerializable 标记当前类为需要序列化的类
 * @GaeaMember 标记该字段为需要序列化字段
 * 
 * @author @author Service Platform Architecture Team (spat@58.com)
 */

@GaeaSerializable
public class News {

	@GaeaMember
	private int newsID;

	@GaeaMember
	private String title;

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
}
