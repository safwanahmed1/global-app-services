package com.sendme.android.slideshow.task.media;

import com.sendme.android.slideshow.source.MediaSource;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public interface MediaSourceUpdateTaskListener
{
	public abstract void onSourceUpdated(MediaSource source);

	public abstract void onSourceUpdateProgress(MediaSource source, Float progress);
}
