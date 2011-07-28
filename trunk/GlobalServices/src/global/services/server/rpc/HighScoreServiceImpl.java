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
	public Long DeleteScores(String userId, Long appId) {
		Long scoreId = null;
		if (userId != null) {
			ScoreDataBase scoreDB = new ScoreDataBase();
			scoreId = scoreDB.DeleteScores(userId, appId);
			scoreDB.Finalize();
		}
		return scoreId;
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

	@Override
	public Long InsertScore(HighScore score) {
		// TODO Auto-generated method stub
		ScoreDataBase scoreDB = new ScoreDataBase();
		Long scoreIdRet = scoreDB.InsertScore(score);
		scoreDB.Finalize();
		return scoreIdRet;
	}

	@Override
	public Long UpdateScore(HighScore score) {
		// TODO Auto-generated method stub
		ScoreDataBase scoreDB = new ScoreDataBase();
		Long ret = scoreDB.UpdateScore(score);
		scoreDB.Finalize();
		return ret;
	}

	@Override
	public HighScore SelectScore(String userId, long scoreId) {
		// TODO Auto-generated method stub
		ScoreDataBase scoreDB = new ScoreDataBase();
		HighScore scoreRet = scoreDB.SelectScore(userId, scoreId);
		scoreDB.Finalize();
		return scoreRet;
	}

	@Override
	public List<HighScore> SelectScores(String userId, long appId) {
		// TODO Auto-generated method stub
		ScoreDataBase scoreDB = new ScoreDataBase();
		List<HighScore> scoreListRet = scoreDB.SelectScores(userId, appId);
		scoreDB.Finalize();
		return scoreListRet;
	}
}
