package com.vandgoo.tv;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class VandTVActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GridView gridChannel = (GridView) findViewById(R.id.gridChannel);
        gridChannel.setAdapter(new ImageAdapter(this));
        gridChannel.setOnItemClickListener(new OnItemClickListener() {
        	@Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Toast.makeText(VandTVActivity.this, "" + position, Toast.LENGTH_SHORT).show();
            }

		
        });
    }
}