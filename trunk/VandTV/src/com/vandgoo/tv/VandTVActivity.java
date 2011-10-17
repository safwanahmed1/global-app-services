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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Gallery;
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
	private SharedPreferences favourites = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		favourites = getSharedPreferences("FavouriteChannels", 0);
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
							if (catalogItem.getId().equals("Favourite")) {
								if (favourites.getString(channelObj.getId(),
										null) != null)
									catalogItem.AddCatalog(channelObj);

							}

							if ((catalog != null)
									&& (catalogItem.getId().equals(catalog))) {
								catalogItem.AddCatalog(channelObj);
								isCatalogExist = true;
								break;
							}

						}
						if (!isCatalogExist) {
							Catalog newCatalog = new Catalog(
									channelObj.getCatalog(),
									channelObj.getCatalog());
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
		Intent playerIntent = new Intent(VandTVActivity.this,
				PlayerActivity.class);
		playerIntent.putExtra("channelid", channelId);
		startActivity(playerIntent);
	}

	private OnItemLongClickListener channelLongClickListener = new OnItemLongClickListener() {
		private AlertDialog alert;

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			final TVChannel channel = channelList.get(arg2);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(
					VandTVActivity.this);
			builder.setTitle(channel.getName());

			Context mContext = getApplicationContext();
			LayoutInflater inflater = LayoutInflater.from(mContext);
			// (LayoutInflater)
			// mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
			View menuLayout = inflater.inflate(R.layout.channel_menu_list,
					(ViewGroup) findViewById(R.id.channel_menu));
			
			ListView channelMenu = (ListView) menuLayout
					.findViewById(R.id.channel_menu_list);

			ChannelMenuAdapter menuAdapter = new ChannelMenuAdapter(
					VandTVActivity.this);
			// ListView listCatalog = (ListView)
			// findViewById(R.id.channel_menu_list);
			channelMenu.setAdapter(menuAdapter);
			channelMenu.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					switch (arg2) {
					case 0:
						PlayChannel(channel.getId());
						break;
					case 1:
						if (favourites.getString(channel.getId(), null) == null) {
							channelCatalog.get(1).getChannels().add(channel);
							SharedPreferences.Editor favEditor = favourites
									.edit();
							favEditor.putString(channel.getId(),
									channel.getName());
							favEditor.commit();
							RefreshCatalog();
						}

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
					if (alert.isShowing())
						alert.dismiss();
				}

			});
			builder.setView(menuLayout);
			alert = builder.create();
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
			RefreshChannelGrid();
			RefreshCatalog();
		}

	}

	private void RefreshCatalog() {
		// TODO Auto-generated method stub
		int oriMode = getResources().getConfiguration().orientation;
		if (oriMode == getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
			RefreshCatalogList();
		} else {
			RefreshCatalogGallery();
		}

	}

	private void RefreshChannelGrid() {
		ChannelAdapter channelAdapter = new ChannelAdapter(VandTVActivity.this,
				channelList);

		GridView gridChannel = (GridView) findViewById(R.id.gridChannel);
		gridChannel.setAdapter(channelAdapter);
		gridChannel.setOnItemClickListener(channelClickListener);
		gridChannel.setOnItemLongClickListener(channelLongClickListener);
	}

	private void RefreshCatalogGallery() {
		// TODO Auto-generated method stub
		CatalogGalleryAdapter catalogGalleryAdapter = new CatalogGalleryAdapter(
				VandTVActivity.this, channelCatalog);
		Gallery galleryCatalog = (Gallery) findViewById(R.id.galleryCatalog);
		galleryCatalog.setAdapter(catalogGalleryAdapter);

		galleryCatalog.setOnItemClickListener(catalogClickListener);

	}

	private void RefreshCatalogList() {
		CatalogListAdapter catalogAdapter = new CatalogListAdapter(
				VandTVActivity.this, channelCatalog);
		ListView listCatalog = (ListView) findViewById(R.id.catalog_list);
		listCatalog.setAdapter(catalogAdapter);
		listCatalog.setOnItemClickListener(catalogClickListener);
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