package com.sendme.android.slideshow.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.R;
import com.sendme.android.slideshow.manager.SettingsManager;

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
}
