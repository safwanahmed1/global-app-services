package com.sendme.android.slideshow.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.util.Log;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.R;
import com.sendme.android.slideshow.manager.SettingsManager;
import com.sendme.apps.android.coreg.CoRegManager;
import com.sendme.apps.android.util.SendMeAppMode;
import com.sendme.apps.android.util.SendMeUtil;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class SettingsActivity
extends PreferenceActivity
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	private SettingsManager settingsManager = null;

	public SettingsActivity()
	{
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// Load the Co-­‐Reg Manager in the background.
		new CoRegTask().execute(this);
		
		addPreferencesFromResource(R.xml.preferences);

		// We have to bind this one by hand, since we can't inject into a preferences
		// activity.
		settingsManager = new SettingsManager();

		settingsManager.setAndroidSlideshow((AndroidSlideshow) getApplication());
	}

	@Override
	protected void onStop()
	{
		super.onStop();
	}

	@Override
	protected void onPause()
	{
		super.onPause();

		settingsManager.setPreferencesAsRunOnce();

		log.debug("Preferences saved.");
	}

	public SettingsManager getSettingsManager()
	{
		return settingsManager;
	}

	public void setSettingsManager(SettingsManager settingsManager)
	{
		this.settingsManager = settingsManager;
	}
	
	private class CoRegTask extends AsyncTask<Context, Void, Void> {
		private CoRegManager coRegManager;
		// Constant App-­‐Specific Values required forinterfacing with Co-­‐Reg
		// API
		final SendMeAppMode APP_MODE = SendMeAppMode.PROD;
		// DEV, QA or PROD
		static final String DEV_ANALYTICS_ID = "UA-23108669-18";
		// Note: QA installs will show up under the DEV profile in google
		// analytics
		static final String PROD_ANALYTICS_ID = "UA-23108669-17";
		static final String PROD_CID = "194";
		static final String PROD_TOKEN = "P20Y4";
		static final String DEV_CID = "194";
		static final String DEV_TOKEN = "dIv7j";
		static final String TAG = "Debug";
		static final String ADVERTISER_KEY = "SLIDESHOW";

		public CoRegTask() {
		}

		@Override
		protected Void doInBackground(Context... contexts) {
			try {
				String mdn = SendMeUtil.getDeviceMDN(getApplicationContext());
				coRegManager = new CoRegManager(contexts[0],ADVERTISER_KEY, PROD_CID,
						PROD_TOKEN, DEV_CID, DEV_TOKEN, mdn, PROD_ANALYTICS_ID,
						DEV_ANALYTICS_ID, APP_MODE,
						GoogleAnalyticsTracker.getInstance());
				
			} catch (Exception ex) {
				Log.d(TAG,
						"Found an error	in initializing	CoRegManager: "
								+ ex.toString());
				return null;
			}
			return null;
		}

		@Override
        protected void onPostExecute(Void voids)
        {
            if(coRegManager != null && coRegManager.isOfferAvailable())
                coRegManager.displayOffer();
            else
                Log.d(TAG, "Null CoReg landing pages... no offer will be shown");
        }

	}
	
}
