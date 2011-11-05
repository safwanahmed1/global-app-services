package com.sendme.android.slideshow.source;

import com.google.inject.Inject;
import com.sendme.android.slideshow.manager.AudioPlaylistManager;
import com.sendme.android.slideshow.manager.AudioTrackManager;
import com.sendme.android.slideshow.manager.ORMException;
import com.sendme.android.slideshow.manager.PersonManager;
import com.sendme.android.slideshow.manager.PhotoManager;
import com.sendme.android.slideshow.manager.SettingsManager;
import com.sendme.android.slideshow.manager.SlideshowManager;
import com.sendme.android.slideshow.model.AudioTrack;
import com.sendme.android.slideshow.model.Person;
import com.sendme.android.slideshow.model.Photo;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public abstract class MediaSource
{
	protected MediaSourceListener listener = null;

	@Inject
	protected SettingsManager settingsManager = null;

	@Inject
	protected PhotoManager photoManager = null;

	@Inject
	protected PersonManager personManager = null;

	@Inject
	protected SlideshowManager slideshowManager = null;

	@Inject
	protected AudioTrackManager audioTrackManager = null;

	@Inject
	protected AudioPlaylistManager audioPlaylistManager = null;

	protected boolean active = false;

	protected boolean updating = false;

	protected boolean declinedAuthentication = false;

	public abstract void checkForUpdates(Long time, boolean notify)
	throws MediaSourceException;

	protected boolean storePhoto(Long time, Photo photo)
	throws MediaSourceException
	{
		boolean output = false;

		try
		{
			Photo test = photoManager.findByExternalId(photo.getExternalId());

			if (test == null)
			{
				photo.setLastSeenInUpdate(time);

				photoManager.persist(photo);

				output = true;
			}
			else
			{
				test.setLastSeenInUpdate(time);

				test.setPerson(photo.getPerson());
				test.setPhotoSource(photo.getPhotoSource());
				test.setPhotoType(photo.getPhotoType());
				test.setText(photo.getText());
				test.setURI(photo.getURI());

				photoManager.persist(test);

				output = false;
			}
		}
		catch (ORMException e)
		{
			throw new MediaSourceException("Error storing image.", e);
		}

		return output;
	}

	protected boolean storePerson(Long time, Person person)
	throws MediaSourceException
	{
		boolean output = false;

		try
		{
			Person test = personManager.findByExternalId(person.getExternalId());

			if (test == null)
			{
				person.setLastSeenInUpdate(time);

				personManager.persist(person);

				output = true;
			}
			else
			{
				test.setLastSeenInUpdate(time);

				test.setMe(person.isMe());
				test.setName(person.getName());
				test.setPhotoSource(person.getPhotoSource());
				
				personManager.persist(test);

				output = false;
			}
		}
		catch (ORMException e)
		{
			throw new MediaSourceException("Error storing image.", e);
		}

		return output;
	}

	protected boolean storeAudioTrack(Long time, AudioTrack track)
	throws MediaSourceException
	{
		boolean output = false;

		try
		{
			AudioTrack test = audioTrackManager.findByExternalId(track.getExternalId());

			if (test == null)
			{
				track.setLastSeenInUpdate(time);

				audioTrackManager.persist(track);

				output = true;
			}
			else
			{
				test.setLastSeenInUpdate(time);

				test.setAlbum(track.getAlbum());
				test.setArtist(track.getArtist());
				test.setTrackNumber(track.getTrackNumber());
				test.setTrackTitle(track.getTrackTitle());
				test.setURI(track.getURI());
				test.setAlbumArtURI(track.getAlbumArtURI());

				audioTrackManager.persist(test);

				output = false;
			}
		}
		catch (ORMException e)
		{
			throw new MediaSourceException("Error storing audio track.", e);
		}

		return output;
	}

	public MediaSourceListener getListener()
	{
		return listener;
	}

	public void setListener(MediaSourceListener listener)
	{
		this.listener = listener;
	}

	public SettingsManager getSettingsManager()
	{
		return settingsManager;
	}

	public void setSettingsManager(SettingsManager settingsManager)
	{
		this.settingsManager = settingsManager;
	}

	public PersonManager getPersonManager()
	{
		return personManager;
	}

	public void setPersonManager(PersonManager personManager)
	{
		this.personManager = personManager;
	}

	public PhotoManager getPhotoManager()
	{
		return photoManager;
	}

	public void setPhotoManager(PhotoManager photoManager)
	{
		this.photoManager = photoManager;
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

	public AudioTrackManager getAudioTrackManager()
	{
		return audioTrackManager;
	}

	public void setAudioTrackManager(AudioTrackManager audioTrackManager)
	{
		this.audioTrackManager = audioTrackManager;
	}

	public boolean isActive()
	{
		return active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public boolean isUpdating()
	{
		return updating;
	}

	public void setUpdating(boolean updating)
	{
		this.updating = updating;
	}

	public boolean isDeclinedAuthentication()
	{
		return declinedAuthentication;
	}

	public void setDeclinedAuthentication(boolean declinedAuthentication)
	{
		this.declinedAuthentication = declinedAuthentication;
	}
}
