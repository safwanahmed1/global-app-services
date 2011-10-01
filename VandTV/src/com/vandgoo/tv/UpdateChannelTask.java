package com.vandgoo.tv;

import global.services.lib.android.objects.Advertisement;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.apache.http.util.ByteArrayBuffer;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.vandgoo.tv.TaskListener.OnTaskFinishedListener;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class UpdateChannelTask extends AsyncTask<String, Integer, Boolean> {
	private static final String CHANNEL_LIST_URL = "http://vietandtv.appspot.com/data/channels.xml";
	private static final String CHANNEL_FILE = "channels.xml";
	private RestClient channelRest = new RestClient(CHANNEL_LIST_URL);
	private Context ctx;
	private ProgressDialog dialog;
	private OnTaskFinishedListener mOnTaskFinishedListener;

	public UpdateChannelTask(Context context) {
		ctx = context;
		dialog = new ProgressDialog(context);
	}

	public void setOnTaskFinishedListener(OnTaskFinishedListener listener) {
		mOnTaskFinishedListener = listener;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (dialog.isShowing())
			dialog.dismiss();
		if (mOnTaskFinishedListener != null)
			mOnTaskFinishedListener.onTaskFinished(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog.setMessage("Downloading channel data...");
		dialog.show();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			channelRest.Execute(RequestMethod.POST);

			InputStream is = channelRest.getInstream();
			BufferedInputStream bis = new BufferedInputStream(is);

			FileOutputStream fos = null;

			fos = ctx.openFileOutput(CHANNEL_FILE, Context.MODE_PRIVATE);

			// fos = new FileOutputStream(imgFile);

			ByteArrayBuffer baf = new ByteArrayBuffer(1024);
			byte[] buffer = new byte[1024];

			int current = 0;
			while ((current = is.read(buffer)) != -1) {
				fos.write(buffer, 0, current);
			}
			fos.close();
			is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		StringBuffer fileContent = new StringBuffer("");
		try {

			byte[] buffer = new byte[1024];
			FileInputStream fis = ctx.openFileInput(CHANNEL_FILE);

			int length;
			while ((length = fis.read(buffer)) != -1) {
				fileContent.append(new String(buffer), 0, length);
			}
			
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		XmlPullParser chnXML;
		try {

			chnXML = XmlPullParserFactory.newInstance().newPullParser();
			chnXML.setInput(new StringReader(fileContent.toString()));
		} catch (XmlPullParserException e) {
			chnXML = null;
		}
		if (chnXML != null) {
			int eventType = -1;
			// boolean bFoundScores = false;

			// Find Score records from XML
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {

					// Get the name of the tag (eg scores or score)
					String strElemName = chnXML.getName();

					if (strElemName.equals("channel")) {
						// bFoundScores = true;
						
						String code = chnXML.getAttributeValue(null, "code");
						
						
					

						advList.add(advObj);

					}
				}
				try {
					eventType = chnXML.next();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		
		return true;
	}

}
