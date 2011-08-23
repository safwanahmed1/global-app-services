package global.services.server.servlet;
import global.services.server.database.ScoreDataBase;
import global.services.shared.HighScore;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

import java.util.logging.Level;
import java.util.logging.Logger;


@SuppressWarnings("serial")
public class HighscoreServlet extends HttpServlet {
	 private static Logger logger = Logger.getLogger("HighscoreServlet");
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// super.doPost(req, resp);
		log("Do Post method.");
		String reqType = req.getHeader("requesttype");
		if ((reqType != null) && (reqType == "submitscore")) {
			logger.log(Level.SEVERE, "requesttype = submitscore");
			SubmitScore(req, resp);
		}
		if ((reqType != null) && (reqType == "getscore")) {
			logger.log(Level.SEVERE, "requesttype = getscore");
			GetScore(req, resp);
		}
	}

	private void SubmitScore(HttpServletRequest req, HttpServletResponse resp) {
		resp.setContentType("text/plain");
		// UserService userService = UserServiceFactory.getUserService();
		// User user = userService.getCurrentUser();
		String userID = req.getHeader("userid");
		logger.log(Level.SEVERE, "userid = " + userID);
		Long gameID = Long.parseLong(req.getHeader("appid"));
		logger.log(Level.SEVERE, "appid = " + gameID);
		String subBoard = req.getHeader("subboard");
		String player = req.getHeader("player");
		Integer highScore = Integer.parseInt(req.getHeader("score"));
		Long during = Long.parseLong(req.getHeader("during"));
		String location = req.getHeader("location");
		String comment = req.getHeader("comment");
		Long date = Long.parseLong(req.getHeader("date"));
		//String avatar = req.getHeader("avatar"); not support yet

		if ((gameID != null) && (player != null)) {
			HighScore highscore = new HighScore(userID, gameID);
			if (subBoard != null)
				highscore.setSubBoard(subBoard);
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
			/* Not support yet
			if (avatar != null) 
				highscore.setAvatar(avatar);
			*/
			ScoreDataBase scoreDB = new ScoreDataBase();
			scoreDB.InsertScore(highscore);
		}
	}

	private void GetScore(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String userId = null;
		Long appId = null;
		userId = req.getHeader("userid");
		appId = Long.parseLong(req.getHeader("appid"));

		ScoreDataBase scoreDB = new ScoreDataBase();
		ServletOutputStream stream = null;
		try {
			List<HighScore> highscore = scoreDB.SelectScores(userId, appId);

			if (!highscore.isEmpty()) {
				resp.setContentType("text/xml; charset=UTF-8");
				resp.setHeader("Content-Disposition",
						"attachment; filename=globalscore.xml");
				// resp.setLocale(arg0)
				stream = resp.getOutputStream();
				stream.print("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
				stream.print("<scores>");

				for (HighScore score : highscore) {
					stream.print("<score ");
					if (score.getSubBoard() != null)
						stream.print(" subboard=\"" + score.getSubBoard()
								+ "\"");
					if (score.getPlayer() != null)
						stream.print(" player=\"" + score.getPlayer() + "\"");
					if (score.getHighScore() != 0)
						stream.print(" highscore=\"" + score.getHighScore()
								+ "\"");
					if (score.getDuring() != 0)
						stream.print(" during=\"" + score.getDuring() + "\"");
					if (score.getComment() != null)
						stream.print(" comment=\"" + score.getComment() + "\"");
					if (score.getDate() != 0)
						stream.print(" date=\"" + score.getDate() + "\"");
					if (score.getLocation() != null)
						stream.print(" location=\"" + score.getLocation()
								+ "\"");
					/*
					 * Not support yet 
					 * if (score.getAvatar() != null)
					 * stream.print(" avatar=\"" + score.getAvatar() + "\"");
					 */
					stream.println("/>");
				}
				stream.print("</scores>");

			}
		} finally {
			if (stream != null) {
				stream.close();
			}

		}

	}
}
