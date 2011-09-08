package global.services.sample.android.tasks;

import global.services.lib.android.factories.AdvertisementFactory;
import global.services.sample.android.tasks.TaskListener.OnTaskFinishedListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class DownloadAdvToLocal extends AsyncTask<String, Integer, Boolean> {
	private String ADVERTISEMENT_FILE = "advertisement.xml";
	private Context context;
	private ProgressDialog dialog;
	private OnTaskFinishedListener mOnTaskFinishedListener;
	public DownloadAdvToLocal(Context ctx) {
		context = ctx;
		dialog = new ProgressDialog(context);
	}
	public void setOnTaskFinishedListener(OnTaskFinishedListener listener) {
		mOnTaskFinishedListener = listener;
	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
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
		dialog.setMessage("Downloading data...");
		dialog.show();
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		AdvertisementFactory advFactory = new AdvertisementFactory(params[0]);
		String advsXML = advFactory.GetAdvsXMLContent();
		FileOutputStream fos;
		try {
			if (context != null) {
				fos = context.openFileOutput(ADVERTISEMENT_FILE,
						Context.MODE_PRIVATE);
				fos.write(advsXML.getBytes());
				fos.close();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
}
