package global.services.server.database;

import global.services.server.PMF;
import global.services.shared.AppScore;
import global.services.shared.HighScore;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;


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

	public Long DeleteApp(String userId, Long appId) {
		// TODO Auto-generated method stub
		Long ret = null;
		Query query = pm_.newQuery(AppScore.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if (appId != null)
			query.setFilter("id == " + appId);
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
		AppScore appScore = pm_.getObjectById(AppScore.class, app.getId());
		appScore.setAppName(app.getAppName());
		appScore.setAppTittle(app.getAppTittle());
		return app.getId();
	}

	public AppScore SelectApp(String userId, Long appId) {
		// TODO Auto-generated method stub
		String strQuery = "select from " + AppScore.class.getName();
		// + HighScore.class.getName() ;
		Query query = pm_.newQuery(strQuery);
		// query.setOrdering("highScore desc, during asc");
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if (appId != null)
			query.setFilter("id == " + appId);

		List<AppScore> appscores = (List<AppScore>) query.execute();

		return appscores.get(0);
	}
	@SuppressWarnings("unchecked")
	public List<AppScore> SelectApps(String userId) {
		// TODO Auto-generated method stub
		Transaction tx =  pm_.currentTransaction();
		tx.begin();
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
		tx.commit();
		for (AppScore app : selectedApps) {
			tx.begin();
			strQuery = "select from " + HighScore.class.getName();
			query = pm_.newQuery(strQuery);
			if (userId != null)
				query.setFilter("userId_ == \"" + userId + "\"");

			query.setFilter("appId_ == " + app.getId());
			List<HighScore> scores = (List<HighScore>) query.execute();
			
			app.setScoreEntries(scores.size());
			tx.commit();
			retApps.add(app);
		}
		//Window.alert("selectedApps size in database: " + selectedApps.size());
		return retApps;
	}

}
