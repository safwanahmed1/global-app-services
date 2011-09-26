package com.vandgoo.tv.server;

import java.io.IOException;
import javax.servlet.http.*;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

@SuppressWarnings("serial")
public class VandTVServerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String channelId = req.getParameter("channelid");
		String streamer;
		String file;
		//resp.setContentType("text/plain");
		//resp.getWriter().println("Hello, world");
		
		Document messageDom = XMLParser.parse("");
		
		String embed = "<embed allowfullscreen=\"true\" allowscriptaccess=\"never\" flashvars=\""+
		"streamer=rtmp://117.103.225.16:1935/live&file=vtc11&autostart=true&controlbar=bottom&skin=http://www.longtailvideo.com/files/skins/modieus/5/modieus.zip \" "+
		"height=\"600\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\" src=\"http://tv.droidviet.com/player.swf\" type=\"application/x-shockwave-flash\" "+
		"width=\"800\" ></embed>";
		resp.getWriter().println(embed);
	}
}
