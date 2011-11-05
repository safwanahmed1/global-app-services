package com.sendme.android.slideshow.model;

import android.net.Uri;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Represents a Photo
 */
@DatabaseTable(tableName = "slideshow_audio_track")
public class AudioTrack
extends DataObject
{
	@DatabaseField
	private String externalId = null;

	@DatabaseField
	private String artist = null;

	@DatabaseField
	private String album = null;

	@DatabaseField
	private String trackNumber = null;

	@DatabaseField
	private String trackTitle = null;

	@DatabaseField
	private String uri = null;

	@DatabaseField
	private String albumArtURI = null;

	@DatabaseField(index = true)
	private Long lastSeenInUpdate = null;

	public AudioTrack()
	{
	}

	@Override
	public String toString()
	{
		return "AudioTrack{" + "externalId=" + externalId + ", artist=" + artist + ", album=" + album + ", trackNumber=" + trackNumber + ", trackTitle=" + trackTitle + ", uri=" + uri + ", lastSeenInUpdate=" + lastSeenInUpdate + "'}";
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

		final AudioTrack other = (AudioTrack) obj;
		
		if ((this.externalId == null) ? (other.externalId != null) : !this.externalId.equals(other.externalId))
		{
			return false;
		}
		if ((this.artist == null) ? (other.artist != null) : !this.artist.equals(other.artist))
		{
			return false;
		}
		if ((this.album == null) ? (other.album != null) : !this.album.equals(other.album))
		{
			return false;
		}
		if ((this.trackNumber == null) ? (other.trackNumber != null) : !this.trackNumber.equals(other.trackNumber))
		{
			return false;
		}
		if ((this.trackTitle == null) ? (other.trackTitle != null) : !this.trackTitle.equals(other.trackTitle))
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
		hash = 89 * hash + (this.artist != null ? this.artist.hashCode() : 0);
		hash = 89 * hash + (this.album != null ? this.album.hashCode() : 0);
		hash = 89 * hash + (this.trackNumber != null ? this.trackNumber.hashCode() : 0);
		hash = 89 * hash + (this.trackTitle != null ? this.trackTitle.hashCode() : 0);
		hash = 89 * hash + (this.uri != null ? this.uri.hashCode() : 0);
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

	public String getArtist()
	{
		return artist;
	}

	public void setArtist(String artist)
	{
		this.artist = artist;
	}

	public String getAlbum()
	{
		return album;
	}

	public void setAlbum(String album)
	{
		this.album = album;
	}

	public String getTrackNumber()
	{
		return trackNumber;
	}

	public void setTrackNumber(String trackNumber)
	{
		this.trackNumber = trackNumber;
	}

	public String getTrackTitle()
	{
		return trackTitle;
	}

	public void setTrackTitle(String trackTitle)
	{
		this.trackTitle = trackTitle;
	}

	public String getURI()
	{
		return uri;
	}

	public void setURI(String uri)
	{
		this.uri = uri;
	}

	public String getAlbumArtURI()
	{
		return albumArtURI;
	}

	public void setAlbumArtURI(String albumArtURI)
	{
		this.albumArtURI = albumArtURI;
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
