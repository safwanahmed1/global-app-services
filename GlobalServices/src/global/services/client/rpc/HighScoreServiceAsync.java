package global.services.client.rpc;



import global.services.shared.HighScore;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HighScoreServiceAsync {
	 public void DeleteScore(String userId, Long id, AsyncCallback<Long> callback);
	 public void DeleteScores(String userId, List<Long> listScoreId, AsyncCallback<Integer> callback);
	 public void DeleteScores(String userId, String appId, AsyncCallback<Long> callback);
	 public void SelectScores(String userId, String appId, AsyncCallback<List<HighScore>> callback);
}
