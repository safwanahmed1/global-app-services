package global.services.client.rpc;


import global.services.shared.HighScore;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("appScores")
public interface HighScoreService extends RemoteService {

	public Long DeleteScore(String userId, Long id);
	public Long DeleteScores(String userId, String appId);
	public List<HighScore> SelectScores(String userId, String appId);
}
