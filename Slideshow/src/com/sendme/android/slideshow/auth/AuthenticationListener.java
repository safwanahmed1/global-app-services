package com.sendme.android.slideshow.auth;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public interface AuthenticationListener
{
	public abstract void onAuthenticationComplete(int requestCode);

	public abstract void onAuthenticationCancelled(int requestCode);

	public abstract void onAuthenticationError(int requestCode, Throwable t);
}
