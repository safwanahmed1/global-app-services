package com.sendme.android.slideshow.share;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONException;
import org.json.JSONObject;

import roboguice.inject.InjectView;

import com.facebook.android.Facebook;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sendme.android.logging.AndroidLogger;
import com.sendme.android.slideshow.AndroidSlideshow;
import com.sendme.android.slideshow.R;
import com.sendme.android.slideshow.manager.ORMException;
import com.sendme.android.slideshow.manager.SettingsManager;
import com.sendme.android.slideshow.manager.SlideshowManager;
import com.sendme.android.slideshow.model.Photo;
import com.sendme.android.slideshow.model.SlideshowEntry;
import com.sendme.android.slideshow.runnable.AudioPlaybackUIAnimation;
import com.sendme.android.slideshow.runnable.TextUpdater;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * 
 * @author Todd Tidwell <ttidwell@sendme.com>
 */
@Singleton
public class FacebookAlbumShareTask extends AsyncTask<Void, Integer, Void> {
	private final static AndroidLogger log = AndroidLogger
			.getAndroidLogger(AndroidSlideshow.LOG_TAG);

	@Inject
	private ContentResolver contentResolver = null;
	@Inject
	private SlideshowManager slideshowManager = null;
	@Inject
	private SettingsManager settingsManager = null;

	private String message;

	private Facebook facebookApplication;
	private String albumName = "Slideshow sharing album - "
			+ DateFormat.getDateTimeInstance().format(new Date());
	private boolean retValue;
	private String errMessage;
	private String albumId;
	private Integer photoId;

	public ContentResolver getContentResolver() {
		return contentResolver;
	}

	public void setContentResolver(ContentResolver contentResolver) {
		this.contentResolver = contentResolver;
	}

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

	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (listener != null)
			listener.onFacebookeShareingPrepare();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		if (listener != null)
			listener.onFacebookeShareingProgress(values[0], values[1]);

	}

	@Override
	protected Void doInBackground(Void... params) {

		Bundle bundleParams = new Bundle();
		if ((message != null) || (!message.equals("")))
			albumName = message;
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

		try {
			List<Integer> ids = slideshowManager.getPhotoManager()
					.getEligiblePhotosForSlideshow();

			int limit = settingsManager.getSlideshowSize();

			if (limit == 0 || limit > ids.size()) {
				limit = ids.size();
			}
			//photoId = settingsManager.getCurrentSlideshowEntryId();
			for (int i = 0; i < limit; ) {
				if (isCancelled())
					break;
				
				SlideshowEntry entry = slideshowManager.getNextEntry(photoId);
				if (entry.getPhotoId() != null) {
					Photo photo = slideshowManager.getPhotoManager().find(
							entry.getPhotoId());

					UploadPhoto(Uri.parse(photo.getURI()));
					i++;
					publishProgress(i, limit);
				}
				photoId = entry.getId();

				// UploadPhoto("/mnt/sdcard/image/doremon.jpg");
			}

		} catch (ORMException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
		if (uri.getPath().contains("external")) {
			try {
				is = contentResolver.openInputStream(uri);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

			HttpClient httpClient = new DefaultHttpClient();

			HttpGet get = new HttpGet(uri.toString());

			HttpResponse httpResponse;

			try {
				httpResponse = httpClient.execute(get);
				is = httpResponse.getEntity().getContent();
			} catch (ClientProtocolException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();

			} catch (IllegalStateException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}

		// File file = new File(photoPath);
		String fbAccessToken = facebookApplication.getAccessToken();
		// DEBUG
		// Log.d("TSET", "FILE::" + file.exists()); // IT IS NOT NULL
		Log.d("TEST", "AT:" + fbAccessToken); // I GOT SOME ACCESS TOKEN

		MultipartEntity mpEntity = new MultipartEntity();
		// ContentBody cbFile = new FileBody(file, "image/png");
		ContentBody cbFile = new InputStreamBody(is, "Test");
		ContentBody messageBody = null;
		ContentBody cbAccessToken = null;
		try {

			messageBody = new StringBody(
					"Photo shared via Slideshow Magic Maker application (I'm testing my app, pls do not comment:)))");
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
		mpEntity.addPart("message", messageBody);

		httppost.setEntity(mpEntity);

		// DEBUG
		System.out.println("executing request " + httppost.getRequestLine());
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
			is.close();
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/*
	 * public String getRealPathFromImageURI(Uri contentUri) { String[] proj = {
	 * MediaStore.Images.Media.DATA }; Cursor cursor =
	 * settingsManager.getAndroidSlideshow().managedQuery( contentUri, proj,
	 * null, null, null); if (cursor != null) { int column_index = cursor
	 * .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	 * cursor.moveToFirst(); return cursor.getString(column_index); } return
	 * null; }
	 */

}
