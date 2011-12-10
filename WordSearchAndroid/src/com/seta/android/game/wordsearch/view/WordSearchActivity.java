
package com.seta.android.game.wordsearch.view;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.seta.android.game.wordsearch.view.R;
import com.seta.android.game.util.AnalyticsTask;

import com.seta.android.game.wordsearch.model.HighScore;
import com.seta.android.game.wordsearch.util.ConversionUtil;

import com.seta.android.game.wordsearch.view.controller.TextViewGridController;
import com.seta.android.game.wordsearch.view.controller.WordSearchActivityController;


public class WordSearchActivity extends Activity {
	 private Button newGameButton;
	 private Button hintGameButton;
	 private int screenWidth;
	 private int screeenHeight;
	 private Display display;
     class mView extends View
     {
    	int beginY;
     	int wordSize;
     	
     	int beginWordPosX;
     	int beginWordPosY;
     	
     	int endWordPosX;
     	int endWordPosY;
     	
     	boolean isNeedToDrawLine = false;
		public mView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			wordSize = (int) ((float)screenWidth / 10 );
		}
		@Override
		public void onDraw(Canvas canvas){
			beginY = canvas.getHeight() / 8 +10;
			Paint paint = new Paint();
			paint.setColor(0xffffffff);
			
			Log.d("adHeight", ""+adHeight);
			drawLine(canvas);
		}
		public void drawLine(Canvas canvas){
			if(isNeedToDrawLine){
				Paint paint = new Paint();
				if(beginWordPosX == endWordPosX || beginWordPosY == endWordPosY
						|| Math.abs(beginWordPosX - endWordPosX) == Math.abs(beginWordPosY - endWordPosY)){
					
					
					int beginx = beginWordPosX;
					int beginy = beginWordPosY;
					int XDirection = 1;
					int YDirection = 1;
					int wordCount = 0;
					
					if(beginWordPosX < endWordPosX){
						XDirection = 1;
						wordCount = Math.abs(beginWordPosX - endWordPosX);
					}
					else if(beginWordPosX > endWordPosX){
						XDirection = -1;
						wordCount = Math.abs(beginWordPosX - endWordPosX);
					}
					else {
						XDirection = 0;
						wordCount = Math.abs(beginWordPosY - endWordPosY);
					}
					
					if(beginWordPosY < endWordPosY){
						YDirection = 1;
					}
					else if(beginWordPosY > endWordPosY){
						YDirection = -1;
					}
					else YDirection = 0;
					paint.setColor(0x993399ff);
				}
				else{
					paint.setColor(0x99ff3300);
					toastLayout.setVisibility(View.GONE);
				}
				
				canvas.drawCircle(beginWordPosX * wordSize + wordSize / 2, beginWordPosY * wordSize + beginY + wordSize / 2, wordSize / 4, paint);
				canvas.drawCircle(endWordPosX * wordSize + wordSize / 2, endWordPosY * wordSize + beginY + wordSize / 2, wordSize / 4, paint);
				paint.setStrokeWidth(wordSize / 5 * 2);
				canvas.drawLine(beginWordPosX * wordSize + wordSize / 2, beginWordPosY * wordSize + beginY + wordSize / 2,
						endWordPosX * wordSize + wordSize / 2, endWordPosY * wordSize + beginY + wordSize / 2, paint);
			}
		}
		public boolean onTouchEvent(MotionEvent event){
			if(true){
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:				
					if(event.getY() - beginY > 0 && event.getY() - beginY < screenWidth 
							&& event.getX() > 0 && event.getX() <screenWidth){
						isNeedToDrawLine = true;
						beginWordPosX = (int) (event.getX() / wordSize);
						beginWordPosY = (int) ((event.getY() - beginY) / wordSize);
						
						endWordPosX = (int) (event.getX() / wordSize);
						endWordPosY = (int) ((event.getY() - beginY) / wordSize);
						invalidate();
					}
					else{
						isNeedToDrawLine = false;
						invalidate();
					}
					break;

				case MotionEvent.ACTION_MOVE:
					if(event.getY() - beginY > 0 && event.getY() - beginY < screenWidth 
							&& event.getX() > 0 && event.getX() <screenWidth){
						endWordPosX = (int) (event.getX() / wordSize);
						endWordPosY = (int) ((event.getY() - beginY) / wordSize);
						invalidate();
					}
					else{
						isNeedToDrawLine = false;
						invalidate();
						toastLayout.setVisibility(View.GONE);
					}
					break;

				case MotionEvent.ACTION_UP:
					isNeedToDrawLine = false;
					invalidate();					
					toastLayout.setVisibility(View.GONE);
					break;
				}
			}
			return true;
		}
		
     }

        class DialogGameNewListener implements DialogInterface.OnClickListener {
                public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                                getControl().newWordSearch();
                                break;
                        case DialogInterface.BUTTON_NEUTRAL:
                                getControl().resetGrid();
                                break;
                        }
                }
        }
        class DialogGameOverListener implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {
                public void onClick(DialogInterface dialog, int which) {
                        String name = ((EditText)((AlertDialog)dialog).findViewById(android.R.id.input)).getText().toString();
                        HighScore hs = getControl().getCurrentHighScore();
                        if (!TextUtils.isEmpty(name)) {
                                hs.setName(name);
                                getControl().getPrefs().setDetaultName(name);
                        } else {
                                hs.setName("?");
                        }
                        switch(which) {
                        case DialogInterface.BUTTON_POSITIVE: {
                                if (!getControl().isReplaying()) {
                                       
                                }
                        }
                        case DialogInterface.BUTTON_NEUTRAL: {
                                LinkedList<HighScore> scores = getControl().getPrefs().getTopScores();
                                scores.add(hs);
                                getControl().getPrefs().setTopScores(scores);
                                showDialog(WordSearchActivity.DIALOG_ID_GAME_NEW);
                                break;
                        }
                        }
                        removeDialog(DIALOG_ID_GAME_OVER);
                }

                public void onCancel(DialogInterface dialog) {
                        removeDialog(DIALOG_ID_GAME_OVER);
                }
        }
        class DialogHighScoresGlobalShowListener implements DialogInterface.OnClickListener {
                public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                                control.getPrefs().resetTopScores();
                        }
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                                showDialog(WordSearchActivity.DIALOG_ID_HIGH_SCORES_LOCAL_SHOW);
                        } else if (!getControl().isGameRunning()) {
                                showDialog(WordSearchActivity.DIALOG_ID_GAME_NEW);
                        }
                }
        }
        class DialogHighScoresLocalShowListener implements DialogInterface.OnClickListener {
                public void onClick(DialogInterface dialog, int which) {
                        if (which == DialogInterface.BUTTON_NEGATIVE) {
                                control.getPrefs().resetTopScores();
                        }
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                             
                        }
                }
        }
        class DialogNoWordsCustomListener implements DialogInterface.OnClickListener {
                public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                                Intent intent = new Intent(Intent.ACTION_EDIT, com.seta.android.game.wordsearch.view.WordDictionaryProvider.Word.CONTENT_URI);
                                intent.setType(com.seta.android.game.wordsearch.view.WordDictionaryProvider.Word.CONTENT_TYPE);
                                startActivity(intent);
                                break;
                        case DialogInterface.BUTTON_NEGATIVE:
                                startActivity(new Intent(WordSearchActivity.this, WordSearchPreferences.class));
                                break;
                        default:
                                break;
                        }
                }
        }
        class DialogNoWordsListener implements DialogInterface.OnClickListener {
                public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                                getControl().newWordSearch();
                                break;
                        case DialogInterface.BUTTON_NEUTRAL:
                                startActivity(new Intent(WordSearchActivity.this, WordSearchPreferences.class));
                                break;
                        case DialogInterface.BUTTON_NEGATIVE:
                                startActivity(new Intent(WordSearchActivity.this, WordSearchPreferences.class));
                                break;
                        default:
                                break;
                        }
                }
        }
        class DialogIntroListener implements DialogInterface.OnClickListener {
                public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                        case DialogInterface.BUTTON_POSITIVE: {
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(WordSearchActivity.this);
                                sp.edit().putString(getString(R.string.prefs_touch_mode), getString(R.string.TAP)).commit();
                                break;
                        }
                        case DialogInterface.BUTTON_NEUTRAL:
                                break;
                        case DialogInterface.BUTTON_NEGATIVE: {
                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(WordSearchActivity.this);
                                sp.edit().putString(getString(R.string.prefs_touch_mode), getString(R.string.DRAG)).commit();
                                break;
                        }
                        default:
                                break;
                        }
                }
        }
       
        class DialogNewGameListener implements DialogInterface.OnClickListener {
            public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                    case DialogInterface.BUTTON_POSITIVE: {
                    	for(int i = 0;i<text.length;i++)
                    		{
                    			text[i].setText("");
                    		}
                            getControl().newWordSearch();
                            break;
                    }
                    case DialogInterface.BUTTON_NEGATIVE: {
                           
                            break;
                    }
                    default:
                            break;
                    }
            }
    }
        final public static int DIALOG_ID_NO_WORDS = 0;
        final public static int DIALOG_ID_NO_WORDS_CUSTOM = 1;
        final public static int DIALOG_ID_GAME_OVER = 2;
        final public static int DIALOG_ID_HIGH_SCORES_LOCAL_SHOW = 3;
        final public static int DIALOG_ID_GAME_NEW = 5;
        final public static int DIALOG_ID_INTRO_INPUT_TYPE = 6;
        final public static int DIALOG_ID_INTRO_DONATE = 7;
        final public static int DIALOG_ID_DONATE = 8;
        final public static int DIALOG_ID_NEWGAME = 9;

        final private static String LOG_TAG = "WordSearchActivity";
        /**
         * control classes were made to segment the complex game logic away from the display logic
         */
        private WordSearchActivityController control;
        private String appVer;

        public WordSearchActivityController getControl() {
                return control;
        }
        public static TextView text[];
        LinearLayout toastLayout;
        TextView toastText;
    	public int adWidth;
    	public int adHeight;
    	
        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                try {
                        appVer = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
                } catch (NameNotFoundException e) {
                        appVer = "unknown";
                }
                try {
                        AnalyticsTask analytics = new AnalyticsTask(this, true);
                        analytics.execute(new String[] {"/WordSearchActivity"});
                } catch (RuntimeException re) {
                        Log.e(LOG_TAG, "tracker failed!");
                } catch (Exception e) {
                        Log.e(LOG_TAG, "tracker failed!");
                }
                setContentView(R.layout.test);
                control = new WordSearchActivityController(this);
                control.restoreState(savedInstanceState);
