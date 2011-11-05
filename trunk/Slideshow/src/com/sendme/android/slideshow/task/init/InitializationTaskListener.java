package com.sendme.android.slideshow.task.init;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public interface InitializationTaskListener
{
	public abstract void onInitializationStatusUpdate(InitializationEventType status);
}
