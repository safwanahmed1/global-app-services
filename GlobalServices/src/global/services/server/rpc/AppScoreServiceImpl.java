package global.services.server.rpc;

import java.util.ArrayList;
import java.util.List;

import global.services.client.rpc.AppScoreService;
import global.services.server.database.AppScoreDataBase;
import global.services.shared.AppScore;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AppScoreServiceImpl extends RemoteServiceServlet implements
		AppScoreService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Long DeleteApp(String userId, Long appId) {
		// TODO Auto-generated method stub
		Long ret = null;
		if (userId != null) {
			AppScoreDataBase appDB = new AppScoreDataBase();
			ret = appDB.DeleteApp(userId, appId);
			appDB.Finalize();
		}
			
		return ret;
	}

	@Override
	public Long DeleteApps(String userId) {
		// TODO Auto-generated method stub
		Long ret = null;
		AppScoreDataBase appDB = new AppScoreDataBase();
		ret = appDB.DeleteApps(userId);
		appDB.Finalize();
		return ret;

	}

	
	@Override
	public Long InsertApp(AppScore app) {
		// TODO Auto-generated method stub
		Long ret = null;

		AppScoreDataBase appDB = new AppScoreDataBase();
		ret = appDB.InsertApp(app);
		appDB.Finalize();
		return ret;
	}

	@Override
	public Long UpdateApp(AppScore app) {
		// TODO Auto-generated method stub
		AppScoreDataBase appDB = new AppScoreDataBase();
		Long ret = appDB.UpdateApp(app);
		appDB.Finalize();
		return ret;
	}

	@Override
	public AppScore SelectApp(String userId, Long appId) {
		// TODO Auto-generated method stub
		AppScoreDataBase appDB = new AppScoreDataBase();
		AppScore appRet = appDB.SelectApp(userId, appId);
		appDB.Finalize();
		return appRet;
	}

	@Override
	public List<AppScore> SelectApps(String userId) {
		// TODO Auto-generated method stub
		AppScoreDataBase appDB = new AppScoreDataBase();
		List<AppScore> selectedApps = appDB.SelectApps(userId);
		List<AppScore> retApps = new ArrayList<AppScore>();
		for (AppScore app : selectedApps) {
			retApps.add(app);
		}
		appDB.Finalize();
		return retApps;
	}

	@Override
	public int DeleteApps(String userId, List<Long> listAppId) {
		// TODO Auto-generated method stub
		Long tmpRet = (long) 0;
		int ret = 0;
		AppScoreDataBase appDB = new AppScoreDataBase();
		for (Long appId : listAppId) {
			tmpRet = appDB.DeleteApp(userId, appId);
			if (tmpRet != null) 
				ret++;
		}
		appDB.Finalize();
		return ret;
	}

}
