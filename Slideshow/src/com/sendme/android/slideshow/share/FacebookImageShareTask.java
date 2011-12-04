package com.sendme.android.slideshow.share;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import com.facebook.android.Facebook;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import android.os.AsyncTask;
import android.util.Log;

/**
 *
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class FacebookImageShareTask
extends AsyncTask<Void, Float, Void>
{
	private final static AndroidLogger log = AndroidLogger.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	private Facebook facebookApplication;
	private String imagePath;
	private boolean retValue;
	private String errMessage;
	FacebookShareTaskListener listener;

	public FacebookShareTaskListener getListener() {
		return listener;
	}

	public void setListener(FacebookShareTaskListener listener) {
		this.listener = listener;
	}

	public FacebookImageShareTask(Facebook fbApp, String filePath)
	{
		facebookApplication = fbApp;
		imagePath = filePath;

	}

	@Override
	protected Void doInBackground(Void... params)
	{
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httppost = new HttpPost("https://graph.facebook.com/me/photos");
		File file = new File(imagePath);
		String fbAccessToken = facebookApplication.getAccessToken();
		// DEBUG
		Log.d("TSET", "FILE::" + file.exists()); // IT IS NOT NULL
		Log.d("TEST", "AT:" + fbAccessToken); // I GOT SOME ACCESS TOKEN

		MultipartEntity mpEntity = new MultipartEntity();
		ContentBody cbFile = new FileBody(file, "image/png");
		ContentBody message = null;
		ContentBody cbAccessToken = null;
		try {
			message = new StringBody("Photo shared via Slideshow Magic Maker application");
			cbAccessToken = new StringBody(fbAccessToken);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retValue = false;
			errMessage = e.getMessage();
			return null;
		}

		mpEntity.addPart("access_token", cbAccessToken);
		mpEntity.addPart("source", cbFile);
		mpEntity.addPart("message", message);

		httppost.setEntity(mpEntity);

		// DEBUG
		System.out.println("executing request " + httppost.getRequestLine());
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retValue = false;
			errMessage = e.getMessage();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			retValue = false;
			errMessage = e.getMessage();
			return null;
		}
		HttpEntity resEntity = response.getEntity();

		// DEBUG
		System.out.println(response.getStatusLine());
		if (resEntity != null) {
			try {
				System.out.println(EntityUtils.toString(resEntity));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				retValue = false;
				errMessage = e.getMessage();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				retValue = false;
				errMessage = e.getMessage();
				return null;
			}
		} // end if

		if (resEntity != null) {
			try {
				resEntity.consumeContent();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				retValue = false;
				errMessage = e.getMessage();
				return null;
			}
		} // end if

		httpclient.getConnectionManager().shutdown();
		retValue = true;
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (listener!= null) {
			if (retValue) {
			Log.d("Test listener", "Task - Upload image success"); // IT IS NOT NULL
			listener.onFacebookeShareingSuccess();
			} else {
				listener.onFacebookeShareingFail(errMessage);
			}
		}
	}
	

	
}
