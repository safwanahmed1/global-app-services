package com.sendme.android.slideshow.runnable;

import com.sendme.android.slideshow.activity.SplashScreenActivity;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class SplashScreenPause
implements Runnable
{
	private SplashScreenActivity splashScreenActivity = null;
	
	public SplashScreenPause()
	{

	}

	public void run()
	{
		splashScreenActivity.showPreferences();
	}

	public SplashScreenActivity getSplashScreenActivity()
	{
		return splashScreenActivity;
	}

	public void setSplashScreenActivity(SplashScreenActivity splashScreenActivity)
	{
		this.splashScreenActivity = splashScreenActivity;
	}
}
