package global.services.server.database;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

import global.services.server.PMF;
import global.services.shared.Advertisement;
import global.services.shared.AppScore;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class AdvertisementDataBase {
	private static final Logger LOG = Logger.getLogger(AdvertisementDataBase.class.getName());
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

	public Long UpdateAdv(Advertisement adv) {
		Advertisement advTemp = pm_.getObjectById(Advertisement.class, adv.getId());
		advTemp.setAppName(adv.getAppName());
		advTemp.setContent(adv.getContent());
		advTemp.setIconFile(adv.getIconFile());
		advTemp.setStoreUrl(adv.getStoreUrl());
		advTemp.setTittle(adv.getTittle());
		advTemp.setType(adv.getType());
		return advTemp.getId();
	}

	public Long DeleteAdv(String userId, Long appId) {
		Query query = pm_.newQuery(Advertisement.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if (appId != null) 
			query.setFilter("appId_ == " + appId);
		return query.deletePersistentAll();

	}

	public Long DeleteAdvs(String userId) {
		Query query = pm_.newQuery(Advertisement.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		return query.deletePersistentAll();

	}
	

	public Advertisement SelectAdv(String userId, Long appId) {
		String strQuery = "select from " + Advertisement.class.getName();
		Query query = pm_.newQuery(strQuery);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if (appId != null)
			query.setFilter("id == \"" + appId);
		List<Advertisement> retAdv = (List<Advertisement>) query.execute();
		return retAdv.get(0);

	}
	@SuppressWarnings("unchecked")
	public List<Advertisement> SelectAdvs(String userId) {
		String strQuery = "select from " + Advertisement.class.getName();
		Query query = pm_.newQuery(strQuery);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		List<Advertisement> retAdvs = new ArrayList<Advertisement>();
		List<Advertisement> selectedAdvs = (List<Advertisement>) query.execute();
		for (Advertisement adv : selectedAdvs) {
			retAdvs.add(adv);
		}
		LOG.log(Level.INFO, String.valueOf(retAdvs.size()));
		return retAdvs;
	}

}
