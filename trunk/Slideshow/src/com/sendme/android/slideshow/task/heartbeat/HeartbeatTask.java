package com.sendme.android.slideshow.task.heartbeat;

import android.os.AsyncTask;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class HeartbeatTask
extends AsyncTask<Long, Long, Void>
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	private final static AtomicInteger IDENTIFIER_GENERATOR = new AtomicInteger(0);

	private static final ReentrantLock lock = new ReentrantLock(true);

	private static final Condition condition = lock.newCondition();

	private static Map<Integer, HeartbeatTaskEventListener> listeners = new HashMap<Integer, HeartbeatTaskEventListener>();

	private boolean started = false;
	
	private boolean paused = false;

	public HeartbeatTask()
	{

	}

	@Override
	protected Void doInBackground(Long... params)
	{
		started = true;
		
		while (!isCancelled())
		{
			try
			{
				lock.lockInterruptibly();

				if (paused)
				{
					log.debug("Heartbeat paused and waiting...");
					
					condition.await();
				}
				else
				{
					condition.await(params[0], TimeUnit.MILLISECONDS);
				}

				if (!paused && !isCancelled())
				{
					for (HeartbeatTaskEventListener listener : listeners.values())
					{
						listener.onHeartbeatTaskEvent(System.currentTimeMillis());
					}
				}
			}
			catch (InterruptedException e)
			{
			}
			finally
			{
				lock.unlock();
			}
		}

		log.debug("Heartbeat stopped...");

		started = false;
		
		return null;
	}

	public void pause()
	{
		lock.lock();

		try
		{
			setPaused(true);
			
			condition.signal();
		}
		finally
		{
			lock.unlock();
		}
	}

	public void resume()
	{
		lock.lock();

		try
		{
			setPaused(false);

			condition.signal();
		}
		finally
		{
			lock.unlock();
		}
	}

	public void stop()
	{
		cancel(false);

		lock.lock();

		try
		{
			condition.signal();
		}
		finally
		{
			lock.unlock();
		}
	}

	public static void addListener(HeartbeatTaskEventListener listener)
	{
		lock.lock();

		try
		{
			Integer id = listener.getHeartbeatIdentifier();
			
			if (id == null)
			{
				id = IDENTIFIER_GENERATOR.getAndIncrement();

				listener.setHeartbeatIdentifier(id);
			}

			log.info("I have " + listeners.size() + " listeners to broadcast to.");
			
			if (!listeners.containsKey(id))
			{
				listeners.put(id, listener);
			}
		}
		finally
		{
			lock.unlock();
		}
	}

	public static void removeListener(HeartbeatTaskEventListener listener)
	{
		lock.lock();

		try
		{
			listeners.remove(listener.getHeartbeatIdentifier());
		}
		finally
		{
			lock.unlock();
		}
	}

	public boolean isStarted()
	{
		return started;
	}

	public void setStarted(boolean started)
	{
		this.started = started;
	}

	public boolean isPaused()
	{
		return paused;
	}

	private void setPaused(boolean paused)
	{
		this.paused = paused;
	}
}
