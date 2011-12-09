package com.sendme.android.slideshow.share;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.google.inject.Singleton;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.R;
import com.sendme.android.slideshow.source.MediaSource;
import com.sendme.android.slideshow.source.MediaSourceException;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
@Singleton
public class FacebookImageShare extends MediaSource {
	private final static AndroidLogger log = AndroidLogger
			.getAndroidLogger(AndroidSlideshow.LOG_TAG);
	private Context context;
	public FacebookImageShare(Context context) {
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

		public void onFacebookeShareingSuccess() {
			// TODO Auto-generated method stub
			Log.d("Test listener", "Main - Upload image success"); // IT IS NOT
																	// NULL
			Toast.makeText(context,
					context.getString(R.string.slidescreenShareImageSuccess),
					Toast.LENGTH_LONG).show();

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
			Log.d("Test listener", "Main - Upload image fail"); // IT IS NOT
																// NULL
			Toast.makeText(context,
					context.getString(R.string.slidescreenShareImageFail),
					Toast.LENGTH_LONG).show();
		}

		public void onFacebookeShareingProgress(int completed, int total) {
			// TODO Auto-generated method stub
			
		}

		public void onFacebookeShareingPrepare() {
			// TODO Auto-generated method stub
			
		}

	};

	public void ShareImage(String imagePath) {
		/*
		String query = "me/photos";
		
		RequestListener reqListener = new FacebookRequestListener();
		byte[] data = null;
		
		Facebook facebookApplication = getFacebookApplication();
		
		AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(
				facebookApplication);
		
		InputStream is = null;
		try {
			is = new FileInputStream(imagePath);
			data = readBytes(is);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Bundle params = new Bundle();
		//params.putString("method", "photos.upload");
		params.putByteArray("picture", data);

		mAsyncRunner = new AsyncFacebookRunner(facebookApplication);
		mAsyncRunner.request(query, params, "POST", reqListener, null);

		// return output;
		*/
		Facebook facebookApplication = getFacebookApplication();
		FacebookImageShareTask videoShareTask = new FacebookImageShareTask(
				facebookApplication, imagePath);
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
