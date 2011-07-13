package global.services.server.rpc;

import java.util.List;

import global.services.client.rpc.HighScoreService;
import global.services.server.database.AppScoreDataBase;
import global.services.server.database.ScoreDataBase;
import global.services.shared.HighScore;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class HighScoreServiceImpl extends RemoteServiceServlet implements
		HighScoreService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Long DeleteScore(String userId, Long id) {
		// TODO Auto-generated method stub
		Long scoreId = null;
		if (userId != null) {
			ScoreDataBase scoreDB = new ScoreDataBase();
			scoreId = scoreDB.DeleteScore(userId, id);
			scoreDB.Finalize();
		}
		return scoreId;
			
	}

	@Override
	public Long DeleteScores(String userId, String appId) {
		Long scoreId = null;
		if (userId != null) {
			ScoreDataBase scoreDB = new ScoreDataBase();
			scoreId = scoreDB.DeleteScores(userId, appId);
			scoreDB.Finalize();
		}
		return scoreId;
	}

	@Override
	public List<HighScore> SelectScores(String userId, String appId) {
		// TODO Auto-generated method stub
		ScoreDataBase scoreDB = new ScoreDataBase();
		List<HighScore> scoreListRet = scoreDB.SelectScores(userId, appId);
		scoreDB.Finalize();
		return scoreListRet;
	}

	@Override
	public int DeleteScores(String userId, List<Long> listScoreId) {
		// TODO Auto-generated method stub
		Long tmpRet = (long) 0;
		int ret = 0;
		for (Long scoreId : listScoreId) {
			tmpRet = DeleteScore(userId, scoreId);
			if (tmpRet != null) 
				ret++;
		}
		return ret;
	}
}
