package com.seta.android.game.wordsearch.view.controller;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.WindowManager.BadTokenException;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.seta.android.game.wordsearch.view.R;
import com.seta.android.game.wordsearch.Constants;
import com.seta.android.game.wordsearch.model.Grid;
import com.seta.android.game.wordsearch.model.HighScore;
import com.seta.android.game.wordsearch.model.Preferences;
import com.seta.android.game.wordsearch.model.Theme;
import com.seta.android.game.wordsearch.model.dictionary.DictionaryFactory;
import com.seta.android.game.wordsearch.view.WordSearchActivity;

public class WordSearchActivityController implements
		SharedPreferences.OnSharedPreferenceChangeListener {
	private final static String LOG_TAG = WordSearchActivityController.class
			.getName();

	class GameOver implements Runnable {
		public void run() {
			new GameOverTask().execute(new Integer[0]);
		}
	}

	class GameOverTask extends AsyncTask<Integer, Integer, Boolean> {
		final private ProgressDialog pd = new ProgressDialog(wordSearch);

		@Override
		protected Boolean doInBackground(Integer... res) {
			try {
				LinkedList<HighScore> scores = wordSearch.getControl()
						.getHighScores();
				scores.add(hs);
				Collections.sort(scores);
				int positionLocal = scores.indexOf(hs);
				hs.setRank(positionLocal);
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair(Constants.SECURITY_TOKEN,
						Constants.VALUE_SECRET));
				nvps.add(new BasicNameValuePair(Constants.KEY_PAYLOAD, hs
						.toJSON().toString()));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				return true;
			} catch (Exception e) {
				if (hs != null) {
					hs.setGlobalRank(-1);
				}
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			try {
				if (pd.isShowing()) {
					pd.dismiss();
					if (getCurrentHighScore() != null && !this.isCancelled()
							&& WordSearchActivityController.this.isVisible()) {
						wordSearch
								.showDialog(WordSearchActivity.DIALOG_ID_GAME_OVER);
					}
				}
			} catch (BadTokenException bte) {
				Log.e(LOG_TAG, bte.getMessage());
			} catch (IllegalArgumentException iae) {
				Log.e(LOG_TAG, iae.getMessage());
			}
		}

		@Override
		protected void onPreExecute() {
			ConnectivityManager conman = (ConnectivityManager) wordSearch
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (getCurrentHighScore() == null
					|| conman.getActiveNetworkInfo() == null
					|| !conman.getActiveNetworkInfo().isConnected()) {
				this.cancel(true);
				wordSearch.showDialog(WordSearchActivity.DIALOG_ID_GAME_OVER);
			} else {
				pd.setMessage(wordSearch
						.getString(R.string.HIGH_SCORE_CALCULATING));
				pd.setIndeterminate(true);
				pd.show();
			}
		}
	}

	private Theme theme = Theme.ORIGINAL;
	private IWordBoxController wordBoxManager;
	private TextViewGridController gridManager;
	private DictionaryFactory dictionaryFactory;
	private long timeStart = 0L;
	private long timeSum = 0L;
	private Grid grid;
	final private Preferences prefs;
	private WordSearchActivity wordSearch;
	private HighScore hs;
	private static final String BUNDLE_TIME = "ws_time";
	private static final String BUNDLE_GRID = "ws_grid";
	private static final String BUNDLE_HIGH_SCORE = "ws_high_score";

	public WordSearchActivityController(WordSearchActivity wordSearch) {
		super();
		this.wordSearch = wordSearch;
		prefs = new Preferences(this.wordSearch);
		dictionaryFactory = new DictionaryFactory(this.wordSearch);
		{
			TextView wordBox = (TextView) wordSearch.findViewById(R.id.text16);
			if (wordBox != null) {
				TextView letterBox = (TextView) wordSearch
						.findViewById(R.id.text_toast);
				Button newGameButton = (Button) wordSearch
						.findViewById(R.id.new_button);
				Button hintButton = (Button) wordSearch
						.findViewById(R.id.hint_button);
				wordBoxManager = new WordBoxController(newGameButton,
						hintButton, wordBox, letterBox);
			} else {
				ListView wordList = (ListView) wordSearch
						.findViewById(R.id.wordList);
				TextView letterBox = (TextView) wordSearch
						.findViewById(R.id.letterBox);
				wordBoxManager = new WordBoxControllerLand(
						wordSearch.getApplicationContext(), wordList, letterBox);
			}
		}
		{
			gridManager = new TextViewGridController(this);
		}
		this.setLetter("l");
		this.setLetter(null);
		this.updateTouchMode();
		PreferenceManager.getDefaultSharedPreferences(wordSearch)
				.registerOnSharedPreferenceChangeListener(this);
	}

	protected void foundWord(String word) {
		wordBoxManager.wordFound(word);
		if (!grid.isRunning()) {
			Long diffMill = System.currentTimeMillis() - timeStart + timeSum;
			setHighScore(diffMill);
		}
	}

	public HighScore getCurrentHighScore() {
		return hs;
	}

	public void clearCurrentHighScore() {
		hs = null;
	}

	public String getCurrentTheme() {
		return dictionaryFactory.getCurrentTheme();
	}

	public TextViewGridController getGridManager() {
		return gridManager;
	}

	public int getGridSize() {
		return grid.getSize();
	}

	public LinkedList<HighScore> getHighScores() {
		return prefs.getTopScores();
	}

	public Preferences getPrefs() {
		return prefs;
	}

	public Theme getTheme() {
		return theme;
	}

	public String guessWord(Point pointStart, Point pointEnd) {
		return grid.guessWord(pointStart, pointEnd);
	}

	public boolean isGameRunning() {
		return grid.isRunning();
	}

	public void newWordSearch() {
		String category = PreferenceManager.getDefaultSharedPreferences(
				wordSearch).getString(
				wordSearch.getString(R.string.prefs_category),
				wordSearch.getString(R.string.RANDOM));
		grid = Grid.generateGrid(dictionaryFactory.getDictionary(category), 12,
				4, prefs.getSize());
		wordSearch.setupViewGrid();
		if (grid.getWordListLength() == 0) {
			if (dictionaryFactory.isCustomDictionary()) {
				wordSearch
						.showDialog(WordSearchActivity.DIALOG_ID_NO_WORDS_CUSTOM);
			} else {
				wordSearch.showDialog(WordSearchActivity.DIALOG_ID_NO_WORDS);
			}
		}
		timeSum = 0L;
		hs = null;
		this.setGrid(grid);
		updateTheme();
		wordSearch.trackGame();
		this.prefs.increaseGamePlayCount();
	}

	public void resetGrid() {
		grid.reset();
		this.setGrid(grid);
		this.updateTheme();
		timeSum = 0L;
		hs = null;
		wordSearch.trackReplay();
		wordSearch.trackGame();
	}

	public void restoreState(Bundle inState) {
		if (inState != null) {
			Bundle hsBundle = inState.getBundle(BUNDLE_HIGH_SCORE);
			if (hsBundle != null) {
				hs = new HighScore(hsBundle);
			} else {
				hs = null;
			}
			this.grid = inState.getParcelable(BUNDLE_GRID);
			this.setGrid(grid);
			wordSearch.setupViewGrid();
			updateTheme();
			this.timeSum = inState.getLong(BUNDLE_TIME, 0);
		} else {
			this.newWordSearch();
		}
	}

	public void saveState(Bundle outState) {
		if (outState != null) {
			this.timePause();
			outState.putLong(BUNDLE_TIME, this.timeSum);
			outState.putParcelable(BUNDLE_GRID, this.grid);
			if (this.hs != null) {
				outState.putBundle(BUNDLE_HIGH_SCORE, this.hs.toBundle());
			}
		}
	}

	private void setGrid(Grid grid) {
		wordBoxManager.resetWords(grid);
		timeStart = System.currentTimeMillis();
	}

	private void setHighScore(long time) {
		hs = new HighScore(time, getGridSize(),
				dictionaryFactory.getCurrentTheme(),
				this.grid.getWordListLength());
		wordSearch.runOnUiThread(new GameOver());
	}

	public void setLetter(CharSequence charSequence) {
		wordBoxManager.setLetter(charSequence);
	}

	public void timePause() {
		if (timeStart != 0) {
			timeSum += System.currentTimeMillis() - timeStart;
			timeStart = 0;
		}
	}

	public void timeResume() {
		timeStart = System.currentTimeMillis();
	}

	public boolean isVisible() {
		return timeStart != 0;
	}

	public void updateTouchMode() {
		this.gridManager.setTouchMode(prefs.getTouchMode());
	}

	public boolean isReplaying() {
		return grid.isReplaying();
	}

	public void updateTheme() {
		String themeStr = PreferenceManager.getDefaultSharedPreferences(
				wordSearch).getString(
				wordSearch.getString(R.string.PREFS_THEME),
				Theme.ORIGINAL.toString());
		this.theme = Theme.valueOf(themeStr);
		Log.d("theme" + theme, themeStr);
		if (this.theme == null) {
			this.theme = Theme.ORIGINAL;
		}
		theme.reset(grid.getWordListLength());
		wordSearch.findViewById(R.id.wordsearch_base).setBackgroundResource(
				theme.background);
		this.gridManager.reset(grid);
		this.wordBoxManager.updateTheme(theme);
	}

	public void updateLetterCase() {
		String letterCaseStr = PreferenceManager.getDefaultSharedPreferences(
				wordSearch).getString(
				wordSearch.getString(R.string.prefs_letter_case_mode),
				"Upper Case");
		Log.d("letterCase ", letterCaseStr);
		if("Upper Case".equals(letterCaseStr))
			TextViewGridController.isLetterCase = true;
		else if("Lower Case".equals(letterCaseStr))
			TextViewGridController.isLetterCase = false;
		this.gridManager.reset(grid);
		
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (wordSearch.getString(R.string.PREFS_THEME).equals(key)) {
			updateTheme();
		} else if (wordSearch.getString(R.string.prefs_touch_mode).equals(key)) {
			updateTouchMode();
		} else if (wordSearch.getString(R.string.prefs_letter_case_mode)
				.equals(key)) {
//			updateLetterCase();
		}
		String letterCaseStr = PreferenceManager.getDefaultSharedPreferences(
				wordSearch).getString(
				wordSearch.getString(R.string.prefs_letter_case_mode),
				"Upper Case");
		Log.d("letterCase ", letterCaseStr);
		if("Upper Case".equals(letterCaseStr))
			TextViewGridController.isLetterCase = true;
		else if("Lower Case".equals(letterCaseStr))
			TextViewGridController.isLetterCase = false;
		this.gridManager.reset(grid);
	}

}