package com.sendme.android.slideshow.manager;

import com.google.inject.Singleton;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedDelete;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.model.Person;
import com.sendme.android.slideshow.model.PhotoSource;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
@Singleton
public class PersonManager
extends ObjectManager<Person>
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	public PersonManager()
	{
	}

	@Override
	public Class<Person> getManagedClass()
	{
		return Person.class;
	}

	public boolean existsByExternalId(String externalId)
	throws ORMException
	{
		boolean output = false;

		Person test = findByExternalId(externalId);

		if (test != null)
		{
			output = true;
		}

		return output;
	}

	public Person findByExternalId(String externalId)
	throws ORMException
	{
		Person output = null;

		try
		{
			Dao<Person, Integer> dao = getDAO();

			QueryBuilder<Person, Integer> qb = dao.queryBuilder();

			Where where = qb.where();

			where.eq("externalId", externalId);

			PreparedQuery pq = qb.prepare();

			output = dao.queryForFirst(pq);
		}
		catch (SQLException e)
		{
			throw new ORMException("Error finding Person by external id.", e);
		}

		return output;
	}

	public List<Person> findExpired(Long time, PhotoSource source)
	throws ORMException
	{
		List<Person> output = null;

		try
		{
			Dao<Person, Integer> dao = getDAO();

			QueryBuilder<Person, Integer> qb = dao.queryBuilder();

			Where where = qb.where();

			where.lt("lastSeenInUpdate", time);
			where.and();
			where.eq("photoSource", source);

			PreparedQuery pq = qb.prepare();

			output = dao.query(pq);
		}
		catch (SQLException e)
		{
			throw new ORMException("Error finding expired people.", e);
		}

		return output;
	}

	public int deleteExpired(Long time, PhotoSource source)
	throws ORMException
	{
		int output = 0;

		try
		{
			// TODO: We really need to delete photos first, to maintain referential integrity.
			// This probably means also removing slideshow entries.
			
			Dao<Person, Integer> dao = getDAO();

			DeleteBuilder<Person, Integer> db = dao.deleteBuilder();

			Where where = db.where();

			where.lt("lastSeenInUpdate", time);
			where.and();
			where.eq("photoSource", source);

			PreparedDelete pd = db.prepare();

			output = dao.delete(pd);
		}
		catch (SQLException e)
		{
			throw new ORMException("Error deleting expired people.", e);
		}

		return output;
	}
}
