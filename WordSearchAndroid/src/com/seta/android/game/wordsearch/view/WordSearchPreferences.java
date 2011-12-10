
package com.seta.android.game.wordsearch.view;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

import com.seta.android.game.wordsearch.view.R;
import com.seta.android.game.wordsearch.view.controller.TextViewGridController;
import com.seta.android.game.wordsearch.view.controller.WordSearchActivityController;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class WordSearchPreferences extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
		public static boolean isSoundEffect = true;
		public static int index = 0;
		private WordSearchActivityController control;
        @Override
        public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.testpreferences);
                this.updateCategorySummary();
                this.updateLevelSummary();
                this.updateLetterCaseModeSummary();
                this.updateThemeSummary();
                
                PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        }

        private void updateCategorySummary() {
                String categorySum = this.getString(R.string.prefs_category_summary);
                Preference p = this.findPreference(this.getString(R.string.prefs_category));
                String category = p.getSharedPreferences().getString(p.getKey(), getString(R.string.RANDOM));
                List<String> catValues = Arrays.asList(this.getResources().getStringArray(R.array.categories_list_values));
                String[] catLabels = this.getResources().getStringArray(R.array.categories_list_labels);
                int index = catValues.indexOf(category);
                categorySum = categorySum.replaceAll("%replaceme", catLabels[index]);
                p.setSummary(categorySum);
        }

        private void updateLevelSummary() {
        	 String levelSum = this.getString(R.string.prefs_level_summary);
             Preference p = this.findPreference(this.getString(R.string.prefs_level_mode));
             String touchmode = p.getSharedPreferences().getString(p.getKey(), getString(R.string.LEVEL));
             List<String> modeValues = Arrays.asList(this.getResources().getStringArray(R.array.choice_list_values));
             String[] modeLabels = this.getResources().getStringArray(R.array.choice_list_lables);
             index = modeValues.indexOf(touchmode);
             levelSum = levelSum.replaceAll("%replaceme", modeLabels[index]);
             if(index == 0)
            	 TextViewGridController.isLetterCase = true;
             else if(index == 1)
            	 TextViewGridController.isLetterCase = false;
             p.setSummary(levelSum);
        }

        private void updateLetterCaseModeSummary() {
                String touchmodeSum = this.getString(R.string.prefs_letter_case_summary);
                Preference p = this.findPreference(this.getString(R.string.prefs_letter_case_mode));
                String touchmode = p.getSharedPreferences().getString(p.getKey(), getString(R.string.UPPER_CASE));
                List<String> modeValues = Arrays.asList(this.getResources().getStringArray(R.array.letter_case_values));
                String[] modeLabels = this.getResources().getStringArray(R.array.letter_case_lables);
                int index = modeValues.indexOf(touchmode);
                touchmodeSum = touchmodeSum.replaceAll("%replaceme", modeLabels[index]);
                Log.d("index " +index, "seta");
                System.out.print(index);
                p.setSummary(touchmodeSum);
        }

        private void updateThemeSummary() {
                String themeSum = this.getString(R.string.PREFS_THEME_SUMMARY);
                Preference p = this.findPreference(this.getString(R.string.PREFS_THEME));
                String theme = p.getSharedPreferences().getString(p.getKey(), getString(R.string.THEME_ORIGINAL));
                List<String> themeValues = Arrays.asList(this.getResources().getStringArray(R.array.THEME_VALUES));
                String[] themeLabels = this.getResources().getStringArray(R.array.THEME_LABELS);
                int index = themeValues.indexOf(theme);
                themeSum = themeSum.replaceAll("%replaceme", themeLabels[index]);
                Log.d("index "+index ,"isLetterCase "+TextViewGridController.isLetterCase);
                
                p.setSummary(themeSum);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
                this.getMenuInflater().inflate(R.menu.prefrences_option, menu);
                menu.findItem(R.id.menu_quit).setIcon(android.R.drawable.ic_menu_close_clear_cancel);
                return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                case R.id.menu_quit:
                        this.finish();
                        return true;
                }
                return super.onOptionsItemSelected(item);
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (this.getString(R.string.prefs_category).equals(key)) {
                        this.updateCategorySummary();
                } else if (this.getString(R.string.prefs_level_mode).equals(key)) {
                        this.updateLevelSummary();
                } else if (this.getString(R.string.prefs_letter_case_mode).equals(key)) {
                        this.updateLetterCaseModeSummary();
                } else if (this.getString(R.string.PREFS_THEME).equals(key)) {
                        this.updateThemeSummary();
                }
        }
}