//                {
//                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//                        if (!appVer.equals(sp.getString(Constants.KEY_INTRO_VER, null)) && sp.getString(getString(R.string.prefs_touch_mode), null) == null) {
//                                this.showDialog(DIALOG_ID_INTRO_INPUT_TYPE);
//                                sp.edit().putString(Constants.KEY_INTRO_VER, appVer).commit();
//                        } else if (control.getPrefs().getGamePlayCount() >= Constants.DONATE_GAME_PLAY_COUNT && !control.getPrefs().isDonateIngored()) {
//                                this.showDialog(DIALOG_ID_INTRO_DONATE);
//                        }
//                }
                //TextView 
                text = new TextView[16];
                text[0]=(TextView)findViewById(R.id.text1);
                text[1]=(TextView)findViewById(R.id.text2);
                text[2]=(TextView)findViewById(R.id.text3);
                text[3]=(TextView)findViewById(R.id.text4);
                text[4]=(TextView)findViewById(R.id.text5);
                text[5]=(TextView)findViewById(R.id.text6);
                text[6]=(TextView)findViewById(R.id.text7);
                text[7]=(TextView)findViewById(R.id.text8);
                text[8]=(TextView)findViewById(R.id.text9);
                text[9]=(TextView)findViewById(R.id.text10);
                text[10]=(TextView)findViewById(R.id.text11);
                text[11]=(TextView)findViewById(R.id.text12);
                text[12]=(TextView)findViewById(R.id.text13);
                text[13]=(TextView)findViewById(R.id.text14);
                text[14]=(TextView)findViewById(R.id.text15);
                text[15]=(TextView)findViewById(R.id.text16);
                
                
                for(int i = 0;i<text.length;i++)
                {
                	text[i].setText("");
                }
                
                display=getWindowManager().getDefaultDisplay();
                screenWidth=display.getWidth();
                screeenHeight=display.getHeight();
                //New -hint Button
                newGameButton =(Button)findViewById(R.id.new_button);
                hintGameButton=(Button)findViewById(R.id.hint_button);
                newGameButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
							showDialog(DIALOG_ID_NEWGAME);
					}
				});
                hintGameButton.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
					}
				});
                //Toast layout
                toastLayout = (LinearLayout) findViewById(R.id.toast_layout);
        		toastText = (TextView) findViewById(R.id.text_toast);
                
        		LinearLayout adLayout = (LinearLayout) findViewById(R.id.ad_layout);
                adWidth = adLayout.getWidth();
                adHeight = adLayout.getLayoutParams().height;
                Log.d("adHeight", ""+adHeight);
                mView view = new mView(this);

                LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.layout_button);
                buttonLayout.bringToFront();
                FrameLayout frameLayout = (FrameLayout) findViewById(R.id.sublayout);
                frameLayout.addView(view);
                view.bringToFront();
                view.setEnabled(true);
               
        }

        @Override
        protected void onDestroy() {
                super.onDestroy();
               
        }
        @Override
        protected Dialog onCreateDialog(int id) {
                Dialog dialog;
                switch(id) {
                case DIALOG_ID_NO_WORDS: {
                        final DialogNoWordsListener DIALOG_LISTENER_NO_WORDS = new DialogNoWordsListener();
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(R.string.no_words);
                        builder.setNegativeButton(R.string.category, DIALOG_LISTENER_NO_WORDS);
                        builder.setPositiveButton(R.string.new_game, DIALOG_LISTENER_NO_WORDS);
                        builder.setNeutralButton(R.string.size, DIALOG_LISTENER_NO_WORDS);
                        dialog = builder.create();
                        break;
                }
                case DIALOG_ID_NO_WORDS_CUSTOM: {
                        final DialogNoWordsCustomListener DIALOG_LISTENER_NO_WORDS_CUSTOM = new DialogNoWordsCustomListener();
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(R.string.no_words_custom);
                        builder.setNegativeButton(R.string.category, DIALOG_LISTENER_NO_WORDS_CUSTOM);
                        builder.setPositiveButton(R.string.custom_editor, DIALOG_LISTENER_NO_WORDS_CUSTOM);
                        dialog = builder.create();
                        break;
                }
                case DIALOG_ID_GAME_OVER: {
                        final DialogGameOverListener DIALOG_LISTENER_GAME_OVER = new DialogGameOverListener();
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("blank");
                        EditText text = new EditText(this);
                        text.setSingleLine();
                        text.setId(android.R.id.input);
                        builder.setView(text);
                        builder.setPositiveButton(R.string.SAVE_SUBMIT, DIALOG_LISTENER_GAME_OVER);
                        builder.setNeutralButton(R.string.SAVE, DIALOG_LISTENER_GAME_OVER);
                        builder.setOnCancelListener(DIALOG_LISTENER_GAME_OVER);
                        dialog = builder.create();
                        break;
                }
                case DIALOG_ID_HIGH_SCORES_LOCAL_SHOW: {
                        final DialogHighScoresLocalShowListener DIALOG_LISTENER_HIGH_SCORES_SHOW = new DialogHighScoresLocalShowListener();
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("blank");
                        builder.setTitle(R.string.LOCAL_HIGH_SCORES);
                        builder.setNegativeButton(R.string.reset, DIALOG_LISTENER_HIGH_SCORES_SHOW);
                        builder.setPositiveButton(android.R.string.ok, DIALOG_LISTENER_HIGH_SCORES_SHOW);
                       
                        dialog = builder.create();
                        break;
                }
                case DIALOG_ID_GAME_NEW: {
                        final DialogGameNewListener DIALOG_LISTENER_GAME_NEW = new DialogGameNewListener();
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(this.getString(R.string.game_over));
                        builder.setPositiveButton(R.string.new_game, DIALOG_LISTENER_GAME_NEW);
                        builder.setNeutralButton(R.string.REPLAY, DIALOG_LISTENER_GAME_NEW);
                        builder.setNegativeButton(android.R.string.cancel, DIALOG_LISTENER_GAME_NEW);
                        dialog = builder.create();
                        break;
                }
                case DIALOG_ID_INTRO_INPUT_TYPE: {
                        final DialogIntroListener DIALOG_LISTENER_INTRO = new DialogIntroListener();
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(R.string.INTRO);
                        builder.setPositiveButton(R.string.tap, DIALOG_LISTENER_INTRO);
                        builder.setNeutralButton(android.R.string.cancel, DIALOG_LISTENER_INTRO);
                        builder.setNegativeButton(R.string.drag, DIALOG_LISTENER_INTRO);
                        dialog = builder.create();
                        break;
                }

                case DIALOG_ID_NEWGAME:
                {
                	final DialogInterface.OnClickListener LISTENER = new DialogNewGameListener();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Do you want to start a new game?");
                    builder.setPositiveButton("New Game", LISTENER);
                    builder.setIcon(R.drawable.newgame);
                    builder.setTitle("Start a new game");
                    builder.setNegativeButton("Cancel", LISTENER);
                    dialog = builder.create();
                    break;
                }
                default:
                        dialog = super.onCreateDialog(id);
                        break;
                }
                return dialog;
        }

        /** hook into menu button for activity */
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                this.getMenuInflater().inflate(R.menu.wordsearch_option, menu);
                menu.findItem(R.id.menu_new).setIcon(R.drawable.newgame);
                menu.findItem(R.id.menu_options).setIcon(R.drawable.setting);
                menu.findItem(R.id.menu_freegames).setIcon(R.drawable.smiley);
                menu.findItem(R.id.menu_help).setIcon(R.drawable.help);
                menu.findItem(R.id.menu_scores).setIcon(R.drawable.highscore);
                menu.findItem(R.id.menu_upgrade).setIcon(R.drawable.upgrade);
                return super.onCreateOptionsMenu(menu);
        }

        @Override
        protected void onPrepareDialog(int id, Dialog dialog) {
                super.onPrepareDialog(id, dialog);
                switch(id) {
                case DIALOG_ID_GAME_OVER: {
                        HighScore hs = control.getCurrentHighScore();
                        TextView label = (TextView)((AlertDialog)dialog).findViewById(android.R.id.message);
                        String msg = this.getString(R.string.SCORE_CONGRATULATIONS).replace("%replaceme", hs.getScore().toString()+" ("+ConversionUtil.formatTime.format(new Date(hs.getTime()))+")");
                        if (hs.isHighScore()) {
                                msg += this.getString(R.string.SCORE_LOCAL_HIGH).replace("%replaceme", Integer.toString(hs.getRank()+1));
   
                        } 
                        EditText edit = (EditText)((AlertDialog)dialog).findViewById(android.R.id.input);
                        Button save = (Button)((AlertDialog)dialog).findViewById(android.R.id.button3);
                        edit.setText(getControl().getPrefs().getDefaultName());
                        if (hs.isHighScore()) {
                                save.setVisibility(EditText.VISIBLE);
                        } else {
                                save.setVisibility(EditText.GONE);
                        }
                        if (hs.isHighScore() || !hs.isGlobalError()) {
                                edit.setVisibility(EditText.VISIBLE);
                                msg += this.getString(R.string.SCORE_INITIALS);
                        } else {
                                edit.setVisibility(EditText.GONE);
                        }
                        label.setText(msg);
                        break;
                }
                case DIALOG_ID_HIGH_SCORES_LOCAL_SHOW: {
                        List<HighScore> highScores = this.getControl().getHighScores();
                        StringBuilder str = new StringBuilder();
                        if (highScores.size() == 0) {
                                str.append(this.getString(R.string.no_high_scores));
                        } else {
                                Collections.sort(highScores);
                                for (int index = 0; index < highScores.size(); index++) {
                                        str.append(Integer.toString(index+1)+": "+highScores.get(index).getName()+" " + highScores.get(index).getScore() + " ( " + ConversionUtil.formatTime.format(new Date(highScores.get(index).getTime())) + " )\n");
                                }
                        }
                        TextView label = (TextView)((AlertDialog)dialog).findViewById(android.R.id.message);
                        label.setText(str);
                        break;
                }
                default:
                        break;
                }
        }

        /** when menu button option selected */
        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                case R.id.menu_scores:
                        this.showDialog(DIALOG_ID_HIGH_SCORES_LOCAL_SHOW);
                        return true;
                case R.id.menu_options:
                        startActivity(new Intent(this, WordSearchPreferences.class));
                        return true;
                case R.id.menu_new:           	
                		this.showDialog(DIALOG_ID_NEWGAME);                   
                        return true;
                case R.id.menu_freegames:
                {
                        return true;
                }
                case R.id.menu_help:
                {
                        return true;
                }
                case R.id.menu_upgrade:
                {
                      
                        return true;
                }
                default:
                        return super.onOptionsItemSelected(item);
                }
        }

        @Override
        protected void onPause() {
                super.onPause();
                control.timePause();
        }

        @Override
        protected void onResume() {
                super.onResume();
//              Log.v(LOG_TAG, "onResume");
                if (control.isGameRunning()) {
                        control.timeResume();
                } else if (control.getCurrentHighScore() != null) {
                        showDialog(WordSearchActivity.DIALOG_ID_GAME_OVER);
                }
        }

        @Override
        protected void onSaveInstanceState(Bundle outState) {
                super.onSaveInstanceState(outState);
                control.saveState(outState);
                this.removeDialog(DIALOG_ID_GAME_OVER);
        }
        public void setupViewGrid() {
                int gridSize = control.getGridSize();
                TextViewGridController controller = control.getGridManager();
                ViewGroup gridTable = (ViewGroup) this.findViewById(R.id.gridTable);
                if (gridTable.getChildCount() != gridSize) {
                        if (gridTable.getChildCount() == 0) {
                                gridTable.setKeepScreenOn(true);
                                gridTable.setOnTouchListener(controller);
                        }
                        controller.clearPointDemension();
                        gridTable.removeAllViews();
                        Point point = new Point();
                        controller.setGridView(new TextView[gridSize][]);
                        TextView[][] gridView = controller.getGridView();
                        for (point.y = 0; point.y < gridSize; point.y++) {
                                this.getLayoutInflater().inflate(R.layout.grid_row, gridTable, true);
                                ViewGroup row = (ViewGroup)gridTable.getChildAt(point.y);
                                TextView[] rowText = new TextView[gridSize];
                                for (point.x = 0; point.x < gridSize; point.x++) {
                                        this.getLayoutInflater().inflate(R.layout.grid_text_view, row, true);
                                        TextView view = (TextView)row.getChildAt(point.x);
                                        view.setId(ConversionUtil.convertPointToID(point, control.getGridSize()));
                                        view.setOnKeyListener(controller);

                                        rowText[point.x] = view;
                                }
                                gridView[point.y] = rowText;
                        }
                        gridTable.requestLayout();
                }
        }

        public void trackGame() {
                try {
                        String category = control.getPrefs().getCategory();
                        String input = "Tap";
                        if (control.getPrefs().getTouchMode()) {
                                input = "Drag";
                        }
                        AnalyticsTask analytics = new AnalyticsTask(this, false);
                        analytics.execute(new String[] {category, input, Integer.toString(control.getGridSize())});
                } catch (RuntimeException re) {
                        Log.e(LOG_TAG, "tracker failed!");
                } catch (Exception e) {
                        Log.e(LOG_TAG, "tracker failed!");
                }
        }

        public void trackReplay() {
                try {
                        String category = control.getPrefs().getCategory();
                        AnalyticsTask analytics = new AnalyticsTask(this, false);
                        analytics.execute(new String[] {category, "replay", Integer.toString(control.getGridSize())});
                } catch (RuntimeException re) {
                        Log.e(LOG_TAG, "tracker failed!");
                } catch (Exception e) {
                        Log.e(LOG_TAG, "tracker failed!");
                }
        }
}
