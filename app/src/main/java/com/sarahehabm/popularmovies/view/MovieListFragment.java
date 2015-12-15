package com.sarahehabm.popularmovies.view;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sarahehabm.popularmovies.Constants;
import com.sarahehabm.popularmovies.R;
import com.sarahehabm.popularmovies.controller.GetMoviesTask;
import com.sarahehabm.popularmovies.controller.OnItemSelectedCallback;
import com.sarahehabm.popularmovies.controller.OnMoviesRetrieveSuccessListener;
import com.sarahehabm.popularmovies.controller.database.MovieContract;
import com.sarahehabm.popularmovies.controller.database.MovieContract.MovieEntry;
import com.sarahehabm.popularmovies.model.Movie;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sarah E. Mostafa on 11-Dec-15.
 */
public class MovieListFragment extends BaseFragment
        implements OnMoviesRetrieveSuccessListener,
        AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener,
        LoaderManager.LoaderCallbacks<Cursor>{
    private static final int ALL = 0;
    private static final int FAVORITES = 1;
    private final String TAG = MovieListFragment.class.getSimpleName();
    private static final int MOVIES_LOADER = 0;

    @Bind(R.id.spinner_filter) Spinner spinnerFilter;
    @Bind(R.id.movies_grid) GridView gridView;
    @Bind(R.id.textView_error_message) TextView textViewErrorMessage;
    private MoviesGridAdapter moviesGridAdapter;
    private int selectedPosition = ListView.INVALID_POSITION;

    private static final String[] PROJECTION = {
            MovieEntry.COLUMN_ID,
            MovieEntry.COLUMN_TITLE,
            MovieEntry.COLUMN_OVERVIEW,
            MovieEntry.COLUMN_RELEASE_DATE,
            MovieEntry.COLUMN_POPULARITY,
            MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieEntry.COLUMN_VOTE_COUNT,
            MovieEntry.COLUMN_POSTER_PATH,
            MovieEntry.COLUMN_BACKDROP_PATH,
            MovieEntry.COLUMN_VIDEO
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        moviesGridAdapter = new MoviesGridAdapter(getActivity(), null, 0, null);

        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        ButterKnife.bind(this, view);

        gridView.setAdapter(moviesGridAdapter);
        gridView.setOnItemClickListener(this);

        spinnerFilter.setOnItemSelectedListener(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(Constants.SELECTED_POSITION)) {
            selectedPosition = savedInstanceState.getInt(Constants.SELECTED_POSITION);
        }

        return view;
    }

    @Override
     public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public void onSortOrderChange(){
        executeTask();
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }

    private void executeTask(){
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        String[] preference_values_sort_order = getResources()
                .getStringArray(R.array.preference_values_sort_order);
        String sortOrder = sharedPreferences.getString(
                getString(R.string.preference_key_sort_order), preference_values_sort_order[0]);

        GetMoviesTask getMoviesTask = new GetMoviesTask(getActivity(), this, this);
        getMoviesTask.execute(sortOrder);
    }

    private void queryDatabase() {
        Cursor cursor = getContext().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI, null, null, null, null);

        if(cursor == null || cursor.getCount()<=0) { //Nothing in favorites
            gridView.setVisibility(View.GONE);
            textViewErrorMessage.setVisibility(View.VISIBLE);
            textViewErrorMessage.setText("Nothing in favorites yet");
        } else {
            gridView.setVisibility(View.VISIBLE);
            textViewErrorMessage.setVisibility(View.GONE);

            ArrayList<Movie> movies = new ArrayList<Movie>();
            while (cursor.moveToNext()) {
                int voteCount = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_COUNT));
                float voteAverage = cursor.getFloat(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE));
                String videoStr = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_VIDEO));
                boolean video = Boolean.getBoolean(videoStr);
                String title = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_TITLE));
                float popularity = cursor.getFloat(cursor.getColumnIndex(MovieEntry.COLUMN_POPULARITY));
                String posterPath = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
                String releaseDate = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
                String overview = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW));
                int id = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_ID));
                String backdropPath = cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_BACKDROP_PATH));
                boolean isFavorite = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_IS_FAVORITE)) == 0?
                        false : true;
                Movie movie = new Movie(voteCount, voteAverage, video, title, popularity,
                        posterPath, releaseDate, overview, null, null, id,
                        null, backdropPath, false, isFavorite);
                movies.add(movie);
            }

            moviesGridAdapter.setMovies(movies);
            moviesGridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(selectedPosition != ListView.INVALID_POSITION)
            outState.putInt(Constants.SELECTED_POSITION, selectedPosition);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void updateUI(ArrayList<Movie> movies) {
        if(movies == null || movies.isEmpty()) { //Error retrieving movies
            gridView.setVisibility(View.GONE);
            textViewErrorMessage.setVisibility(View.VISIBLE);
            textViewErrorMessage.setText("Error occurred retrieving movies.\nPlease try again later");
        } else { //Movies retrieved successfully
            gridView.setVisibility(View.VISIBLE);
            textViewErrorMessage.setVisibility(View.GONE);

            moviesGridAdapter.setMovies(movies);
            moviesGridAdapter.notifyDataSetChanged();
            if (selectedPosition != ListView.INVALID_POSITION) {
                gridView.smoothScrollToPosition(selectedPosition);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedPosition = position;

        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
        if(cursor!=null) {
            int movieId = cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_ID));
            Uri uri = MovieEntry.buildMovieUri(movieId);
            ((OnItemSelectedCallback) getActivity()).onItemSelected(uri);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Cursor cursor = null;
        switch (position){
            case ALL:
                cursor = getContext().getContentResolver().query(MovieEntry.CONTENT_URI,
                        null, null, null, null);
                break;

            case FAVORITES:
                cursor = getContext().getContentResolver().query(MovieEntry.CONTENT_URI,
                        PROJECTION, MovieEntry.COLUMN_IS_FAVORITE + " = \"true\" ",
                        null, null);
                break;

            default:
                break;
        }

        if(cursor!=null)
            moviesGridAdapter.changeCursor(cursor);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (spinnerFilter.getSelectedItemPosition()){
            case ALL:
                Uri uri =  MovieEntry.CONTENT_URI.buildUpon().build();
                return new CursorLoader(getActivity(), uri, PROJECTION, null, null, null);

            case FAVORITES:
                return new CursorLoader(getActivity(), MovieContract.MovieEntry.CONTENT_URI,
                        PROJECTION, MovieEntry.COLUMN_IS_FAVORITE + " = ? ",
                        new String[]{String.valueOf(true)}, null);

            default:
                return null;

        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        moviesGridAdapter.swapCursor(data);

        if(selectedPosition != ListView.INVALID_POSITION) {
            gridView.smoothScrollToPosition(selectedPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        moviesGridAdapter.swapCursor(null);
    }
}
