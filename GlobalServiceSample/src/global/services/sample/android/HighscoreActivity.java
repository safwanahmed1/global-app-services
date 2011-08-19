package global.services.sample.android;

import global.services.sample.android.R;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

public class HighscoreActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscore);
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
		submitTabSpec.setIndicator(createIndicatorView(tabHost,"Submit score",
				getResources().getDrawable(R.drawable.ic_submit_score)))
				.setContent(submitScore);
		Intent getScore = new Intent(this, GetScoreActivity.class);
		getTabSpec.setIndicator(createIndicatorView(tabHost,"Get score",
				getResources().getDrawable(R.drawable.ic_get_score)))
				.setContent(getScore);
		

		/** Add tabSpec to the TabHost to display. */
		Resources res = getResources();
		Configuration cfg = res.getConfiguration();
		boolean hor = cfg.orientation == Configuration.ORIENTATION_LANDSCAPE;

		if (hor) {
		    TabWidget tw = tabHost.getTabWidget();
		    tw.setOrientation(LinearLayout.VERTICAL);
		}
		
		tabHost.addTab(submitTabSpec);
		tabHost.addTab(getTabSpec);
		
    }

    private View createIndicatorView(TabHost tabHost, CharSequence label, Drawable icon) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View tabIndicator = inflater.inflate(R.layout.tab_indicator,
                tabHost.getTabWidget(), // tab widget is the parent
                false); // no inflate params

        final TextView tv = (TextView) tabIndicator.findViewById(R.id.title);
        tv.setText(label);

        final ImageView iconView = (ImageView) tabIndicator.findViewById(R.id.icon);
        iconView.setImageDrawable(icon);

        return tabIndicator;
    }
}