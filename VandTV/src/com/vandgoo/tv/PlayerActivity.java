package com.vandgoo.tv;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

public class PlayerActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		Bundle bundle = getIntent().getExtras();
		String channelId = bundle.getString("channelid");
		if (channelId != null) {
			WebView myWebView = (WebView) findViewById(R.id.webViewTV);
			myWebView
					.loadUrl("http://vietandtv.appspot.com/vandtvserver?channelid="
							+ channelId);
		}
		int oriMode = getResources().getConfiguration().orientation;
		if (oriMode == getResources().getConfiguration().ORIENTATION_LANDSCAPE) {
			RefreshChannelList();
		} else {
			RefreshChannelGallery();
			}

		
	}

	private void RefreshChannelGallery() {
		// TODO Auto-generated method stub
		ChannelAdapter channelGalleryAdapter = new ChannelAdapter(
				PlayerActivity.this, VandTVActivity.channelList);
		Gallery galleryChannel = (Gallery) findViewById(R.id.gallery_channels);
		galleryChannel.setAdapter(channelGalleryAdapter);

		// galleryCatalog.setOnItemClickListener(catalogClickListener);
		// galleryCatalog.setOnFocusChangeListener(catalogFocusListener);
		galleryChannel.setOnItemClickListener(channelClickListener);
	}
	private void RefreshChannelList() {
		// TODO Auto-generated method stub
		ChannelAdapter channelListAdapter = new ChannelAdapter(
				PlayerActivity.this, VandTVActivity.channelList);
		ListView listChannel = (ListView) findViewById(R.id.list_channel);
		listChannel.setAdapter(channelListAdapter);

		// galleryCatalog.setOnItemClickListener(catalogClickListener);
		// galleryCatalog.setOnFocusChangeListener(catalogFocusListener);
		listChannel.setOnItemClickListener(channelClickListener);
	}
	private OnItemClickListener channelClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			String channelId = String.valueOf(arg3);
			while (channelId.length() < 3) {
				channelId = "0".concat(channelId);
			}
			WebView myWebView = (WebView) findViewById(R.id.webViewTV);
			myWebView
					.loadUrl("http://vietandtv.appspot.com/vandtvserver?channelid="
							+ channelId);
			//myWebView.reload();
		}

	};

}
