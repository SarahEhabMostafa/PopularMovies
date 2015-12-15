package com.sarahehabm.popularmovies.controller.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Sarah E. Mostafa on 12-Dec-15.
 */
public class MovieContract {
    public static final String CONTENT_AUTHORITY = "com.sarahehabm.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {
        //Table name
        public static final String TABLE_NAME = "movie";

        //Column names
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VIDEO = "video";
        public static final String COLUMN_IS_FAVORITE = "is_favorite";


        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        private static final String SQL_CREATE_TABLE = "CREATE TABLE " +  TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, "
                + COLUMN_TITLE + " TEXT, "
                + COLUMN_RELEASE_DATE + " TEXT, "
                + COLUMN_OVERVIEW + " TEXT, "
                + COLUMN_POSTER_PATH + " TEXT, "
                + COLUMN_POPULARITY + " REAL, "
                + COLUMN_VOTE_AVERAGE + " REAL, "
                + COLUMN_VOTE_COUNT + " INTEGER, "
                + COLUMN_BACKDROP_PATH + " TEXT, "
                + COLUMN_VIDEO + " TEXT, "
                + COLUMN_IS_FAVORITE + " INTEGER"
                + ");";

        public static void createTable(SQLiteDatabase database) {
            database.execSQL(SQL_CREATE_TABLE);
        }
    }
}
