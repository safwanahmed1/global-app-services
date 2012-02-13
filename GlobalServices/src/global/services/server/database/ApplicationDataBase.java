package global.services.server.database;

import global.services.server.PMF;
import global.services.shared.Application;
import global.services.shared.HighScore;
import global.services.shared.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

public class ApplicationDataBase {
	private static final Logger LOG = Logger
			.getLogger(AdvertisementDataBase.class.getName());
	private Application application_;
	private PersistenceManager pm_;

	public ApplicationDataBase() {
		pm_ = PMF.get().getPersistenceManager();
	}

	public ApplicationDataBase(Application app) {
		application_ = app;
		pm_ = PMF.get().getPersistenceManager();
	}

	public void Finalize() {
		pm_.close();
	}

	public Long DeleteApp(String userId, Long appId) {
		// TODO Auto-generated method stub
		Long ret = null;
		Query query = pm_.newQuery(Application.class);
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
		Query query = pm_.newQuery(Application.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		ret = query.deletePersistentAll();
		return ret;

	}

	public Long InsertApp(Application app) {
		// TODO Auto-generated method stub
		Long ret = null;

		ret = pm_.makePersistent(app).getId();
		return ret;
	}

	public Long UpdateApp(Application app) {
		// TODO Auto-generated method stub
		Application Application = pm_.getObjectById(Application.class, app.getId());
		Application.setAppName(app.getAppName());
		Application.setAppTittle(app.getAppTittle());
		return app.getId();
	}

	public Application SelectApp(String userId, Long appId) {
		// TODO Auto-generated method stub
		String strQuery = "select from " + Application.class.getName();
		// + HighScore.class.getName() ;
		Query query = pm_.newQuery(strQuery);
		// query.setOrdering("highScore desc, during asc");
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if (appId != null)
			query.setFilter("id == " + appId);

		List<Application> appscores = (List<Application>) query.execute();

		return appscores.get(0);
	}

	@SuppressWarnings("unchecked")
	public List<Application> SelectApps(String userId) {
		// TODO Auto-generated method stub
		Transaction tx = pm_.currentTransaction();
		tx.begin();
		String strQuery = "select from " + Application.class.getName();
		// + HighScore.class.getName() ;
		Query query = pm_.newQuery(strQuery);
		// query.setOrdering("highScore desc, during asc");
		if (userId != null)
			query.setFilter("userId_ == \"" + userId + "\"");
		List<Application> retApps = new ArrayList<Application>();
		// (List<Question>)pm.detachCopyAll((List<Question>)query.execute());
		// List<Application> selectedApps =
		// (List<Application>)pm_.detachCopyAll((List<Application>) query.execute());
		List<Application> selectedApps = (List<Application>) query.execute();
		tx.commit();
		for (Application app : selectedApps) {
			tx.begin();
			strQuery = "select from " + HighScore.class.getName();
			query = pm_.newQuery(strQuery);
			if (userId != null)
				query.setFilter("userId_ == \"" + userId + "\"");

			query.setFilter("appId_ == " + app.getId());
			List<HighScore> scores = (List<HighScore>) query.execute();
			if (scores != null)
				app.setScoreEntries(scores.size());
			else
				app.setScoreEntries(0);

			tx.commit();

			tx.begin();

			strQuery = "select from " + Notification.class.getName();

			query = pm_.newQuery(strQuery);
			if (userId != null)
				query.setFilter("userId_ == \"" + userId + "\"");

			query.setFilter("appId_ == " + app.getId());
			List<Notification> notes = (List<Notification>) query.execute();
			if (notes != null)
				app.setNoteEntries(notes.size());
			else
				app.setNoteEntries(0);

			tx.commit();

			retApps.add(app);
		}
		// Window.alert("selectedApps size in database: " +
		// selectedApps.size());
		return retApps;
	}

}
