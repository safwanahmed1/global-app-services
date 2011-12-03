package com.sendme.android.slideshow.auth;

import android.app.Activity;
import android.content.Context;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.sendme.android.slideshow.manager.SettingsManager;
import roboguice.inject.InjectorProvider;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public abstract class Authenticator
{
	protected Activity activity = null;

	@Inject
	protected SettingsManager settingsManager = null;

	protected AuthenticationListener listener = null;

	protected Integer requestCode = null;

	private boolean injected = false;

	public abstract boolean needsAuthentication()
	throws AuthenticationException;

	public abstract void doAuthentication()
	throws AuthenticationException;

	public final void authenticate(int requestCode)
	throws AuthenticationException
	{
		setRequestCode(requestCode);

		if (!injected)
		{
			Context context = activity.getApplicationContext();

			InjectorProvider provider = (InjectorProvider) context;

			Injector injector = provider.getInjector();

			injector.injectMembers(this);
			
			injected = true;
		}

		doAuthentication();
	}

	public Activity getActivity()
	{
		return activity;
	}

	public void setActivity(Activity activity)
	{
		this.activity = activity;
	}

	public SettingsManager getSettingsManager()
	{
		return settingsManager;
	}

	public void setSettingsManager(SettingsManager settingsManager)
	{
		this.settingsManager = settingsManager;
	}

	public AuthenticationListener getListener()
	{
		return listener;
	}

	public void setListener(AuthenticationListener listener)
	{
		this.listener = listener;
	}

	public Integer getRequestCode()
	{
		return requestCode;
	}

	public void setRequestCode(Integer requestCode)
	{
		this.requestCode = requestCode;
	}
}
