package global.services.client.rpc;

import global.services.shared.HighScore;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface HighScoreServiceAsync {
	public void InsertScore(HighScore score, AsyncCallback<Long> callback);

	public void UpdateScore(HighScore score, AsyncCallback<Long> callback);

	public void DeleteScore(String userId, Long id, AsyncCallback<Long> callback);

	public void DeleteScores(String userId, List<Long> listScoreId,
			AsyncCallback<Integer> callback);

	public void DeleteScores(String userId, Long appId,
			AsyncCallback<Long> callback);
	public void SelectScore(String userId, long scoreId,
			AsyncCallback<HighScore> callback);
	public void SelectScores(String userId, long appId,
			AsyncCallback<List<HighScore>> callback);
}
