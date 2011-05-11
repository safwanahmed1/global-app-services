package global.services.client.rpc;


import global.services.shared.AppScore;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AppScoreServiceAsync {
	 public void InsertApp(AppScore app, AsyncCallback<Long> callback);
	 public void UpdateApp(AppScore app, AsyncCallback<Long> callback);
	 public void DeleteApp(String userId, String appId, AsyncCallback<Long> callback);
	 public void DeleteApps(String userId, AsyncCallback<Long> callback);
	 public void SelectApp(String userId, String appId, AsyncCallback<AppScore> callback);
	 public void SelectApps(String userId, AsyncCallback<List<AppScore>> callback);
	 // Thu edit cho nay xem sao
}
