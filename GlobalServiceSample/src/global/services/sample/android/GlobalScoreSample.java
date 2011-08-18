package global.services.sample.android;

import global.sample.android.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class GlobalScoreSample extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /** TabHost will have Tabs */
		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);

		/**
		 * TabSpec used to create a new tab. By using TabSpec only we can able
		 * to setContent to the tab. By using TabSpec setIndicator() we can set
		 * name to tab.
		 */

		/** tid1 is firstTabSpec Id. Its used to access outside. */
		TabSpec submitTabSpec = tabHost.newTabSpec("submit");
		TabSpec getTabSpec = tabHost.newTabSpec("get");
		
		/** TabSpec setIndicator() is used to set name for the tab. */
		/** TabSpec setContent() is used to set content for a particular tab. */

		Intent submitScore = new Intent(this, SubmitScoreActivity.class);
		submitTabSpec.setIndicator("Submit score",
				getResources().getDrawable(R.drawable.ic_submit_score))
				.setContent(submitScore);
		Intent getScore = new Intent(this, GetScoreActivity.class);
		getTabSpec.setIndicator("Get score",
				getResources().getDrawable(R.drawable.ic_get_score))
				.setContent(getScore);
		

		/** Add tabSpec to the TabHost to display. */
		tabHost.addTab(submitTabSpec);
		tabHost.addTab(getTabSpec);
		
    }
}