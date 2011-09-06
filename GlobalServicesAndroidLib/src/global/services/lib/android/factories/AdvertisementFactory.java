package global.services.lib.android.factories;

import global.services.lib.android.httpclient.RequestMethod;
import global.services.lib.android.httpclient.RestClient;
import global.services.lib.android.objects.Advertisement;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class AdvertisementFactory {
	private static final String ADVERTISEMENT_SERVLET = "http://global-app-services.appspot.com/globalservices/advertisementservlet";
	private String userId_;
	private RestClient advRest;

	public AdvertisementFactory(String userId) {
		userId_ = userId;
		advRest = new RestClient(ADVERTISEMENT_SERVLET);

	}
	public String GetAdvsXMLContent() {
		advRest.ClearParams();
		advRest.AddParam("userid", userId_);

		try {
			advRest.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// textView.setText(e.getMessage());
		}
		String strResponse = advRest.getResponse();
		return strResponse;
	}
	public List<Advertisement> GetAdvertisements(Long appId) {
		List<Advertisement> advList = new ArrayList<Advertisement>();
		String strElemName;
		String id;
		String userId;
		String appName;
		String tittle;
		String content;
		String type;
		String iconFileId;
		InputStream iconFile;
		String storeUrl;

		advRest.ClearParams();
		advRest.AddParam("userid", userId_);
		if (appId != null)
			advRest.AddParam("appid", String.valueOf(appId));

		try {
			advRest.Execute(RequestMethod.POST);
		} catch (Exception e) {
			// textView.setText(e.getMessage());
		}
		String strResponse = advRest.getResponse();
		strResponse = strResponse.replace("\n", "");
		XmlPullParser advs;
		try {

			advs = XmlPullParserFactory.newInstance().newPullParser();
			advs.setInput(new StringReader(strResponse));
		} catch (XmlPullParserException e) {
			advs = null;
		}
		if (advs != null) {
			int eventType = -1;
			// boolean bFoundScores = false;

			// Find Score records from XML
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {

					// Get the name of the tag (eg scores or score)
					strElemName = advs.getName();

					if (strElemName.equals("advertesment")) {
						// bFoundScores = true;
						Advertisement advObj = new Advertisement();
						id = advs.getAttributeValue(null, "id");
						advObj.setId(Long.parseLong(id));
						userId = advs.getAttributeValue(null, "useid");
						advObj.setUserId(userId);

						appName = advs.getAttributeValue(null, "name");
						advObj.setAppName(appName);

						tittle = advs.getAttributeValue(null, "tittle");
						advObj.setTittle(tittle);

						content = advs.getAttributeValue(null, "content");
						advObj.setContent(content);

						type = advs.getAttributeValue(null, "type");
						advObj.setType(type);

						iconFileId = advs.getAttributeValue(null, "iconid");
						advObj.setIconFileId(Long.parseLong(iconFileId));
						iconFile = new FileInfoFactory(userId).Download(Long.parseLong(iconFileId));
						advObj.setIconFile(iconFile);

						storeUrl = advs.getAttributeValue(null, "storeurl");
						advObj.setStoreUrl(storeUrl);

						advList.add(advObj);

					}
				}
				try {
					eventType = advs.next();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

		return advList;

	}
	
	

}
