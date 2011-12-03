package com.sendme.android.slideshow.source.impl;

import android.content.Intent;
import android.net.Uri;
import com.facebook.android.Facebook;
import com.google.inject.Singleton;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.manager.ORMException;
import com.sendme.android.slideshow.model.Person;
import com.sendme.android.slideshow.model.Photo;
import com.sendme.android.slideshow.model.PhotoSource;
import com.sendme.android.slideshow.model.PhotoType;
import com.sendme.android.slideshow.source.MediaSource;
import com.sendme.android.slideshow.source.MediaSourceException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.EnumSet;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
@Singleton
public class FacebookImageSource
extends MediaSource
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	public FacebookImageSource()
	{
	}

	public Facebook getFacebookApplication()
	{
		Facebook output = new Facebook(settingsManager.getFacebookApplicationId());

		output.setAccessToken(settingsManager.getFacebookAuthorizationToken());

		output.setAccessExpires(settingsManager.getFacebookAuthorizationTokenExpiration());

		return output;
	}

	@Override
	public void checkForUpdates(Long time, boolean notify)
	throws MediaSourceException
	{
		log.debug("FacebookImageSource asked to check for updates: " + time);

		try
		{
			updating = true;

			int updateCount = 0;

			try
			{
				updateCount += updateMe(time);

				updateCount += updateFriends(time);

				// OK, now we should have stored all the people.  We need to clean up
				// missing entries.  That's going to mean deleting any photos that are
				// associated with those people, as well as their own entries.
				List<Person> expiredPeople = personManager.findExpired(time, PhotoSource.FACEBOOK);

				for (Person expiredPerson : expiredPeople)
				{
					photoManager.deleteByPersonId(expiredPerson.getId());
				}

				personManager.deleteExpired(time, PhotoSource.FACEBOOK);

				photoManager.deleteExpired(time, PhotoSource.FACEBOOK);
			}
			catch (MediaSourceException e)
			{
				log.error("Cannot update Facebook photos.", e);
			}
			catch (ORMException e)
			{
				log.error("Cannot update Facebook photos.", e);
			}

			log.debug("FacebookImageSource detected: " + updateCount + " new image(s).");

			if (updateCount > 0 && notify)
			{
				// TODO:  We need to update the slideshow
			}
		}
		finally
		{
			updating = false;

			settingsManager.setFacebookImagesLastUpdateTime(time);
		}
	}

	private int updateMe(Long time)
	throws MediaSourceException
	{
		int output = 0;

		JSONObject response = doFacebookQuery("me");

		try
		{
			Person person = new Person();

			person.setExternalId(response.getString("id"));
			person.setName(response.getString("name"));
			person.setPhotoSource(PhotoSource.FACEBOOK);
			person.setMe(true);

			storePerson(time, person);

			EnumSet<PhotoType> types = settingsManager.getFacebookPhotoTypes();

			if (types.contains(PhotoType.PERSONAL_TAG))
			{
				output += updatePhotos(time, person, PhotoType.PERSONAL_TAG);
			}

			if (types.contains(PhotoType.PERSONAL_ALBUM))
			{
				output += updateAlbums(time, person, PhotoType.PERSONAL_ALBUM);
			}
		}
		catch (JSONException e)
		{
			throw new MediaSourceException("Could not update personal information from Facebook.", e);
		}

		return output;
	}

	private int updateFriends(Long time)
	throws MediaSourceException
	{
		int output = 0;

		JSONObject response = doFacebookQuery("me/friends");

		if (response != null)
		{
			try
			{
				EnumSet<PhotoType> types = settingsManager.getFacebookPhotoTypes();

				boolean doPhotos = types.contains(PhotoType.FRIEND_TAG);
				boolean doAlbums = types.contains(PhotoType.FRIEND_ALBUM);

				JSONArray array = response.getJSONArray("data");

				for (int x = 0; x < array.length() && updating; x++)
				{
					JSONObject jsonPerson = array.getJSONObject(x);

					Person person = new Person();

					person.setExternalId(jsonPerson.getString("id"));
					person.setName(jsonPerson.getString("name"));
					person.setPhotoSource(PhotoSource.FACEBOOK);
					person.setMe(false);

					storePerson(time, person);

					if (doPhotos)
					{
						output += updatePhotos(time, person, PhotoType.FRIEND_TAG);
					}

					if (doAlbums)
					{
						output += updateAlbums(time, person, PhotoType.FRIEND_ALBUM);
					}
				}
			}
			catch (JSONException e)
			{
				throw new MediaSourceException("Cannot update Facebook friends.", e);
			}
		}

		return output;
	}

	private int updatePhotos(Long time, Person person, PhotoType type)
	throws MediaSourceException
	{
		int output = 0;

		JSONObject response = doFacebookQuery(person.getExternalId() + "/photos");

		try
		{
			JSONArray array = response.getJSONArray("data");

			for (int x = 0; x < array.length() && updating; x++)
			{
				JSONObject jsonPhoto = array.getJSONObject(x);

				createPhoto(time, person, type, jsonPhoto);

				output++;
			}
		}
		catch (JSONException e)
		{
			log.info("Error updating Facebook photos for person: " + person.getId(), e);
		}

		return output;
	}

	private int updateAlbums(Long time, Person person, PhotoType type)
	throws MediaSourceException
	{
		int output = 0;

		JSONObject response = doFacebookQuery(person.getExternalId() + "/albums");

		try
		{
			JSONArray array = response.getJSONArray("data");

			for (int x = 0; x < array.length() && updating; x++)
			{
				JSONObject album = array.getJSONObject(x);

				String albumId = album.getString("id");

				JSONObject albumResponse = doFacebookQuery(albumId + "/photos");

				JSONArray albumPhotos = albumResponse.getJSONArray("data");

				for (int y=0; y < albumPhotos.length(); y++)
				{
					createPhoto(time, person, type, albumPhotos.getJSONObject(y));

					output++;
				}
			}
		}
		catch (JSONException e)
		{
			throw new MediaSourceException("Cannot update Facebook albums for: " + person.getId(), e);
		}

		return output;
	}

	private void createPhoto(Long time, Person person, PhotoType type, JSONObject jsonPhoto)
	throws MediaSourceException
	{
		Photo photo = new Photo();

		try
		{
			photo.setExternalId(jsonPhoto.getString("id"));
			photo.setPerson(person.getId());
			photo.setPhotoSource(PhotoSource.FACEBOOK);
			photo.setPhotoType(type);
			photo.setURI(jsonPhoto.getString("source"));

			String text = "";

			JSONObject fromObj = jsonPhoto.getJSONObject("from");

			switch (type)
			{
				case PERSONAL_TAG:
				{
					text = "Facebook tag of me from " + fromObj.getString("name");

					break;
				}

				case PERSONAL_ALBUM:
				{
					text = "My Facebook album";

					break;
				}

				case FRIEND_TAG:
				{
					text = "Facebook tag of " + person.getName() + " from " + fromObj.getString("name");

					break;
				}

				case FRIEND_ALBUM:
				{
					text = person.getName() + " Facebook album";
					break;
				}
			}

			photo.setText(text);

			storePhoto(time, photo);
			try {
				slideshowManager.addPhotoToSlideshow(photo);
			} catch (ORMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (JSONException e)
		{
			throw new MediaSourceException("Cannot create Facebook photo.", e);
		}
	}

	private JSONObject doFacebookQuery(String query)
	throws MediaSourceException
	{
		JSONObject output = null;

		Facebook facebookApplication = getFacebookApplication();
		
		try
		{
			String response = facebookApplication.request(query);

			output = new JSONObject(response);

			if (output.has("error"))
			{
				JSONObject error = output.getJSONObject("error");

				throw new MediaSourceException("Facebook Error Detected " + error.getString("type") + ": " + error.getString("message"));
			}
		}
		catch (MalformedURLException e)
		{
			throw new MediaSourceException("Error performing Facebook query.", e);
		}
		catch (IOException e)
		{
			throw new MediaSourceException("Error performing Facebook query.", e);
		}
		catch (JSONException e)
		{
			throw new MediaSourceException("Error performing Facebook query.", e);
		}

		return output;
	}

	public void authorizeCallback(int requestCode, int resultCode, Intent data)
	{
		Facebook facebookApplication = getFacebookApplication();

		facebookApplication.authorizeCallback(requestCode, resultCode, data);
	}
}
