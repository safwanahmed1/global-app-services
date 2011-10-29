package com.vandgoo.tv;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

public class ScheduleActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.schedule);
		
			WebView myWebView = (WebView) findViewById(R.id.webViewSchedule);
			myWebView
					.loadUrl("http://itv.vn/Lichphatsong.aspx");
			WebSettings webSettings = myWebView.getSettings();
			webSettings.setJavaScriptEnabled(true);

		
	}

	
}
