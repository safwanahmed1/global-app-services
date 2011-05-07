package global.services.server.rpc;

import java.util.List;

import global.services.client.rpc.AppScoreService;
import global.services.server.database.AppScoreDataBase;
import global.services.shared.AppScore;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AppScoreServiceImpl extends RemoteServiceServlet implements
		AppScoreService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Long DeleteApp(String userId, String appId) {
		// TODO Auto-generated method stub
		Long ret = null;
		if (userId != null) {
			AppScoreDataBase appDB = new AppScoreDataBase();
			ret = appDB.DeleteApp(userId, appId);
		}
			
		return ret;
	}

	@Override
	public Long DeleteApps(String userId) {
		// TODO Auto-generated method stub
		Long ret = null;
		AppScoreDataBase appDB = new AppScoreDataBase();
		ret = appDB.DeleteApps(userId);
		return ret;

	}

	
	@Override
	public Long InsertApp(AppScore app) {
		// TODO Auto-generated method stub
		Long ret = null;

		AppScoreDataBase appDB = new AppScoreDataBase();
		ret = appDB.InsertApp(app);
		return ret;
	}

	@Override
	public Long UpdateApp(AppScore app) {
		// TODO Auto-generated method stub
		Long ret = null;
		return ret;
	}

	@Override
	public AppScore SelectApp(String userId, String appId) {
		// TODO Auto-generated method stub
		AppScoreDataBase appDB = new AppScoreDataBase();
		return appDB.SelectApp(userId, appId);

		
	}

	@Override
	public List<AppScore> SelectApps(String userId) {
		// TODO Auto-generated method stub
		AppScoreDataBase appDB = new AppScoreDataBase();
		return appDB.SelectApps(userId);
	}

}
