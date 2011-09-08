package global.services.sample.android.tasks;

import global.services.lib.android.factories.FileInfoFactory;
import global.services.sample.android.tasks.TaskListener.OnTaskFinishedListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class DownloadFileInfoToLocal extends AsyncTask<String, Integer, Boolean> {
	private String FILEINFO_FILE = "fileinfo.xml";
	private Context context;
	private ProgressDialog dialog;
	private OnTaskFinishedListener mOnTaskFinishedListener;
	public DownloadFileInfoToLocal(Context ctx) {
		context = ctx;
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
		dialog.setMessage("Downloading data...");
        dialog.show();
	}
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		FileInfoFactory fileInfoFactory = new FileInfoFactory(params[0]);
		String filesXML = fileInfoFactory.GetFilesXMLContent();
		FileOutputStream fos;
		try {
			if (context != null) {
				fos = context.openFileOutput(FILEINFO_FILE,
						Context.MODE_PRIVATE);
				fos.write(filesXML.getBytes());
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
