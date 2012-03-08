package global.services.server.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import global.services.server.PMF;
import global.services.server.servlet.HighscoreServlet;
import global.services.shared.HighScore;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class ScoreDataBase {
	private HighScore score_;
	private PersistenceManager pm_;
	private int pageIdx = 0;
	private int pageSize = 10;
	private static Logger logger = Logger.getLogger(ScoreDataBase.class
			.getName());

	public int getPageIdx() {
		return pageIdx;
	}

	public void setPageIdx(int pageIdx) {
		this.pageIdx = pageIdx;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public ScoreDataBase() {
		pm_ = PMF.get().getPersistenceManager();
	}

	public ScoreDataBase(HighScore score) {
		setScore(score);
		pm_ = PMF.get().getPersistenceManager();
	}

	public void Finalize() {
		pm_.close();
	}

	public HighScore SelectScore(String userId, Long scoreId) {
		// TODO Auto-generated method stub
		String strQuery = "select from " + HighScore.class.getSimpleName();
		Query query = pm_.newQuery(strQuery);
		if (userId != null)
			query.setFilter("userId_ = \"" + userId + "\"");
		if (scoreId != null)
			query.setFilter("id = " + scoreId);
		List<HighScore> scores = (List<HighScore>) query.execute();
		return scores.get(0);

	}

	public void setScore(HighScore score) {
		this.score_ = score;
	}

	public HighScore getScore() {
		return score_;
	}

	public List<HighScore> SelectScores(String userId, Long appId) {
		// TODO Auto-generated method stub
		List<HighScore> retScores = new ArrayList<HighScore>();
		String strQuery = "select from " + HighScore.class.getName();
		Query query = pm_.newQuery(strQuery);
		if (userId != null)
			query.setFilter("userId_ == \"" + userId + "\"");
		if (appId != null)
			query.setFilter("appId_ == " + appId);

		query.setOrdering("score_ desc");
		query.setRange(pageIdx * pageSize, (pageIdx + 1) * pageSize);
		//logger.warning("Query :" + query.toString());
		List<HighScore> scores = (List<HighScore>) query.execute();
		for (HighScore score : scores) {
			retScores.add(score);
		}
		return retScores;

	}

	public Long InsertScore(HighScore score) {
		// TODO Auto-generated method stub

		return pm_.makePersistent(score).getId();

	}

	public Long UpdateScore(HighScore score) {
		// TODO Auto-generated method stub
		HighScore scoreTemp = pm_.getObjectById(HighScore.class, score.getId());
		scoreTemp.setGameID(score.getGameID());
		scoreTemp.setSubBoard(score.getSubBoard());
		scoreTemp.setLocation(score.getLocation());
		scoreTemp.setHighScore(score.getHighScore());
		scoreTemp.setDuring(score.getDuring());
		scoreTemp.setComment(score.getComment());
		scoreTemp.setDate(new Date().getTime());
		return scoreTemp.getId();

	}

	public Long DeleteScore(String userId, Long id) {
		// TODO Auto-generated method stub

		Query query = pm_.newQuery(HighScore.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if (id != null)
			query.setFilter("id == " + id);
		return query.deletePersistentAll();

	}

	public Long DeleteScores(String userId, Long appId) {
		// TODO Auto-generated method stub
		Query query = pm_.newQuery(HighScore.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if (appId != null)
			query.setFilter("appId_ == " + appId);
		return query.deletePersistentAll();

	}

}
