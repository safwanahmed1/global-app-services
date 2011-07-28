package global.services.client.rpc;



import global.services.shared.Advertisement;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AdvertisementServiceAsync {
	 public void InsertAdv(Advertisement adv, AsyncCallback<Long> callback);
	 public void UpdateAdv(Advertisement adv, AsyncCallback<Long> callback);
	 public void DeleteAdv(String userId, Long appId, AsyncCallback<Long> callback);
	 public void DeleteAdvs(String userId, List<Long> listAppId, AsyncCallback<Integer> callback);
	 public void DeleteAdvs(String userId, AsyncCallback<Long> callback);
	 public void SelectAdv(String userId, Long appId, AsyncCallback<Advertisement> callback);
	 public void SelectAdvs(String userId, AsyncCallback<List<Advertisement>> callback);
}
