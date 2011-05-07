package global.services.server.database;

import java.util.ArrayList;
import java.util.List;

import global.services.server.PMF;
import global.services.shared.Advertisement;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class AdvertisementDataBase {
	private Advertisement advertisement_;
	private PersistenceManager pm_;

	public AdvertisementDataBase() {
		pm_ = PMF.get().getPersistenceManager();
	}

	public AdvertisementDataBase(Advertisement adv) {
		pm_ = PMF.get().getPersistenceManager();
		advertisement_ = adv;
	}
	public void Finalize() {
		pm_.close();
	}

	public Advertisement InsertAdv(Advertisement adv) {
			return pm_.makePersistent(adv);
	}

	public void UpdateAdv(Advertisement adv) {

	}

	public Long DeleteAdv(String userId, String appId) {
		Query query = pm_.newQuery(Advertisement.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if ((appId != null) && !appId.isEmpty())
			query.setFilter("appId_ == \"" + appId + "\"");
		return query.deletePersistentAll();

	}

	public Long DeleteAdvs(String userId) {
		Query query = pm_.newQuery(Advertisement.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		return query.deletePersistentAll();

	}
	

	public Advertisement SelectAdv(String userId, String appId) {
		String strQuery = "select from " + Advertisement.class.getName();
		Query query = pm_.newQuery(strQuery);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if ((appId != null) && !appId.isEmpty())
			query.setFilter("appId_ == \"" + appId + "\"");
		List<Advertisement> retAdv = (List<Advertisement>) query.execute();
		return retAdv.get(0);

	}

	public List<Advertisement> SelectAdvs(String userId) {
		String strQuery = "select from " + Advertisement.class.getName();
		Query query = pm_.newQuery(strQuery);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		List<Advertisement> retAdvs = (List<Advertisement>) query.execute();
		
		return retAdvs;
	}

}
