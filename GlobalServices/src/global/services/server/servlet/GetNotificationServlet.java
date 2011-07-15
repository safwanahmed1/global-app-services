package global.services.server.servlet;

import global.services.server.database.NotificationDataBase;
import global.services.shared.Notification;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class GetNotificationServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String userId = null;
		Long appId = null;
		// resp.setContentType("text/xml");
		if (req.getParameterMap().containsKey("userid"))
			userId = req.getParameter("userid");
		if (req.getParameterMap().containsKey("appid"))
			appId = Long.parseLong(req.getParameter("appid"));

		NotificationDataBase noteDB = new NotificationDataBase();
		ServletOutputStream stream = null;
		try {
			Notification note = noteDB.SelectNote(userId, appId);

			if (note != null) {
				resp.setContentType("text/xml; charset=UTF-8");
				resp.setHeader("Content-Disposition",
						"attachment; filename=globalscore.xml");
				// resp.setLocale(arg0)
				stream = resp.getOutputStream();
				stream.print("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
				stream.print("<notes>");

				stream.print("<note ");
				if (note.getTittle() != null)
					stream.print(" titlte=\"" + note.getTittle() + "\"");
				if (note.getContent() != null)
					stream.print(" content=\"" + note.getContent() + "\"");
				if (note.getFromDate() != null)
					stream.print(" from=\"" + note.getFromDate() + "\"");
				if (note.getToDate() != null)
					stream.print(" to=\"" + note.getToDate() + "\"");
				stream.println("/>");
				stream.print("</notes>");

			}
		} finally {
			if (stream != null) {
				stream.close();
			}

		}

	}
}
