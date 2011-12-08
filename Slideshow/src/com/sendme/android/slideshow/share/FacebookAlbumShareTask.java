package com.sendme.android.slideshow.share;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.Facebook;
import com.google.inject.Inject;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.manager.ORMException;
import com.sendme.android.slideshow.manager.SettingsManager;
import com.sendme.android.slideshow.manager.SlideshowManager;
import com.sendme.android.slideshow.model.Photo;
import com.sendme.android.slideshow.model.SlideshowEntry;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

/**
 * 
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
public class FacebookAlbumShareTask extends AsyncTask<Void, Float, Void> {
	private final static AndroidLogger log = AndroidLogger
			.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	@Inject
	private ContentResolver contentResolver = null;
	@Inject
	private SlideshowManager slideshowManager = null;
	@Inject
	private SettingsManager settingsManager = null;	
	
	private Facebook facebookApplication;
	private String albumName = "Slideshow sharing album - "
			+ DateFormat.getDateTimeInstance().format(new Date());
	private boolean retValue;
	private String errMessage;
	private String albumId;
	private Integer photoId;


	public SettingsManager getSettingsManager() {
		return settingsManager;
	}

	public void setSettingsManager(SettingsManager settingsManager) {
		this.settingsManager = settingsManager;
	}

	public Facebook getFacebookApplication() {
		return facebookApplication;
	}

	public void setFacebookApplication(Facebook facebookApplication) {
		this.facebookApplication = facebookApplication;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	public String getAlbumId() {
		return albumId;
	}

	public void setAlbumId(String albumId) {
		this.albumId = albumId;
	}

	public Integer getPhotoId() {
		return photoId;
	}

	public void setPhotoId(Integer photoId) {
		this.photoId = photoId;
	}

	public SlideshowManager getSlideshowManager() {
		return slideshowManager;
	}

	public void setSlideshowManager(SlideshowManager slideshowManager) {
		this.slideshowManager = slideshowManager;
	}

	FacebookShareTaskListener listener;

	public FacebookShareTaskListener getListener() {
		return listener;
	}

	public void setListener(FacebookShareTaskListener listener) {
		this.listener = listener;
	}

	public FacebookAlbumShareTask(Facebook fbApp) {

		facebookApplication = fbApp;
		// imagePath = filePath;

	}

	@Override
	protected Void doInBackground(Void... params) {
		Bundle bundleParams = new Bundle();
		bundleParams.putString("name", albumName);
		String response = null;
		try {
			response = facebookApplication.request("me/albums", bundleParams,
					"POST");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(response);

			albumId = jsonObj.getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < settingsManager.getSlideshowSize(); i++) {
			try {
				SlideshowEntry entry = slideshowManager.getNextEntry(photoId);
				Photo photo = slideshowManager.getPhotoManager().find(entry.getPhotoId());
				
				//String photoPath = 
				UploadPhoto(Uri.parse(photo.getURI()));
				photoId = entry.getId();
			} catch (ORMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//UploadPhoto("/mnt/sdcard/image/doremon.jpg");
		}
		return null;
	}

	private Void UploadPhoto(Uri uri) {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient.getParams().setParameter(
				CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
		HttpPost httppost = new HttpPost("https://graph.facebook.com/"
				+ albumId + "/photos");
		Context ctx = settingsManager.getAndroidSlideshow();
		
		InputStream is = null;
		try {
			is = contentResolver.openInputStream(uri);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//File file = new File(photoPath);
		String fbAccessToken = facebookApplication.getAccessToken();
		// DEBUG
		//Log.d("TSET", "FILE::" + file.exists()); // IT IS NOT NULL
		Log.d("TEST", "AT:" + fbAccessToken); // I GOT SOME ACCESS TOKEN

		MultipartEntity mpEntity = new MultipartEntity();
		//ContentBody cbFile = new FileBody(file, "image/png");
		ContentBody cbFile = new InputStreamBody(is, "Test");
		ContentBody message = null;
		ContentBody cbAccessToken = null;
		try {
			message = new StringBody(
					"Photo shared via Slideshow Magic Maker application");
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
		if (listener != null) {
			if (retValue) {
				Log.d("Test listener", "Task - Upload image success"); // IT IS
																		// NOT
																		// NULL
				listener.onFacebookeShareingSuccess();
			} else {
				listener.onFacebookeShareingFail(errMessage);
			}
		}
	}
	
	


}
