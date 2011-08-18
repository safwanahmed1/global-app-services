/* 
 * Copyright (C) 2009 Roman Masek
 * 
 * This file is part of OpenSudoku.
 * 
 * OpenSudoku is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * OpenSudoku is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with OpenSudoku.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package global.services.sample.android;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * 
 * Wrapper around opensudoku's database.
 * 
 * You have to pass application context when creating instance:
 * <code>SudokuDatabase db = new SudokuDatabase(getApplicationContext());</code>
 * 
 * You have to explicitly close connection when you're done with database (see {@link #close()}).
 * 
 * This class supports database transactions using {@link #beginTransaction()}, \
 * {@link #setTransactionSuccessful()} and {@link #endTransaction()}. 
 * See {@link SQLiteDatabase} for details on how to use them.
 * 
 * @author romario
 *
 */
public class GlobalDatabase {
	public static final String DATABASE_NAME = "lines_droid";
    public static final String SCORE_TABLE_NAME = "score";
    
    //private static final String TAG = "SudokuDatabase";

    private DatabaseHelper mOpenHelper;
    
    public GlobalDatabase(Context context) {
    	mOpenHelper = new DatabaseHelper(context);
    }

    
    
    
    public void insertScore(String player, int score, String comment, String location, String avatar, long date) {
    	long now = System.currentTimeMillis();
        ContentValues values = new ContentValues();
        values.put(ScoreColumns.PLAYER, player);
        values.put(ScoreColumns.SCORE, score);
        values.put(ScoreColumns.COMMENT, comment);
        values.put(ScoreColumns.LOCATION, location);
        values.put(ScoreColumns.AVATAR, avatar);
        values.put(ScoreColumns.DATE, date);

        
    	SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long scoreId= db.insert(SCORE_TABLE_NAME, ScoreColumns._ID, values);

        if (scoreId > 0) {
            
        	return;
        }

        throw new SQLException(String.format("Failed to insert score."));
    }
    
    

    public Cursor getScoreList() {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        qb.setTables(SCORE_TABLE_NAME);
        //qb.setProjectionMap(sPlacesProjectionMap);
        //qb.appendWhere(ScoreColumns.SUBBOARD + "='" + level+"'");
        
               
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        return qb.query(db, null, null, null, null, null, "during ASC, date DESC");
    }
    
 
    
    public void deleteAllScores() {
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.delete(SCORE_TABLE_NAME,null , null);
    }
    
    
    
    public void close() {
    	mOpenHelper.close();
    }

    public void beginTransaction() {
    	mOpenHelper.getWritableDatabase().beginTransaction();
    }
    
    public void setTransactionSuccessful() {
    	mOpenHelper.getWritableDatabase().setTransactionSuccessful();
    }
    
    public void endTransaction() {
    	mOpenHelper.getWritableDatabase().endTransaction();
    }
}
