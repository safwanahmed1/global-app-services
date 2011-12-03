package com.sendme.android.slideshow.source.impl;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.manager.ORMException;
import com.sendme.android.slideshow.model.Photo;
import com.sendme.android.slideshow.model.PhotoSource;
import com.sendme.android.slideshow.model.PhotoType;
import com.sendme.android.slideshow.source.MediaSource;
import com.sendme.android.slideshow.source.MediaSourceException;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
@Singleton
public class LocalImageSource
extends MediaSource
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	@Inject
	private ContentResolver contentResolver = null;

	public LocalImageSource()
	{
	}

	@Override
	public void checkForUpdates(Long time, boolean notify)
	throws MediaSourceException
	{
		log.debug("LocalImageSource asked to check for updates: " + time);

		try
		{
			updating = true;

			log.debug("LocalImageSource update required.");

			int updateCount = 0;

			String projection[] = new String[]
			{
				Media._ID,
				Media.TITLE
			};

			Cursor cursor = null;

			try
			{
				Media.query(contentResolver, Media.INTERNAL_CONTENT_URI, projection);

				if (cursor != null)
				{
					updateCount += storePhotos(time, cursor);
				}
				else
				{
					log.debug("Local interal media store is empty.");
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
				cursor = Media.query(contentResolver, Media.EXTERNAL_CONTENT_URI, projection);

				if (cursor != null)
				{
					updateCount += storePhotos(time, cursor);
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

			photoManager.deleteExpired(time, PhotoSource.LOCAL);

			log.debug("LocalImageSource detected: " + updateCount + " new image(s).");

			if (updateCount > 0 && notify)
			{
				// TODO:  We probably should update the slideshow
			}
		}
		catch (ORMException e)
		{
			throw new MediaSourceException("Cannot update photos for local image source", e);
		}
		finally
		{
			updating = false;

			settingsManager.setLocalImagesLastUpdateTime(time);
		}
	}

	private int storePhotos(Long time, Cursor cursor)
	throws MediaSourceException
	{
		int output = 0;

		if (cursor != null)
		{
			boolean loop = cursor.moveToFirst();

			while (loop)
			{
				String id = cursor.getString(0);
				String title = cursor.getString(1);

				Photo photo = new Photo();

				photo.setExternalId(id);
				photo.setPhotoSource(PhotoSource.LOCAL);
				photo.setPhotoType(PhotoType.PERSONAL_ALBUM);
				photo.setURI(Uri.withAppendedPath(Media.EXTERNAL_CONTENT_URI, id).toString());
				photo.setText(title);
				photo.setLastSeenInUpdate(time);

				storePhoto(time, photo);
				try {
					slideshowManager.addPhotoToSlideshow(photo);
				} catch (ORMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				output++;

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
