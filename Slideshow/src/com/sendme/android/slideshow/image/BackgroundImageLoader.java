package com.sendme.android.slideshow.image;

import android.net.Uri;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class BackgroundImageLoader
implements Runnable
{
	private BackgroundImageLoaderListener listener = null;

	private ImageLoader imageLoader = null;

	private ImageType type = null;
	
	private Uri uri = null;

	private String text = null;

	private int targetWidth = 0;

	private int targetHeight = 0;
	
	public BackgroundImageLoader()
	{

	}

	public void run()
	{
		try
		{
			LoadedImage image = imageLoader.loadImage(type, uri, text, targetWidth, targetHeight);

			if (image != null)
			{
				listener.onBackgroundImageLoadingSuccess(image);
			}
			else
			{
				listener.onBackgroundImageLoadingFailure(new ImageLoaderException("No image returned from image loader."));
			}
		}
		catch (ImageLoaderException e)
		{
			listener.onBackgroundImageLoadingFailure(e);
		}
	}

	public ImageLoader getImageLoader()
	{
		return imageLoader;
	}

	public void setImageLoader(ImageLoader imageLoader)
	{
		this.imageLoader = imageLoader;
	}

	public BackgroundImageLoaderListener getListener()
	{
		return listener;
	}

	public void setListener(BackgroundImageLoaderListener listener)
	{
		this.listener = listener;
	}

	public ImageType getType()
	{
		return type;
	}

	public void setType(ImageType type)
	{
		this.type = type;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public int getTargetHeight()
	{
		return targetHeight;
	}

	public void setTargetHeight(int targetHeight)
	{
		this.targetHeight = targetHeight;
	}

	public int getTargetWidth()
	{
		return targetWidth;
	}

	public void setTargetWidth(int targetWidth)
	{
		this.targetWidth = targetWidth;
	}

	public Uri getURI()
	{
		return uri;
	}

	public void setURI(Uri uri)
	{
		this.uri = uri;
	}
}
