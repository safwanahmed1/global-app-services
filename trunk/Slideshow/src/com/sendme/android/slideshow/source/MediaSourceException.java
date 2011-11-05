package com.sendme.android.slideshow.source;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 * @version 1.0
 */
public class MediaSourceException
extends Exception
{
	public MediaSourceException()
	{
	}

	public MediaSourceException(String msg)
	{
		super(msg);
	}

	public MediaSourceException(Throwable t)
	{
		super(t);
	}

	public MediaSourceException(String msg, Throwable t)
	{
		super(msg);

		initCause(t);
	}
}
