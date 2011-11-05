package com.sendme.android.slideshow.manager;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.model.AudioPlaylistEntry;
import com.sendme.android.slideshow.model.AudioTrack;
import com.sendme.android.slideshow.runnable.BatchAudioPlaylistEntryInserter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
@Singleton
public class AudioPlaylistManager
extends ObjectManager<AudioPlaylistEntry>
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	@Inject
	private AndroidSlideshow ass = null;

	@Inject
	private AudioTrackManager audioTrackManager = null;

	public AudioPlaylistManager()
	{
	}

	@Override
	public Class<AudioPlaylistEntry> getManagedClass()
	{
		return AudioPlaylistEntry.class;
	}

	/**
	 * Randomizes a playlist based on all audio tracks in the database that
	 * are eligible based on the current settings.
	 * 
	 * @throws SQLException
	 */
	public void shufflePlaylist()
	throws ORMException
	{
		try
		{
			settingsManager.removeCurrentAudioPlaylistEntryId();
			
			TableUtils.clearTable(databaseManager.getConnectionSource(), AudioPlaylistEntry.class);

			List<Integer> ids = audioTrackManager.getAllIds();

			log.info("Shuffling ids..");

			Collections.shuffle(ids);

			log.info("IDs shuffled..");

			int limit = settingsManager.getSlideshowSize();

			if (limit == 0 || limit > ids.size())
			{
				limit = ids.size();
			}

			BatchAudioPlaylistEntryInserter batcher = new BatchAudioPlaylistEntryInserter();

			batcher.setAudioPlaylistManager(this);
			batcher.setAudioTrackIds(ids);
			batcher.setLimit(limit);

			Dao<AudioPlaylistEntry, Integer> dao = getDAO();

			dao.callBatchTasks(batcher);

			log.info("Playlist persisted.");
		}
		catch (SQLException e)
		{
			throw new ORMException("Error creating playlist.", e);
		}
		catch (Exception e)
		{
			throw new ORMException("Error creating playlist.", e);
		}
	}

	/**
	 * We're going to take the given track and add it to the current playlist.
	 *
	 * This is going to create a bit of psuedo-randomness, since we're just going
	 * to be tacking it onto the end of the list.  However, since we get tracks in
	 * fairly odd orders during updates, the user shouldn't notice, and we'll get
	 * true randomness every few hours or so and every time the app starts.
	 * 
	 * @param photo
	 */
	public void addAudioTrackToPlaylist(AudioTrack track)
	throws ORMException
	{
		Dao<AudioPlaylistEntry, Integer> dao = getDAO();

		try
		{
			// First, we need to find the max "order" attribute of photos
			QueryBuilder<AudioPlaylistEntry, Integer> qb = dao.queryBuilder();

			qb.limit(1);

			qb.orderBy("playlistOrder", false);

			AudioPlaylistEntry maxEntry = dao.queryForFirst(qb.prepare());

			int newOrder = 0;

			if (maxEntry != null)
			{
				newOrder = maxEntry.getPlaylistOrder();

				newOrder++;
			}

			AudioPlaylistEntry entry = new AudioPlaylistEntry();

			entry.setAudioTrackId(track.getId());

			entry.setPlaylistOrder(newOrder);

			persist(entry);

			log.info("Added " + newOrder + " entries into database.");
		}
		catch (SQLException e)
		{
			throw new ORMException("Error adding entry to playlist.", e);
		}
	}

	public AudioPlaylistEntry getNextEntry(Integer currentEntryId)
	throws ORMException
	{
		AudioPlaylistEntry output = null;

		Dao<AudioPlaylistEntry, Integer> dao = getDAO();

		try
		{
			if (currentEntryId != null)
			{
				AudioPlaylistEntry currentEntry = find(currentEntryId);

				QueryBuilder<AudioPlaylistEntry, Integer> qb = dao.queryBuilder();

				qb.limit(1);

				PreparedQuery<AudioPlaylistEntry> pq = null;

				Where where = qb.where();

				where.gt("playlistOrder", currentEntry.getPlaylistOrder());

				qb.orderBy("playlistOrder", true);

				pq = qb.prepare();

				log.debug("Executing: " + qb.prepareStatementString());

				output = dao.queryForFirst(pq);
			}

			if (output == null)
			{
				// There are no entries with order greater than the current one
				// lets get the first one
				QueryBuilder<AudioPlaylistEntry, Integer> qb = dao.queryBuilder();

				qb.limit(1);

				PreparedQuery<AudioPlaylistEntry> pq = null;

				qb = dao.queryBuilder();

				qb.orderBy("playlistOrder", true);

				log.debug("Executing: " + qb.prepareStatementString());

				pq = qb.prepare();

				output = dao.queryForFirst(pq);
			}
		}
		catch (SQLException e)
		{
			throw new ORMException("Error getting next playlist entry.", e);
		}

		return output;
	}

	public AudioPlaylistEntry getPreviousEntry(Integer currentEntryId)
	throws ORMException
	{
		AudioPlaylistEntry output = null;

		Dao<AudioPlaylistEntry, Integer> dao = getDAO();

		try
		{
			if (currentEntryId != null)
			{
				AudioPlaylistEntry currentEntry = find(currentEntryId);

				QueryBuilder<AudioPlaylistEntry, Integer> qb = dao.queryBuilder();

				qb.limit(1);

				PreparedQuery<AudioPlaylistEntry> pq = null;

				Where where = qb.where();

				where.lt("playlistOrder", currentEntry.getPlaylistOrder());

				qb.orderBy("playlistOrder", false);

				log.debug("Executing: " + qb.prepareStatementString());

				pq = qb.prepare();

				output = dao.queryForFirst(pq);
			}

			if (output == null)
			{
				// There are no entries with order greater than the current one
				// lets get the first one
				QueryBuilder<AudioPlaylistEntry, Integer> qb = dao.queryBuilder();

				qb.limit(1);

				PreparedQuery<AudioPlaylistEntry> pq = null;

				qb.orderBy("playlistOrder", false);

				log.debug("Executing: " + qb.prepareStatementString());

				pq = qb.prepare();

				output = dao.queryForFirst(pq);
			}
		}
		catch (SQLException e)
		{
			throw new ORMException("Error getting previous playlist entry.", e);
		}

		return output;
	}

	public AndroidSlideshow getAndroidSlideshow()
	{
		return ass;
	}

	public void setAndroidSlideshow(AndroidSlideshow ass)
	{
		this.ass = ass;
	}

	public AudioTrackManager getAudioTrackManager()
	{
		return audioTrackManager;
	}

	public void setAudioTrackManager(AudioTrackManager audioTrackManager)
	{
		this.audioTrackManager = audioTrackManager;
	}
}
