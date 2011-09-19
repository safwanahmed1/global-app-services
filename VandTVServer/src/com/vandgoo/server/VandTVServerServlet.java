package com.vandgoo.server;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class VandTVServerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		//resp.setContentType("text/plain");
		//resp.getWriter().println("Hello, world");
		resp.getWriter().println("<embed allowfullscreen=\"true\" allowscriptaccess=\"always\" flashvars=\"streamer=rtmp://210.245.82.6/live/&amp;file=itvw_hh&amp;type=rtmp&amp;fullscreen=true&amp;autostart=true&amp;controlbar=bottom&amp;skin=long.swf&amp; quality=\" high=\"\" src=\"http://s2free.com/player.swf\" style=\"height: 400px; width: 500px;\" type=\"application/x-shockwave-flash\" wmode=\"opaque\"></embed>");
	}
}
