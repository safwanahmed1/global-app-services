package global.services.server.servlet;

import global.services.server.database.ScoreDataBase;
import global.services.shared.HighScore;

import java.io.IOException;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class SubmitScoreServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		//UserService userService = UserServiceFactory.getUserService();
		//User user = userService.getCurrentUser();
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
			HighScore highscore = new HighScore(gameID, player);
			if (subBoard!=null)
				highscore.setSubBoard(subBoard);
			if (highScore!=null)
				highscore.setHighScore(Integer.parseInt(highScore));
			if (comment!=null)
				highscore.setComment(comment);
			if (location!=null)
				highscore.setLocation(location);
			if (during!=null)
				highscore.setDuring(Integer.parseInt(during));
			if (date!=null)
				highscore.setDate(Long.parseLong(date));
			if (avatar!=null)
				highscore.setAvatar(avatar);
			ScoreDataBase scoreDB = new ScoreDataBase();
			scoreDB.InsertScore(highscore);
		}

	}
}
