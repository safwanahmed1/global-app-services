package com.vandgoo.tv;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

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
	}

}
