package com.seta.android.game.wordsearch.model.dictionary;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

public class DictionaryStringArray implements IDictionary {
        private String[] words;
        private final static Random random = new Random();
        private final LinkedList<String> remainingWords = new LinkedList<String>();

        public DictionaryStringArray(String[] words) {
                this.words = words;
        }

        public String getNextWord(int minLength, int maxLength) {
                String str = null;
                int tries = 0;
                try {
                        do {
                                if (remainingWords.size() == 0) {
                                        Collections.addAll(remainingWords, words);
                                }
                                str = remainingWords.remove(random.nextInt(remainingWords.size()));
                                tries++;
                        } while ((str.length() < minLength || str.length() > maxLength) && tries < DictionaryFactory.MAX_TRIES);
                        str = str.toUpperCase();
                } catch (Exception e) {
                        // Log.v();
                }
                return str;
        }

}


