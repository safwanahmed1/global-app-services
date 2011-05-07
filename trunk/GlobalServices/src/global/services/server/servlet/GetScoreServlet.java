package global.services.server.servlet;

import global.services.server.database.ScoreDataBase;
import global.services.shared.HighScore;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;



@SuppressWarnings("serial")
public class GetScoreServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String userId = null;
		String appId = null;
		String subBoard = null;
		//resp.setContentType("text/xml");
		if (req.getParameterMap().containsKey("userid"))
			userId = req.getParameter("userid");
		if (req.getParameterMap().containsKey("appid"))
			appId = req.getParameter("appid");
		if (req.getParameterMap().containsKey("subboard"))
			subBoard = req.getParameter("subboard");

		ScoreDataBase scoreDB = new ScoreDataBase();
		ServletOutputStream stream = null ;
		try {
			List<HighScore> highscore = scoreDB.SelectScores(userId, appId);
			
			if (!highscore.isEmpty()) {
				resp.setContentType("text/xml; charset=UTF-8");  
				resp.setHeader("Content-Disposition", "attachment; filename=globalscore.xml");
				//resp.setLocale(arg0)
			     stream = resp.getOutputStream();
			     stream.print("<?xml version=\"1.0\" encoding=\"utf-8\" ?>"); 
			     stream.print("<scores>");
				    
				for (HighScore score : highscore) {
					stream.print("<score ");//username="ABC" score="456" rank="3" />
					if (score.getSubBoard() != null)
						stream.print(" subboard=\"" + score.getSubBoard() + "\"");
					if (score.getPlayer() != null)
						stream.print(" player=\"" + score.getPlayer() + "\"");
					if (score.getHighScore() != 0)
						stream.print(" highscore=\"" + score.getHighScore() + "\"");
					if (score.getDuring() != 0)
						stream.print(" during=\"" + score.getDuring() + "\"");
					if (score.getComment() != null)
						stream.print(" comment=\"" + score.getComment() + "\"");
					if (score.getDate() != 0)
						stream.print(" date=\"" + score.getDate() + "\"");
					if (score.getLocation() != null)
						stream.print(" location=\"" + score.getLocation() + "\"");
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
