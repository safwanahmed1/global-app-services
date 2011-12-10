package com.seta.android.game.wordsearch.view.controller;

import java.util.List;

import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;

import android.widget.Button;
import android.widget.TextView;


import com.seta.android.game.wordsearch.model.Grid;
import com.seta.android.game.wordsearch.model.Theme;
import com.seta.android.game.wordsearch.view.WordSearchActivity;

public class WordBoxController implements IWordBoxController, Callback {
	final private TextView wordBox;
	final private TextView letterBox;
	final private Handler handler;
	private List<String> words;
	private int wordsIndex = 0;
	private WordSearchActivity wordSearch;

	protected WordBoxController(Button prev, Button next, TextView wordBox,
			TextView letterBox) {
		this.letterBox = letterBox;
		this.wordBox = wordBox;
		this.handler = new Handler(this);
		wordSearch = new WordSearchActivity();
	}

	public void resetWords(Grid grid) {
		this.words = grid.getWordList();
		this.wordsIndex = 0;
		Message.obtain(handler, MSG_UPDATE_WORD_BOX).sendToTarget();
	}

	public void setLetter(CharSequence charSequence) {
		if (charSequence != null && charSequence.length() > 1) {
			charSequence = String.valueOf(charSequence.charAt(charSequence
					.length() - 1));
		}
		Message.obtain(handler, MSG_SET_LETTER_BOX, charSequence)
				.sendToTarget();
	}

	public void wordFound(String str) {
		// words.remove(str);
		// wordsIndex = 0;
		// Message.obtain(handler, MSG_UPDATE_WORD_BOX).sendToTarget();
	}

	public void updateTheme(Theme theme) {
		Message.obtain(handler, MSG_UPDATE_THEME, theme).sendToTarget();
	}

	final static private int MSG_SET_LETTER_BOX = 0;
	final static private int MSG_UPDATE_WORD_BOX = 1;
	final static private int MSG_UPDATE_THEME = 2;

	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_SET_LETTER_BOX: {
			CharSequence letterBoxText = (CharSequence) msg.obj;

			if (letterBoxText == null) {
				letterBox.setVisibility(TextView.INVISIBLE);
			} else {

				letterBox.setText(letterBoxText);
				letterBox.setVisibility(TextView.VISIBLE);
			}
			break;
		}

		case MSG_UPDATE_WORD_BOX: {
			if (wordsIndex < 0 || wordsIndex > words.size()) {
				wordsIndex = 0;
			}
			CharSequence textView = "";
			for (int i = 0; i < words.size(); i++) {
				textView = words.get(i).toLowerCase();
				WordSearchActivity.text[i].setText(textView);
			}
			break;
		}
		case MSG_UPDATE_THEME: {
			Theme theme = (Theme) msg.obj;
			// this.letterBox.setTextColor(theme.picked);
			this.wordBox.setTextColor(theme.background);
			break;
		}

		default: {
			return false;
		}
		}
		return true;
	}

}
