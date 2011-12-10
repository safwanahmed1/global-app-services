package com.sendme.android.slideshow.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.StatFs;

import com.google.inject.Inject;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.image.ImageLoader;
import com.sendme.android.slideshow.manager.SettingsManager;
import com.sendme.android.slideshow.task.heartbeat.HeartbeatTask;
import com.sendme.android.slideshow.task.heartbeat.HeartbeatTaskEventListener;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class ExternalMemoryController extends Controller implements
		HeartbeatTaskEventListener {
	private final static AndroidLogger log = AndroidLogger
			.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public void setSettingsManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}

	private Integer heartbeatIdentifier = null;

	private Long lastCheckMemory = 0L;
	@Inject
	private SettingsManager settingsManager = null;

	public ExternalMemoryController() {

	}

	@Override
	public void initialize() {
		log.debug("AdController initialized.");

	}

	@Override
	public void start() {
		HeartbeatTask.addListener(this);

		log.debug("AdController started.");
	}

	@Override
	public void resume() {
		log.debug("AdController resumed.");
	}

	@Override
	public void pause() {
		log.debug("AdController paused.");
	}

	@Override
	public void stop() {
		HeartbeatTask.removeListener(this);

		log.debug("AdController stopped.");
	}

	@Override
	public void destroy() {
		log.debug("AdController destroyed.");
	}

	public void onHeartbeatTaskEvent(Long time) {
		if ((time - lastCheckMemory) > 1000) {// settingsManager.getSlideshowInterval())
												// {
			StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
					.getPath());
			long bytesAvailable = (long) stat.getBlockSize()
					* (long) stat.getBlockCount();
			long megAvailable = bytesAvailable / 1048576;
			if (megAvailable < 50000) {
				AlertDialog.Builder builder = new AlertDialog.Builder(settingsManager.getAndroidSlideshow());
				builder.setMessage("There is not enough room on your SD card to make your slideshow. Please clear some space and try again")
				       .setCancelable(false)
				       .setNegativeButton("Okay", new DialogInterface.OnClickListener() {
				           public void onClick(DialogInterface dialog, int id) {
				        	   dialog.cancel();
				           }
				       })     ;
				//AlertDialog alert = builder.create();
				
			}

			lastCheckMemory = time;
		}
	}

	public Integer getHeartbeatIdentifier() {
		return heartbeatIdentifier;
	}

	public void setHeartbeatIdentifier(Integer heartbeatIdentifier) {
		this.heartbeatIdentifier = heartbeatIdentifier;
	}

}
