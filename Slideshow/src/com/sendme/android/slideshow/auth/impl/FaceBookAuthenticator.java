package com.sendme.android.slideshow.auth.impl;

import android.os.Bundle;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.auth.AuthenticationException;
import com.sendme.android.slideshow.auth.Authenticator;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class FaceBookAuthenticator
extends Authenticator
implements DialogListener
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	private Facebook facebookApplication = null;

	public void initializeFacebookApplication()
	{
		facebookApplication = new Facebook(settingsManager.getFacebookApplicationId());

		facebookApplication.setAccessToken(settingsManager.getFacebookAuthorizationToken());

		facebookApplication.setAccessExpires(settingsManager.getFacebookAuthorizationTokenExpiration());
	}

	@Override
	public boolean needsAuthentication()
	throws AuthenticationException
	{
		if (facebookApplication == null)
		{
			initializeFacebookApplication();
		}

		boolean output = !facebookApplication.isSessionValid();

		return output;
	}

	@Override
	public void doAuthentication()
	throws AuthenticationException
	{
		if (facebookApplication == null)
		{
			initializeFacebookApplication();
		}

		String permissions[] = new String[]
		{
			"user_photos",
			"user_photo_video_tags",
			"friends_photos",
			"friends_photo_video_tags"
		};

		facebookApplication.authorize(activity, permissions, AndroidSlideshow.FACEBOOK_ACTIVITY_RESULT_CODE, this);
	}

	public void onComplete(Bundle bundle)
	{
		log.debug("Facebook authentication complete: " + bundle);

		settingsManager.setFacebookAuthorizationToken(facebookApplication.getAccessToken());

		settingsManager.setFacebookAuthorizationTokenExpiration(facebookApplication.getAccessExpires());

		listener.onAuthenticationComplete(requestCode);
	}

	public void onCancel()
	{
		listener.onAuthenticationCancelled(requestCode);
	}

	public void onError(DialogError de)
	{
		log.debug("Facebook authentication error", de);

		AuthenticationException error=new AuthenticationException("Error authenticating with Facebook.", de);

		listener.onAuthenticationError(requestCode, error);
	}

	public void onFacebookError(FacebookError fe)
	{
		log.debug("Facebook authentication error", fe);

		AuthenticationException error=new AuthenticationException("Error authenticating with Facebook.", fe);

		listener.onAuthenticationError(requestCode, error);
	}

	public Facebook getFacebookApplication()
	{
		return facebookApplication;
	}

	public void setFacebookApplication(Facebook facebookApplication)
	{
		this.facebookApplication = facebookApplication;
	}
}
