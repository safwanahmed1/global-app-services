package com.sendme.android.slideshow.share;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.R;
import com.sendme.android.slideshow.runnable.AudioPlaybackUIAnimation;
import com.sendme.android.slideshow.runnable.TextUpdater;
import com.sendme.android.slideshow.source.MediaSource;
import com.sendme.android.slideshow.source.MediaSourceException;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import roboguice.inject.InjectView;

/**
 * 
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
@Singleton
public class FacebookAlbumShare extends MediaSource {
	private final static AndroidLogger log = AndroidLogger
			.getAndroidLogger(AndroidSlideshow.LOG_TAG);
	private Context context;
	private ContentResolver contentResolver = null;
	private FacebookAlbumShareTask albumShareTask;

	@InjectView(R.id.shareprogressText)
	private TextView shareProgress;
	@InjectView(R.id.shareprogressLayout)
	private View shareProgressLayout = null;

	public FacebookAlbumShare(Context context) {
		this.context = context;

	}

	public Facebook getFacebookApplication() {
		Facebook output = new Facebook(
				settingsManager.getFacebookApplicationId());

		output.setAccessToken(settingsManager.getFacebookAuthorizationToken());

		output.setAccessExpires(settingsManager
				.getFacebookAuthorizationTokenExpiration());

		return output;
	}

	FacebookShareTaskListener listener = new FacebookShareTaskListener() {

		private TextUpdater textUpdater;

		public void onFacebookeShareingSuccess() {
			// TODO Auto-generated method stub
			Log.d("Test listener", "Main - Upload image success"); // IT IS NOT
																	// NULL
			Toast.makeText(context,
					context.getString(R.string.slidescreenShareImageSuccess),
					Toast.LENGTH_SHORT).show();
			/*
			 * if (shareProgressLayout.getVisibility() != View.GONE) {
			 * AudioPlaybackUIAnimation anim = new AudioPlaybackUIAnimation();
			 * anim.setView(shareProgressLayout);
			 * anim.setAnimation(AnimationUtils.loadAnimation(
			 * shareProgressLayout.getContext(), R.anim.slide_menu_hide));
			 * anim.setFinalVisibility(View.GONE);
			 * shareProgressLayout.post(anim); }
			 */
		}

		public void onFacebookeShareingFail(String error) {
			// TODO Auto-generated method stub
			Log.d("Test listener", "Main - Upload image fail"); // IT IS NOT
																// NULL
			Toast.makeText(context,
					context.getString(R.string.slidescreenShareImageFail),
					Toast.LENGTH_LONG).show();
		}

		public void onFacebookeShareingProgress(int completed, int total) {
			// TODO Auto-generated method stub
			Toast.makeText(
					context,
					context.getString(R.string.slidescreenShareProgress)
							+ completed + "/" + total, Toast.LENGTH_SHORT)
					.show();
			/*
			 * textUpdater = new TextUpdater();
			 * 
			 * textUpdater.setTextView(shareProgress);
			 * textUpdater.setText(shareProgress.getContext().getString(
			 * R.string.slidescreenShareProgress) + completed + "/" + total);
			 * shareProgress.post(textUpdater);
			 */
		}

		public void onFacebookeShareingPrepare() {
			// TODO Auto-generated method stub
			if (shareProgressLayout == null)
				getView();
			
			textUpdater = new TextUpdater();

			textUpdater.setTextView(shareProgress);
			textUpdater.setText(shareProgress.getContext().getString(
					R.string.slidescreenShareProgress));
			shareProgress.post(textUpdater);
			if (shareProgressLayout.getVisibility() != View.VISIBLE) {
				AudioPlaybackUIAnimation anim = new AudioPlaybackUIAnimation();
				anim.setView(shareProgressLayout);
				anim.setAnimation(AnimationUtils.loadAnimation(
						shareProgressLayout.getContext(),
						R.anim.slide_menu_show));
				anim.setFinalVisibility(View.VISIBLE);
				shareProgressLayout.post(anim);
			}

		}

	};

	public void ShareAlbum() {

		Facebook facebookApplication = getFacebookApplication();
		albumShareTask = new FacebookAlbumShareTask(facebookApplication);
		albumShareTask.setListener(listener);
		albumShareTask.setSettingsManager(settingsManager);
		albumShareTask.setSlideshowManager(slideshowManager);
		albumShareTask.setContentResolver(contentResolver);
		albumShareTask.execute();

	}

	@Override
	public void checkForUpdates(Long time, boolean notify)
			throws MediaSourceException {
		// TODO Auto-generated method stub

	}

	public ContentResolver getContentResolver() {
		return contentResolver;
	}

	public void setContentResolver(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

	public void pause() {
		// TODO Auto-generated method stub
		if (albumShareTask != null)
			albumShareTask.cancel(false);

	}

	private void getView() {
		LayoutInflater vi = (LayoutInflater) this.settingsManager
				.getAndroidSlideshow().getSystemService(
						Context.LAYOUT_INFLATER_SERVICE);
		View shareProgressBar = vi.inflate(R.layout.share_slideshow_progress,
				null);

		// imagePath = filePath;
		shareProgress = (TextView) shareProgressBar
				.findViewById(R.id.shareprogressText);
		shareProgressLayout = (View) shareProgressBar
				.findViewById(R.id.shareprogressLayout);

	}

}
