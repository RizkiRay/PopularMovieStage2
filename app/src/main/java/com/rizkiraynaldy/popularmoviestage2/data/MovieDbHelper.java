package com.rizkiraynaldy.popularmoviestage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ray on 31/07/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moviesDb.db";
    private static final int VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + "("
                + MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_DATE + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, "
                + MovieContract.MovieEntry.COLUMN_RATING + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
