package com.sendme.android.slideshow.runnable;

import android.view.View;
import android.view.animation.Animation;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class AudioPlaybackUIAnimation
implements Runnable
{
	private int finalVisibility = View.VISIBLE;

	private View view = null;

	private Animation animation = null;

	public AudioPlaybackUIAnimation()
	{
		
	}
	

	public void run()
	{
		view.startAnimation(animation);
		
		view.setVisibility(finalVisibility);
	}

	public int getFinalVisibility()
	{
		return finalVisibility;
	}

	public void setFinalVisibility(int finalVisibility)
	{
		this.finalVisibility = finalVisibility;
	}

	public Animation getAnimation()
	{
		return animation;
	}

	public void setAnimation(Animation animation)
	{
		this.animation = animation;
	}

	public View getView()
	{
		return view;
	}
	public void setView(View view)
	{
		this.view = view;
	}
	
	
}
