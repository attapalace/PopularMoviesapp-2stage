package com.examples.apps.atta.popularmoviesapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ahmedatta on 3/4/18.
 */

public class MoviesDBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1 ;

    static final String DATABASE_NAME = "movie.db";

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MoviesContract.MoviesEntry.TABLE_NAME + " (" +

                MoviesContract.MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_TITLE+ " TEXT NOT NULL, " +
                MoviesContract.MoviesEntry.COLUMN_MOVIE_IMAGE + " TEXT, " +
                MoviesContract.MoviesEntry.COLUMN_OVERVIEW + " TEXT," +
                MoviesContract.MoviesEntry.COLUMN_RATING + " INTEGER, " +
                MoviesContract.MoviesEntry.COLUMN_DATE + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.MoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
