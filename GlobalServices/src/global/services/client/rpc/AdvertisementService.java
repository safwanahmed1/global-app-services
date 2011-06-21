package global.services.client.rpc;

import global.services.shared.Advertisement;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("advertisements")
public interface AdvertisementService extends RemoteService {

	public Long InsertAdv(Advertisement adv);
	public Long UpdateAdv(Advertisement adv);
	public Long DeleteAdv(String userId, String appId);
	public int DeleteAdvs(String userId, List<String> listAppId);
	public Long DeleteAdvs(String userId);
	public Advertisement SelectAdv(String userId, String appId);
	public List<Advertisement> SelectAdvs(String userId);
}
