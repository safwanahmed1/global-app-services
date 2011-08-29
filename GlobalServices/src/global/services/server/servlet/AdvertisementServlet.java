package global.services.server.servlet;

import global.services.server.database.AdvertisementDataBase;
import global.services.shared.Advertisement;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;



@SuppressWarnings("serial")
public class AdvertisementServlet extends HttpServlet {

	
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doGet(req, resp);
		String userId = null;
		//resp.setContentType("text/xml");
		if (req.getParameterMap().containsKey("userid"))
			userId = req.getParameter("userid");

		GetAdvertisement(userId, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		//super.doPost(req, resp);
		String userId = null;
		//resp.setContentType("text/xml");
		userId = req.getParameter("userid");
		GetAdvertisement(userId, resp);

	}

	public void GetAdvertisement(String userId, HttpServletResponse resp)
			throws IOException {
		
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
					if (adv.getId() != null)
						stream.print(" id=\"" + adv.getId() + "\"");
					if (adv.getUserId() != null)
						stream.print(" userid=\"" + adv.getUserId() + "\"");
					if (adv.getAppName() != null)
						stream.print(" name=\"" + adv.getAppName() + "\"");
					if (adv.getTittle() != null)
						stream.print(" title=\"" + adv.getTittle() + "\"");
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
