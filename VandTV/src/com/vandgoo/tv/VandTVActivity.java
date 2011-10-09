package com.vandgoo.tv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.vandgoo.tv.TaskListener.OnTaskFinishedListener;

public class VandTVActivity extends Activity {
	/** Called when the activity is first created. */
	private final String CHANNEL_LIST_FILE = "channels.xml";
	private final ArrayList<Catalog> channelCatalog = new ArrayList<Catalog>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		File channelFile = new File(this.getFilesDir(), CHANNEL_LIST_FILE);
		if (!channelFile.exists()) {
			UpdateChannelTask channelTask = new UpdateChannelTask(this);
			channelTask.setOnTaskFinishedListener(mOnTaskFinishedListener);
			channelTask.execute(null);

		} else {
			ArrayList<TVChannel> allChannel = LoadChannelFromFile();
			if (allChannel != null) {
				ChannelAdapter channelAdapter = new ChannelAdapter(this,
						allChannel);

				GridView gridChannel = (GridView) findViewById(R.id.gridChannel);
				gridChannel.setAdapter(channelAdapter);
				gridChannel.setOnItemClickListener(channelClickListener);
				CatalogAdapter catalogAdapter = new CatalogAdapter(this,
						channelCatalog);
				ListView listCatalog = (ListView) findViewById(R.id.catalog_list);
				listCatalog.setAdapter(catalogAdapter);
				listCatalog.setOnItemClickListener(catalogClickListener);

			}
		}

	}

	private ArrayList<TVChannel> LoadChannelFromFile() {
		// TODO Auto-generated method stub
		String strElemName;
		String id;
		String code;
		String url;
		String file;
		String catalog;
		String iconId;
		String type;
		channelCatalog.add(new Catalog("All", "All"));
		channelCatalog.add(new Catalog("Favourite", "Favourite"));
		XmlPullParser channels = null;
		try {
			channels = XmlPullParserFactory.newInstance().newPullParser();
			FileInputStream channelStream;
			channelStream = openFileInput(CHANNEL_LIST_FILE);
			channels.setInput(new InputStreamReader(channelStream));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (channels != null) {
			int eventType = -1;
			// boolean bFoundScores = false;
			boolean isCatalogExist = false;
			// Find Score records from XML
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {

					// Get the name of the tag (eg scores or score)
					strElemName = channels.getName();

					if (strElemName.equals("channel")) {
						// bFoundScores = true;
						TVChannel channelObj = new TVChannel();
						id = channels.getAttributeValue(null, "id");
						if (id != null)
							channelObj.setId(id);
						code = channels.getAttributeValue(null, "code");
						if (code != null)
							channelObj.setName(code);
						url = channels.getAttributeValue(null, "url");
						if (url != null)
							channelObj.setUrl(url);
						file = channels.getAttributeValue(null, "file");
						if (file != null)
							channelObj.setFile(file);
						catalog = channels.getAttributeValue(null, "catalog");
						if (catalog != null)
							channelObj.setCatalog(catalog);
						isCatalogExist = false;
						for (Catalog catalogItem : channelCatalog) {
							if (catalogItem.getId().equals("All")) {

								catalogItem.AddCatalog(channelObj);
								if (catalog == null) {
									isCatalogExist = true;
									break;
								}
							}

							if ((catalog != null)
									&& (catalogItem.getId().equals(catalog))) {
								catalogItem.AddCatalog(channelObj);
								isCatalogExist = true;
								break;
							}

						}
						if (!isCatalogExist) {
							Catalog newCatalog = new Catalog(channelObj
									.getCatalog(), channelObj.getCatalog());
							newCatalog.AddCatalog(channelObj);
							channelCatalog.add(newCatalog);

						}

					}
				}
				try {
					eventType = channels.next();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		return channelCatalog.get(0).getChannels();
	}

	private OnItemClickListener catalogClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView parent, View v, int position,
				long id) {
			ChannelAdapter channelAdapter = new ChannelAdapter(
					VandTVActivity.this, channelCatalog.get(position).getChannels());

			GridView gridChannel = (GridView) findViewById(R.id.gridChannel);
			gridChannel.setAdapter(channelAdapter);
		}

	};
	private OnItemClickListener channelClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView parent, View v, int position,
				long id) {
			Toast.makeText(VandTVActivity.this, "" + position,
					Toast.LENGTH_SHORT).show();
		}

	};
	private OnTaskFinishedListener mOnTaskFinishedListener = new OnTaskFinishedListener() {

		@Override
		public void onTaskFinished(boolean successful) {
			if (successful) {
				ArrayList<TVChannel> channelList = LoadChannelFromFile();
				if (channelList != null) {
					ChannelAdapter channelAdapter = new ChannelAdapter(
							VandTVActivity.this, channelList);

					GridView gridChannel = (GridView) findViewById(R.id.gridChannel);
					gridChannel.setAdapter(channelAdapter);
					gridChannel.setOnItemClickListener(channelClickListener);
					CatalogAdapter catalogAdapter = new CatalogAdapter(
							VandTVActivity.this, channelCatalog);
					ListView listCatalog = (ListView) findViewById(R.id.catalog_list);
					listCatalog.setAdapter(catalogAdapter);
					listCatalog.setOnItemClickListener(catalogClickListener);

				}
			}

		}
	};
}