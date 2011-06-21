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
		if (userId != null) {
			ScoreDataBase scoreDB = new ScoreDataBase();
			return scoreDB.DeleteScore(userId, id);
		}
		return null;
			
	}

	@Override
	public Long DeleteScores(String userId, String appId) {
		if (userId != null) {
			ScoreDataBase scoreDB = new ScoreDataBase();
			return scoreDB.DeleteScores(userId, appId);
		}
		return null;
	}

	@Override
	public List<HighScore> SelectScores(String userId, String appId) {
		// TODO Auto-generated method stub
		ScoreDataBase scoreDB = new ScoreDataBase();
		return scoreDB.SelectScores(userId, appId);
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
