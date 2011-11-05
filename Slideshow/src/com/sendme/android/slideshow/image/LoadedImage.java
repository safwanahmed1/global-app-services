package com.sendme.android.slideshow.image;

import android.graphics.Bitmap;
import android.net.Uri;
import java.io.Serializable;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class LoadedImage
implements Serializable
{
	private Uri uri = null;

	private Bitmap bitmap = null;

	private String text = null;

	private Long addedToCache = null;

	public LoadedImage()
	{
	}

	@Override
	public String toString()
	{
		return "CachedImage{" + "uri=" + uri + ", text=" + text + ", addedToCache=" + addedToCache + '}';
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		final LoadedImage other = (LoadedImage) obj;
		if ((this.uri == null) ? (other.uri != null) : !this.uri.equals(other.uri))
		{
			return false;
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 71 * hash + (this.uri != null ? this.uri.hashCode() : 0);
		return hash;
	}

	public Uri getURI()
	{
		return uri;
	}

	public void setURI(Uri uri)
	{
		this.uri = uri;
	}

	public Bitmap getBitmap()
	{
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap)
	{
		this.bitmap = bitmap;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public Long getAddedToCache()
	{
		return addedToCache;
	}

	public void setAddedToCache(Long addedToCache)
	{
		this.addedToCache = addedToCache;
	}
}
