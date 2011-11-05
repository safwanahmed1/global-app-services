package com.sendme.android.slideshow.controller;

import android.app.Activity;
import android.content.Intent;
import com.google.inject.Inject;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.manager.SettingsManager;
import com.sendme.android.slideshow.source.MediaSourceListener;
import com.sendme.android.slideshow.source.impl.FacebookImageSource;
import com.sendme.android.slideshow.source.impl.LocalAudioTrackSource;
import com.sendme.android.slideshow.source.impl.LocalImageSource;
import com.sendme.android.slideshow.task.heartbeat.HeartbeatTask;
import com.sendme.android.slideshow.task.heartbeat.HeartbeatTaskEventListener;
import com.sendme.android.slideshow.task.media.MediaSourceUpdateTask;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class MediaSourceController
extends Controller
implements HeartbeatTaskEventListener, MediaSourceListener
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	@Inject
	private Activity activity = null;

	@Inject
	private SettingsManager settingsManager = null;

	@Inject
	private LocalImageSource localImageSource = null;

	@Inject
	private FacebookImageSource facebookImageSource = null;

	@Inject
	private LocalAudioTrackSource localAudioTrackSource = null;

	private Integer heartbeatIdentifier = null;

	public MediaSourceController()
	{
	}

	@Override
	public void initialize()
	{
		log.info("MediaSourceController initialized.");
	}

	@Override
	public void start()
	{
		log.info("MediaSourceController started.");

		localImageSource.setListener(this);
		facebookImageSource.setListener(this);
		localAudioTrackSource.setListener(this);

		HeartbeatTask.addListener(this);
	}

	@Override
	public void resume()
	{
		log.debug("MediaSourceController resumed.");
	}

	@Override
	public void pause()
	{
		log.debug("MediaSourceController paused.");
	}

	@Override
	public void stop()
	{
		HeartbeatTask.removeListener(this);

		localImageSource.setUpdating(false);
		facebookImageSource.setUpdating(false);
		localAudioTrackSource.setUpdating(false);

		log.info("MediaSourceController stopped.");
	}

	@Override
	public void destroy()
	{
		log.info("MediaSourceController destroyed.");
	}

	public void onHeartbeatTaskEvent(Long time)
	{
		log.debug("MediaSourceController got heartbeat: " + time);

		if (settingsManager.isUseLocalImages())
		{
			if (localImageSource.isActive())
			{
				if (!localImageSource.isUpdating())
				{
					long lastUpdate = settingsManager.getLocalImagesLastUpdateTime();

					if ((time - lastUpdate) >= settingsManager.getImageSourceUpdateInterval())
					{
						MediaSourceUpdateTask task = new MediaSourceUpdateTask();

						task.setImageSource(localImageSource);

						task.execute(new Void[0]);
					}
				}
				else
				{
					log.debug("LocalImageSource is currently updating.");
				}
			}
			else
			{
				log.debug("LocalImageSource is not active.");
			}
		}
		else
		{
			log.info("LocalImageSource not configured for use.");
		}

		if (settingsManager.isUseFacebookImages())
		{
			if (facebookImageSource.isActive())
			{
				if (!facebookImageSource.isUpdating())
				{
					long lastUpdate = settingsManager.getFacebookImagesLastUpdateTime();

					if ((time - lastUpdate) >= settingsManager.getImageSourceUpdateInterval())
					{
						MediaSourceUpdateTask task = new MediaSourceUpdateTask();

						task.setImageSource(facebookImageSource);

						task.execute(new Void[0]);
					}
				}
				else
				{
					log.debug("FacebookImageSource is currently updating.");
				}
			}
			else
			{
				log.debug("FacebookImageSource is not active.");
			}
		}
		else
		{
			log.info("FacebookImageSource not configured for use.");
		}

		if (settingsManager.isPlayMusic())
		{
			if (localAudioTrackSource.isActive())
			{
				if (!localAudioTrackSource.isUpdating())
				{
					long lastUpdate = settingsManager.getAudioTracksLastUpdateTime();

					if ((time - lastUpdate) >= settingsManager.getAudioTrackSourceUpdateInterval())
					{
						MediaSourceUpdateTask task = new MediaSourceUpdateTask();

						task.setImageSource(localAudioTrackSource);

						task.execute(new Void[0]);
					}
				}
				else
				{
					log.debug("LocalAudioTrackSource is currently updating.");
				}
			}
			else
			{
				log.debug("LocalAudioTrackSource is not active.");
			}
		}
		else
		{
			log.info("LocalAudioTrackSource not configured for use.");
		}
	}

	public void authorizeCallback(int requestCode, int resultCode, Intent data)
	{
		facebookImageSource.authorizeCallback(requestCode, resultCode, data);
	}

	public Activity getActivity()
	{
		return activity;
	}

	public void setActivity(Activity activity)
	{
		this.activity = activity;
	}

	public SettingsManager getSettingsManager()
	{
		return settingsManager;
	}

	public void setSettingsManager(SettingsManager settingsManager)
	{
		this.settingsManager = settingsManager;
	}

	public LocalImageSource getLocalImageSource()
	{
		return localImageSource;
	}

	public void setLocalImageSource(LocalImageSource localImageSource)
	{
		this.localImageSource = localImageSource;
	}

	public FacebookImageSource getFacebookImageSource()
	{
		return facebookImageSource;
	}

	public void setFacebookImageSource(FacebookImageSource facebookImageSource)
	{
		this.facebookImageSource = facebookImageSource;
	}

	public LocalAudioTrackSource getLocalAudioTrackSource()
	{
		return localAudioTrackSource;
	}

	public void setLocalAudioTrackSource(LocalAudioTrackSource localAudioTrackSource)
	{
		this.localAudioTrackSource = localAudioTrackSource;
	}

	public Integer getHeartbeatIdentifier()
	{
		return heartbeatIdentifier;
	}

	public void setHeartbeatIdentifier(Integer heartbeatIdentifier)
	{
		this.heartbeatIdentifier = heartbeatIdentifier;
	}
}
