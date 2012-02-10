package global.services.client.rpc;


import global.services.shared.Application;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ApplicationServiceAsync {
	 public void InsertApp(Application app, AsyncCallback<Long> callback);
	 public void UpdateApp(Application app, AsyncCallback<Long> callback);
	 public void DeleteApp(String userId, Long appId, AsyncCallback<Long> callback);
	 public void DeleteApps(String userId, List<Long> listAppId, AsyncCallback<Integer> callback);
	 public void DeleteApps(String userId, AsyncCallback<Long> callback);
	 public void SelectApp(String userId, Long appId, AsyncCallback<Application> callback);
	 public void SelectApps(String userId, AsyncCallback<List<Application>> callback);
	 // Thu edit cho nay xem sao
}
