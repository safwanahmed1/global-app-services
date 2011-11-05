package com.sendme.android.slideshow.source.impl;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.manager.ORMException;
import com.sendme.android.slideshow.model.AudioTrack;
import com.sendme.android.slideshow.source.MediaSource;
import com.sendme.android.slideshow.source.MediaSourceException;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
@Singleton
public class LocalAudioTrackSource
extends MediaSource
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	private final static Uri BASE_ALBUM_ART_URI = Uri.parse("content://media/external/audio/albumart");

	@Inject
	private ContentResolver contentResolver = null;

	public LocalAudioTrackSource()
	{
	}

	@Override
	public void checkForUpdates(Long time, boolean notify)
	throws MediaSourceException
	{
		log.debug("LocalAudioTrackSource asked to check for updates: " + time);

		try
		{
			updating = true;

			int updateCount = 0;

			String[] projection = new String[]
			{
				Media._ID,
				Media.ARTIST,
				Media.ALBUM,
				Media.ALBUM_ID,
				Media.TRACK,
				Media.TITLE,
				Media.DURATION,
				Media.IS_MUSIC
			};

			Cursor cursor = null;

			try
			{
				cursor = contentResolver.query(Media.INTERNAL_CONTENT_URI, projection, null, null, null);

				if (cursor != null)
				{
					updateCount += storeAudioTracks(cursor, time);
				}
				else
				{
					log.debug("Local external media store is empty.");
				}
			}
			finally
			{
				if (cursor != null && !cursor.isClosed())
				{
					cursor.close();
				}
			}

			try
			{
				cursor = contentResolver.query(Media.EXTERNAL_CONTENT_URI, projection, null, null, null);

				if (cursor != null)
				{
					updateCount += storeAudioTracks(cursor, time);
				}
				else
				{
					log.debug("Local external media store is empty.");
				}
			}
			finally
			{
				if (cursor != null && !cursor.isClosed())
				{
					cursor.close();
				}
			}

			audioTrackManager.deleteExpired(time);

			log.debug("LocalAudioTrackSource detected: " + updateCount + " new track(s).");

			if (updateCount > 0 && notify)
			{
				// TODO:  We need to update the playlist, maybe?
			}
		}
		catch (ORMException e)
		{
			throw new MediaSourceException("Cannot update tracks for local audio source", e);
		}
		finally
		{
			updating = false;

			settingsManager.setAudioTracksLastUpdateTime(time);
		}
	}

	private int storeAudioTracks(Cursor cursor, Long time)
	throws MediaSourceException
	{
		int output = 0;

		if (cursor != null)
		{
			boolean loop = cursor.moveToFirst();

			while (loop)
			{
				String id = cursor.getString(0);
				String artist = cursor.getString(1);
				String album = cursor.getString(2);
				String albumId = cursor.getString(3);
				String trackNumber = cursor.getString(4);
				String title = cursor.getString(5);

				int trackLength = cursor.getInt(6);
				int isMusic = cursor.getInt(7);

				if (isMusic > 0 && trackLength > 15000)
				{
					// This should make sure that we are seeing only music.

					AudioTrack track = new AudioTrack();

					// Little bit of cleanup.  Android encodes disc number and track number
					// together
					if (trackNumber != null && trackNumber.length() > 2)
					{
						trackNumber = trackNumber.substring(trackNumber.length() - 2);
					}

					track.setExternalId(id);
					track.setArtist(artist);
					track.setAlbum(album);
					track.setTrackNumber(trackNumber);
					track.setTrackTitle(title);
					track.setURI(Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, id).toString());
					track.setLastSeenInUpdate(time);

					if (albumId != null)
					{
						track.setAlbumArtURI(Uri.withAppendedPath(BASE_ALBUM_ART_URI, albumId).toString());
					}

					storeAudioTrack(time, track);

					output++;
				}

				loop = cursor.moveToNext();
			}
		}

		return output;
	}

	public ContentResolver getContentResolver()
	{
		return contentResolver;
	}

	public void setContentResolver(ContentResolver contentResolver)
	{
		this.contentResolver = contentResolver;
	}
}
