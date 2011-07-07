package global.services.server.servlet;

import global.services.server.database.AdvertisementDataBase;
import global.services.shared.Advertisement;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;



@SuppressWarnings("serial")
public class GetAdvertisementServlet extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String userId = null;
		String type = null;
		//resp.setContentType("text/xml");
		if (req.getParameterMap().containsKey("userid"))
			userId = req.getParameter("userid");

		AdvertisementDataBase advDB = new AdvertisementDataBase();
		ServletOutputStream stream = null ;
		try {
			List<Advertisement> advList = advDB.SelectAdvs(userId);
			
			if (!advList.isEmpty()) {
				resp.setContentType("text/xml; charset=UTF-8");  
				resp.setHeader("Content-Disposition", "attachment; filename=globalscore.xml");
				//resp.setLocale(arg0)
			     stream = resp.getOutputStream();
			     stream.print("<?xml version=\"1.0\" encoding=\"utf-8\" ?>"); 
			     stream.print("<advs>");
				    
				for (Advertisement adv : advList) {
					stream.print("<adv ");
					if (adv.getAppId() != null)
						stream.print(" appid=\"" + adv.getAppId() + "\"");
					if (adv.getTittle() != null)
						stream.print(" titlte=\"" + adv.getTittle() + "\"");
					if (adv.getContent() != null)
						stream.print(" content=\"" +  adv.getContent() + "\"");
					if (adv.getType() != null)
						stream.print(" type=\"" +  adv.getType() + "\"");
					if (adv.getIconFile() != null)
						stream.print(" icon=\"" + adv.getIconFile() + "\"");
					if (adv.getStoreUrl() != null)
						stream.print(" store=\"" + adv.getStoreUrl() + "\"");
					stream.println("/>");
				}
				stream.print("</advs>");

			}
		} finally {
			if (stream != null) {
				stream.close();
			}
			
		}

	}
}
