package com.sendme.android.slideshow.task.media;

import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.source.MediaSource;

import android.os.AsyncTask;
import com.sendme.android.slideshow.source.MediaSourceException;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class MediaSourceUpdateTask
extends AsyncTask<Void, Float, Void>
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	private MediaSource imageSource = null;

	public MediaSourceUpdateTask()
	{

	}

	@Override
	protected Void doInBackground(Void... params)
	{
		try
		{
			imageSource.checkForUpdates(System.currentTimeMillis(), true);
		}
		catch (MediaSourceException e)
		{
			log.error("Error updating image source", e);
		}
		
		return null;
	}

	public MediaSource getImageSource()
	{
		return imageSource;
	}

	public void setImageSource(MediaSource imageSource)
	{
		this.imageSource = imageSource;
	}
}
