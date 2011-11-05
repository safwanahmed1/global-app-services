package com.sendme.android.slideshow.manager;

import com.google.inject.Inject;
import com.j256.ormlite.dao.Dao;
import com.sendme.android.slideshow.model.DataObject;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public abstract class ObjectManager<T extends DataObject>
{
	@Inject
	protected DatabaseManager databaseManager = null;

	@Inject
	protected SettingsManager settingsManager = null;

	public abstract Class<T> getManagedClass();

	public Dao<T, Integer> getDAO()
	throws ORMException
	{
		Dao<T, Integer> output = null;

		try
		{
			output = databaseManager.getDao(getManagedClass());
		}
		catch (SQLException e)
		{
			throw new ORMException("Error getting DAO for: " + getManagedClass(), e);
		}

		return output;
	}

	public T find(Integer id)
	throws ORMException
	{
		Dao<T, Integer> dao = getDAO();

		T output = null;

		try
		{
			output = dao.queryForId(id);
		}
		catch (SQLException e)
		{
			throw new ORMException("Error finding by id for: " + getManagedClass(), e);
		}

		return output;
	}

	public List<T> findAll()
	throws ORMException
	{
		Dao<T, Integer> dao = getDAO();

		List<T> output = null;

		try
		{
			output = dao.queryForAll();
		}
		catch (SQLException e)
		{
			throw new ORMException("Error returning all entries for: " + getManagedClass(), e);
		}

		return output;
	}

	public void persist(T object)
	throws ORMException
	{
		Dao<T, Integer> dao = getDAO();

		if (object != null)
		{
			Long now = System.currentTimeMillis();

			if (object.getCreatedOn() == null)
			{
				object.setCreatedOn(now);
			}

			object.setModifiedOn(now);

			try
			{
				if (object.getId() == null)
				{
					dao.create(object);
				}
				else
				{
					dao.update(object);
				}
			}
			catch (SQLException e)
			{
				throw new ORMException("Error persisting: " + getManagedClass(), e);
			}
		}
	}

	public void refresh(T object)
	throws ORMException
	{
		Dao<T, Integer> dao = getDAO();

		try
		{
			dao.refresh(object);
		}
		catch (SQLException e)
		{
			throw new ORMException("Error refreshing: " + getManagedClass(), e);
		}
	}

	public void delete(T object)
	throws ORMException
	{
		Dao<T, Integer> dao = getDAO();

		try
		{
			dao.delete(object);
		}
		catch (SQLException e)
		{
			throw new ORMException("Error deleting: " + getManagedClass(), e);
		}
	}

	public DatabaseManager getDatabaseManager()
	{
		return databaseManager;
	}

	public void setDatabaseManager(DatabaseManager databaseManager)
	{
		this.databaseManager = databaseManager;
	}

	public SettingsManager getSettingsManager()
	{
		return settingsManager;
	}

	public void setSettingsManager(SettingsManager settingsManager)
	{
		this.settingsManager = settingsManager;
	}
}
