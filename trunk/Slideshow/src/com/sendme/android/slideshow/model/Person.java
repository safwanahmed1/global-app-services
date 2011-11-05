package com.sendme.android.slideshow.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Represents a Person
 */
@DatabaseTable(tableName = "slideshow_person")
public class Person
extends DataObject
{
	@DatabaseField(index=true)
	private String externalId = null;

	@DatabaseField(index=true)
	private PhotoSource photoSource = null;

	@DatabaseField
	private String name = null;

	@DatabaseField
	private boolean me = false;

	@DatabaseField
	private Long lastSeenInUpdate = null;

	public Person()
	{
	}

	@Override
	public String toString()
	{
		return "Person{" + "externalId=" + externalId + ", photoSource=" + photoSource + ", name=" + name + ", me=" + me + ", lastSeenInUpdate=" + lastSeenInUpdate + "}";
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

		final Person other = (Person) obj;

		if ((this.externalId == null) ? (other.externalId != null) : !this.externalId.equals(other.externalId))
		{
			return false;
		}
		if (this.photoSource != other.photoSource)
		{
			return false;
		}
		if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name))
		{
			return false;
		}
		if (this.me != other.me)
		{
			return false;
		}
		if (this.lastSeenInUpdate != other.lastSeenInUpdate && (this.lastSeenInUpdate == null || !this.lastSeenInUpdate.equals(other.lastSeenInUpdate)))
		{
			return false;
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = super.hashCode();
		hash = 89 * hash + (this.externalId != null ? this.externalId.hashCode() : 0);
		hash = 89 * hash + (this.photoSource != null ? this.photoSource.hashCode() : 0);
		hash = 89 * hash + (this.name != null ? this.name.hashCode() : 0);
		hash = 89 * hash + (this.me ? 1 : 0);
		hash = 89 * hash + (this.lastSeenInUpdate != null ? this.lastSeenInUpdate.hashCode() : 0);
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

	public PhotoSource getPhotoSource()
	{
		return photoSource;
	}

	public void setPhotoSource(PhotoSource photoSource)
	{
		this.photoSource = photoSource;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean isMe()
	{
		return me;
	}

	public void setMe(boolean me)
	{
		this.me = me;
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