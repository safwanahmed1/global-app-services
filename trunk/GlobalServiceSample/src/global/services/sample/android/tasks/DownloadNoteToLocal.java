package global.services.sample.android.tasks;

import global.services.lib.android.factories.NotificationFactory;
import global.services.sample.android.tasks.TaskListener.OnTaskFinishedListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class DownloadNoteToLocal extends AsyncTask<String, Integer, Boolean> {
	private Context context;
	private ProgressDialog dialog;
	private static final String NOTIFICATION_FILE = "notification.xml";
	private OnTaskFinishedListener mOnTaskFinishedListener;

	public DownloadNoteToLocal(Context ctx) {
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
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
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
		NotificationFactory noteFactory = new NotificationFactory(params[0], Long.parseLong(params[1]));
		String notesXML = noteFactory.GetNotesXMLContent();
		FileOutputStream fos;
		try {
			if (context != null) {
				fos = context.openFileOutput(NOTIFICATION_FILE,
						Context.MODE_PRIVATE);
				fos.write(notesXML.getBytes());
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
