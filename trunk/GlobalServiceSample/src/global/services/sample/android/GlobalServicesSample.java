package global.services.sample.android;

import global.services.sample.android.R;
import global.services.sample.android.R.array;
import global.services.sample.android.R.id;
import global.services.sample.android.R.layout;
import global.services.sample.android.activities.AdvertisementActivity;
import global.services.sample.android.activities.FilesActivity;
import global.services.sample.android.activities.HighscoreActivity;
import global.services.sample.android.activities.NotificationActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GlobalServicesSample extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.global_services_main);
		String[] menus = getResources().getStringArray(R.array.menus_array);

		ListView menuView = (ListView) findViewById(R.id.list_menu);
		menuView.setAdapter(new ArrayAdapter<String>(this, R.layout.menu_item,
				menus));
		menuView.setTextFilterEnabled(true);

		menuView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				// TODO Auto-generated method stub
				switch (position) {
				case 0:
					Intent scoreIntent = new Intent(GlobalServicesSample.this,
							HighscoreActivity.class);
					startActivity(scoreIntent);
					break;
				case 1:
					Intent advIntent = new Intent(GlobalServicesSample.this,
							AdvertisementActivity.class);
					startActivity(advIntent);
					break;
				case 2:
					Intent noteIntent = new Intent(GlobalServicesSample.this,
							NotificationActivity.class);
					startActivity(noteIntent);
					break;
				case 3:
					Intent fileIntent = new Intent(GlobalServicesSample.this,
							FilesActivity.class);
					startActivity(fileIntent);
					break;
				case 4:
					finish();
					break;
				}

			}
		});

	}

}
