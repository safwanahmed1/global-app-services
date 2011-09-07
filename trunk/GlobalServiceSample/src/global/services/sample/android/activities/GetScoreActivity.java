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
import global.services.sample.android.tasks.DownloadScoreToLocal;
import global.services.sample.android.tasks.DownloadScoreToLocal.OnTaskFinishedListener;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GetScoreActivity extends ListActivity {
	private static final String SCORE_FILE = "highscore.xml";
	private ScoreArrayAdapter adapter;
	private List<Highscore> scoreList;
	private boolean refreshList = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.score_list);
		scoreList = LoadScoreFromFileToListView();
		if (scoreList != null) {
			adapter = new ScoreArrayAdapter(getApplicationContext(),
					R.layout.score_list, (ArrayList<Highscore>) scoreList);

			setListAdapter(adapter);

		}

	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		if (refreshList) {
			refreshList = false;
			scoreList = LoadScoreFromFileToListView();
			if (adapter == null) {
				adapter = new ScoreArrayAdapter(getApplicationContext(),
						R.layout.score_list,
						(ArrayList<Highscore>) scoreList);

				setListAdapter(adapter);

			} else
				adapter.notifyDataSetChanged();
		}
		
		super.onResume();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.get_score_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.refresh_score:
			// GetScoreToLocalFile();
			DownloadScoreToLocal downScore = new DownloadScoreToLocal(this);
			//downScore.setOnTaskFinishedListener(mOnTaskFinishedListener);
			downScore.execute(getResources().getString(R.string.userid),
					getResources().getString(R.string.appid));

			// setListAdapter(adapter);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* Download score from server and save to XML find in internal memory */
	private void GetScoreToLocalFile() {
		HighscoreFactory scoreFactory = new HighscoreFactory(getResources()
				.getString(R.string.userid), Long.parseLong(getResources()
				.getString(R.string.appid)));
		String scoresXML = scoreFactory.GetScoresXMLContent();
		FileOutputStream fos = null;
		if (scoresXML != null) {
			try {
				fos = openFileOutput(SCORE_FILE, Context.MODE_PRIVATE);
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
	}

	/* Refresh score from xml file to listview */
	public List<Highscore> LoadScoreFromFileToListView() {
		List<Highscore> scoreList = null;
		FileInputStream fis;
		StringBuffer fileContent = new StringBuffer("");
		try {

			byte[] buffer = new byte[1024];
			fis = openFileInput(SCORE_FILE);

			int length;
			while ((length = fis.read(buffer)) != -1) {
				fileContent.append(new String(buffer), 0, length);
			}
			scoreList = GetScoreListFromXML(fileContent.toString());

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scoreList;
	}

	public List<Highscore> GetScoreListFromXML(String scoresXMLContent) {
		List<Highscore> scoreList = new ArrayList<Highscore>();
		String strElemName;
		String id;
		String userId;
		String appId;
		String level;
		String player;
		String score;
		String during;
		String comment;
		String location;
		// String avatar; Not support yet
		String date;

		scoresXMLContent = scoresXMLContent.replace("\n", "");
		XmlPullParser scores;
		try {

			scores = XmlPullParserFactory.newInstance().newPullParser();
			scores.setInput(new StringReader(scoresXMLContent));
		} catch (XmlPullParserException e) {
			scores = null;
		}
		if (scores != null) {
			int eventType = -1;
			// boolean bFoundScores = false;

			// Find Score records from XML
			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {

					// Get the name of the tag (eg scores or score)
					strElemName = scores.getName();

					if (strElemName.equals("score")) {
						// bFoundScores = true;
						Highscore scoreObj = new Highscore();
						id = scores.getAttributeValue(null, "id");
						if (id != null)
							scoreObj.setId(Long.parseLong(id));
						userId = scores.getAttributeValue(null, "useid");
						if (userId != null)
							scoreObj.setUserID(userId);
						appId = scores.getAttributeValue(null, "appid");
						if (appId != null)
							scoreObj.setGameID(Long.parseLong(appId));
						level = scores.getAttributeValue(null, "subboard");
						if (level != null)
							scoreObj.setSubBoard(level);
						player = scores.getAttributeValue(null, "player");
						if (player != null)
							scoreObj.setPlayer(player);
						score = scores.getAttributeValue(null, "highscore");
						if (score != null)
							scoreObj.setHighScore(Integer.parseInt(score));
						during = scores.getAttributeValue(null, "during");
						if (during != null)
							scoreObj.setDuring(Long.parseLong(during));
						comment = scores.getAttributeValue(null, "comment");
						if (comment != null)
							scoreObj.setComment(comment);
						location = scores.getAttributeValue(null, "location");
						if (location != null)
							scoreObj.setLocation(location);
						// avatar = scores.getAttributeValue(null, "avatar");
						// Not support yet
						// scoreObj.setAvatar(avatar);Not support yet
						date = scores.getAttributeValue(null, "date");
						scoreObj.setDate(Long.parseLong(date));

						scoreList.add(scoreObj);

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
		return scoreList;
	}

	private OnTaskFinishedListener mOnTaskFinishedListener = new OnTaskFinishedListener() {

		@Override
		public void onTaskFinished(boolean successful) {
			refreshList = successful;
			
			

		}
	};

}
