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
import com.sendme.android.slideshow.model.Photo;
import com.sendme.android.slideshow.model.PhotoType;
import com.sendme.android.slideshow.model.PhotoSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
@Singleton
public class PhotoManager
extends ObjectManager<Photo>
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	public PhotoManager()
	{

	}

	@Override
	public Class<Photo> getManagedClass()
	{
		return Photo.class;
	}

	public boolean existsByExternalId(String externalId)
	throws ORMException
	{
		boolean output = false;

		Photo test = findByExternalId(externalId);

		output = (test != null);

		return output;
	}

	public Photo findByExternalId(String externalId)
	throws ORMException
	{
		Photo output = null;

		try
		{
			Dao<Photo, Integer> dao = getDAO();

			QueryBuilder<Photo, Integer> qb = dao.queryBuilder();

			Where where = qb.where();

			where.eq("externalId", externalId);

			PreparedQuery pq = qb.prepare();

			output = dao.queryForFirst(pq);
		}
		catch (SQLException e)
		{
			throw new ORMException("Error finding Photo by external id.", e);
		}

		return output;
	}

	public List<Integer> getEligiblePhotosForSlideshow()
	throws ORMException
	{
		List<Integer> output = new ArrayList<Integer>();

		try
		{
			Dao<Photo, Integer> dao = getDAO();

			// I hate this.  I really, truly loathe this.  However, ORMLite does not
			// have the maturity yet to allow me to do this in their query code.
			String query = "SELECT id FROM slideshow_photo WHERE ";

			boolean hasConditions = false;

			if (settingsManager.isUseLocalImages())
			{
				query += "photoSource='" + PhotoSource.LOCAL + "' ";

				hasConditions = true;
			}

			if (settingsManager.isUseFacebookImages())
			{
				if (hasConditions)
				{
					query += " OR ";
				}

				query += "(photoSource='" + PhotoSource.FACEBOOK + "'";

				EnumSet<PhotoType> types = settingsManager.getFacebookPhotoTypes();

				if (types.size() > 0)
				{
					query += " AND (";

					Iterator<PhotoType> it = types.iterator();

					while (it.hasNext())
					{
						PhotoType type = it.next();

						query += " photoType = '" + type + "'";

						if (it.hasNext())
						{
							query += " OR ";
						}
					}

					query += ")";
				}

				query += ")";

				hasConditions = true;
			}

			GenericRawResults<Object[]> results = dao.queryRaw(query, new DataType[] {DataType.INTEGER});

			for (Object[] resultObjects : results)
			{
				output.add((Integer) resultObjects[0]);
			}

			log.info("Found " + output.size() + " eligible photos...");
		}
		catch (SQLException e)
		{
			throw new ORMException("Error getting eligible slideshow results.", e);
		}

		return output;
	}

	public int deleteExpired(Long time, PhotoSource source)
	throws ORMException
	{
		int output = 0;

		try
		{
			// TODO:  We need to delete slideshow entries first!
			
			Dao<Photo, Integer> dao = getDAO();

			DeleteBuilder<Photo, Integer> db = dao.deleteBuilder();

			Where where = db.where();

			where.lt("lastSeenInUpdate", time);
			where.and();
			where.eq("photoSource", source);

			PreparedDelete pd = db.prepare();

			output = dao.delete(pd);
		}
		catch (SQLException e)
		{
			throw new ORMException("Error deleting expired photos.", e);
		}

		return output;
	}

	public int deleteByPersonId(Integer personId)
	throws ORMException
	{
		int output = 0;

		try
		{
			Dao<Photo, Integer> dao = getDAO();

			DeleteBuilder<Photo, Integer> db = dao.deleteBuilder();

			Where where = db.where();

			where.eq("person", personId);

			PreparedDelete pd = db.prepare();

			output = dao.delete(pd);
		}
		catch (SQLException e)
		{
			throw new ORMException("Error deleting photos by person id: " + personId, e);
		}

		return output;
	}
}
