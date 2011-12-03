package com.sendme.android.slideshow.controller;

import android.media.MediaPlayer;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
//import com.facebook.android.R;
import com.google.inject.Inject;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.R;
import com.sendme.android.slideshow.image.ImageLoader;
import com.sendme.android.slideshow.manager.AudioPlaylistManager;
import com.sendme.android.slideshow.manager.AudioTrackManager;
import com.sendme.android.slideshow.manager.ORMException;
import com.sendme.android.slideshow.manager.SettingsManager;
import com.sendme.android.slideshow.model.AudioPlaylistEntry;
import com.sendme.android.slideshow.model.AudioTrack;
import com.sendme.android.slideshow.runnable.AudioPlaybackUIAnimation;
import com.sendme.android.slideshow.runnable.AudioPlaybackUIUpdater;
import com.sendme.android.slideshow.task.heartbeat.HeartbeatTask;
import com.sendme.android.slideshow.task.heartbeat.HeartbeatTaskEventListener;
import com.sendme.android.ui.event.AdvancedGestureDetector;
import com.sendme.android.ui.event.FlingDirection;
import com.sendme.android.ui.event.GestureListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import roboguice.inject.InjectView;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class UIController
extends Controller
implements OnClickListener, GestureListener, HeartbeatTaskEventListener
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	private final static AtomicInteger UI_EVENT_LISTENER_IDENTIFIER_GENERATOR = new AtomicInteger(0);

	private final static ReentrantLock listenerLock = new ReentrantLock(true);

	private static Map<Integer, UIEventListener> uiEventListeners = new HashMap<Integer, UIEventListener>();

	private static Long lastVanishEvent = null;

	@Inject
	private SettingsManager settingsManager = null;

	@Inject
	private AudioPlaylistManager audioPlaylistManager = null;

	@Inject
	private AudioTrackManager audioTrackManager = null;

	@Inject
	private ImageLoader imageLoader = null;

	@Inject
	private AdvancedGestureDetector gestureDetector = null;

	@InjectView(R.id.gallery)
	private ImageView gallery = null;

	@InjectView(R.id.controlLayout)
	private View controlLayout = null;

	@InjectView(R.id.previousTrackButton)
	private ImageButton previousTrackButton = null;

	@InjectView(R.id.playButton)
	private ImageButton playButton = null;

	@InjectView(R.id.pauseButton)
	private ImageButton pauseButton = null;

	@InjectView(R.id.nextTrackButton)
	private ImageButton nextTrackButton = null;

	@InjectView(R.id.currentlyPlayingLabel)
	private TextView currentlyPlayingLabel = null;

	@InjectView(R.id.albumArtView)
	private ImageView albumArtView = null;

	@InjectView(R.id.preferencesButton)
	private Button preferencesButton = null;

	@InjectView(R.id.aboutButton)
	private Button aboutButton = null;

	@Inject
	private Vibrator vibrator = null;

	private Integer heartbeatIdentifier = null;

	private Integer lastSeenAudioPlaylistEntryId = null;

	public UIController()
	{
	}

	@Override
	public void initialize()
	{
		log.debug("UIController initialized.");
	}

	@Override
	public void start()
	{
		controlLayout.setVisibility(View.GONE);

		gestureDetector.setGestureListener(this);

		gallery.setOnTouchListener(gestureDetector);

		previousTrackButton.setOnClickListener(this);

		playButton.setOnClickListener(this);

		pauseButton.setOnClickListener(this);

		nextTrackButton.setOnClickListener(this);

		preferencesButton.setOnClickListener(this);

		aboutButton.setOnClickListener(this);

		HeartbeatTask.addListener(this);

		log.debug("UIController started.");
	}

	@Override
	public void resume()
	{
		log.debug("UIController resumed.");
	}

	@Override
	public void pause()
	{
		log.debug("UIController paused.");
	}

	@Override
	public void stop()
	{
		HeartbeatTask.removeListener(this);

		log.info("UIController stopped.");
	}

	@Override
	public void destroy()
	{
		log.info("UIController destroyed.");
	}

	public void onHeartbeatTaskEvent(Long time)
	{
		if (lastVanishEvent != null)
		{
			if ((lastVanishEvent + settingsManager.getUIDisplayLifetime()) <= time)
			{
				hideControls();

				clearLastVanishEvent();
			}
		}

		Integer currentEntryId = settingsManager.getCurrentAudioPlaylistEntryId();

		if (currentEntryId != null)
		{
			if (!currentEntryId.equals(lastSeenAudioPlaylistEntryId))
			{
				try
				{
					AudioPlaylistEntry entry = audioPlaylistManager.find(currentEntryId);

					if (entry != null)
					{
						AudioTrack track = audioTrackManager.find(entry.getAudioTrackId());

						showUpdate(track);

						lastSeenAudioPlaylistEntryId = currentEntryId;
					}
				}
				catch (ORMException e)
				{
					log.error("Cannot do track information update", e);
				}
			}
		}
	}

	public void onClick(View view)
	{
		log.info("Got a click: " + view.getId());

		registerVanishEvent();

		// Quickly, lets play a little sound and provide some tactile feedback
		vibrator.vibrate(75L);

		try
		{
			MediaPlayer player = MediaPlayer.create(ass, R.raw.button_click);

			player.reset();

			player.start();

			while (player.isPlaying())
			{
				// Just wait a tiny bit.
			}

			player.release();
		}
		catch (IllegalStateException e)
		{
			log.error("Could not play sound for button click.", e);
		}

		switch (view.getId())
		{
			case R.id.playButton:
			{
				publishUIEvent(UIEvent.PLAY);

				break;
			}

			case R.id.pauseButton:
			{
				publishUIEvent(UIEvent.PAUSE);

				break;
			}

			case R.id.nextTrackButton:
			{
				publishUIEvent(UIEvent.NEXT_TRACK);

				break;
			}

			case R.id.previousTrackButton:
			{
				publishUIEvent(UIEvent.PREVIOUS_TRACK);

				break;
			}

			case R.id.preferencesButton:
			{
				publishUIEvent(UIEvent.PREFERENCES);

				break;
			}

			case R.id.aboutButton:
			{
				publishUIEvent(UIEvent.ABOUT);

				break;
			}
		}
	}

	public boolean onDoubleTap(MotionEvent event)
	{
		// NOOP

		return false;
	}

	public boolean onDoubleTapEvent(MotionEvent event)
	{
		// NOOP

		return false;
	}

	public boolean onFling(FlingDirection direction, MotionEvent event1, MotionEvent event2, float horizontalVelocity, float verticalVelocity)
	{
		boolean output = false;

		switch (direction)
		{
			case LEFT:
			{
				output = true;

				publishUIEvent(UIEvent.NEXT_PHOTO);

				break;
			}

			case RIGHT:
			{
				output = true;

				publishUIEvent(UIEvent.PREVIOUS_PHOTO);

				break;
			}

			default:
			{
				break;
			}
		}

		return output;
	}

	public void onLongPress(MotionEvent event)
	{
		// NOOP
	}

	public boolean onScroll(MotionEvent event1, MotionEvent event2, float horizontalDistance, float verticalDistance)
	{
		// NOOP

		return false;
	}

	public void onShowPress(MotionEvent event)
	{
		// NOOP
	}

	public boolean onSingleTap(MotionEvent event)
	{
		registerVanishEvent();
		toggleControls();

		return true;
	}

	public boolean onTapDown(MotionEvent event)
	{
		// NOOP

		return false;
	}

	public boolean onTapUp(MotionEvent event)
	{
		// NOOP

		return false;
	}

	public void publishUIEvent(UIEvent event)
	{
		listenerLock.lock();

		try
		{
			for (UIEventListener listener : uiEventListeners.values())
			{
				listener.onUIEvent(event);
			}
		}
		finally
		{
			listenerLock.unlock();
		}
	}

	public void showUpdate(AudioTrack update)
	{
		AudioPlaybackUIUpdater updater = new AudioPlaybackUIUpdater();

		updater.setUIController(this);
		updater.setAudioTrack(update);

		controlLayout.post(updater);
	}

	public void showControls()
	{
		AudioPlaybackUIAnimation anim = new AudioPlaybackUIAnimation();

		if (controlLayout.getVisibility() != View.VISIBLE)
		{
			anim.setView(controlLayout);

			anim.setAnimation(AnimationUtils.loadAnimation(controlLayout.getContext(), R.anim.slide_menu_show));

			anim.setFinalVisibility(View.VISIBLE);

			controlLayout.post(anim);
		}
	}

	public void hideControls()
	{
		AudioPlaybackUIAnimation anim = new AudioPlaybackUIAnimation();

		if (controlLayout.getVisibility() != View.GONE)
		{
			anim.setView(controlLayout);

			anim.setAnimation(AnimationUtils.loadAnimation(controlLayout.getContext(), R.anim.slide_menu_hide));

			anim.setFinalVisibility(View.GONE);

			controlLayout.post(anim);
		}
	}

	public void toggleControls()
	{
		switch (controlLayout.getVisibility())
		{
			case View.VISIBLE:
			{
				
				//hideControls();

				break;
			}

			case View.INVISIBLE:
			{
				
				showControls();

				break;
			}

			case View.GONE:
			{
				showControls();

				break;
			}
		}
	}

	public static void registerVanishEvent()
	{
		lastVanishEvent = System.currentTimeMillis();
	}

	public static void clearLastVanishEvent()
	{
		lastVanishEvent = null;
	}

	public static void addListener(UIEventListener listener)
	{
		listenerLock.lock();

		try
		{
			Integer identifier = null;

			if (identifier == null)
			{
				identifier = UI_EVENT_LISTENER_IDENTIFIER_GENERATOR.getAndIncrement();

				listener.setUIEventListenerIdentifier(identifier);
			}

			if (!uiEventListeners.containsKey(identifier))
			{
				uiEventListeners.put(identifier, listener);
			}
		}
		finally
		{
			listenerLock.unlock();
		}
	}

	public static void removeListener(UIEventListener listener)
	{
		listenerLock.lock();

		try
		{
			if (listener.getUIEventListenerIdentifier() != null)
			{
				uiEventListeners.remove(listener.getUIEventListenerIdentifier());
			}
		}
		finally
		{
			listenerLock.unlock();
		}
	}

	public SettingsManager getSettingsManager()
	{
		return settingsManager;
	}

	public void setSettingsManager(SettingsManager settingsManager)
	{
		this.settingsManager = settingsManager;
	}

	public AudioPlaylistManager getAudioPlaylistManager()
	{
		return audioPlaylistManager;
	}

	public void setAudioPlaylistManager(AudioPlaylistManager audioPlaylistManager)
	{
		this.audioPlaylistManager = audioPlaylistManager;
	}

	public AudioTrackManager getAudioTrackManager()
	{
		return audioTrackManager;
	}

	public void setAudioTrackManager(AudioTrackManager audioTrackManager)
	{
		this.audioTrackManager = audioTrackManager;
	}

	public ImageLoader getImageLoader()
	{
		return imageLoader;
	}

	public void setImageLoader(ImageLoader imageLoader)
	{
		this.imageLoader = imageLoader;
	}

	public AdvancedGestureDetector getGestureDetector()
	{
		return gestureDetector;
	}

	public void setGestureDetector(AdvancedGestureDetector gestureDetector)
	{
		this.gestureDetector = gestureDetector;
	}

	public ImageView getGallery()
	{
		return gallery;
	}

	public void setGallery(ImageView gallery)
	{
		this.gallery = gallery;
	}

	public View getControlLayout()
	{
		return controlLayout;
	}

	public void setControlLayout(View controlLayout)
	{
		this.controlLayout = controlLayout;
	}

	public ImageButton getPreviousTrackButton()
	{
		return previousTrackButton;
	}

	public void setPreviousTrackButton(ImageButton previousTrackButton)
	{
		this.previousTrackButton = previousTrackButton;
	}

	public ImageButton getPlayButton()
	{
		return playButton;
	}

	public void setPlayButton(ImageButton playButton)
	{
		this.playButton = playButton;
	}

	public ImageButton getPauseButton()
	{
		return pauseButton;
	}

	public void setPauseButton(ImageButton pauseButton)
	{
		this.pauseButton = pauseButton;
	}

	public ImageButton getNextTrackButton()
	{
		return nextTrackButton;
	}

	public void setNextTrackButton(ImageButton nextTrackButton)
	{
		this.nextTrackButton = nextTrackButton;
	}

	public TextView getCurrentlyPlayingLabel()
	{
		return currentlyPlayingLabel;
	}

	public void setCurrentlyPlayingLabel(TextView currentlyPlayingLabel)
	{
		this.currentlyPlayingLabel = currentlyPlayingLabel;
	}

	public ImageView getAlbumArtView()
	{
		return albumArtView;
	}

	public void setAlbumArtView(ImageView albumArtView)
	{
		this.albumArtView = albumArtView;
	}

	public Button getAboutButton()
	{
		return aboutButton;
	}

	public void setAboutButton(Button aboutButton)
	{
		this.aboutButton = aboutButton;
	}

	public Button getPreferencesButton()
	{
		return preferencesButton;
	}

	public void setPreferencesButton(Button preferencesButton)
	{
		this.preferencesButton = preferencesButton;
	}

	public Vibrator getVibrator()
	{
		return vibrator;
	}

	public void setVibrator(Vibrator vibrator)
	{
		this.vibrator = vibrator;
	}

	public Integer getHeartbeatIdentifier()
	{
		return heartbeatIdentifier;
	}

	public void setHeartbeatIdentifier(Integer heartbeatIdentifier)
	{
		this.heartbeatIdentifier = heartbeatIdentifier;
	}

	public Integer getLastSeenAudioPlaylistEntryId()
	{
		return lastSeenAudioPlaylistEntryId;
	}

	public void setLastSeenAudioPlaylistEntryId(Integer lastSeenAudioPlaylistEntryId)
	{
		this.lastSeenAudioPlaylistEntryId = lastSeenAudioPlaylistEntryId;
	}
}
