package com.sendme.android.slideshow.controller;

import com.google.inject.Inject;
import com.sendme.android.slideshow.AndroidSlideshow;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public abstract class Controller
{
	@Inject
	protected AndroidSlideshow ass = null;

	public abstract void initialize();

	public abstract void start();

	public abstract void resume();

	public abstract void pause();

	public abstract void stop();

	public abstract void destroy();

	public AndroidSlideshow getAndroidSlideshow()
	{
		return ass;
	}

	public void setAndroidSlideshow(AndroidSlideshow ass)
	{
		this.ass = ass;
	}
}
