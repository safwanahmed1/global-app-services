package com.sendme.android.slideshow.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.sendme.android.slideshow.controller.UIEvent;
import roboguice.activity.RoboActivity;

import com.google.inject.Inject;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.R;
import com.sendme.android.slideshow.auth.AuthenticationListener;
import com.sendme.android.slideshow.auth.impl.FaceBookAuthenticator;
import com.sendme.android.slideshow.controller.AdController;
import com.sendme.android.slideshow.controller.AudioPlaybackController;
import com.sendme.android.slideshow.controller.MediaSourceController;
import com.sendme.android.slideshow.controller.SlideshowController;
import com.sendme.android.slideshow.controller.UIController;
import com.sendme.android.slideshow.controller.UIEventListener;
import com.sendme.android.slideshow.image.ImageLoader;
import com.sendme.android.slideshow.manager.SettingsManager;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class MainActivity
extends RoboActivity
implements AuthenticationListener, UIEventListener
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	@Inject
	private AndroidSlideshow ass = null;

	private WakeLock wakeLock = null;

	@Inject
	private SettingsManager settingsManager = null;

	@Inject
	private FaceBookAuthenticator facebookAuthenticator = null;

	@Inject
	private UIController uiController = null;

	@Inject
	private MediaSourceController mediaSourceController = null;

	@Inject
	private SlideshowController slideshowController = null;

	@Inject
	private AudioPlaybackController audioPlaybackController = null;

	@Inject
	private AdController adController = null;

	private Integer uiEventListenerIdentifier = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.slideshow);

		facebookAuthenticator.setActivity(this);
		facebookAuthenticator.setListener(this);

		adController.initialize();
		audioPlaybackController.initialize();
		mediaSourceController.initialize();
		slideshowController.initialize();
		uiController.initialize();

		log.debug("MainActivity created...");
	}

	@Override
	public void onStart()
	{
		super.onStart();

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

		wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

		wakeLock.acquire();

		adController.start();
		audioPlaybackController.start();
		mediaSourceController.start();
		slideshowController.start();
		uiController.start();

		UIController.addListener(this);

		log.debug("MainActivity started...");
	}

	@Override
	public void onResume()
	{
		super.onResume();

		adController.resume();
		audioPlaybackController.resume();
		mediaSourceController.resume();
		slideshowController.resume();
		uiController.resume();

		log.debug("MainActivity resumed...");
	}

	@Override
	public void onPause()
	{
		super.onPause();

		adController.pause();
		audioPlaybackController.pause();
		mediaSourceController.pause();
		slideshowController.pause();
		uiController.pause();

		log.debug("MainActivity paused...");
	}

	@Override
	protected void onStop()
	{
		super.onStop();

		wakeLock.release();

		UIController.removeListener(this);

		adController.stop();
		audioPlaybackController.stop();
		mediaSourceController.stop();
		slideshowController.stop();
		uiController.stop();

		log.debug("MainActivity stopped...");
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();

		adController.destroy();
		audioPlaybackController.destroy();
		mediaSourceController.destroy();
		slideshowController.destroy();
		uiController.destroy();

		log.debug("MainActivity destroyed...");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode)
		{
			case AndroidSlideshow.SPLASH_ACTIVITY_RESULT_CODE:
			{
				log.debug("Splash screen activity completed callback.");

				break;
			}

			case AndroidSlideshow.ABOUT_ACTIVITY_RESULT_CODE:
			{
				log.debug("About activity completed callback.");

				break;
			}

			case AndroidSlideshow.PREFERENCES_ACTIVITY_RESULT_CODE:
			{
				log.debug("Preferences activity completed callback.");

				showSplashScreen();

				finish();

				break;
			}

			case AndroidSlideshow.FACEBOOK_ACTIVITY_RESULT_CODE:
			{
				log.debug("Facebook activity completed callback.");

				mediaSourceController.authorizeCallback(requestCode, resultCode, data);

				break;
			}

			default:
			{
				log.debug("Unknown activity completed callback: " + requestCode + " -- " + resultCode);
			}
		}
	}

	public void onAuthenticationComplete(int requestCode)
	{
		// TODO: What to do when we have new authentication.  Anything?  Maybe restart stuff?
		switch (requestCode)
		{
			case AndroidSlideshow.FACEBOOK_AUTHENTICATION_RESULT_CODE:
			{
				break;
			}
		}
	}

	public void onAuthenticationCancelled(int requestCode)
	{
		// TODO: What to do when we have a cancelled authentication.  Anything?  Maybe restart stuff?
		switch (requestCode)
		{
			case AndroidSlideshow.FACEBOOK_AUTHENTICATION_RESULT_CODE:
			{
				break;
			}
		}
	}

	public void onAuthenticationError(int requestCode, Throwable error)
	{
		switch (requestCode)
		{
			case AndroidSlideshow.FACEBOOK_AUTHENTICATION_RESULT_CODE:
			{
				break;
			}
		}
	}

	@Override
	public void onLowMemory()
	{
		super.onLowMemory();

		log.info("OnLowMemory Called..");

		ImageLoader.clearCache();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.options, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.aboutMenuItem:
			{
				showAbout();

				break;
			}

			case R.id.preferencesMenuItem:
			{
				showPreferences();

				break;
			}
		}

		return true;
	}

	@Override
	public void onOptionsMenuClosed(Menu menu)
	{
		super.onOptionsMenuClosed(menu);
	}

	public void onUIEvent(UIEvent event)
	{
		switch (event)
		{
			case PREFERENCES:
			{
				showPreferences();

				break;
			}

			case ABOUT:
			{
				showAbout();

				break;
			}
		}
	}

	public void showAbout()
	{
		startActivityForResult(new Intent(this, AboutActivity.class), AndroidSlideshow.ABOUT_ACTIVITY_RESULT_CODE);
	}

	public void showPreferences()
	{
		startActivityForResult(new Intent(this, SettingsActivity.class), AndroidSlideshow.PREFERENCES_ACTIVITY_RESULT_CODE);
	}

	public void showSplashScreen()
	{
		startActivityForResult(new Intent(this, SplashScreenActivity.class), AndroidSlideshow.SPLASH_ACTIVITY_RESULT_CODE);
	}

	public AndroidSlideshow getAndroidSlideshow()
	{
		return ass;
	}

	public void setAndroidSlideshow(AndroidSlideshow ass)
	{
		this.ass = ass;
	}

	public WakeLock getWakeLock()
	{
		return wakeLock;
	}

	public void setWakeLock(WakeLock wakeLock)
	{
		this.wakeLock = wakeLock;
	}

	public SettingsManager getSettingsManager()
	{
		return settingsManager;
	}

	public void setSettingsManager(SettingsManager settingsManager)
	{
		this.settingsManager = settingsManager;
	}

	public FaceBookAuthenticator getFacebookAuthenticator()
	{
		return facebookAuthenticator;
	}

	public void setFacebookAuthenticator(FaceBookAuthenticator facebookAuthenticator)
	{
		this.facebookAuthenticator = facebookAuthenticator;
	}

	public UIController getUiController()
	{
		return uiController;
	}

	public void setUiController(UIController uiController)
	{
		this.uiController = uiController;
	}

	public MediaSourceController getMediaSourceController()
	{
		return mediaSourceController;
	}

	public void setMediaSourceController(MediaSourceController mediaSourceController)
	{
		this.mediaSourceController = mediaSourceController;
	}

	public SlideshowController getSlideshowController()
	{
		return slideshowController;
	}

	public void setSlideshowController(SlideshowController slideshowController)
	{
		this.slideshowController = slideshowController;
	}

	public AudioPlaybackController getAudioPlaybackController()
	{
		return audioPlaybackController;
	}

	public void setAudioPlaybackController(AudioPlaybackController audioPlaybackController)
	{
		this.audioPlaybackController = audioPlaybackController;
	}

	public AdController getAdController()
	{
		return adController;
	}

	public void setAdController(AdController adController)
	{
		this.adController = adController;
	}

	public Integer getUIEventListenerIdentifier()
	{
		return uiEventListenerIdentifier;
	}

	public void setUIEventListenerIdentifier(Integer uiEventListenerIdentifier)
	{
		this.uiEventListenerIdentifier = uiEventListenerIdentifier;
	}
}
