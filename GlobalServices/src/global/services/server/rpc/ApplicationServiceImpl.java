package global.services.server.rpc;

import java.util.ArrayList;
import java.util.List;

import global.services.client.rpc.ApplicationService;
import global.services.server.database.ApplicationDataBase;
import global.services.shared.Application;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ApplicationServiceImpl extends RemoteServiceServlet implements
		ApplicationService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Long DeleteApp(String userId, Long appId) {
		// TODO Auto-generated method stub
		Long ret = null;
		if (userId != null) {
			ApplicationDataBase appDB = new ApplicationDataBase();
			ret = appDB.DeleteApp(userId, appId);
			appDB.Finalize();
		}
			
		return ret;
	}

	@Override
	public Long DeleteApps(String userId) {
		// TODO Auto-generated method stub
		Long ret = null;
		ApplicationDataBase appDB = new ApplicationDataBase();
		ret = appDB.DeleteApps(userId);
		appDB.Finalize();
		return ret;

	}

	
	@Override
	public Long InsertApp(Application app) {
		// TODO Auto-generated method stub
		Long ret = null;

		ApplicationDataBase appDB = new ApplicationDataBase();
		ret = appDB.InsertApp(app);
		appDB.Finalize();
		return ret;
	}

	@Override
	public Long UpdateApp(Application app) {
		// TODO Auto-generated method stub
		ApplicationDataBase appDB = new ApplicationDataBase();
		Long ret = appDB.UpdateApp(app);
		appDB.Finalize();
		return ret;
	}

	@Override
	public Application SelectApp(String userId, Long appId) {
		// TODO Auto-generated method stub
		ApplicationDataBase appDB = new ApplicationDataBase();
		Application appRet = appDB.SelectApp(userId, appId);
		appDB.Finalize();
		return appRet;
	}

	@Override
	public List<Application> SelectApps(String userId) {
		// TODO Auto-generated method stub
		ApplicationDataBase appDB = new ApplicationDataBase();
		List<Application> selectedApps = appDB.SelectApps(userId);
		List<Application> retApps = new ArrayList<Application>();
		for (Application app : selectedApps) {
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
		ApplicationDataBase appDB = new ApplicationDataBase();
		for (Long appId : listAppId) {
			tmpRet = appDB.DeleteApp(userId, appId);
			if (tmpRet != null) 
				ret++;
		}
		appDB.Finalize();
		return ret;
	}

}
