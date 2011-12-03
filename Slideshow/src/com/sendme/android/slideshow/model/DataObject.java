package com.sendme.android.slideshow.model;

import com.j256.ormlite.field.DatabaseField;
import java.io.Serializable;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public abstract class DataObject
implements Serializable
{
	@DatabaseField(generatedId = true, index=true)
	protected Integer id = null;

	@DatabaseField(index=true)
	protected Long createdOn = null;

	@DatabaseField(index=true)
	protected Long modifiedOn = null;

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public Long getCreatedOn()
	{
		return createdOn;
	}

	public void setCreatedOn(Long createdOn)
	{
		this.createdOn = createdOn;
	}

	public Long getModifiedOn()
	{
		return modifiedOn;
	}

	public void setModifiedOn(Long modifiedOn)
	{
		this.modifiedOn = modifiedOn;
	}
}
