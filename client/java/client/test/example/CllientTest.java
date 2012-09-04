package example;

import java.util.List;
import example.entity.News;
import example.contract.INewsService;
import com.bj58.spat.gaea.client.GaeaInit;
import com.bj58.spat.gaea.client.proxy.builder.ProxyFactory;

public class CllientTest {
	
	public static void main(String[] args) throws Exception {
		//加载配置文件
		GaeaInit.init("e:/gaea.config");
		/**
		 * 调用URL
		 * 格式:tcp://服务名//接口实现类
		 * 备注:
		 * 服务名：需要与gaea.config中的服务名一一对应，
		 * 接口实现类：具体调用接口的接口实现类
		 */
		final String url = "tcp://demo/NewsService";
		INewsService newsService = ProxyFactory.create(INewsService.class, url);
		List<News> list = newsService.getNewsByCateID();
		for(News news : list){
			System.out.println("ID is "+news.getNewsID()+" title is "+news.getTitle());
		}
	}
}
