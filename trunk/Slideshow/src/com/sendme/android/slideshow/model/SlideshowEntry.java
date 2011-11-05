package com.sendme.android.slideshow.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
@DatabaseTable(tableName = "slideshow_entry")
public class SlideshowEntry
extends DataObject
{
	@DatabaseField(index=true)
	private Integer slideshowOrder = null;

	@DatabaseField
	private Integer photoId = null;

	public SlideshowEntry()
	{
	}

	@Override
	public String toString()
	{
		return "SlideshowEntry{" + "id=" + id + ", slideshowOrder=" + slideshowOrder + ", photoId=" + photoId + ", createdOn=" + createdOn + ", modifiedOn=" + modifiedOn + "}";
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

		if (!super.equals(obj))
		{
			return false;
		}

		final SlideshowEntry other = (SlideshowEntry) obj;

		if (this.slideshowOrder != other.slideshowOrder && (this.slideshowOrder == null || !this.slideshowOrder.equals(other.slideshowOrder)))
		{
			return false;
		}

		if (this.photoId != other.photoId && (this.photoId == null || !this.photoId.equals(other.photoId)))
		{
			return false;
		}
		
		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = super.hashCode();

		hash = 79 * hash + (this.slideshowOrder != null ? this.slideshowOrder.hashCode() : 0);

		hash = 79 * hash + (this.photoId != null ? this.photoId.hashCode() : 0);
		
		return hash;
	}

	public Integer getSlideshowOrder()
	{
		return slideshowOrder;
	}

	public void setSlideshowOrder(Integer slideshowOrder)
	{
		this.slideshowOrder = slideshowOrder;
	}

	public Integer getPhotoId()
	{
		return photoId;
	}

	public void setPhotoId(Integer photoId)
	{
		this.photoId = photoId;
	}
}
