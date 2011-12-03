package com.sendme.android.slideshow.share;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;

public class FacebookRequestListener implements RequestListener {
	private final static AndroidLogger log = AndroidLogger
			.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	public void onComplete(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		log.debug("Upload video complete. (" + arg0 + ")");

	}

	public void onFacebookError(FacebookError arg0, Object arg1) {
		// TODO Auto-generated method stub
		log.debug("Upload video error. (" + arg0.getMessage() + ")");

	}

	public void onFileNotFoundException(FileNotFoundException arg0, Object arg1) {
		// TODO Auto-generated method stub
		log.debug("Upload video file not found. (" + arg0.getMessage() + ")");
	}

	public void onIOException(IOException arg0, Object arg1) {
		// TODO Auto-generated method stub
		log.debug("Upload video IO exception. (" + arg0.getMessage() + ")");
	}

	public void onMalformedURLException(MalformedURLException arg0, Object arg1) {
		// TODO Auto-generated method stub
		log.debug("Upload video onMalformedURLException. (" + arg0.getMessage()
				+ ")");
	}

}
