package com.sendme.android.slideshow.image;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 * @version 1.0
 */
public class ImageLoaderException
extends Exception
{
	public ImageLoaderException()
	{
	}

	public ImageLoaderException(String msg)
	{
		super(msg);
	}

	public ImageLoaderException(Throwable t)
	{
		super(t);
	}

	public ImageLoaderException(String msg, Throwable t)
	{
		super(msg);

		initCause(t);
	}
}
