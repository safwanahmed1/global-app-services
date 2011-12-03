package com.sendme.android.slideshow.image;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public interface BackgroundImageLoaderListener
{
	public abstract void onBackgroundImageLoadingSuccess(LoadedImage image);

	public abstract void onBackgroundImageLoadingFailure(ImageLoaderException e);
}
