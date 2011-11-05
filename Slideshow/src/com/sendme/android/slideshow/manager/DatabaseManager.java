package com.sendme.android.slideshow.manager;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.google.inject.Inject;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.model.AudioPlaylistEntry;
import com.sendme.android.slideshow.model.AudioTrack;
import com.sendme.android.slideshow.model.Person;
import com.sendme.android.slideshow.model.Photo;
import com.sendme.android.slideshow.model.SlideshowEntry;
import java.sql.SQLException;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public final class DatabaseManager
extends OrmLiteSqliteOpenHelper
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	private final static String DATABASE_NAME = "androidslideshow.db";

	private final static int DATABASE_VERSION = 2;

	private Context context = null;

	@Inject
	public DatabaseManager(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

		setContext(context);
	}

	public boolean databaseExists()
	{
		boolean output = false;

		log.debug("Checking database...");

		// We're simply going to get a database and chekc it.
		SQLiteDatabase database = null;

		Cursor cursor = null;

		try
		{
			database = getWritableDatabase();

			String tables[] = new String[]
			{
				"slideshow_person",
				"slideshow_photo",
				"slideshow_entry",
				"slideshow_audio_playlist_entry",
				"slideshow_audio_track"
			};

			cursor = database.rawQuery("SELECT COUNT(name) FROM sqlite_master WHERE type='table' AND name in (?, ?, ?, ?, ?)", tables);

			if (cursor != null)
			{
				cursor.moveToFirst();

				int results = cursor.getInt(0);

				log.info("Got " + results + " tables from database check query.");

				output = (results == tables.length);
			}
			else
			{
				log.info("Cursor came back null.");
			}
		}
		catch (Exception e)
		{
			log.error("Error checking database.", e);
		}
		finally
		{
			if (database != null)
			{
				database.close();
			}
			
			if (cursor != null && !cursor.isClosed())
			{
				cursor.close();
			}
		}

		return output;
	}

	public void createSchema()
	throws ORMException
	{
		log.debug("Create schema called...");

		try
		{
			ConnectionSource connectionSource = getConnectionSource();

			TableUtils.createTableIfNotExists(connectionSource, Person.class);
			TableUtils.createTableIfNotExists(connectionSource, Photo.class);
			TableUtils.createTableIfNotExists(connectionSource, SlideshowEntry.class);
			TableUtils.createTableIfNotExists(connectionSource, AudioTrack.class);
			TableUtils.createTableIfNotExists(connectionSource, AudioPlaylistEntry.class);
		}
		catch (SQLException e)
		{
			throw new ORMException("Error creating schema.", e);
		}
	}

	public void dropSchema()
	throws ORMException
	{
		log.debug("Drop schema called...");

		try
		{
			ConnectionSource connectionSource = getConnectionSource();

			TableUtils.dropTable(connectionSource, SlideshowEntry.class, true);
			TableUtils.dropTable(connectionSource, Photo.class, true);
			TableUtils.dropTable(connectionSource, Person.class, true);
			TableUtils.dropTable(connectionSource, AudioPlaylistEntry.class, true);
			TableUtils.dropTable(connectionSource, AudioTrack.class, true);
		}
		catch (SQLException e)
		{
			throw new ORMException("Error dropping schema.", e);
		}
	}

	@Override
	public void onCreate(SQLiteDatabase sqld, ConnectionSource cs)
	{
		log.debug("DatabaseManager onCreate called...");

		// We're not going to do anything.  We want to manage this lifecycle ourselves
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqld, ConnectionSource cs, int i, int i1)
	{
		log.debug("DatabaseManager onUpdate called...");

		// We're not going to do anything.  We want to manage this lifecycle ourselves
	}

	public Context getContext()
	{
		return context;
	}

	public void setContext(Context context)
	{
		this.context = context;
	}
}
