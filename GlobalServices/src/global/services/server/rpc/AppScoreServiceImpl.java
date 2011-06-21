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
		List<AppScore> selectedApps = appDB.SelectApps(userId);
		List<AppScore> retApps = new ArrayList<AppScore>();
		for (AppScore app : selectedApps) {
			retApps.add(app);
		}
		//Window.alert("selectedApps size in Service implement: " + selectedApps.size());
		return retApps;
	}

	@Override
	public int DeleteApps(String userId, List<String> listAppId) {
		// TODO Auto-generated method stub
		Long tmpRet = (long) 0;
		int ret = 0;
		AppScoreDataBase appDB = new AppScoreDataBase();
		for (String appId : listAppId) {
			tmpRet = appDB.DeleteApp(userId, appId);
			if (tmpRet != null) 
				ret++;
		}
		return ret;
	}

}
