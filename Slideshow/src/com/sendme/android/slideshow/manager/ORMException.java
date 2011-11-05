package com.sendme.android.slideshow.manager;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 * @version 1.0
 */
public class ORMException
extends Exception
{
	public ORMException()
	{
	}

	public ORMException(String msg)
	{
		super(msg);
	}

	public ORMException(Throwable t)
	{
		super(t);
	}

	public ORMException(String msg, Throwable t)
	{
		super(msg);

		initCause(t);
	}
}
