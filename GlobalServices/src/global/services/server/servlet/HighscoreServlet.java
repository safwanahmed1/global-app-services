package global.services.server.servlet;

import global.services.server.database.ScoreDataBase;
import global.services.shared.HighScore;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class HighscoreServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// super.doPost(req, resp);
		String reqType = req.getHeader("requesttype");
		if ((reqType != null) && (reqType == "submitscore")) {
			SubmitScore(req, resp);
		}
		if ((reqType != null) && (reqType == "getscore")) {
			
			GetScore(req, resp);
		}
	}
	private void SubmitScore(HttpServletRequest req, HttpServletResponse resp){
		resp.setContentType("text/plain");
		// UserService userService = UserServiceFactory.getUserService();
		// User user = userService.getCurrentUser();
		String userID = req.getParameter("UserID");
		Long gameID = Long.parseLong(req.getParameter("GameID"));
		String subBoard = req.getParameter("SubBoard");
		String player = req.getParameter("Player");
		String highScore = req.getParameter("HighScore");
		String comment = req.getParameter("Comment");
		String location = req.getParameter("Location");
		String during = req.getParameter("During");
		String date = req.getParameter("Date");
		String avatar = req.getParameter("Avatar");
		if ((gameID != null) && (player != null)) {
			HighScore highscore = new HighScore(userID, gameID);
			if (subBoard != null)
				highscore.setSubBoard(subBoard);
			if (highScore != null)
				highscore.setHighScore(Integer.parseInt(highScore));
			if (comment != null)
				highscore.setComment(comment);
			if (location != null)
				highscore.setLocation(location);
			if (during != null)
				highscore.setDuring(Integer.parseInt(during));
			if (date != null)
				highscore.setDate(Long.parseLong(date));
			if (avatar != null)
				highscore.setAvatar(avatar);
			ScoreDataBase scoreDB = new ScoreDataBase();
			scoreDB.InsertScore(highscore);
		}
	}

	private void GetScore(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String userId = null;
		Long appId = null;
		String subBoard = null;
		// resp.setContentType("text/xml");
		if (req.getParameterMap().containsKey("userid"))
			userId = req.getParameter("userid");
		if (req.getParameterMap().containsKey("appid"))
			appId = Long.parseLong(req.getParameter("appid"));
		if (req.getParameterMap().containsKey("subboard"))
			subBoard = req.getParameter("subboard");

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
					stream.print("<score ");// username="ABC" score="456"
											// rank="3" />
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
					if (score.getAvatar() != null)
						stream.print(" avatar=\"" + score.getAvatar() + "\"");
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
