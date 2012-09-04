package example.contract;

import java.util.List;
import example.entity.News;

import com.bj58.spat.gaea.server.contract.annotation.OperationContract;
import com.bj58.spat.gaea.server.contract.annotation.ServiceContract;

/**
 * 对外提供服务接口类
 * 
 * @ServiceContract 标记该接口对外提供服务
 * @OperationContract 标记该方法对外暴露
 * 
 * @author @author Service Platform Architecture Team (spat@58.com)
 */
@ServiceContract
public interface INewsService {
	@OperationContract
	public News getNewsByID(int newsID) throws Exception;

	@OperationContract
	public List<News> getNewsByCateID() throws Exception;

}
