package global.services.server.servlet;

import global.services.server.database.NotificationDataBase;
import global.services.shared.Notification;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class NotificationServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// super.doGet(req, resp);
		String userId = null;
		Long appId = null;
		if (req.getParameterMap().containsKey("userid"))
			userId = req.getParameter("userid");
		if (req.getParameterMap().containsKey("appid"))
			appId = Long.parseLong(req.getParameter("appid"));
		GetNotification(userId, appId, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// super.doPost(req, resp);
		String userId = null;
		Long appId = null;
		userId = req.getHeader("userid");
		appId = Long.parseLong(req.getHeader("appid"));
		GetNotification(userId, appId, resp);
	}

	public void GetNotification(String userId, Long appId,
			HttpServletResponse resp) throws IOException {

		NotificationDataBase noteDB = new NotificationDataBase();
		ServletOutputStream stream = null;
		try {
			List<Notification> noteList = noteDB.SelectNotes(userId, appId);
			resp.setContentType("text/xml; charset=UTF-8");
			resp.setHeader("Content-Disposition",
					"attachment; filename=globalscore.xml");
			stream = resp.getOutputStream();
			stream.print("<?xml version=\"1.0\" encoding=\"utf-8\" ?>");
			stream.print("<notes>");
			for (Notification note : noteList) {

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

			}
			stream.print("</notes>");
		} finally {
			if (stream != null) {
				stream.close();
			}

		}

	}
}
