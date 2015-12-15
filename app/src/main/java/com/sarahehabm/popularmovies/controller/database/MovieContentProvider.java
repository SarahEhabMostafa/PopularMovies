package com.sarahehabm.popularmovies.controller.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.sarahehabm.popularmovies.controller.database.MovieContract.MovieEntry;

/**
 * Created by Sarah E. Mostafa on 12-Dec-15.
 */
public class MovieContentProvider extends ContentProvider {
    private MoviesDatabaseHelper databaseHelper;
    private static final UriMatcher uriMatcher = buildUriMatcher();

    static final int MOVIES = 100;
    static final int MOVIE_BY_ID = 101;

    static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_BY_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        databaseHelper = new MoviesDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        final int match = uriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case MOVIES:
                cursor = databaseHelper.getReadableDatabase().query(MovieEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;

            case MOVIE_BY_ID:
                long movieId = ContentUris.parseId(uri);
                cursor = databaseHelper.getReadableDatabase().query(MovieEntry.TABLE_NAME,
                        projection, MovieEntry.COLUMN_ID + " = ?",
                        new String[]{String.valueOf(movieId)}, null, null, null);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return MovieContract.MovieEntry.CONTENT_TYPE;

            case MOVIE_BY_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = uriMatcher.match(uri);
        Uri returnUri;
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        String whereClause = MovieEntry.COLUMN_ID + " = ?";
        switch (match) {
            case MOVIES: {
                int movieId = (int) values.get(MovieEntry.COLUMN_ID);
                Cursor cursor = databaseHelper.getReadableDatabase().query(MovieEntry.TABLE_NAME,
                        null, whereClause, new String[]{String.valueOf(movieId)},
                        null, null, null);

                long id;
                if(cursor!=null && cursor.moveToFirst()) { //Update if already exists in database
                    id = database.update(MovieEntry.TABLE_NAME, values, whereClause,
                            new String[]{String.valueOf(movieId)});
                } else { //Insert if not
                    id = database.insert(MovieEntry.TABLE_NAME, null, values);
                }

                if (id > 0)
                    returnUri = MovieEntry.buildMovieUri(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
            }
            break;

            case MOVIE_BY_ID: {
                long id = database.insert(MovieEntry.TABLE_NAME, null, values);
                if(id>0)
                    returnUri = MovieEntry.buildMovieUri(id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
            }
            break;

            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowsDeleted = 0;

        if(selection == null)
            selection = "1";

        switch (match) {
            case MOVIES:
                rowsDeleted = database.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case MOVIE_BY_ID:
                rowsDeleted = database.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsDeleted!=0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase database = databaseHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int rowsUpdated = 0;

        if(selection == null)
            selection = "1";

        switch (match) {
            case MOVIES:
                rowsUpdated = database.update(MovieEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                break;

            case MOVIE_BY_ID:
                rowsUpdated = database.update(MovieEntry.TABLE_NAME, values,
                        selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(rowsUpdated!=0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }
}
