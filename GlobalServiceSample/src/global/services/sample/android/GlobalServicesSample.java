package global.services.sample.android;

import global.services.sample.android.R;
import android.app.Activity;
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

		String[] menus = getResources().getStringArray(R.array.menus_array);

		ListView menuView = (ListView) findViewById(R.id.list_menu);
		menuView.setAdapter(new ArrayAdapter<String>(this, R.layout.menu_item,
				menus));
		menuView.setTextFilterEnabled(true);

		menuView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

			}
		});
		setContentView(R.layout.global_services_main);
	}

}
