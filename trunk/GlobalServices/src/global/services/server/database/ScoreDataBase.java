package global.services.server.database;

import java.util.List;

import global.services.server.PMF;
import global.services.shared.HighScore;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class ScoreDataBase {
	private HighScore score_;
	private PersistenceManager pm_;

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


	public HighScore SelectScore(Long id) {
		// TODO Auto-generated method stub
		return null;

	}
	
	public void setScore(HighScore score) {
		this.score_ = score;
	}

	public HighScore getScore() {
		return score_;
	}

	@SuppressWarnings("unchecked")
	public List<HighScore> SelectScores(String userId, String appId) {
		// TODO Auto-generated method stub

		String strQuery = "select from " + HighScore.class.getName();
		Query query = pm_.newQuery(strQuery);
		if (userId != null)
			query.setFilter("userId_ == \"" + userId + "\"");
		if (appId != null)
			query.setFilter("appId_ == \"" + appId + "\"");
		List<HighScore> scores = (List<HighScore>) query.execute();
		return scores;

	}

	public void InsertScore(HighScore score) {
		// TODO Auto-generated method stub
			pm_.makePersistent(score);

	}

	public Long UpdateScore(HighScore score) {
		// TODO Auto-generated method stub
		return null;

	}

	public Long DeleteScore(String userId, Long id) {
		// TODO Auto-generated method stub
		
		Query query = pm_.newQuery(HighScore.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if (id != null)
			query.setFilter("id == " + id );
		return query.deletePersistentAll();

	}

	public Long DeleteScores(String userId, String appId) {
		// TODO Auto-generated method stub
		Query query = pm_.newQuery(HighScore.class);
		if ((userId != null) && !userId.isEmpty())
			query.setFilter("userId_ == \"" + userId + "\"");
		if ((appId != null) && !appId.isEmpty())
			query.setFilter("appId_ == \"" + appId + "\"");
		return query.deletePersistentAll();

	}



}
