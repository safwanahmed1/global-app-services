package com.seta.android.game.wordsearch.view;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class SplashScreen extends Activity {
	protected boolean _active = true;
	protected int _splashTime = 5000; // time to display the splash screen in ms
	private boolean act = true;

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		setContentView(R.layout.splash);
	
		
		
		final Context myContext = this;
		// thread for displaying the SplashScreen
		Thread splashThread = new Thread() {
			@Override
			public void run() {
				try {
					int waited = 0;
					LinearLayout mlayout = (LinearLayout)findViewById(R.id.splash);
					Animation slide = AnimationUtils.loadAnimation(myContext, R.anim.animation);
					mlayout.startAnimation(slide);
					while (_active && (waited < _splashTime)) {
						sleep(100);
						if (_active) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
					// do nothing
				} finally {
					if (act == true) {
						finish();
						startActivity(new Intent(myContext,WordSearchActivity.class));
						act = false;
					}
				}
			}
		};
		splashThread.start();
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			_active = false;
		}
		return true;
	}

	
	
}
