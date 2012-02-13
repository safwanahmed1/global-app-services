package global.services.client.rpc;


import global.services.shared.HighScore;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("highscores")
public interface HighScoreService extends RemoteService {

	public Long InsertScore(HighScore score);
	public Long UpdateScore(HighScore score);
	public Long DeleteScore(String userId, Long id);
	public int DeleteScores(String userId, List<Long> listScoreId);
	public Long DeleteScores(String userId, Long appId);
	public HighScore SelectScore(String userId, long scoreId);
	public List<HighScore> SelectScores(String userId, long appId, int pageIdx, int pageSize);
}
