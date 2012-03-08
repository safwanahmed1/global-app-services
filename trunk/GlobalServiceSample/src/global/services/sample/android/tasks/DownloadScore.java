package global.services.sample.android.tasks;

import global.services.lib.android.factories.HighscoreFactory;
import global.services.lib.android.objects.Highscore;
import global.services.sample.android.tasks.TaskListener.OnTaskFinishedListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class DownloadScore extends AsyncTask<String, Integer, Boolean> {
	private static final String SCORE_FILE = "highscore.xml";
	private Context context;
	private ProgressDialog dialog;
	private OnTaskFinishedListener mOnTaskFinishedListener;
	private List<Highscore> scoreList;

	public DownloadScore(Context ctx) {
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
		if ((mOnTaskFinishedListener != null) && (result))
			mOnTaskFinishedListener.onTaskFinished(scoreList);

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
		HighscoreFactory scoreFactory = new HighscoreFactory(params[0],
				Long.parseLong(params[1]));
		if (params.length > 2)
			scoreFactory.setPageIdx(Integer.parseInt(params[2]));
		if (params.length > 3)
			scoreFactory.setPageSize(Integer.parseInt(params[3]));
		scoreList = scoreFactory.GetScores();
		return true;

	}

}
