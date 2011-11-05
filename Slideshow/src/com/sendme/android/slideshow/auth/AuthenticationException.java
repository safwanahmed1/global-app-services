package com.sendme.android.slideshow.auth;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 * @version 1.0
 */
public class AuthenticationException
extends Exception
{
	public AuthenticationException()
	{
	}

	public AuthenticationException(String msg)
	{
		super(msg);
	}

	public AuthenticationException(Throwable t)
	{
		super(t);
	}

	public AuthenticationException(String msg, Throwable t)
	{
		super(msg);

		initCause(t);
	}
}
