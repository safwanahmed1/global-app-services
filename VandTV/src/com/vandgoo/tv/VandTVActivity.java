package com.vandgoo.tv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.vandgoo.tv.TaskListener.OnTaskFinishedListener;

public class VandTVActivity extends Activity {
	/** Called when the activity is first created. */
	private final String CHANNEL_LIST_FILE = "channels.xml";
	private ArrayList<Catalog> channelCatalog = new ArrayList<Catalog>();
	private ArrayList<TVChannel> channelList = new ArrayList<TVChannel>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		File channelFile = new File(this.getFilesDir(), CHANNEL_LIST_FILE);
		if (!channelFile.exists()) {
			UpdataChannels();
		} else {

			RefreshActivity();
		}

	}

	private void UpdataChannels() {
		UpdateChannelTask channelTask = new UpdateChannelTask(this);
		channelTask.setOnTaskFinishedListener(mOnTaskFinishedListener);
		channelTask.execute(null);

	}

	private ArrayList<TVChannel> LoadChannelFromFile() {
		// TODO Auto-generated method stub
		String strElemName;
		String id;
		String code;
		String url;
		String file;
		String catalog;
		channelCatalog.clear();
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
			channelList = channelCatalog.get(position).getChannels();
			ChannelAdapter channelAdapter = new ChannelAdapter(
					VandTVActivity.this, channelList);

			GridView gridChannel = (GridView) findViewById(R.id.gridChannel);
			gridChannel.setAdapter(channelAdapter);
		}

	};
	private OnItemClickListener channelClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView parent, View v, int position,
				long id) {
			PlayChannel(String.valueOf(id));

		}

	};

	private void PlayChannel(String channelId) {
		while (channelId.length() < 3) {
			channelId = "0".concat(channelId);
		}
		Intent playerIntent = new Intent(VandTVActivity.this, PlayerActivity.class);
		playerIntent.putExtra("channelid", channelId);
		startActivity(playerIntent);
	}

	private OnItemLongClickListener channelLongClickListener = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			final TVChannel channel = channelList.get(arg2);
			final CharSequence[] items = { "Play", "Add to favourite",
					"View schedule", "Update channels", "Bad channel" };
			AlertDialog.Builder builder = new AlertDialog.Builder(
					VandTVActivity.this);
			builder.setTitle(channel.getName());
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					switch (item) {
					case 0:
						PlayChannel(channel.getId());
						break;
					case 1:
						channelCatalog.get(1).getChannels().add(channel);
						break;
					case 2:
						ViewChannelSchedule();
						break;
					case 3:
						UpdataChannels();
						break;
					case 4:
						Toast.makeText(getApplicationContext(), "Thank you!",
								Toast.LENGTH_LONG).show();
						ReportBadChannels();
						break;
					}
				}

			});
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		}

	};

	private void ViewChannelSchedule() {
		// TODO ViewChannelSchedule

	}

	private void ReportBadChannels() {
		// TODO Auto-generated method stub

	}

	private void RefreshActivity() {
		channelList = LoadChannelFromFile();
		if (channelList != null) {
			ChannelAdapter channelAdapter = new ChannelAdapter(
					VandTVActivity.this, channelList);

			GridView gridChannel = (GridView) findViewById(R.id.gridChannel);
			gridChannel.setAdapter(channelAdapter);
			gridChannel.setOnItemClickListener(channelClickListener);
			gridChannel.setOnItemLongClickListener(channelLongClickListener);

			CatalogAdapter catalogAdapter = new CatalogAdapter(
					VandTVActivity.this, channelCatalog);
			ListView listCatalog = (ListView) findViewById(R.id.catalog_list);
			listCatalog.setAdapter(catalogAdapter);
			listCatalog.setOnItemClickListener(catalogClickListener);

		}

	}

	private OnTaskFinishedListener mOnTaskFinishedListener = new OnTaskFinishedListener() {

		@Override
		public void onTaskFinished(boolean successful) {
			if (successful) {
				RefreshActivity();
			}

		}
	};
}