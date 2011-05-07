package global.services.server.rpc;

import java.util.List;

import global.services.client.rpc.AdvertisementService;
import global.services.server.database.AdvertisementDataBase;
import global.services.server.database.AppScoreDataBase;
import global.services.shared.Advertisement;
import global.services.shared.AppScore;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AdvertisementServiceImpl extends RemoteServiceServlet implements
AdvertisementService {

	@Override
	public Long DeleteAdv(String userId, String appId) {
		// TODO Auto-generated method stub
		Long ret = null;
		if (userId != null) {
			AdvertisementDataBase advDB = new AdvertisementDataBase();
			ret = advDB.DeleteAdv(userId, userId);
		}
			
		return ret;
	}

	@Override
	public Long DeleteAdvs(String userId) {
		// TODO Auto-generated method stub
		Long ret = null;
		if (userId != null) {
			AdvertisementDataBase advDB = new AdvertisementDataBase();
			ret = advDB.DeleteAdvs(userId);
		}
			
		return ret;
	}

	@Override
	public Long InsertAdv(Advertisement adv) {
		Long ret = null;

		AdvertisementDataBase advDB = new AdvertisementDataBase();
		ret = advDB.InsertAdv(adv).getId();
		return ret;
	}

	@Override
	public Advertisement SelectAdv(String userId, String appId) {
		// TODO Auto-generated method stub
		AdvertisementDataBase advDB = new AdvertisementDataBase();
		return advDB.SelectAdv(userId, appId);

	}

	@Override
	public List<Advertisement> SelectAdvs(String userId) {
		// TODO Auto-generated method stub
		AdvertisementDataBase advDB = new AdvertisementDataBase();
		return advDB.SelectAdvs(userId);
	}

	@Override
	public Long UpdateAdv(Advertisement adv) {
		// TODO Auto-generated method stub
		return null;

	}


}
