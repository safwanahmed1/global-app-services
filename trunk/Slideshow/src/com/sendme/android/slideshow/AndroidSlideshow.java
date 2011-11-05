package com.sendme.android.slideshow;

import android.content.Context;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.image.ImageLoader;
import com.sendme.android.slideshow.manager.DatabaseManager;
import com.sendme.android.slideshow.task.heartbeat.HeartbeatTask;
import roboguice.application.RoboInjectableApplication;
import roboguice.inject.InjectorProvider;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class AndroidSlideshow
extends RoboInjectableApplication
{
	public final static String LOG_TAG = "ANDROID_SLIDESHOW";

	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(LOG_TAG);

	public final static int SPLASH_ACTIVITY_RESULT_CODE = 5322472;

	public final static int ABOUT_ACTIVITY_RESULT_CODE = 2389472;

	public final static int PREFERENCES_ACTIVITY_RESULT_CODE = 2312346;

	public final static int FACEBOOK_ACTIVITY_RESULT_CODE = 8324823;

	public final static int FACEBOOK_AUTHENTICATION_RESULT_CODE = 8324823;

	public final static int DATABASE_ERROR_DIALOG=0;

	public final static int LOCAL_IMAGE_SOURCE_ERROR_DIALOG=1;

	public final static int FACEBOOK_IMAGE_SOURCE_ERROR_DIALOG=2;

	private static AndroidSlideshow ass = null;

	@Inject
	private DatabaseManager databaseManager = null;

	@Inject
	private HeartbeatTask heartbeat = null;

	public AndroidSlideshow()
	{
	}

	public static void performInjection(Object obj)
	{
		Context context = ass.getApplicationContext();

		InjectorProvider provider = (InjectorProvider) context;

		Injector injector = provider.getInjector();

		injector.injectMembers(obj);
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

		ass = this;

		heartbeat.execute(1000L);
	}

	@Override
	public void onLowMemory()
	{
		super.onLowMemory();

		log.debug("Application onLowMemory called...");

		ImageLoader.cleanCache();
	}

	@Override
	public void onTerminate()
	{
		super.onTerminate();

		log.debug("Application onTerminate called...");

		heartbeat.stop();

		databaseManager.close();
	}

	public static AndroidSlideshow getAndroidSlideshow()
	{
		return ass;
	}

	public static void setAndroidSlideshow(AndroidSlideshow ass)
	{
		AndroidSlideshow.ass = ass;
	}

	public HeartbeatTask getHeartbeat()
	{
		return heartbeat;
	}

	public void setHeartbeat(HeartbeatTask heartbeat)
	{
		this.heartbeat = heartbeat;
	}
}
