package com.sendme.android.slideshow.share;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.google.inject.Singleton;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.R;
import com.sendme.android.slideshow.runnable.AudioPlaybackUIAnimation;
import com.sendme.android.slideshow.runnable.TextUpdater;
import com.sendme.android.slideshow.source.MediaSource;
import com.sendme.android.slideshow.source.MediaSourceException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONObject;

import roboguice.inject.InjectView;

/**
 * 
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
@Singleton
public class FacebookVideoShare extends MediaSource {
	private final static AndroidLogger log = AndroidLogger
			.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	/*
	 * @InjectView(R.id.slidetextLayout) private View textLayout = null;
	 * 
	 * @InjectView(R.id.slidescreenText) private TextView slideMessage = null;
	 */
	private Context context;
	FacebookShareTaskListener listener = new FacebookShareTaskListener() {

		public void onFacebookeShareingSuccess() {
			// TODO Auto-generated method stub
			Log.d("Test listener", "Main - Upload video success"); // IT IS NOT
																	// NULL
			Toast.makeText(context,
					context.getString(R.string.slidescreenShareVideoSuccess),
					Toast.LENGTH_LONG);

			/*
			 * TextUpdater textUpdater = new TextUpdater();
			 * 
			 * textUpdater.setTextView(slideMessage);
			 * textUpdater.setText(slideMessage.getContext().getString(
			 * R.string.slidescreenShareVideoSuccess));
			 * slideMessage.post(textUpdater); AudioPlaybackUIAnimation anim =
			 * new AudioPlaybackUIAnimation(); anim.setView(textLayout);
			 * anim.setAnimation(AnimationUtils.loadAnimation(
			 * textLayout.getContext(), R.anim.slide_menu_show));
			 * anim.setFinalVisibility(View.VISIBLE); textLayout.post(anim);
			 * anim.setAnimation(AnimationUtils.loadAnimation(
			 * textLayout.getContext(), R.anim.slide_menu_hide));
			 * anim.setFinalVisibility(View.GONE); textLayout.post(anim);
			 */
		}

		public void onFacebookeShareingFail(String error) {
			// TODO Auto-generated method stub
			Log.d("Test listener", "Main - Upload video fail"); // IT IS NOT
																// NULL
			Toast.makeText(context,
					context.getString(R.string.slidescreenShareVideoFail),
					Toast.LENGTH_LONG);
		}

	};

	public FacebookVideoShare(Context context) {
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

	@SuppressWarnings("unused")
	public void ShareVideo(String videoPath) throws MediaSourceException {
		String filePath = "/mnt/sdcard/sample.3gp";
		String dataMsg = "Your video description here.";
		String dataName = "sample.3gpp";
		String query = "me/videos";
		JSONObject output = null;
		RequestListener reqListener = new FacebookRequestListener();
		Facebook facebookApplication = getFacebookApplication();
		FacebookVideoShareTask videoShareTask = new FacebookVideoShareTask(
				facebookApplication, videoPath);
		videoShareTask.setListener(listener);
		videoShareTask.execute();

	}

	public byte[] readBytes(InputStream inputStream) throws IOException {
		// this dynamically extends to take the bytes you read
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

		// this is storage overwritten on each iteration with bytes
		int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];

		// we need to know how may bytes were read to write them to the
		// byteBuffer
		int len = 0;
		while ((len = inputStream.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}

		// and then we can return your byte array.
		return byteBuffer.toByteArray();
	}

	@Override
	public void checkForUpdates(Long time, boolean notify)
			throws MediaSourceException {
		// TODO Auto-generated method stub

	}

}
