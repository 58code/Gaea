package com.bj58.spat.gaea.server.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.bj58.spat.gaea.server.contract.annotation.HttpPathParameter;
import com.bj58.spat.gaea.server.contract.annotation.HttpRequestMapping;
import com.bj58.spat.gaea.server.contract.annotation.ServiceBehavior;
import com.bj58.spat.gaea.server.contract.entity.Out;

@ServiceBehavior
public class RestService implements INewsService  {
	
	static {
		System.out.println("RestService static run!!!");
	}

	@Override
	@HttpRequestMapping(uri = "/news/{newsID}")
	public News getNews(int newsID) throws Exception {
		News news = new News();
		news.setAddTime(new Date());
		news.setContent("content");
		news.setNewsID(101001);
		news.setTitle("title");
		System.out.println("+++++++++++++++++getNews+++++++++++++++++");
		return news;
	}
	
	@Override
	@HttpRequestMapping(uri = "/newslist/{cateID}/{userID}")
	public List<News> getNews(@HttpPathParameter(mapping="cateID") int cid, @HttpPathParameter(mapping="userID") int uid) throws Exception {
		List<News> newsList = new ArrayList<News>();
		
		newsList.add(new News(101001, "title1", "content1--cateID:" + cid + "--userID:" + uid, new Date()));
		newsList.add(new News(101002, "title2", "content2--cateID:" + cid + "--userID:" + uid, new Date()));
		newsList.add(new News(101003, "title3", "content3--cateID:" + cid + "--userID:" + uid, new Date()));
		
		System.out.println("newsList == null: "+ (newsList == null));
		return newsList;
	}
	
	
	

	@Override
	public List<News> getNews(int cateID, int userID, Out<Integer> totalCount)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteNews(int newsID) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addNews(News news) throws Exception {
		// TODO Auto-generated method stub
	}
}