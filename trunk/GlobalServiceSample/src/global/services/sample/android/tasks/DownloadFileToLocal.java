package global.services.sample.android.tasks;

import global.services.lib.android.factories.FileInfoFactory;
import global.services.sample.android.tasks.TaskListener.OnTaskFinishedListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class DownloadFileToLocal extends AsyncTask<String, Integer, Boolean> {
	private Context context;
	private ProgressDialog dialog;
	private OnTaskFinishedListener mOnTaskFinishedListener;

	public DownloadFileToLocal(Context ctx) {
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
		FileInfoFactory fileFactory = new FileInfoFactory(params[0]);
		InputStream isFile = fileFactory.Download(Long.parseLong(params[1]));
		FileOutputStream fos;
		try {
			if (context != null) {
//				fos = context.openFileOutput(params[2],
//						Context.MODE_PRIVATE);
				File file = new File(params[2]);
				fos  = new FileOutputStream(file);
				
				byte[] buffer = new byte[1024];

				int length = 0;
				while ((length = isFile.read(buffer)) != -1) {
					fos.write(buffer, 0, length);
				}
				fos.flush();
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
