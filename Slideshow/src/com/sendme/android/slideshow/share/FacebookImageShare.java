package com.sendme.android.slideshow.share;

import android.os.Bundle;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook;
import com.google.inject.Singleton;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
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

	public FacebookImageShare() {
	}

	public Facebook getFacebookApplication() {
		Facebook output = new Facebook(
				settingsManager.getFacebookApplicationId());

		output.setAccessToken(settingsManager.getFacebookAuthorizationToken());

		output.setAccessExpires(settingsManager
				.getFacebookAuthorizationTokenExpiration());

		return output;
	}

	public void ShareImage(String imagePath) {
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
