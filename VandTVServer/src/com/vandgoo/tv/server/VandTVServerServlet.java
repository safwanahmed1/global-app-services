package com.vandgoo.tv.server;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

@SuppressWarnings("serial")
public class VandTVServerServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(VandTVServerServlet.class
			.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String channelId = req.getParameter("channelid");
		logger.warning("Channel ID: " + channelId);
		String streamer = null;
		String file = null;
		// resp.setContentType("text/plain");
		// resp.getWriter().println("Hello, world");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();

			Document channelsDoc = db
					.parse("http://vietandtv.appspot.com/data/channels.xml");
			Element chEle = channelsDoc.getElementById(channelId);

			if (chEle != null) {
				streamer = chEle.getAttribute("url");
				logger.warning("streamer: " + streamer);
				file = chEle.getAttribute("file");
				logger.warning("file: " + file);
			}
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (streamer != null && file != null) {
			String embed = "<embed allowfullscreen=\"true\" allowscriptaccess=\"never\" flashvars=\""
					+ "streamer="
					+ streamer
					+ "&file="
					+ file
					+ "&autostart=true&controlbar=bottom&skinName=data/skin/modieus \" "
					+ "height=\"600\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" src=\"data/player.swf\" type=\"application/x-shockwave-flash\" "
					+ "width=\"800\" ></embed>";
			resp.getWriter().println(embed);
		}
	}
}
