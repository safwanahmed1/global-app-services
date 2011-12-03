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
import com.sendme.android.slideshow.model.Photo;
import com.sendme.android.slideshow.model.SlideshowEntry;
import com.sendme.android.slideshow.runnable.BatchSlideshowEntryInserter;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
@Singleton
public class SlideshowManager
extends ObjectManager<SlideshowEntry>
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	@Inject
	private AndroidSlideshow ass = null;

	@Inject
	private PhotoManager photoManager = null;

	public SlideshowManager()
	{
	}

	@Override
	public Class<SlideshowEntry> getManagedClass()
	{
		return SlideshowEntry.class;
	}

	/**
	 * Randomizes a slideshow based on all photos in the database that
	 * are eligible based on the current settings.
	 * 
	 * @throws SQLException
	 */
	public void shuffleSlideshow()
	throws ORMException
	{
		try
		{
			settingsManager.removeCurrentSlideshowEntryId();
			
			TableUtils.clearTable(databaseManager.getConnectionSource(), SlideshowEntry.class);

			List<Integer> ids = photoManager.getEligiblePhotosForSlideshow();

			log.info("Shuffling ids..");

			Collections.shuffle(ids);

			log.info("IDs shuffled..");

			int limit = settingsManager.getSlideshowSize();

			if (limit == 0 || limit > ids.size())
			{
				limit = ids.size();
			}

			BatchSlideshowEntryInserter batcher = new BatchSlideshowEntryInserter();

			batcher.setSlideshowManager(this);
			batcher.setPhotoIds(ids);
			batcher.setLimit(limit);

			Dao<SlideshowEntry, Integer> dao = getDAO();

			dao.callBatchTasks(batcher);

			log.info("Slideshow persisted with " + ids.size() + " photo(s)");
		}
		catch (SQLException e)
		{
			throw new ORMException("Error creating slideshow.", e);
		}
		catch (Exception e)
		{
			throw new ORMException("Error creating slideshow.", e);
		}
	}

	/**
	 * We're going to take the given photo and add it to the current slideshow.
	 *
	 * This is going to create a bit of psuedo-randomness, since we're just going
	 * to be tacking it onto the end of the list.  However, since we get photos in
	 * fairly odd orders during updates, the user shouldn't notice, and we'll get
	 * true randomness every few hours or so and every time the app starts.
	 * 
	 * @param photo
	 */
	public void addPhotoToSlideshow(Photo photo)
	throws ORMException
	{
		Dao<SlideshowEntry, Integer> dao = getDAO();

		try
		{
			// First, we need to find the max "order" attribute of photos
			QueryBuilder<SlideshowEntry, Integer> qb = dao.queryBuilder();

			qb.limit(1);

			qb.orderBy("slideshowOrder", false);

			SlideshowEntry maxEntry = dao.queryForFirst(qb.prepare());

			int newOrder = 0;

			if (maxEntry != null)
			{
				newOrder = maxEntry.getSlideshowOrder();

				newOrder++;
			}

			SlideshowEntry entry = new SlideshowEntry();

			entry.setPhotoId(photo.getId());

			entry.setSlideshowOrder(newOrder);

			persist(entry);

			log.info("Added " + newOrder + " entries into database.");
		}
		catch (SQLException e)
		{
			throw new ORMException("Error adding entry to slideshow", e);
		}
	}

	public SlideshowEntry getNextEntry(Integer currentEntryId)
	throws ORMException
	{
		SlideshowEntry output = null;

		Dao<SlideshowEntry, Integer> dao = getDAO();

		try
		{
			if (currentEntryId != null)
			{
				SlideshowEntry currentEntry = find(currentEntryId);
				
				QueryBuilder<SlideshowEntry, Integer> qb = dao.queryBuilder();

				qb.limit(1);

				PreparedQuery<SlideshowEntry> pq = null;

				Where where = qb.where();

				where.gt("slideshowOrder", currentEntry.getSlideshowOrder());

				qb.orderBy("slideshowOrder", true);

				pq = qb.prepare();

				log.debug("Executing: " + qb.prepareStatementString());

				output = dao.queryForFirst(pq);
			}

			if (output == null)
			{
				// There are no entries with order greater than the current one
				// lets get the first one
				QueryBuilder<SlideshowEntry, Integer> qb = dao.queryBuilder();

				qb.limit(1);

				PreparedQuery<SlideshowEntry> pq = null;

				qb = dao.queryBuilder();

				qb.orderBy("slideshowOrder", true);

				log.debug("Executing: " + qb.prepareStatementString());

				pq = qb.prepare();

				output = dao.queryForFirst(pq);
			}
		}
		catch (SQLException e)
		{
			throw new ORMException("Error getting next slideshow entry.", e);
		}

		return output;
	}

	public SlideshowEntry getPreviousEntry(Integer currentEntryId)
	throws ORMException
	{
		SlideshowEntry output = null;

		Dao<SlideshowEntry, Integer> dao = getDAO();

		try
		{
			if (currentEntryId != null)
			{
				SlideshowEntry currentEntry = find(currentEntryId);

				QueryBuilder<SlideshowEntry, Integer> qb = dao.queryBuilder();

				qb.limit(1);

				PreparedQuery<SlideshowEntry> pq = null;

				Where where = qb.where();

				where.lt("slideshowOrder", currentEntry.getSlideshowOrder());

				qb.orderBy("slideshowOrder", false);

				log.debug("Executing: " + qb.prepareStatementString());

				pq = qb.prepare();

				output = dao.queryForFirst(pq);
			}

			if (output == null)
			{
				// There are no entries with order greater than the current one
				// lets get the first one
				QueryBuilder<SlideshowEntry, Integer> qb = dao.queryBuilder();

				qb.limit(1);

				PreparedQuery<SlideshowEntry> pq = null;

				qb.orderBy("slideshowOrder", false);

				log.debug("Executing: " + qb.prepareStatementString());

				pq = qb.prepare();

				output = dao.queryForFirst(pq);
			}
		}
		catch (SQLException e)
		{
			throw new ORMException("Error getting previous slideshow entry.", e);
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

	public PhotoManager getPhotoManager()
	{
		return photoManager;
	}

	public void setPhotoManager(PhotoManager photoManager)
	{
		this.photoManager = photoManager;
	}
}
