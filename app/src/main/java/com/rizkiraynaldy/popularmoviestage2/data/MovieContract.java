package com.rizkiraynaldy.popularmoviestage2.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ray on 31/07/2017.
 */

public class MovieContract {
    public final static String AUTHORITY = "com.rizkiraynaldy.popularmoviestage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static String TABLE_NAME = "movies";

        public static String COLUMN_TITLE = "title";
        public static String COLUMN_MOVIE_ID = "id";
        public static String COLUMN_POSTER = "poster";
        public static String COLUMN_SYNOPSIS = "synopsis";
        public static String COLUMN_RATING = "user_rating";
        public static String COLUMN_DATE = "release_date";

    }
}
