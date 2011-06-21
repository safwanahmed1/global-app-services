package global.services.server.database;

import global.services.server.PMF;
import global.services.shared.AppScore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.client.Window;

public class AppScoreDataBase {
	private static final Logger LOG = Logger.getLogger(AdvertisementDataBase.class.getName());
	private AppScore application_;
	private PersistenceManager pm_;

	public AppScoreDataBase() {
		pm_ = PMF.get().getPersistenceManager();
	}

	public AppScoreDataBase(AppScore app) {
		application_ = app;
		pm_ = PMF.get().getPersistenceManager();
	}

	public void Finalize() {
		pm_.close();
	}

	public Long DeleteApp(String userId, String appId) {
		// TODO Auto-generated method stub
		Long ret = null;
		Query query = pm_.newQuery(AppScore.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if ((appId != null) && !appId.isEmpty())
			query.setFilter("appId_ == \"" + appId + "\"");
		ret = query.deletePersistentAll();
		return ret;

	}

	public Long DeleteApps(String userId) {
		// TODO Auto-generated method stub
		Long ret = null;
		Query query = pm_.newQuery(AppScore.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		ret = query.deletePersistentAll();
		return ret;

	}

	public Long InsertApp(AppScore app) {
		// TODO Auto-generated method stub
		Long ret = null;

		ret = pm_.makePersistent(app).getId();
		return ret;
	}

	public Long UpdateApp(AppScore app) {
		// TODO Auto-generated method stub
		Long ret = null;
		return ret;
	}

	public AppScore SelectApp(String userId, String appId) {
		// TODO Auto-generated method stub
		String strQuery = "select from " + AppScore.class.getName();
		// + HighScore.class.getName() ;
		Query query = pm_.newQuery(strQuery);
		// query.setOrdering("highScore desc, during asc");
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if ((appId != null) && !appId.isEmpty())
			query.setFilter("appId_ == \"" + appId + "\"");

		List<AppScore> appscores = (List<AppScore>) query.execute();

		return appscores.get(0);
	}
	@SuppressWarnings("unchecked")
	public List<AppScore> SelectApps(String userId) {
		// TODO Auto-generated method stub
		String strQuery = "select from " + AppScore.class.getName();
		// + HighScore.class.getName() ;
		Query query = pm_.newQuery(strQuery);
		// query.setOrdering("highScore desc, during asc");
		if (userId != null)
			query.setFilter("userId_ == \"" + userId + "\"");
		List<AppScore> retApps = new ArrayList<AppScore>();
		//(List<Question>)pm.detachCopyAll((List<Question>)query.execute());
		//List<AppScore> selectedApps = (List<AppScore>)pm_.detachCopyAll((List<AppScore>) query.execute());
		List<AppScore> selectedApps =(List<AppScore>) query.execute();
		for (AppScore app : selectedApps) {
			retApps.add(app);
		}
		//Window.alert("selectedApps size in database: " + selectedApps.size());
		return retApps;
	}

}
