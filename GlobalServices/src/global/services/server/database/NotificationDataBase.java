package global.services.server.database;

import java.util.List;

import global.services.server.PMF;
import global.services.shared.Notification;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class NotificationDataBase {

	private Notification notification_;
	private PersistenceManager pm_;

	public NotificationDataBase() {
		pm_ = PMF.get().getPersistenceManager();
	}

	public NotificationDataBase(Notification note) {
		pm_ = PMF.get().getPersistenceManager();
		notification_ = note;
	}

	public void Finalize() {
		pm_.close();
	}

	public Notification InsertNote(Notification note) {
		return pm_.makePersistent(note);
	}

	public void UpdateNote(Notification note) {

	}

	public Long DeleteNote(String userId, String appId) {
		Query query = pm_.newQuery(Notification.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if ((appId != null) && !appId.isEmpty())
			query.setFilter("appId_ == \"" + appId + "\"");
		return query.deletePersistentAll();

	}

	public Long DeleteNotes(String userId) {
		Query query = pm_.newQuery(Notification.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		return query.deletePersistentAll();

	}

	public Notification SelectNote(String userId, String appId) {
		String strQuery = "select from " + Notification.class.getName();
		Query query = pm_.newQuery(strQuery);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if ((appId != null) && !appId.isEmpty())
			query.setFilter("appId_ == \"" + appId + "\"");
		List<Notification> retAdvs = (List<Notification>) query.execute();
		return retAdvs.get(0);

	}

	public List<Notification> SelectNotes(String userId) {
		String strQuery = "select from " + Notification.class.getName();
		Query query = pm_.newQuery(strQuery);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		List<Notification> retAdvs = (List<Notification>) query.execute();

		return retAdvs;
	}

}
