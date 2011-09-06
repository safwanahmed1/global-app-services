package global.services.sample.android.tasks;

import global.services.lib.android.factories.HighscoreFactory;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class DownLoadScoreToLocal extends AsyncTask<String, Integer, Void> {
	private static final String SCORE_FILE = "highscore.xml";
	private Context context;
	private ProgressDialog dialog;
	private OnTaskFinishedListener mOnTaskFinishedListener;

	public DownLoadScoreToLocal(Context ctx) {
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
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		dialog.setMessage("Downloading data...");
		dialog.show();
	}

	@Override
	protected Void doInBackground(String... params) {
		HighscoreFactory scoreFactory = new HighscoreFactory(params[0], Long
				.parseLong(params[1]));
		String scoresXML = scoreFactory.GetScoresXMLContent();
		FileOutputStream fos = null;
		if (scoresXML != null) {
			try {
				fos = context.openFileOutput(SCORE_FILE, Context.MODE_PRIVATE);
				fos.write(scoresXML.getBytes());
				fos.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (mOnTaskFinishedListener != null) {

			mOnTaskFinishedListener.onTaskFinished(true);
		}
		return null;
	}

	public interface OnTaskFinishedListener {
		/**
		 * Occurs when import is finished.
		 * 
		 * @param importSuccessful
		 *            Indicates whether import was successful.
		 * @param folderId
		 *            Contains id of imported folder, or -1 if multiple folders
		 *            were imported.
		 */
		void onTaskFinished(boolean success);
	}

}
