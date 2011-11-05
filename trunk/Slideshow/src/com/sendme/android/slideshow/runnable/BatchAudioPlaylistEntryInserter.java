package com.sendme.android.slideshow.runnable;

import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.manager.AudioPlaylistManager;
import com.sendme.android.slideshow.model.AudioPlaylistEntry;
import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class BatchAudioPlaylistEntryInserter
implements Callable<Integer>
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	private AudioPlaylistManager audioPlaylistManager = null;

	private List<Integer> audioTrackIds = null;

	private int limit = 0;

	public BatchAudioPlaylistEntryInserter()
	{
	}

	public Integer call()
	throws Exception
	{
		int order = 0;

		log.info("Persisting playlist...");

		for (order = 0; order < limit; order++)
		{
			AudioPlaylistEntry entry = new AudioPlaylistEntry();

			entry.setAudioTrackId(audioTrackIds.get(order));

			entry.setPlaylistOrder(order);

			audioPlaylistManager.persist(entry);
		}

		return limit;
	}

	public AudioPlaylistManager getAudioPlaylistManager()
	{
		return audioPlaylistManager;
	}

	public void setAudioPlaylistManager(AudioPlaylistManager audioPlaylistManager)
	{
		this.audioPlaylistManager = audioPlaylistManager;
	}

	public int getLimit()
	{
		return limit;
	}

	public void setLimit(int limit)
	{
		this.limit = limit;
	}

	public List<Integer> getAudioTrackIds()
	{
		return audioTrackIds;
	}

	public void setAudioTrackIds(List<Integer> audioTrackIds)
	{
		this.audioTrackIds = audioTrackIds;
	}
}
