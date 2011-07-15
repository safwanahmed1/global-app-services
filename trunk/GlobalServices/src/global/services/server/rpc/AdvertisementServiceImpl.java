package global.services.server.rpc;

import java.util.List;

import global.services.client.rpc.AdvertisementService;
import global.services.server.database.AdvertisementDataBase;
import global.services.shared.Advertisement;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AdvertisementServiceImpl extends RemoteServiceServlet implements
AdvertisementService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Long DeleteAdv(String userId, String appId) {
		// TODO Auto-generated method stub
		Long ret = null;
		if (userId != null) {
			AdvertisementDataBase advDB = new AdvertisementDataBase();
			ret = advDB.DeleteAdv(userId, appId);
			advDB.Finalize();
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
			advDB.Finalize();
		}
			
		return ret;
	}

	@Override
	public Long InsertAdv(Advertisement adv) {
		Long ret = null;

		AdvertisementDataBase advDB = new AdvertisementDataBase();
		ret = advDB.InsertAdv(adv).getId();
		advDB.Finalize();
		return ret;
	}

	@Override
	public Advertisement SelectAdv(String userId, Long appId) {
		// TODO Auto-generated method stub
		AdvertisementDataBase advDB = new AdvertisementDataBase();
		Advertisement advRet = advDB.SelectAdv(userId, appId);
		advDB.Finalize();
		return advRet;

	}

	@Override
	public List<Advertisement> SelectAdvs(String userId) {
		// TODO Auto-generated method stub
		AdvertisementDataBase advDB = new AdvertisementDataBase();
		List<Advertisement> advListRet = advDB.SelectAdvs(userId);
		advDB.Finalize();
		return advListRet;
	}

	@Override
	public Long UpdateAdv(Advertisement adv) {
		// TODO Auto-generated method stub
		AdvertisementDataBase advDB = new AdvertisementDataBase();
		Long advIdRet = advDB.UpdateAdv(adv);
		advDB.Finalize();
		return advIdRet;

	}

	@Override
	public int DeleteAdvs(String userId, List<String> listAppId) {
		// TODO Auto-generated method stub
		Long tmpRet = (long) 0;
		int ret = 0;
		for (String appId : listAppId) {
			tmpRet = DeleteAdv(userId, appId);
			if (tmpRet != null) 
				ret++;
		}
		return ret;
	}


}
