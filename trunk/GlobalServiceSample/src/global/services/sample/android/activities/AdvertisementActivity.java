package global.services.sample.android.activities;

import global.services.lib.android.objects.Advertisement;
import global.services.sample.android.R;
import global.services.sample.android.adapters.AdvArrayAdapter;
import global.services.sample.android.tasks.DownloadAdvToLocal;
import global.services.sample.android.tasks.TaskListener.OnTaskFinishedListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class AdvertisementActivity extends ListActivity {

	private List<Advertisement> advList;
	private AdvArrayAdapter adapter;
	private String ADVERTISEMENT_FILE = "advertisement.xml";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adv_list);

		advList = LoadAdvFromFileToListView();
		if (advList != null) {
			adapter = new AdvArrayAdapter(getApplicationContext(),
					R.layout.adv_list, (ArrayList<Advertisement>) advList);

			setListAdapter(adapter);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.get_adv_menu, menu);
		return true;
	}
	
	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Advertisement advItem = (Advertisement) this.getListAdapter().getItem(position);
		
         Uri uri = Uri.parse(advItem.getStoreUrl()); 
         Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
         startActivity(intent); 

         
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.refresh_adv:
			DownloadAdvToLocal downAdv = new DownloadAdvToLocal(this);
			downAdv.setOnTaskFinishedListener(mOnTaskFinishedListener);
			downAdv.execute(getResources().getString(R.string.userid));

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	

	private List<Advertisement> LoadAdvFromFileToListView() {
		// TODO Auto-generated method stub
		List<Advertisement> advList = null;
		FileInputStream fis;
		StringBuffer fileContent = new StringBuffer("");
		try {

			byte[] buffer = new byte[1024];
			fis = openFileInput(ADVERTISEMENT_FILE);

			int length;
			while ((length = fis.read(buffer)) != -1) {
				fileContent.append(new String(buffer), 0, length);
			}
			advList = GetAdvListFromXML(fileContent.toString());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return advList;
	}

	private List<Advertisement> GetAdvListFromXML(String advsXMLContent) {
		// TODO Auto-generated method stub
		List<Advertisement> advList = new ArrayList<Advertisement>();
		String strElemName;
		String id;
		String userId;
		String name;
		String title;
		String content;
		String iconId;
		String url;
		String type;

		advsXMLContent = advsXMLContent.replace("\n", "");
		XmlPullParser advs;
		try {

			advs = XmlPullParserFactory.newInstance().newPullParser();
			advs.setInput(new StringReader(advsXMLContent));
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

					if (strElemName.equals("adv")) {
						// bFoundScores = true;
						Advertisement advObj = new Advertisement();
						id = advs.getAttributeValue(null, "id");
						if (id != null)
							advObj.setId(Long.parseLong(id));
						userId = advs.getAttributeValue(null, "useid");
						if (userId != null)
							advObj.setUserId(userId);
						name = advs.getAttributeValue(null, "name");
						if (name != null)
							advObj.setAppName(name);
						title = advs.getAttributeValue(null, "title");
						if (title != null)
							advObj.setTittle(title);
						content = advs.getAttributeValue(null, "content");
						if (content != null)
							advObj.setContent(content);
						type = advs.getAttributeValue(null, "type");
						if (content != null)
							advObj.setType(type);
						iconId = advs.getAttributeValue(null, "icon");
						if (iconId != null)
							advObj.setIconFileId(Long.parseLong(iconId));
						url = advs.getAttributeValue(null, "store");
						if (url != null)
							advObj.setStoreUrl(url);

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
	private OnTaskFinishedListener mOnTaskFinishedListener = new OnTaskFinishedListener() {

		@Override
		public void onTaskFinished(Object result) {
			advList = LoadAdvFromFileToListView();
			if (advList != null) {
				adapter = new AdvArrayAdapter(getApplicationContext(),
						R.layout.adv_list, (ArrayList<Advertisement>) advList);

				setListAdapter(adapter);

			}

		}
	};

}
