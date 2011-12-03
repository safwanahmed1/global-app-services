package com.sendme.android.slideshow.controller;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import com.google.inject.Inject;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.manager.AudioPlaylistManager;
import com.sendme.android.slideshow.manager.AudioTrackManager;
import com.sendme.android.slideshow.manager.ORMException;
import com.sendme.android.slideshow.manager.SettingsManager;
import com.sendme.android.slideshow.model.AudioPlaylistEntry;
import com.sendme.android.slideshow.model.AudioTrack;
import com.sendme.android.slideshow.task.heartbeat.HeartbeatTask;
import com.sendme.android.slideshow.task.heartbeat.HeartbeatTaskEventListener;
import java.io.IOException;

/**
 * 
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class AudioPlaybackController extends Controller implements
		OnCompletionListener, HeartbeatTaskEventListener, UIEventListener {
	private final static AndroidLogger log = AndroidLogger
			.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	@Inject
	private SettingsManager settingsManager = null;

	@Inject
	private AudioPlaylistManager audioPlaylistManager = null;

	@Inject
	private AudioTrackManager audioTrackManager = null;

	private MediaPlayer mediaPlayer = null;

	private Integer heartbeatIdentifier = null;

	private Integer uiEventListenerIdentifier = null;

	public AudioPlaybackController() {
	}

	@Override
	public void initialize() {
		log.info("AudioPlaybackController initialized.");
	}

	@Override
	public void start() {
		log.info("AudioPlaybackController started.");
	}

	@Override
	public void resume() {
		UIController.addListener(this);

		HeartbeatTask.addListener(this);

		log.debug("AudioPlaybackController resumed.");
	}

	@Override
	public void pause() {
		HeartbeatTask.removeListener(this);

		UIController.removeListener(this);

		log.debug("AudioPlaybackController paused.");
	}

	@Override
	public void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();

			mediaPlayer.release();
		}

		mediaPlayer = null;

		log.info("AudioPlaybackController stopped.");
	}

	@Override
	public void destroy() {
		log.info("AudioPlaybackController destroyed.");
	}

	public void onCompletion(MediaPlayer mp) {
		playNextAudioTrack();
	}

	public void onHeartbeatTaskEvent(Long time) {
		if (mediaPlayer == null) {
			playNextAudioTrack();
		}
	}

	public void onUIEvent(UIEvent event) {
		switch (event) {
		case NEXT_TRACK: {
			playNextAudioTrack();

			break;
		}

		case PAUSE:
		case PLAY: {
			toggleMusic();

			break;
		}

		case PREVIOUS_TRACK: {
			playPreviousAudioTrack();

			break;
		}
		}
	}

	public void playMusic() {
		if ((mediaPlayer != null) && (!mediaPlayer.isPlaying())) {
			mediaPlayer.start();
		}
	}

	public void pauseMusic() {
		if ((mediaPlayer != null) && (mediaPlayer.isPlaying())) {
			mediaPlayer.pause();
		}
	}

	public void toggleMusic() {
		if ((mediaPlayer != null) && (mediaPlayer.isPlaying())) {
			pauseMusic();
		} else {
			playMusic();
		}
	}

	public void playCurrentAudioPlaylistEntry() {
		Integer currentId = settingsManager.getCurrentAudioPlaylistEntryId();

		try {
			AudioPlaylistEntry entry = null;

			if (currentId == null) {
				entry = audioPlaylistManager.getNextEntry(currentId);
			} else {
				entry = audioPlaylistManager.find(currentId);
			}

			if (entry != null) {
				AudioTrack track = audioTrackManager.find(entry
						.getAudioTrackId());

				if (mediaPlayer != null) {
					mediaPlayer.stop();

					mediaPlayer.reset();
				} else {
					mediaPlayer = new MediaPlayer();

					mediaPlayer.setOnCompletionListener(this);
				}

				mediaPlayer.setDataSource(ass, Uri.parse(track.getURI()));

				mediaPlayer.prepare();

				mediaPlayer.start();
			} else {
				log.debug("No entry found...");
			}
		} catch (ORMException e) {
			log.error("Could not play audio track: " + currentId, e);

			try {
				if (mediaPlayer != null) {
					mediaPlayer.release();
				}
			} catch (Throwable e2) {
			}

			mediaPlayer = null;
		} catch (IOException e) {
			log.error("Exception switching track.", e);

			try {
				if (mediaPlayer != null) {
					mediaPlayer.release();
				}
			} catch (Throwable e2) {
			}

			mediaPlayer = null;
		} catch (IllegalStateException e) {
			log.error("Exception switching track.", e);

			try {
				if (mediaPlayer != null) {
					mediaPlayer.release();
				}
			} catch (Throwable e2) {
			}

			mediaPlayer = null;
		} catch (Throwable e) {
			// This is a last resort, because it isn't always picking up some
			// exceptions
			log.error("Exception switching track.", e);

			try {
				if (mediaPlayer != null) {
					mediaPlayer.release();
				}
			} catch (Throwable e2) {
			}

			mediaPlayer = null;
		}
	}

	public void playNextAudioTrack() {
		// Ok, first we need to figure out which track we're going to play.
		// Let's ask
		// the handy-dandy playlist manager.
		try {
			Integer currentId = settingsManager
					.getCurrentAudioPlaylistEntryId();

			AudioPlaylistEntry nextEntry = audioPlaylistManager
					.getNextEntry(currentId);

			log.info("About to play next song: " + nextEntry + " -- "
					+ currentId);

			if (nextEntry != null && !nextEntry.getId().equals(currentId)) {
				settingsManager.setCurrentAudioPlaylistEntryId(nextEntry
						.getId());

				playCurrentAudioPlaylistEntry();
			} else {
				log.debug("No audio playlist entry returned???");
			}
		} catch (ORMException e) {
			log.error("Error showing next photo", e);
		}
	}

	public void playPreviousAudioTrack() {
		// Ok, first we need to figure out which track we're going to play.
		// Let's ask
		// the handy-dandy playlist manager.
		try {
			Integer currentId = settingsManager
					.getCurrentAudioPlaylistEntryId();

			AudioPlaylistEntry previousEntry = audioPlaylistManager
					.getPreviousEntry(currentId);

			log.info("About to play previous song: " + previousEntry + " -- "
					+ currentId);

			if (previousEntry != null
					&& !previousEntry.getId().equals(currentId)) {
				settingsManager.setCurrentAudioPlaylistEntryId(previousEntry
						.getId());

				playCurrentAudioPlaylistEntry();
			} else {
				log.debug("No audio playlist entry returned???");
			}
		} catch (ORMException e) {
			log.error("Error showing next photo", e);
		}
	}

	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public void setSettingsManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}

	public AudioPlaylistManager getAudioPlaylistManager() {
		return audioPlaylistManager;
	}

	public void setAudioPlaylistManager(
			AudioPlaylistManager audioPlaylistManager) {
		this.audioPlaylistManager = audioPlaylistManager;
	}

	public AudioTrackManager getAudioTrackManager() {
		return audioTrackManager;
	}

	public void setAudioTrackManager(AudioTrackManager audioTrackManager) {
		this.audioTrackManager = audioTrackManager;
	}

	public Integer getHeartbeatIdentifier() {
		return heartbeatIdentifier;
	}

	public void setHeartbeatIdentifier(Integer heartbeatIdentifier) {
		this.heartbeatIdentifier = heartbeatIdentifier;
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public void setMediaPlayer(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}

	public Integer getUIEventListenerIdentifier() {
		return uiEventListenerIdentifier;
	}

	public void setUIEventListenerIdentifier(Integer uiEventListenerIdentifier) {
		this.uiEventListenerIdentifier = uiEventListenerIdentifier;
	}
}
