package com.bj58.spat.gaea.server.util;

import java.util.List;

import com.bj58.spat.gaea.server.contract.annotation.OperationContract;
import com.bj58.spat.gaea.server.contract.annotation.ServiceContract;
import com.bj58.spat.gaea.server.contract.entity.Out;

/**
 * A ServiceContract for news
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
@ServiceContract  //标记该接口对外提供服务
public interface INewsService {
	
	/**
	 * get news by newsID
	 * @param newsID
	 * @return news entity
	 * @throws Exception
	 */
	@OperationContract  //标记该方法对外暴露
	public News getNews(int newsID) throws Exception;
	
	
	/**
	 * get news list by cateID
	 * @param cateID
	 * @param totalCount
	 * @return news list
	 * @throws Exception
	 */
	@OperationContract  //标记该方法对外暴露
	public List<News> getNews(int cateID, int userID, Out<Integer> totalCount) throws Exception;
	
	/**
	 * 
	 * @param cateID
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	@OperationContract  //标记该方法对外暴露
	public List<News> getNews(int cateID, int userID) throws Exception;
	
	/**
	 * delete news 
	 * @param newsID
	 * @throws Exception
	 */
	@OperationContract  //标记该方法对外暴露
	public void deleteNews(int newsID) throws Exception;
	
	/**
	 * 
	 * @throws Exception
	 */
	@OperationContract  //标记该方法对外暴露
	public void addNews(News news) throws Exception;
}