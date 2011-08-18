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


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * This class helps open, create, and upgrade the database file.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

private static final String TAG = "DatabaseHelper";

public static final int DATABASE_VERSION = 8;

private Context mContext;

    DatabaseHelper(Context context) {
        super(context, GlobalDatabase.DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + GlobalDatabase.SCORE_TABLE_NAME + " ("
                + ScoreColumns._ID + " INTEGER PRIMARY KEY,"
                + ScoreColumns.PLAYER + " TEXT,"
                + ScoreColumns.SCORE + " INTEGER,"
                + ScoreColumns.DATE + " INTEGER,"
                + ScoreColumns.COMMENT + " TEXT,"
                + ScoreColumns.LOCATION + " TEXT,"
                + ScoreColumns.AVATAR + " TEXT"
                + ");");
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

}
