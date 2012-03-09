package global.services.sample.android.activities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import global.services.lib.android.factories.HighscoreFactory;
import global.services.lib.android.objects.Highscore;
import global.services.sample.android.R;
import global.services.sample.android.adapters.ScoreArrayAdapter;
import global.services.sample.android.tasks.DownloadScore;
import global.services.sample.android.tasks.TaskListener.OnTaskFinishedListener;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

public class ViewScoreActivity extends ListActivity {
	private static final String SCORE_FILE = "highscore.xml";
	private ScoreArrayAdapter adapter;
	private List<Highscore> scoreList = new ArrayList<Highscore>();
	private boolean refreshList = false;
	private int pageIdx = 0;
	private int pageSize = 20;
	private boolean isFirstLoad = true;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.score_list);
		ListView scoreListView = getListView();
		scoreListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

				if (loadMore) {
					DownloadScores();
				}
			}
		});

		DownloadScores();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();

	}

	/*
	 * 
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { MenuInflater
	 * inflater = getMenuInflater(); inflater.inflate(R.menu.get_score_menu,
	 * menu); return true; }
	 * 
	 * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
	 * item selection switch (item.getItemId()) { case R.id.refresh_score:
	 * 
	 * DownloadScores(); return true; default: return
	 * super.onOptionsItemSelected(item); } }
	 */
	private void DownloadScores() {
		getParent().setProgressBarIndeterminateVisibility(true);
		DownloadScore downScore = new DownloadScore(this);
		downScore.setOnTaskFinishedListener(mOnTaskFinishedListener);
		downScore.execute(getResources().getString(R.string.userid),
				getResources().getString(R.string.appid),
				String.valueOf(pageIdx), String.valueOf(pageSize));

	}

	private OnTaskFinishedListener mOnTaskFinishedListener = new OnTaskFinishedListener() {

		@SuppressWarnings("unchecked")
		@Override
		public void onTaskFinished(Object result) {

			scoreList.addAll((List<Highscore>) result);
			if (scoreList != null) {
				if (isFirstLoad) {
					adapter = new ScoreArrayAdapter(getApplicationContext(),
							R.layout.score_list,
							(ArrayList<Highscore>) scoreList);

					setListAdapter(adapter);
					isFirstLoad = false;
				} else {
					adapter.notifyDataSetChanged();
				}
				getParent().setProgressBarIndeterminateVisibility(false);
				pageIdx++;

			}

		}
	};

}
