package global.services.server.servlet;

import global.services.server.database.ScoreDataBase;
import global.services.shared.HighScore;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import java.util.logging.Logger;

@SuppressWarnings("serial")
public class HighscoreServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(HighscoreServlet.class
			.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// super.doPost(req, resp);
		logger.warning("Do post method of HighscoreServlet.");

		// String reqType = req.getParameter("requesttype");
		String reqType = req.getParameter("requesttype");
		logger.warning("reqType :" + reqType);
		if ((reqType != null) && ("submitscore".equals(reqType))) {
			logger.warning("requesttype = submitscore");
			SubmitScore(req, resp);
		}
		if ((reqType != null) && ("getscore".equals(reqType))) {
			logger.warning("requesttype = getscore");
			GetScore(req, resp);
		}
	}

	private void SubmitScore(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("text/plain");
		// UserService userService = UserServiceFactory.getUserService();
		// User user = userService.getCurrentUser();
		String userID = req.getParameter("userid");
		logger.warning("userid = " + userID);
		Long gameID = Long.parseLong(req.getParameter("appid"));
		logger.warning("appid = " + gameID);
		String subBoard = req.getParameter("subboard");
		String player = req.getParameter("player");
		Integer highScore = Integer.parseInt(req.getParameter("score"));
		Long during = Long.parseLong(req.getParameter("during"));
		String location = req.getParameter("location");
		String comment = req.getParameter("comment");
		Long date = Long.parseLong(req.getParameter("date"));
		// String avatar = req.getParameter("avatar"); not support yet

		if ((gameID != null) && (player != null)) {
			HighScore highscore = new HighScore(userID, gameID);
			if (subBoard != null)
				highscore.setSubBoard(subBoard);
			if (player != null)
				highscore.setPlayer(player);
			if (highScore != null)
				highscore.setHighScore(highScore);
			if (comment != null)
				highscore.setComment(comment);
			if (location != null)
				highscore.setLocation(location);
			if (during != null)
				highscore.setDuring(during);
			if (date != null)
				highscore.setDate(date);
			/*
			 * Not support yet if (avatar != null) highscore.setAvatar(avatar);
			 */
			ScoreDataBase scoreDB = new ScoreDataBase();
			scoreDB.InsertScore(highscore);
		}
	}

	private void GetScore(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String  userId = req.getParameter("userid");
		Long appId = Long.parseLong(req.getParameter("appid"));
		int pageIdx = Integer.parseInt(req.getParameter("pageindex"));
		int pageSize = Integer.parseInt(req.getParameter("pagesize"));

		ScoreDataBase scoreDB = new ScoreDataBase();
		scoreDB.setPageIdx(pageIdx);
		scoreDB.setPageSize(pageSize);
		ServletOutputStream stream = null;
		try {
			List<HighScore> highscore = scoreDB.SelectScores(userId, appId);

			if (!highscore.isEmpty()) {
				resp.setContentType("application/json; charset=UTF-8");
//				resp.setHeader("Content-Disposition",
//						"attachment; filename=globalscore.xml");
				// resp.setLocale(arg0)
				stream = resp.getOutputStream();
				
				stream.print("{\"scorelist\":[");

				for (HighScore score : highscore) {
					
					if (score.getId() != null)
						stream.print("{ \"id\":\"" + score.getId() + "\",");
					if (score.getUserID() != null)
						stream.print("\"userid\":\"" + score.getUserID() + "\",");
					if (score.getSubBoard() != null)
						stream.print("\"subboard\":\"" + score.getSubBoard()
								+ "\",");
					if (score.getGameID() != null)
						stream.print("\"appid\":\"" + score.getGameID()
								+ "\",");
					if (score.getPlayer() != null)
						stream.print("\"player\":\"" + score.getPlayer() + "\",");
					if (score.getHighScore() != 0)
						stream.print("\"highscore\":\"" + score.getHighScore()
								+ "\",");
					if (score.getDuring() != 0)
						stream.print("\"during\":\"" + score.getDuring() + "\",");
					if (score.getComment() != null)
						stream.print("\"comment\":\"" + score.getComment() + "\",");
					if (score.getDate() != 0)
						stream.print("\"date\":\"" + score.getDate() + "\",");
					if (score.getLocation() != null)
						stream.print("\"location\":\"" + score.getLocation()
								+ "\"}");
					/*
					 * Not support yet if (score.getAvatar() != null)
					 * stream.print(" avatar=\"" + score.getAvatar() + "\"");
					 */
					stream.println(",");
				}
				stream.print("]}");

			}
		} finally {
			if (stream != null) {
				stream.close();
			}

		}

	}
}
