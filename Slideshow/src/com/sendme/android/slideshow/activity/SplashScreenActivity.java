package com.sendme.android.slideshow.activity;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.R;
import com.sendme.android.slideshow.auth.AuthenticationException;
import com.sendme.android.slideshow.auth.AuthenticationListener;
import com.sendme.android.slideshow.auth.impl.FaceBookAuthenticator;
import com.sendme.android.slideshow.manager.SettingsManager;
import com.sendme.android.slideshow.runnable.SplashScreenPause;
import com.sendme.android.slideshow.source.impl.FacebookImageSource;
import com.sendme.android.slideshow.task.init.InitializationEventType;
import com.sendme.android.slideshow.task.init.InitializationPhase;
import com.sendme.android.slideshow.task.init.InitializationTask;
import com.sendme.android.slideshow.task.init.InitializationTaskListener;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

/**
 * 
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class SplashScreenActivity extends RoboActivity implements
		InitializationTaskListener, DialogInterface.OnClickListener,
		AuthenticationListener {
	private final static AndroidLogger log = AndroidLogger
			.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	private final static String DOING_AUTHENTICATION_KEY = "DOING_AUTHENTICATION";

	@Inject
	private AndroidSlideshow ass = null;

	@Inject
	private SettingsManager settingsManager = null;

	@Inject
	private FacebookImageSource facebookImageSource = null;

	@Inject
	private FaceBookAuthenticator facebookAuthenticator = null;

	@InjectView(R.id.splashscreenText)
	private TextView splashText = null;

	private boolean doingAuthentication = false;

	public SplashScreenActivity() {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		log.info("Splash onCreate..");

		super.onCreate(savedInstanceState);

		setContentView(R.layout.splashscreen);

		facebookAuthenticator.setActivity(this);
		facebookAuthenticator.setListener(this);

		facebookAuthenticator
				.setRequestCode(AndroidSlideshow.FACEBOOK_AUTHENTICATION_RESULT_CODE);

		if (savedInstanceState != null) {
			doingAuthentication = savedInstanceState.getBoolean(
					DOING_AUTHENTICATION_KEY, false);
		} else {
			doingAuthentication = false;
		}
	}

	@Override
	protected void onStart() {
		log.info("Splash onStart..");

		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();

		log.info("Splash onPause..");
	}

	@Override
	protected void onResume() {
		super.onResume();

		log.info("Splash onResume called..");

		if (settingsManager.needsPreferences()) {
			log.info("We need preferences??");

			SplashScreenPause pause = new SplashScreenPause();

			pause.setSplashScreenActivity(this);

			splashText.postDelayed(pause,
					settingsManager.getSplashScreenPauseDuration());
		} else {
			log.info("OnCreate Calling Initialize..");

			if (!doingAuthentication) {
				initialize(InitializationPhase.START);
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		log.info("OnSaveInstanceState called...");

		outState.putBoolean(DOING_AUTHENTICATION_KEY, doingAuthentication);
	}

	@Override
	protected void onStop() {
		super.onStop();

		log.info("Splash onStop..");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		log.info("Splash onDestroy..");
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog output = null;

		Builder builder = new Builder(this);

		switch (id) {
		case AndroidSlideshow.DATABASE_ERROR_DIALOG: {
			builder.setTitle(R.string.databaseErrorDialogTitle);

			builder.setCancelable(false);

			builder.setMessage(R.string.databaseErrorDialogMessage);

			builder.setPositiveButton(R.string.databaseErrorDialogButton, this);

			output = builder.create();

			break;
		}

		default: {
			output = super.onCreateDialog(id);
		}
		}

		return output;
	}

	public void onClick(DialogInterface di, int i) {
		log.debug("Dialog Clicked: " + di + " -- " + i);

		// For now, this is just the database error dialog, so we'll just exit
		finish();
	}

	public void onInitializationStatusUpdate(InitializationEventType status) {
		log.debug("Status Update: " + status);

		switch (status) {
		case INITIALIZING: {
			updateText(R.string.splashscreenInitializing);

			initialize(InitializationPhase.DATABASE);

			break;
		}

		case DATABASE_INITIALIZING: {
			updateText(R.string.splashscreenDatabaseInitializing);

			break;
		}

		case DATABASE_INITIALIZED: {
			updateText(R.string.splashscreenDatabaseInitialized);

			initialize(InitializationPhase.LOCAL_IMAGE_SOURCE);

			break;
		}

		case DATABASE_ERROR: {
			updateText(R.string.splashscreenDatabaseError);

			showDialog(AndroidSlideshow.DATABASE_ERROR_DIALOG);

			break;
		}

		case LOCAL_IMAGE_SOURCE_INITIALIZING: {
			updateText(R.string.splashscreenLocalImageSourceInitializing);
			initialize(InitializationPhase.FACEBOOK_IMAGE_SOURCE); // QuanPN add
																	// new line
			break;
		}

		case LOCAL_IMAGE_SOURCE_INITIALIZED: {
			updateText(R.string.splashscreenLocalImageSourceInitialized);

			// initialize(InitializationPhase.FACEBOOK_IMAGE_SOURCE);

			break;
		}

		case LOCAL_IMAGE_SOURCE_ERROR: {
			updateText(R.string.splashscreenLocalImageSourceError);

			// initialize(InitializationPhase.FACEBOOK_IMAGE_SOURCE);

			break;
		}

		case FACEBOOK_IMAGE_SOURCE_INITIALIZING: {
			updateText(R.string.splashscreenFacebookInitializing);
			// initialize(InitializationPhase.LOCAL_AUDIO_TRACKS_SOURCE); //
			// QuanPN add new line
			break;
		}

		case FACEBOOK_IMAGE_SOURCE_INITIALIZED: {
			updateText(R.string.splashscreenFacebookInitializing);

			initialize(InitializationPhase.LOCAL_AUDIO_TRACKS_SOURCE);

			break;
		}

		case FACEBOOK_IMAGE_SOURCE_ERROR: {
			updateText(R.string.splashscreenFacebookError);

			initialize(InitializationPhase.LOCAL_AUDIO_TRACKS_SOURCE);

			break;
		}

		case FACEBOOK_IMAGE_SOURCE_AUTHORIZATION: {
			doingAuthentication = true;

			try {
				updateText(R.string.splashscreenFacebookAuthorizing);

				facebookAuthenticator
						.authenticate(AndroidSlideshow.FACEBOOK_AUTHENTICATION_RESULT_CODE);
			} catch (AuthenticationException e) {
				updateText(R.string.splashscreenFacebookError);

				facebookImageSource.setActive(false);

				initialize(InitializationPhase.LOCAL_AUDIO_TRACKS_SOURCE);
			}

			break;
		}

		case FACEBOOK_IMAGE_SOURCE_AUTHORIZATION_CANCELLED: {
			updateText(R.string.splashscreenFacebookError);

			facebookImageSource.setActive(false);

			initialize(InitializationPhase.LOCAL_AUDIO_TRACKS_SOURCE);

			break;
		}

		case FACEBOOK_IMAGE_SOURCE_AUTHORIZATION_ERROR: {
			updateText(R.string.splashscreenFacebookError);

			facebookImageSource.setActive(false);

			initialize(InitializationPhase.LOCAL_AUDIO_TRACKS_SOURCE);

			break;
		}

		case FACEBOOK_IMAGE_SOURCE_AUTHORIZED: {
			updateText(R.string.splashscreenFacebookAuthorized);

			initialize(InitializationPhase.FACEBOOK_IMAGE_SOURCE);

			break;
		}

		case LOCAL_AUDIO_TRACKS_SOURCE_INITIALIZING: {
			updateText(R.string.splashscreenLocalAudioTracksSourceInitializing);
			initialize(InitializationPhase.SLIDESHOW); // QuanPN add new line
			break;
		}

		case LOCAL_AUDIO_TRACKS_SOURCE_INITIALIZED: {
			updateText(R.string.splashscreenLocalAudioTracksSourceInitialized);

			// initialize(InitializationPhase.SLIDESHOW);

			break;
		}

		case LOCAL_AUDIO_TRACKS_SOURCE_ERROR: {
			updateText(R.string.splashscreenLocalAudioTracksSourceError);

			// initialize(InitializationPhase.SLIDESHOW);

			break;
		}

		case SLIDESHOW_INITIALIZING: {
			updateText(R.string.splashscreenSlideshowInitializing);
			initialize(InitializationPhase.PLAYLIST); // QuanPN add new line
			break;
		}

		case SLIDESHOW_INITIALIZED: {
			updateText(R.string.splashscreenSlideshowInitialized);

			// initialize(InitializationPhase.PLAYLIST);

			break;
		}

		case PLAYLIST_INITIALIZING: {
			updateText(R.string.splashscreenPlaylistInitializing);
			// initialize(InitializationPhase.COMPLETE);// QuanPN add new line
			break;
		}

		case PLAYLIST_INITIALIZED: {
			updateText(R.string.splashscreenPlaylistInitialized);

			initialize(InitializationPhase.COMPLETE);

			break;
		}

		case INITIALIZED: {
			updateText(R.string.splashscreenInitialized);

			onInitializationComplete();

			break;
		}
		}
	}

	public void onInitializationComplete() {
		showMain();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case AndroidSlideshow.PREFERENCES_ACTIVITY_RESULT_CODE: {
			log.info("OnActivityResult Calling Initialize..");

			break;
		}

		case AndroidSlideshow.FACEBOOK_ACTIVITY_RESULT_CODE: {
			doingAuthentication = false;

			facebookAuthenticator.getFacebookApplication().authorizeCallback(
					requestCode, resultCode, data);

			break;
		}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		} else {
			return false;
		}
	}

	public void onAuthenticationCancelled(int requestCode) {
		doingAuthentication = false;

		switch (requestCode) {
		case AndroidSlideshow.FACEBOOK_AUTHENTICATION_RESULT_CODE: {
			Toast.makeText(ass,
					ass.getString(R.string.splashscreenLoginCancel),
					Toast.LENGTH_LONG).show();
			onInitializationStatusUpdate(InitializationEventType.FACEBOOK_IMAGE_SOURCE_AUTHORIZATION_CANCELLED);

			break;
		}
		}
	}

	public void onAuthenticationComplete(int requestCode) {
		doingAuthentication = false;

		switch (requestCode) {
		case AndroidSlideshow.FACEBOOK_AUTHENTICATION_RESULT_CODE: {
			onInitializationStatusUpdate(InitializationEventType.FACEBOOK_IMAGE_SOURCE_AUTHORIZED);

			break;
		}
		}
	}

	public void onAuthenticationError(int requestCode, Throwable t) {
		doingAuthentication = false;

		switch (requestCode) {
		case AndroidSlideshow.FACEBOOK_AUTHENTICATION_RESULT_CODE: {
			onInitializationStatusUpdate(InitializationEventType.FACEBOOK_IMAGE_SOURCE_AUTHORIZATION_ERROR);

			break;
		}
		}
	}

	public void showMain() {
		log.info("Showing main activity...");

		startActivity(new Intent(this, MainActivity.class));

		// Make sure we don't get back here with the back button
		finish();
	}

	public void showPreferences() {
		log.info("Showing settings activity...");

		startActivityForResult(new Intent(this, SettingsActivity.class),
				AndroidSlideshow.PREFERENCES_ACTIVITY_RESULT_CODE);
	}

	public void initialize(InitializationPhase phase) {
		InitializationTask task = new InitializationTask();

		task.setListener(this);

		task.execute(phase);
	}

	public void updateText(int stringId) {
		log.info("Setting text: " + getString(stringId));

		splashText.setText(getString(stringId));

		splashText.invalidate();
	}

	public AndroidSlideshow getAndroidSlideshow() {
		return ass;
	}

	public void setAndroidSlideshow(AndroidSlideshow ass) {
		this.ass = ass;
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public void setSettingsManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}

	public TextView getSplashText() {
		return splashText;
	}

	public void setSplashText(TextView splashText) {
		this.splashText = splashText;
	}
}
