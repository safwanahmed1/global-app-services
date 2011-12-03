package com.sendme.android.slideshow.share;
/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public interface FacebookShareTaskListener
{
	public abstract void onFacebookeShareingSuccess();

	public abstract void onFacebookeShareingFail(String error);
}
