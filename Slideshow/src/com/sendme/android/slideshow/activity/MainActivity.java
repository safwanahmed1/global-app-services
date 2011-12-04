package com.sendme.android.slideshow.activity;

import java.net.URI;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.sendme.android.slideshow.controller.UIEvent;
import roboguice.activity.RoboActivity;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
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
import com.sendme.android.slideshow.share.FacebookImageShare;
import com.sendme.android.slideshow.share.FacebookVideoShare;
import com.sendme.android.slideshow.source.MediaSourceException;
import com.sendme.apps.android.coreg.CoRegManager;
import com.sendme.apps.android.util.SendMeAppMode;
import com.sendme.apps.android.util.SendMeUtil;

/**
 * 
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class MainActivity extends RoboActivity implements
		AuthenticationListener, UIEventListener {
	private final static AndroidLogger log = AndroidLogger
			.getAndroidLogger(AndroidSlideshow.LOG_TAG);

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Load the Co-­‐Reg Manager in the background.
		new CoRegTask().execute(this);

		setContentView(R.layout.slideshow);
		facebookAuthenticator.setActivity(this);
		facebookAuthenticator.setListener(this);

		adController.initialize();
		if (settingsManager.isPlayMusic()) {
			audioPlaybackController.initialize();
		}
		mediaSourceController.initialize();
		slideshowController.initialize();
		uiController.initialize();

		log.debug("MainActivity created...");
	}

	@Override
	public void onStart() {
		super.onStart();

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

		wakeLock = pm
				.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");

		wakeLock.acquire();

		adController.start();
		if (settingsManager.isPlayMusic()) {
			audioPlaybackController.start();
		}
		mediaSourceController.start();
		slideshowController.start();
		uiController.start();

		UIController.addListener(this);

		log.debug("MainActivity started...");
	}

	@Override
	public void onResume() {
		super.onResume();

		adController.resume();
		if (settingsManager.isPlayMusic()) {
			audioPlaybackController.resume();
		}
		mediaSourceController.resume();
		slideshowController.resume();
		uiController.resume();

		log.debug("MainActivity resumed...");
	}

	@Override
	public void onPause() {
		super.onPause();

		adController.pause();
		if (settingsManager.isPlayMusic()) {
			audioPlaybackController.pause();
		}
		mediaSourceController.pause();
		slideshowController.pause();
		uiController.pause();

		log.debug("MainActivity paused...");
	}

	@Override
	protected void onStop() {
		super.onStop();

		wakeLock.release();

		UIController.removeListener(this);

		adController.stop();
		if (settingsManager.isPlayMusic()) {
			audioPlaybackController.stop();
		}

		mediaSourceController.stop();
		slideshowController.stop();
		uiController.stop();

		log.debug("MainActivity stopped...");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		adController.destroy();
		if (settingsManager.isPlayMusic()) {
			audioPlaybackController.destroy();
		}
		mediaSourceController.destroy();
		slideshowController.destroy();
		uiController.destroy();

		log.debug("MainActivity destroyed...");
	}

	public String getRealPathFromImageURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		return null;
	}

	public String getRealPathFromVideoURI(Uri contentUri) {
		String[] proj = { MediaStore.Video.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		return null;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case AndroidSlideshow.GALLERY_CHOSEN_VIDEO_RESULT_CODE: {
			if (resultCode == RESULT_OK) {
				// currImageURI = data.getData();
				Uri uri = data.getData();
				String videoPath = getRealPathFromImageURI(uri);
				;
				if (videoPath == null) {
					videoPath = uri.getPath();
				}
				if (videoPath != null)
					shareVideo(videoPath);
			}
			break;

		}
		case AndroidSlideshow.GALLERY_CHOSEN_IMAGE_RESULT_CODE: {
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				String imagePath = getRealPathFromImageURI(uri);
				;
				if (imagePath == null) {
					imagePath = uri.getPath();
				}

				if (imagePath != null)
					shareImage(imagePath);
			}
			break;

		}
		case AndroidSlideshow.SPLASH_ACTIVITY_RESULT_CODE: {
			log.debug("Splash screen activity completed callback.");

			break;
		}

		case AndroidSlideshow.ABOUT_ACTIVITY_RESULT_CODE: {
			log.debug("About activity completed callback.");

			break;
		}

		case AndroidSlideshow.PREFERENCES_ACTIVITY_RESULT_CODE: {
			log.debug("Preferences activity completed callback.");

			showSplashScreen();

			finish();

			break;
		}

		case AndroidSlideshow.FACEBOOK_ACTIVITY_RESULT_CODE: {
			log.debug("Facebook activity completed callback.");

			mediaSourceController.authorizeCallback(requestCode, resultCode,
					data);

			break;
		}

		default: {
			log.debug("Unknown activity completed callback: " + requestCode
					+ " -- " + resultCode);
		}
		}
	}

	public void onAuthenticationComplete(int requestCode) {
		// TODO: What to do when we have new authentication. Anything? Maybe
		// restart stuff?
		switch (requestCode) {
		case AndroidSlideshow.FACEBOOK_AUTHENTICATION_RESULT_CODE: {
			break;
		}
		}
	}

	public void onAuthenticationCancelled(int requestCode) {
		// TODO: What to do when we have a cancelled authentication. Anything?
		// Maybe restart stuff?
		switch (requestCode) {
		case AndroidSlideshow.FACEBOOK_AUTHENTICATION_RESULT_CODE: {
			break;
		}
		}
	}

	public void onAuthenticationError(int requestCode, Throwable error) {
		switch (requestCode) {
		case AndroidSlideshow.FACEBOOK_AUTHENTICATION_RESULT_CODE: {
			break;
		}
		}
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();

		log.info("OnLowMemory Called..");

		ImageLoader.clearCache();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

		inflater.inflate(R.menu.options, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.shareVideoMenuItem: {
			// shareVideo();
			Intent intent = new Intent();
			intent.setType("video/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent, "Select Video"),
					AndroidSlideshow.GALLERY_CHOSEN_VIDEO_RESULT_CODE);

			// To handle when an image is selected from the browser, add the
			// following to your Activity

			break;
		}
		case R.id.shareImageMenuItem: {

			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(
					Intent.createChooser(intent, "Select Picture"),
					AndroidSlideshow.GALLERY_CHOSEN_IMAGE_RESULT_CODE);

			// To handle when an image is selected from the browser, add the
			// following to your Activity
			// shareImage();
			break;
		}
		case R.id.aboutMenuItem: {
			showAbout();

			break;
		}

		case R.id.preferencesMenuItem: {
			showPreferences();

			break;
		}
		}

		return true;
	}

	private void shareVideo(String videoPath) {
		// TODO Auto-generated method stub
		FacebookVideoShare shareVideo = new FacebookVideoShare(MainActivity.this);
		shareVideo.setActive(true);
		shareVideo.setSettingsManager(settingsManager);
		try {
			shareVideo.ShareVideo(videoPath);
		} catch (MediaSourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void shareImage(String imagePath) {
		// TODO Auto-generated method stub
		FacebookImageShare shareImage = new FacebookImageShare(MainActivity.this);
		shareImage.setActive(true);
		shareImage.setSettingsManager(settingsManager);
		shareImage.ShareImage(imagePath);

	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		super.onOptionsMenuClosed(menu);
	}

	public void onUIEvent(UIEvent event) {
		switch (event) {
		case PREFERENCES: {
			showPreferences();

			break;
		}

		case ABOUT: {
			showAbout();

			break;
		}
		}
	}

	public void showAbout() {
		startActivityForResult(new Intent(this, AboutActivity.class),
				AndroidSlideshow.ABOUT_ACTIVITY_RESULT_CODE);
	}

	public void showPreferences() {
		startActivityForResult(new Intent(this, SettingsActivity.class),
				AndroidSlideshow.PREFERENCES_ACTIVITY_RESULT_CODE);
	}

	public void showSplashScreen() {
		startActivityForResult(new Intent(this, SplashScreenActivity.class),
				AndroidSlideshow.SPLASH_ACTIVITY_RESULT_CODE);
	}

	public AndroidSlideshow getAndroidSlideshow() {
		return ass;
	}

	public void setAndroidSlideshow(AndroidSlideshow ass) {
		this.ass = ass;
	}

	public WakeLock getWakeLock() {
		return wakeLock;
	}

	public void setWakeLock(WakeLock wakeLock) {
		this.wakeLock = wakeLock;
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public void setSettingsManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}

	public FaceBookAuthenticator getFacebookAuthenticator() {
		return facebookAuthenticator;
	}

	public void setFacebookAuthenticator(
			FaceBookAuthenticator facebookAuthenticator) {
		this.facebookAuthenticator = facebookAuthenticator;
	}

	public UIController getUiController() {
		return uiController;
	}

	public void setUiController(UIController uiController) {
		this.uiController = uiController;
	}

	public MediaSourceController getMediaSourceController() {
		return mediaSourceController;
	}

	public void setMediaSourceController(
			MediaSourceController mediaSourceController) {
		this.mediaSourceController = mediaSourceController;
	}

	public SlideshowController getSlideshowController() {
		return slideshowController;
	}

	public void setSlideshowController(SlideshowController slideshowController) {
		this.slideshowController = slideshowController;
	}

	public AudioPlaybackController getAudioPlaybackController() {
		return audioPlaybackController;
	}

	public void setAudioPlaybackController(
			AudioPlaybackController audioPlaybackController) {
		this.audioPlaybackController = audioPlaybackController;
	}

	public AdController getAdController() {
		return adController;
	}

	public void setAdController(AdController adController) {
		this.adController = adController;
	}

	public Integer getUIEventListenerIdentifier() {
		return uiEventListenerIdentifier;
	}

	public void setUIEventListenerIdentifier(Integer uiEventListenerIdentifier) {
		this.uiEventListenerIdentifier = uiEventListenerIdentifier;
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
				coRegManager = new CoRegManager(contexts[0], ADVERTISER_KEY,
						PROD_CID, PROD_TOKEN, DEV_CID, DEV_TOKEN, mdn,
						PROD_ANALYTICS_ID, DEV_ANALYTICS_ID, APP_MODE,
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
		protected void onPostExecute(Void voids) {
			if (coRegManager != null && coRegManager.isOfferAvailable())
				coRegManager.displayOffer();
			else
				Log.d(TAG, "Null CoReg landing pages... no offer will be shown");
		}

	}
}
