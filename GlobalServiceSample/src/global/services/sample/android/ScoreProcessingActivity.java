package global.services.sample.android;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * This activity is responsible for importing puzzles from various sources (web,
 * file, .opensudoku, .sdm, extras).
 * 
 * @author romario
 * 
 */
public class ScoreProcessingActivity extends Activity {
	public static final String EXTRA_PROCESS_TYPE = "process_type";
	public static final String EXTRA_PROCESS_LEVEL = "process_level";
	public static final String EXTRA_PROCESS_PLAYER = "process_player";
	public static final String EXTRA_PROCESS_LOCATION = "process_location";
	public static final String EXTRA_PROCESS_COMMENT = "process_comment";
	public static final String EXTRA_PROCESS_DURING = "process_during";
	// public static final String EXTRA_NUM_PUZZLES = "num_puzzles";
	private ScoreProcessingTask scoreProcessingTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.score_processing);
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				R.drawable.ic_menu_global);

		TextView typeProcessing = (TextView) findViewById(R.id.type_progress_text);
		ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
		scoreProcessingTask = new ScoreProcessingTask();
		Intent intent = getIntent();
		String type = intent.getStringExtra(EXTRA_PROCESS_TYPE);
		String level = intent.getStringExtra(EXTRA_PROCESS_LEVEL);
		String player = intent.getStringExtra(EXTRA_PROCESS_PLAYER);
		String location = intent.getStringExtra(EXTRA_PROCESS_LOCATION);
		String comment = intent.getStringExtra(EXTRA_PROCESS_COMMENT);
		String during = intent.getStringExtra(EXTRA_PROCESS_DURING);

		if (type.equals(getString(R.string.type_score_processing_getting)))
			typeProcessing
					.setText(getString(R.string.text_score_processing_getting));
		if (type.equals(R.string.type_score_processing_submit))
			typeProcessing
					.setText(getString(R.string.text_score_processing_submit));
		scoreProcessingTask.initialize(this, progressBar, type, level, player,
				location, comment, during);
		scoreProcessingTask
				.setOnScoreProcessingFinishedListener(mOnScoreProcessingFinishedListener);
		scoreProcessingTask.execute();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (scoreProcessingTask.getStatus() != Status.FINISHED) {
			scoreProcessingTask.setOnBackground(true);
			Toast.makeText(this,
					getString(R.string.score_processing_background_notify),
					Toast.LENGTH_LONG).show();
		}
	}

	private OnScoreProcessingFinishedListener mOnScoreProcessingFinishedListener = new OnScoreProcessingFinishedListener() {

		@Override
		public void onScoreProcessingFinished(boolean processSuccessful,
				String type) {
			// TODO Auto-generated method stub
			// if (processSuccessful && !onBackground) {
			// if (folderId == -1) {
			// // multiple folders were imported, go to folder list
			// Intent i = new Intent(ScoreProcessingActivity.this,
			// FolderListActivity.class);
			// startActivity(i);
			// } else {
			// // one folder was imported, go to this folder
			// Intent i = new Intent(ScoreProcessingActivity.this,
			// SudokuListActivity.class);
			// i.putExtra(SudokuListActivity.EXTRA_FOLDER_ID, folderId);
			// startActivity(i);
			// }
			// }
			// call finish, so this activity won't be part of history
			finish();

		}
	};

}
