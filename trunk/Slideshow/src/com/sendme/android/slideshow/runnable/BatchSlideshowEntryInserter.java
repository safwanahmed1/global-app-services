package com.sendme.android.slideshow.runnable;

import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.manager.SlideshowManager;
import com.sendme.android.slideshow.model.SlideshowEntry;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class BatchSlideshowEntryInserter
implements Callable<Integer>
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	private SlideshowManager slideshowManager = null;

	private List<Integer> photoIds = null;

	private int limit = 0;

	public BatchSlideshowEntryInserter()
	{
	}

	public Integer call()
	throws Exception
	{
		int order = 0;

		log.info("Persisting slideshow...");

		for (order = 0; order < limit; order++)
		{
			SlideshowEntry entry = new SlideshowEntry();

			entry.setPhotoId(photoIds.get(order));

			entry.setSlideshowOrder(order);

			slideshowManager.persist(entry);
		}

		return limit;
	}

	public SlideshowManager getSlideshowManager()
	{
		return slideshowManager;
	}

	public void setSlideshowManager(SlideshowManager slideshowManager)
	{
		this.slideshowManager = slideshowManager;
	}

	public int getLimit()
	{
		return limit;
	}

	public void setLimit(int limit)
	{
		this.limit = limit;
	}

	public List<Integer> getPhotoIds()
	{
		return photoIds;
	}

	public void setPhotoIds(List<Integer> photoIds)
	{
		this.photoIds = photoIds;
	}
}
