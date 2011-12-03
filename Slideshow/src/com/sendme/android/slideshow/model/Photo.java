package com.sendme.android.slideshow.model;

import android.net.Uri;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Represents a Photo
 */
@DatabaseTable(tableName = "slideshow_photo")
public class Photo
extends DataObject
{
	@DatabaseField(index = true)
	private String externalId = null;

	@DatabaseField(index = true)
	private Integer person = null;

	@DatabaseField(index = true)
	private PhotoType photoType = null;

	@DatabaseField(index = true)
	private PhotoSource photoSource = null;

	@DatabaseField
	private String uri = null;

	@DatabaseField
	private String text = null;

	@DatabaseField
	private Long lastSeenInUpdate = null;

	public Photo()
	{
	}

	@Override
	public String toString()
	{
		return "Photo{" + "externalId=" + externalId + ", person=" + person + ", photoType=" + photoType + ", photoSource=" + photoSource + ", uri=" + uri + ", text=" + text + ", lastSeenInUpdate=" + lastSeenInUpdate + "}";
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

		final Photo other = (Photo) obj;

		if ((this.externalId == null) ? (other.externalId != null) : !this.externalId.equals(other.externalId))
		{
			return false;
		}

		if (this.person != other.person && (this.person == null || !this.person.equals(other.person)))
		{
			return false;
		}

		if (this.photoType != other.photoType)
		{
			return false;
		}

		if (this.photoSource != other.photoSource)
		{
			return false;
		}

		if (this.uri != other.uri && (this.uri == null || !this.uri.equals(other.uri)))
		{
			return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = super.hashCode();

		hash = 67 * hash + (this.externalId != null ? this.externalId.hashCode() : 0);
		hash = 67 * hash + (this.person != null ? this.person.hashCode() : 0);
		hash = 67 * hash + (this.photoType != null ? this.photoType.hashCode() : 0);
		hash = 67 * hash + (this.photoSource != null ? this.photoSource.hashCode() : 0);
		hash = 67 * hash + (this.uri != null ? this.uri.hashCode() : 0);
		
		return hash;
	}

	public String getExternalId()
	{
		return externalId;
	}

	public void setExternalId(String externalId)
	{
		this.externalId = externalId;
	}

	public Integer getPerson()
	{
		return person;
	}

	public void setPerson(Integer person)
	{
		this.person = person;
	}

	public PhotoType getPhotoType()
	{
		return photoType;
	}

	public void setPhotoType(PhotoType type)
	{
		this.photoType = type;
	}

	public PhotoSource getPhotoSource()
	{
		return photoSource;
	}

	public void setPhotoSource(PhotoSource source)
	{
		this.photoSource = source;
	}

	public String getURI()
	{
		return uri;
	}

	public void setURI(String uri)
	{
		this.uri = uri;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public Long getLastSeenInUpdate()
	{
		return lastSeenInUpdate;
	}

	public void setLastSeenInUpdate(Long lastSeenInUpdate)
	{
		this.lastSeenInUpdate = lastSeenInUpdate;
	}
}
