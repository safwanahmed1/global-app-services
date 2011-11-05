package com.sendme.android.slideshow.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
@DatabaseTable(tableName = "slideshow_audio_playlist_entry")
public class AudioPlaylistEntry
extends DataObject
{
	@DatabaseField(index = true)
	private Integer playlistOrder = null;

	@DatabaseField
	private Integer audioTrackId = null;

	public AudioPlaylistEntry()
	{
	}

	@Override
	public String toString()
	{
		return "AudioPlaylistEntry{" + "playlistOrder=" + playlistOrder + ", audioTrackId=" + audioTrackId + "}";
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
		final AudioPlaylistEntry other = (AudioPlaylistEntry) obj;

		if (!super.equals(other))
		{
			return false;
		}

		if (this.playlistOrder != other.playlistOrder && (this.playlistOrder == null || !this.playlistOrder.equals(other.playlistOrder)))
		{
			return false;
		}
		if (this.audioTrackId != other.audioTrackId && (this.audioTrackId == null || !this.audioTrackId.equals(other.audioTrackId)))
		{
			return false;
		}
		return true;
	}

	@Override
	public int hashCode()
	{
		int hash = super.hashCode();
		hash = 97 * hash + (this.playlistOrder != null ? this.playlistOrder.hashCode() : 0);
		hash = 97 * hash + (this.audioTrackId != null ? this.audioTrackId.hashCode() : 0);
		return hash;
	}

	public Integer getPlaylistOrder()
	{
		return playlistOrder;
	}

	public void setPlaylistOrder(Integer playlistOrder)
	{
		this.playlistOrder = playlistOrder;
	}

	public Integer getAudioTrackId()
	{
		return audioTrackId;
	}

	public void setAudioTrackId(Integer audioTrackId)
	{
		this.audioTrackId = audioTrackId;
	}
}
