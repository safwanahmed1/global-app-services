package com.sendme.android.slideshow.manager;

import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.model.AudioTrack;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
@Singleton
public class AudioTrackManager
extends ObjectManager<AudioTrack>
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	public AudioTrackManager()
	{

	}

	@Override
	public Class<AudioTrack> getManagedClass()
	{
		return AudioTrack.class;
	}

	public boolean existsByExternalId(String externalId)
	throws ORMException
	{
		boolean output = false;

		AudioTrack test = findByExternalId(externalId);

		output = (test != null);

		return output;
	}

	public AudioTrack findByExternalId(String externalId)
	throws ORMException
	{
		AudioTrack output = null;

		try
		{
			Dao<AudioTrack, Integer> dao = getDAO();

			QueryBuilder<AudioTrack, Integer> qb = dao.queryBuilder();

			Where where = qb.where();

			where.eq("externalId", externalId);

			PreparedQuery pq = qb.prepare();

			output = dao.queryForFirst(pq);
		}
		catch (SQLException e)
		{
			throw new ORMException("Error finding audio track by external id.", e);
		}

		return output;
	}

	public List<Integer> getAllIds()
	throws ORMException
	{
		List<Integer> output = new ArrayList<Integer>();

		try
		{
			Dao<AudioTrack, Integer> dao = getDAO();

			// I hate this.  I really, truly loathe this.  However, ORMLite does not
			// have the maturity yet to allow me to do this in their query code.
			String query = "SELECT id FROM slideshow_audio_track";

			GenericRawResults<Object[]> results = dao.queryRaw(query, new DataType[] {DataType.INTEGER});

			for (Object[] resultObjects : results)
			{
				output.add((Integer) resultObjects[0]);
			}

			log.info("Found " + output.size() + " eligible audio tracks...");
		}
		catch (SQLException e)
		{
			throw new ORMException("Error getting eligible playlist results.", e);
		}

		return output;
	}

	public int deleteExpired(Long time)
	throws ORMException
	{
		int output = 0;

		try
		{
			// TODO:  We really need to delete playlist entries first
			
			Dao<AudioTrack, Integer> dao = getDAO();

			DeleteBuilder<AudioTrack, Integer> db = dao.deleteBuilder();

			Where where = db.where();

			where.lt("lastSeenInUpdate", time);

			PreparedDelete pd = db.prepare();

			output = dao.delete(pd);
		}
		catch (SQLException e)
		{
			throw new ORMException("Error deleting expired audio tracks.", e);
		}

		return output;
	}
}
