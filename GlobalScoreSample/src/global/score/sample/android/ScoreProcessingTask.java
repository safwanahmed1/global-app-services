package global.score.sample.android;

import java.io.IOException;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ScoreProcessingTask extends AsyncTask<Integer, Integer, Boolean> {
	protected Context mContext;
	private ProgressBar mProgressBar;
	private OnScoreProcessingFinishedListener mOnProcessingFinishedListener;
	private String mTypeProcessing;
	private boolean bOnBackground = false;
	private GlobalDatabase mDatabase;
	private String mLevel;
	private String mPlayer;
	private String mLocation;
	private String mComment;
	private String mDuring;

	public Context getContext() {
		return mContext;
	}

	public void setContext(Context context) {
		this.mContext = context;
	}

	public ProgressBar getProgressBar() {
		return mProgressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.mProgressBar = progressBar;
	}

	public void setOnScoreProcessingFinishedListener(
			OnScoreProcessingFinishedListener listener) {
		mOnProcessingFinishedListener = listener;
	}

	public boolean isOnBackground() {
		return bOnBackground;
	}

	public void setOnBackground(boolean onBackground) {
		this.bOnBackground = onBackground;
	}

	public void initialize(Context context, ProgressBar progressBar,
			String typeProcessing, String level, String player,
			String location, String comment, String during) {
		mContext = context;
		mProgressBar = progressBar;
		mTypeProcessing = typeProcessing;
		mLevel = level;
		mPlayer = player;
		mLocation = location;
		mComment = comment;
		mDuring = during;
	}

	@Override
	protected Boolean doInBackground(Integer... params) {
		// TODO Auto-generated method stub
		// ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
		mDatabase = new GlobalDatabase(mContext);
		if (mTypeProcessing.equals(mContext
				.getString(R.string.type_score_processing_getting))) {
			RestClient getScore = new RestClient(
					"http://global-score.appspot.com/gethighscore");
			getScore.AddParam("gameid", "Sudoku");

			try {
				getScore.Execute(RequestMethod.GET);
			} catch (Exception e) {
				// textView.setText(e.getMessage());
			}
			String strResponse = getScore.getResponse();
			strResponse = strResponse.replace("\n", "");
			XmlPullParser scores;
			try {

				scores = XmlPullParserFactory.newInstance().newPullParser();
				scores.setInput(new StringReader(strResponse));
			} catch (XmlPullParserException e) {
				scores = null;
			}
			if (scores != null) {
				mDatabase.deleteAllScores();
				int eventType = -1;
				// boolean bFoundScores = false;

				// Find Score records from XML
				while (eventType != XmlResourceParser.END_DOCUMENT) {
					if (eventType == XmlResourceParser.START_TAG) {

						// Get the name of the tag (eg scores or score)
						String strName = scores.getName();

						if (strName.equals("score")) {
							// bFoundScores = true;
							String level = scores.getAttributeValue(null,
									"subboard");
							String player = scores.getAttributeValue(null,
									"player");
							String during = scores.getAttributeValue(null,
									"during");
							String comment = scores.getAttributeValue(null,
									"comment");
							String location = scores.getAttributeValue(null,
									"location");
							String avatar = scores.getAttributeValue(null,
									"avatar");
							String date = scores
									.getAttributeValue(null, "date");
							mDatabase.insertScore(level, player, Integer
									.parseInt(during), comment, location,
									avatar, Long.parseLong(date));
							// publishProgress(scoreValue, scoreRank,
							// scoreUserName);
						}
					}
					try {
						eventType = scores.next();
					} catch (XmlPullParserException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
		if (mTypeProcessing.equals(mContext
				.getString(R.string.type_score_processing_submit))) {
			RestClient client = new RestClient(
					"http://global-score.appspot.com/highscore");
			// client.AddParam("message",
			// "Hello World");

			client.AddParam("GameID", "Sudoku");
			client.AddParam("SubBoard", mLevel);
			client.AddParam("Player", mPlayer);

			client.AddParam("Location", mLocation);
			client.AddParam("Comment", mComment);
			client.AddParam("During", mDuring);
			long now = System.currentTimeMillis();
			client.AddParam("Date", String.valueOf(now));

			try {
				client.Execute(RequestMethod.GET);
			} catch (Exception e) {
				// textView.setText(e.getMessage());
			}
		}
		return true;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		if (values.length == 2) {
			mProgressBar.setMax(values[1]);
		}
		mProgressBar.setProgress(values[0]);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// mDatabase.close();
		if (result) {
			if (mTypeProcessing.equals(mContext
					.getString(R.string.type_score_processing_submit)))
				Toast
						.makeText(
								mContext,
								mContext
										.getString(R.string.submit_score_processing_success),
								Toast.LENGTH_LONG).show();
			if (mTypeProcessing.equals(mContext
					.getString(R.string.type_score_processing_getting)))
				Toast
						.makeText(
								mContext,
								mContext
										.getString(R.string.get_score_processing_success),
								Toast.LENGTH_LONG).show();
		} else {
			if (mTypeProcessing.equals(mContext
					.getString(R.string.type_score_processing_submit)))
				Toast
						.makeText(
								mContext,
								mContext
										.getString(R.string.submit_score_processing_fail),
								Toast.LENGTH_LONG).show();
			if (mTypeProcessing.equals(mContext
					.getString(R.string.type_score_processing_getting)))
				Toast.makeText(mContext,
						mContext.getString(R.string.get_score_processing_fail),
						Toast.LENGTH_LONG).show();
		}

		if (mOnProcessingFinishedListener != null) {

			mOnProcessingFinishedListener.onScoreProcessingFinished(result,
					mTypeProcessing);
		}
	}

	public interface OnScoreProcessingFinishedListener {
		/**
		 * Occurs when import is finished.
		 * 
		 * @param importSuccessful
		 *            Indicates whether import was successful.
		 * @param folderId
		 *            Contains id of imported folder, or -1 if multiple folders
		 *            were imported.
		 */
		void onScoreProcessingFinished(boolean generateSuccessful,
				String typeProcessing);
	}

}
