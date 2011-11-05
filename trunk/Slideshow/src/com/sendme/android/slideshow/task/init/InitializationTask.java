package com.sendme.android.slideshow.task.init;

import android.os.AsyncTask;
import com.google.inject.Inject;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.auth.AuthenticationException;
import com.sendme.android.slideshow.auth.impl.FaceBookAuthenticator;
import com.sendme.android.slideshow.manager.AudioPlaylistManager;
import com.sendme.android.slideshow.manager.DatabaseManager;
import com.sendme.android.slideshow.manager.ORMException;
import com.sendme.android.slideshow.manager.SettingsManager;
import com.sendme.android.slideshow.manager.SlideshowManager;
import com.sendme.android.slideshow.source.MediaSourceException;
import com.sendme.android.slideshow.source.impl.FacebookImageSource;
import com.sendme.android.slideshow.source.impl.LocalAudioTrackSource;
import com.sendme.android.slideshow.source.impl.LocalImageSource;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class InitializationTask
extends AsyncTask<InitializationPhase, InitializationEventType, Void>
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	@Inject
	private AndroidSlideshow ass = null;

	private InitializationTaskListener listener = null;

	@Inject
	private DatabaseManager databaseManager = null;

	@Inject
	private SettingsManager settingsManager = null;

	@Inject
	private SlideshowManager slideshowManager = null;

	@Inject
	private AudioPlaylistManager audioPlaylistManager = null;

	@Inject
	private LocalImageSource localImageSource = null;

	@Inject
	private FacebookImageSource facebookImageSource = null;

	@Inject
	private FaceBookAuthenticator facebookAuthenticator = null;

	@Inject
	private LocalAudioTrackSource localAudioTrackSource = null;

	public InitializationTask()
	{
	}

	@Override
	protected void onPreExecute()
	{
		AndroidSlideshow.performInjection(this);
	}

	@Override
	protected Void doInBackground(InitializationPhase... params)
	{
		InitializationPhase phase = params[0];

		switch (phase)
		{
			case START:
			{
				publishProgress(InitializationEventType.INITIALIZING);

				break;
			}

			case DATABASE:
			{
				publishProgress(InitializationEventType.DATABASE_INITIALIZING);

				try
				{
					if (!databaseManager.databaseExists())
					{
						databaseManager.dropSchema();
						databaseManager.createSchema();
					}

					publishProgress(InitializationEventType.DATABASE_INITIALIZED);
				}
				catch (ORMException e)
				{
					log.error("Cannot create schema", e);

					publishProgress(InitializationEventType.DATABASE_ERROR);
				}

				break;
			}

			case LOCAL_IMAGE_SOURCE:
			{
				publishProgress(InitializationEventType.LOCAL_IMAGE_SOURCE_INITIALIZING);

				if (settingsManager.isUseLocalImages())
				{
					try
					{
						settingsManager.setLocalImagesLastUpdateTime(0L);

						localImageSource.checkForUpdates(System.currentTimeMillis(), false);

						localImageSource.setActive(true);

						publishProgress(InitializationEventType.LOCAL_IMAGE_SOURCE_INITIALIZED);
					}
					catch (MediaSourceException e)
					{
						log.error("Error updating local image source.", e);

						localImageSource.setActive(false);

						publishProgress(InitializationEventType.LOCAL_IMAGE_SOURCE_ERROR);
					}
				}
				else
				{
					localImageSource.setActive(false);

					publishProgress(InitializationEventType.LOCAL_IMAGE_SOURCE_INITIALIZED);
				}

				break;
			}

			case FACEBOOK_IMAGE_SOURCE:
			{
				// Sadly, there isn't much we can do here.  The Facebook lifecycle just kills us,
				// really.  So, we're going to simply set it active or not, and then let the main
				// activity take care of things.

				publishProgress(InitializationEventType.FACEBOOK_IMAGE_SOURCE_INITIALIZING);

				if (settingsManager.isUseFacebookImages())
				{
					facebookImageSource.setActive(true);

					try
					{
						if (facebookAuthenticator.needsAuthentication())
						{
							publishProgress(InitializationEventType.FACEBOOK_IMAGE_SOURCE_AUTHORIZATION);
						}
						else
						{
							facebookImageSource.setActive(true);

							publishProgress(InitializationEventType.FACEBOOK_IMAGE_SOURCE_INITIALIZED);
						}
					}
					catch (AuthenticationException e)
					{
						publishProgress(InitializationEventType.FACEBOOK_IMAGE_SOURCE_AUTHORIZATION_ERROR);
					}
				}
				else
				{
					facebookImageSource.setActive(false);

					publishProgress(InitializationEventType.FACEBOOK_IMAGE_SOURCE_INITIALIZED);
				}

				break;
			}

			case LOCAL_AUDIO_TRACKS_SOURCE:
			{
				publishProgress(InitializationEventType.LOCAL_AUDIO_TRACKS_SOURCE_INITIALIZING);

				if (settingsManager.isPlayMusic())
				{
					try
					{
						settingsManager.setAudioTracksLastUpdateTime(0L);

						localAudioTrackSource.checkForUpdates(System.currentTimeMillis(), false);

						localAudioTrackSource.setActive(true);

						publishProgress(InitializationEventType.LOCAL_AUDIO_TRACKS_SOURCE_INITIALIZED);
					}
					catch (MediaSourceException e)
					{
						log.error("Error updating local image source.", e);

						localAudioTrackSource.setActive(false);

						publishProgress(InitializationEventType.LOCAL_AUDIO_TRACKS_SOURCE_ERROR);
					}
				}
				else
				{
					localAudioTrackSource.setActive(false);

					publishProgress(InitializationEventType.LOCAL_AUDIO_TRACKS_SOURCE_INITIALIZED);
				}

				break;
			}

			case SLIDESHOW:
			{
				// Ok, out DB should be good, lets see about creating a slideshow
				publishProgress(InitializationEventType.SLIDESHOW_INITIALIZING);

				try
				{
					log.info("Initializing slideshow...");

					slideshowManager.shuffleSlideshow();
				}
				catch (ORMException e)
				{
					log.error("Cannot shuffle slideshow.", e);
				}

				publishProgress(InitializationEventType.SLIDESHOW_INITIALIZED);

				break;
			}

			case PLAYLIST:
			{
				// Ok, out DB should be good, lets see about creating a slideshow
				publishProgress(InitializationEventType.PLAYLIST_INITIALIZING);

				try
				{
					log.info("Initializing playlist...");

					audioPlaylistManager.shufflePlaylist();
				}
				catch (ORMException e)
				{
					log.error("Cannot shuffle playlist.", e);
				}

				publishProgress(InitializationEventType.PLAYLIST_INITIALIZED);

				break;
			}

			case COMPLETE:
			{
				publishProgress(InitializationEventType.INITIALIZED);

				break;
			}
		}

		return null;
	}

	@Override
	protected void onProgressUpdate(InitializationEventType... values)
	{
		listener.onInitializationStatusUpdate(values[0]);
	}

	public AndroidSlideshow getAndroidSlideshow()
	{
		return ass;
	}

	public void setAndroidSlideshow(AndroidSlideshow ass)
	{
		this.ass = ass;
	}

	public InitializationTaskListener getListener()
	{
		return listener;
	}

	public void setListener(InitializationTaskListener listener)
	{
		this.listener = listener;
	}

	public DatabaseManager getDatabaseManager()
	{
		return databaseManager;
	}

	public void setDatabaseManager(DatabaseManager databaseManager)
	{
		this.databaseManager = databaseManager;
	}

	public SettingsManager getSettingsManager()
	{
		return settingsManager;
	}

	public void setSettingsManager(SettingsManager settingsManager)
	{
		this.settingsManager = settingsManager;
	}

	public SlideshowManager getSlideshowManager()
	{
		return slideshowManager;
	}

	public void setSlideshowManager(SlideshowManager slideshowManager)
	{
		this.slideshowManager = slideshowManager;
	}

	public AudioPlaylistManager getAudioPlaylistManager()
	{
		return audioPlaylistManager;
	}

	public void setAudioPlaylistManager(AudioPlaylistManager audioPlaylistManager)
	{
		this.audioPlaylistManager = audioPlaylistManager;
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

	public FaceBookAuthenticator getFacebookAuthenticator()
	{
		return facebookAuthenticator;
	}

	public void setFacebookAuthenticator(FaceBookAuthenticator facebookAuthenticator)
	{
		this.facebookAuthenticator = facebookAuthenticator;
	}

	public LocalAudioTrackSource getLocalAudioTrackSource()
	{
		return localAudioTrackSource;
	}

	public void setLocalAudioTrackSource(LocalAudioTrackSource localAudioTrackSource)
	{
		this.localAudioTrackSource = localAudioTrackSource;
	}
}
