package com.seta.android.game.wordsearch.view.controller;

import com.seta.android.game.wordsearch.model.Grid;
import com.seta.android.game.wordsearch.model.Theme;

public interface IWordBoxController {
       
        public void setLetter(CharSequence charSequence);

       
        public void wordFound(String str);

       
        public void resetWords(Grid grid);

       
        public void updateTheme(Theme theme);
}

