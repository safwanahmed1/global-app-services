package com.sendme.android.slideshow.controller;

import com.google.inject.Inject;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.image.ImageLoader;
import com.sendme.android.slideshow.task.heartbeat.HeartbeatTask;
import com.sendme.android.slideshow.task.heartbeat.HeartbeatTaskEventListener;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class AdController
extends Controller
implements HeartbeatTaskEventListener
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	@Inject
	private ImageLoader imageLoader = null;

	private SecureRandom secureRandom = null;
	
	private Integer deviceUniqueIdentifierHash = null;

	private Integer heartbeatIdentifier = null;

	private Integer currentRandomNumber = null;

	public AdController()
	{

	}

	@Override
	public void initialize()
	{
		log.debug("AdController initialized.");

		try
		{
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
		}
		catch (NoSuchAlgorithmException e)
		{
			log.error("Could not initialize randomizer.  Ads will not be displayed.", e);
		}
	}

	@Override
	public void start()
	{
		HeartbeatTask.addListener(this);
		
		log.debug("AdController started.");
	}

	@Override
	public void resume()
	{
		log.debug("AdController resumed.");
	}

	@Override
	public void pause()
	{
		log.debug("AdController paused.");
	}

	@Override
	public void stop()
	{
		HeartbeatTask.removeListener(this);
		
		log.debug("AdController stopped.");
	}

	@Override
	public void destroy()
	{
		log.debug("AdController destroyed.");
	}

	public void onHeartbeatTaskEvent(Long time)
	{
		if (secureRandom != null)
		{
			currentRandomNumber = secureRandom.nextInt();

			
		}
	}

	public Integer getHeartbeatIdentifier()
	{
		return heartbeatIdentifier;
	}

	public void setHeartbeatIdentifier(Integer heartbeatIdentifier)
	{
		this.heartbeatIdentifier = heartbeatIdentifier;
	}

	public Integer getCurrentRandomNumber()
	{
		return currentRandomNumber;
	}

	public void setCurrentRandomNumber(Integer currentRandomNumber)
	{
		this.currentRandomNumber = currentRandomNumber;
	}

	public Integer getDeviceUniqueIdentifierHash()
	{
		return deviceUniqueIdentifierHash;
	}

	public void setDeviceUniqueIdentifierHash(Integer deviceUniqueIdentifierHash)
	{
		this.deviceUniqueIdentifierHash = deviceUniqueIdentifierHash;
	}

	public ImageLoader getImageLoader()
	{
		return imageLoader;
	}

	public void setImageLoader(ImageLoader imageLoader)
	{
		this.imageLoader = imageLoader;
	}

	public SecureRandom getSecureRandom()
	{
		return secureRandom;
	}

	public void setSecureRandom(SecureRandom secureRandom)
	{
		this.secureRandom = secureRandom;
	}
}
