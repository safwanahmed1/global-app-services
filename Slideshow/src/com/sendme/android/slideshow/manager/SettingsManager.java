package com.sendme.android.slideshow.manager;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.model.PhotoType;
import com.sendme.android.util.AndroidSystemUtil;
import java.util.EnumSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
@Singleton
public class SettingsManager
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	private final static Long HEARTBEAT_INTERVAL = 500L;

	private final static Long IMAGE_CACHE_LIFETIME = 300000L;

	private final static String RUN_ONCE_KEY = "RUN_ONCE";

	private final static String DEVICE_UUID_KEY = "DEVICE_UUID";

	private final static String SPLASHSCREEN_PAUSE_DURATION_KEY = "SPLASHSCREEN_PAUSE_DURATION";

	private final static String SLIDESHOW_INTERVAL_KEY = "SLIDESHOW_INTERVAL";

	private final static String SLIDESHOW_SIZE_KEY = "SLIDESHOW_SIZE";

	private final static String SOURCE_UPDATE_IMAGES_INTERVAL_KEY = "SOURCE_UPDATE_IMAGES_INTERVAL";

	private final static String SOURCE_LOCAL_IMAGES_KEY = "SOURCE_LOCAL_IMAGES";

	private final static String SOURCE_LOCAL_IMAGES_LAST_UPDATE_KEY = "SOURCE_LOCAL_IMAGES_LAST_UPDATE";

	private final static String SOURCE_FACEBOOK_IMAGES_KEY = "SOURCE_FACEBOOK_IMAGES";

	private final static String SOURCE_FACEBOOK_IMAGES_LAST_UPDATE_KEY = "SOURCE_FACEBOOK_IMAGES_LAST_UPDATE";

	private final static String SOURCE_FACEBOOK_TYPE_PERSONAL_ALBUM_KEY = "SOURCE_FACEBOOK_TYPE_PERSONAL_ALBUM";

	private final static String SOURCE_FACEBOOK_TYPE_PERSONAL_TAG_KEY = "SOURCE_FACEBOOK_TYPE_PERSONAL_TAG";

	private final static String SOURCE_FACEBOOK_TYPE_FRIEND_ALBUM_KEY = "SOURCE_FACEBOOK_TYPE_FRIEND_ALBUM";

	private final static String SOURCE_FACEBOOK_TYPE_FRIEND_TAG_KEY = "SOURCE_FACEBOOK_TYPE_FRIEND_TAG";

	private final static String SOURCE_FACEBOOK_TOKEN_KEY = "SOURCE_FACEBOOK_TOKEN";

	private final static String SOURCE_FACEBOOK_TOKEN_EXPIRATION_KEY = "SOURCE_FACEBOOK_TOKEN_EXPIRATION";

	private final static String MUSIC_PLAY_KEY = "MUSIC_PLAY";

	private final static String SOURCE_UPDATE_AUDIO_TRACKS_INTERVAL_KEY = "SOURCE_UPDATE_AUDIO_TRACKS_INTERVAL";

	private final static String SOURCE_AUDIO_TRACKS_LAST_UPDATE_KEY = "SOURCE_AUDIO_TRACKS_LAST_UPDATE";

	private final static String CURRENT_SLIDESHOW_ENTRY_KEY = "CURRENT_SLIDESHOW_ENTRY";

	private final static String CURRENT_AUDIO_PLAYLIST_ENTRY_KEY = "CURRENT_AUDIO_PLAYLIST_ENTRY";

	private final static String UI_DISPLAY_LIFETIME_KEY = "UI_DISPLAY_LIFETIME";

	private final static String AD_DISPLAY_LIFETIME_KEY = "AD_DISPLAY_LIFETIME";

	private final static String FACEBOOK_APP_ID = "221014304598984";

	@Inject
	private AndroidSlideshow ass = null;

	public SharedPreferences getPreferences()
	{
		return PreferenceManager.getDefaultSharedPreferences(ass.getApplicationContext());
	}

	public boolean needsPreferences()
	{
		SharedPreferences preferences = getPreferences();

		boolean hasPreferences = preferences.getBoolean(RUN_ONCE_KEY, false);

		return !hasPreferences;
	}

	public void setPreferencesAsRunOnce()
	{
		SharedPreferences preferences = getPreferences();

		boolean hasPreferences = preferences.getBoolean(RUN_ONCE_KEY, false);

		if (!hasPreferences)
		{
			Editor editor = preferences.edit();

			editor.putBoolean(RUN_ONCE_KEY, true);

			editor.commit();
		}
	}

	public void logPreferences()
	{
		SharedPreferences preferences = getPreferences();

		Map<String, ?> entries = preferences.getAll();

		for (Entry<String, ?> entry : entries.entrySet())
		{
			log.debug("Preference " + entry.getKey() + "( " + entry.getValue().getClass() + "): " + String.valueOf(entry.getValue()));
		}
	}

	public static Long getHeartbeatFrequency()
	{
		return HEARTBEAT_INTERVAL;
	}

	public static Long getImageCacheLifetime()
	{
		return IMAGE_CACHE_LIFETIME;
	}

	public UUID getDeviceUUID()
	{
		UUID output = null;
		
		SharedPreferences preferences = getPreferences();

		String tmp = preferences.getString(DEVICE_UUID_KEY, null);

		if (tmp == null)
		{
			output = AndroidSystemUtil.getDeviceUUID(ass);

			setDeviceUUID(output);
		}
		else
		{
			output = UUID.fromString(tmp);
		}

		return output;
	}

	public void setDeviceUUID(UUID uuid)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putString(DEVICE_UUID_KEY, uuid.toString());

		editor.commit();
	}

	public Long getSplashScreenPauseDuration()
	{
		SharedPreferences preferences = getPreferences();

		return preferences.getLong(SPLASHSCREEN_PAUSE_DURATION_KEY, 1000L);
	}

	public void setSplashScreenPauseDuration(long duration)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putLong(SPLASHSCREEN_PAUSE_DURATION_KEY, duration);

		editor.commit();
	}

	public Long getSlideshowInterval()
	{
		SharedPreferences preferences = getPreferences();

		String tmp = preferences.getString(SLIDESHOW_INTERVAL_KEY, "10000");

		Long tmp2 = 10000L;

		try
		{
			tmp2 = Long.parseLong(tmp);
		}
		catch (NumberFormatException e)
		{
		}

		return tmp2.longValue();
	}

	public void setSlideshowInterval(long interval)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putString(SLIDESHOW_INTERVAL_KEY, "" + interval);

		editor.commit();
	}

	public int getSlideshowSize()
	{
		SharedPreferences preferences = getPreferences();

		String tmp = preferences.getString(SLIDESHOW_SIZE_KEY, "0");

		Integer tmp2 = 0;

		try
		{
			tmp2 = Integer.parseInt(tmp);
		}
		catch (NumberFormatException e)
		{
		}

		return tmp2.intValue();
	}

	public void setSlideshowSize(int size)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putString(SLIDESHOW_SIZE_KEY, "" + size);

		editor.commit();
	}

	public long getImageSourceUpdateInterval()
	{
		SharedPreferences preferences = getPreferences();

		String tmp = preferences.getString(SOURCE_UPDATE_IMAGES_INTERVAL_KEY, "1800000");

		Long tmp2 = 1800000L;

		try
		{
			tmp2 = Long.parseLong(tmp);
		}
		catch (NumberFormatException e)
		{
		}

		return tmp2.intValue();
	}

	public void setImageSourceUpdateInterval(long interval)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putString(SOURCE_UPDATE_IMAGES_INTERVAL_KEY, "" + interval);

		editor.commit();
	}

	public boolean isUseLocalImages()
	{
		SharedPreferences preferences = getPreferences();

		return preferences.getBoolean(SOURCE_LOCAL_IMAGES_KEY, true);
	}

	public void setUseLocalImages(boolean use)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putBoolean(SOURCE_LOCAL_IMAGES_KEY, use);

		editor.commit();
	}

	public Long getLocalImagesLastUpdateTime()
	{
		SharedPreferences preferences = getPreferences();

		return preferences.getLong(SOURCE_LOCAL_IMAGES_LAST_UPDATE_KEY, 0);
	}

	public void setLocalImagesLastUpdateTime(Long time)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putLong(SOURCE_LOCAL_IMAGES_LAST_UPDATE_KEY, time);

		editor.commit();
	}

	public String getFacebookApplicationId()
	{
		return FACEBOOK_APP_ID;
	}

	public String getFacebookAuthorizationToken()
	{
		SharedPreferences preferences = getPreferences();

		return preferences.getString(SOURCE_FACEBOOK_TOKEN_KEY, null);
	}

	public void setFacebookAuthorizationToken(String token)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putString(SOURCE_FACEBOOK_TOKEN_KEY, token);

		editor.commit();
	}

	public long getFacebookAuthorizationTokenExpiration()
	{
		SharedPreferences preferences = getPreferences();

		return preferences.getLong(SOURCE_FACEBOOK_TOKEN_EXPIRATION_KEY, 0);
	}

	public void setFacebookAuthorizationTokenExpiration(Long expiration)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putLong(SOURCE_FACEBOOK_TOKEN_EXPIRATION_KEY, expiration);

		editor.commit();
	}

	public boolean isUseFacebookImages()
	{
		SharedPreferences preferences = getPreferences();

		return preferences.getBoolean(SOURCE_FACEBOOK_IMAGES_KEY, true);
	}

	public void setUseFacebookImages(boolean use)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putBoolean(SOURCE_FACEBOOK_IMAGES_KEY, use);

		editor.commit();
	}

	public Long getFacebookImagesLastUpdateTime()
	{
		SharedPreferences preferences = getPreferences();

		return preferences.getLong(SOURCE_FACEBOOK_IMAGES_LAST_UPDATE_KEY, 0);
	}

	public void setFacebookImagesLastUpdateTime(Long time)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putLong(SOURCE_FACEBOOK_IMAGES_LAST_UPDATE_KEY, time);

		editor.commit();
	}

	public EnumSet<PhotoType> getFacebookPhotoTypes()
	{
		EnumSet<PhotoType> output = EnumSet.noneOf(PhotoType.class);

		SharedPreferences preferences = getPreferences();

		if (preferences.getBoolean(SOURCE_FACEBOOK_TYPE_PERSONAL_ALBUM_KEY, true))
		{
			output.add(PhotoType.PERSONAL_ALBUM);
		}

		if (preferences.getBoolean(SOURCE_FACEBOOK_TYPE_PERSONAL_TAG_KEY, true))
		{
			output.add(PhotoType.PERSONAL_TAG);
		}

		if (preferences.getBoolean(SOURCE_FACEBOOK_TYPE_FRIEND_ALBUM_KEY, true))
		{
			output.add(PhotoType.FRIEND_ALBUM);
		}

		if (preferences.getBoolean(SOURCE_FACEBOOK_TYPE_FRIEND_TAG_KEY, true))
		{
			output.add(PhotoType.FRIEND_TAG);
		}

		return output;
	}

	public void setFacebookPhotoTypes(EnumSet<PhotoType> types)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		if (types.contains(PhotoType.PERSONAL_ALBUM))
		{
			editor.putBoolean(SOURCE_FACEBOOK_TYPE_PERSONAL_ALBUM_KEY, true);
		}
		else
		{
			editor.putBoolean(SOURCE_FACEBOOK_TYPE_PERSONAL_ALBUM_KEY, false);
		}

		if (types.contains(PhotoType.PERSONAL_TAG))
		{
			editor.putBoolean(SOURCE_FACEBOOK_TYPE_PERSONAL_TAG_KEY, true);
		}
		else
		{
			editor.putBoolean(SOURCE_FACEBOOK_TYPE_PERSONAL_TAG_KEY, false);
		}

		if (types.contains(PhotoType.FRIEND_ALBUM))
		{
			editor.putBoolean(SOURCE_FACEBOOK_TYPE_FRIEND_ALBUM_KEY, true);
		}
		else
		{
			editor.putBoolean(SOURCE_FACEBOOK_TYPE_FRIEND_ALBUM_KEY, false);
		}

		if (types.contains(PhotoType.FRIEND_TAG))
		{
			editor.putBoolean(SOURCE_FACEBOOK_TYPE_FRIEND_TAG_KEY, true);
		}
		else
		{
			editor.putBoolean(SOURCE_FACEBOOK_TYPE_FRIEND_TAG_KEY, false);
		}

		editor.commit();
	}

	public void setFacebookPhotoTypeOff(PhotoType type)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		switch (type)
		{
			case PERSONAL_ALBUM:
			{
				editor.putBoolean(SOURCE_FACEBOOK_TYPE_PERSONAL_ALBUM_KEY, false);

				break;
			}

			case PERSONAL_TAG:
			{
				editor.putBoolean(SOURCE_FACEBOOK_TYPE_PERSONAL_TAG_KEY, false);

				break;
			}

			case FRIEND_ALBUM:
			{
				editor.putBoolean(SOURCE_FACEBOOK_TYPE_FRIEND_ALBUM_KEY, false);

				break;
			}

			case FRIEND_TAG:
			{
				editor.putBoolean(SOURCE_FACEBOOK_TYPE_FRIEND_TAG_KEY, false);

				break;
			}
		}

		editor.commit();
	}

	public void setFacebookPhotoTypeOn(PhotoType type)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		switch (type)
		{
			case PERSONAL_ALBUM:
			{
				editor.putBoolean(SOURCE_FACEBOOK_TYPE_PERSONAL_ALBUM_KEY, true);

				break;
			}

			case PERSONAL_TAG:
			{
				editor.putBoolean(SOURCE_FACEBOOK_TYPE_PERSONAL_TAG_KEY, true);

				break;
			}

			case FRIEND_ALBUM:
			{
				editor.putBoolean(SOURCE_FACEBOOK_TYPE_FRIEND_ALBUM_KEY, true);

				break;
			}

			case FRIEND_TAG:
			{
				editor.putBoolean(SOURCE_FACEBOOK_TYPE_FRIEND_TAG_KEY, true);

				break;
			}
		}

		editor.commit();
	}

	public boolean isPlayMusic()
	{
		SharedPreferences preferences = getPreferences();

		return preferences.getBoolean(MUSIC_PLAY_KEY, false);
	}

	public void setPlayMusic(boolean play)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putBoolean(MUSIC_PLAY_KEY, play);

		editor.commit();
	}

	public long getAudioTrackSourceUpdateInterval()
	{
		SharedPreferences preferences = getPreferences();

		String tmp = preferences.getString(SOURCE_UPDATE_AUDIO_TRACKS_INTERVAL_KEY, "1800000");

		Long tmp2 = 1800000L;

		try
		{
			tmp2 = Long.parseLong(tmp);
		}
		catch (NumberFormatException e)
		{
		}

		return tmp2.intValue();
	}

	public void setAudioTrackSourceUpdateInterval(long interval)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putString(SOURCE_UPDATE_AUDIO_TRACKS_INTERVAL_KEY, "" + interval);

		editor.commit();
	}

	public Long getAudioTracksLastUpdateTime()
	{
		SharedPreferences preferences = getPreferences();

		return preferences.getLong(SOURCE_AUDIO_TRACKS_LAST_UPDATE_KEY, 0);
	}

	public void setAudioTracksLastUpdateTime(Long time)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putLong(SOURCE_AUDIO_TRACKS_LAST_UPDATE_KEY, time);

		editor.commit();
	}

	public Integer getCurrentSlideshowEntryId()
	{
		SharedPreferences preferences = getPreferences();

		Integer output = preferences.getInt(CURRENT_SLIDESHOW_ENTRY_KEY, -1);

		if (output == -1)
		{
			output = null;
		}

		return output;
	}

	public void setCurrentSlideshowEntryId(Integer id)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		if (id == null)
		{
			id = -1;
		}

		editor.putInt(CURRENT_SLIDESHOW_ENTRY_KEY, id);

		editor.commit();
	}

	public void removeCurrentSlideshowEntryId()
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.remove(CURRENT_SLIDESHOW_ENTRY_KEY);

		editor.commit();
	}

	public Integer getCurrentAudioPlaylistEntryId()
	{
		SharedPreferences preferences = getPreferences();

		Integer output = preferences.getInt(CURRENT_AUDIO_PLAYLIST_ENTRY_KEY, -1);

		if (output == -1)
		{
			output = null;
		}

		return output;
	}

	public void setCurrentAudioPlaylistEntryId(Integer id)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		if (id == null)
		{
			id = -1;
		}

		editor.putInt(CURRENT_AUDIO_PLAYLIST_ENTRY_KEY, id);

		editor.commit();
	}

	public void removeCurrentAudioPlaylistEntryId()
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.remove(CURRENT_AUDIO_PLAYLIST_ENTRY_KEY);

		editor.commit();
	}

	public Long getUIDisplayLifetime()
	{
		SharedPreferences preferences = getPreferences();

		return preferences.getLong(UI_DISPLAY_LIFETIME_KEY, 5000L);
	}

	public void setUIDisplayLifetime(long lifetime)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putLong(UI_DISPLAY_LIFETIME_KEY, lifetime);

		editor.commit();
	}

	public Long getAdDisplayLifetime()
	{
		SharedPreferences preferences = getPreferences();

		return preferences.getLong(AD_DISPLAY_LIFETIME_KEY, 15000L);
	}

	public void setAdDisplayLifetime(long lifetime)
	{
		SharedPreferences preferences = getPreferences();

		Editor editor = preferences.edit();

		editor.putLong(AD_DISPLAY_LIFETIME_KEY, lifetime);

		editor.commit();
	}

	public AndroidSlideshow getAndroidSlideshow()
	{
		return ass;
	}

	public void setAndroidSlideshow(AndroidSlideshow ass)
	{
		this.ass = ass;
	}
}
