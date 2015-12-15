package com.sarahehabm.popularmovies.view;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sarahehabm.popularmovies.Constants;
import com.sarahehabm.popularmovies.R;
import com.sarahehabm.popularmovies.controller.GetMovieTrailersTask;
import com.sarahehabm.popularmovies.controller.OnMovieTrailersRetrievedListener;
import com.sarahehabm.popularmovies.controller.database.MovieContract.MovieEntry;
import com.sarahehabm.popularmovies.model.Movie;
import com.sarahehabm.popularmovies.model.MovieTrailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Sarah E. Mostafa on 11-Dec-15.
 */
public class MovieDetailFragment extends BaseFragment implements OnMovieTrailersRetrievedListener,
        View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    private final String IMAGE_SIZE = "original";
    private static final int MOVIE_LOADER = 0;

    private Movie movie;
    private Uri uri;
    private boolean isFavorite;
    private ShareActionProvider shareActionProvider;
    private MovieTrailersListAdapter movieTrailersListAdapter;

    @Bind(R.id.imageView) ImageView imageView;
    @Bind(R.id.textView_movie_title) TextView textViewTitle;
    @Bind(R.id.textView_movie_release_date) TextView textViewReleaseDate;
    @Bind(R.id.textView_movie_duration) TextView textViewDuration;
    @Bind(R.id.textView_movie_vote_average) TextView textViewVoteAverage;
    @Bind(R.id.textView_movie_overview) TextView textViewOverview;
    @Bind(R.id.listView_trailers) ListView listViewTrailers;
    @Bind(R.id.button_reviews) Button buttonReviews;
    @Bind(R.id.imageButton_favorite) ImageButton buttonFavorite;

    public MovieDetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        View header = inflater.inflate(R.layout.fragment_movie_detail_header, null);
        listViewTrailers = (ListView) view.findViewById(R.id.listView_trailers);
        listViewTrailers.addHeaderView(header);
        listViewTrailers.setHeaderDividersEnabled(false);
        ButterKnife.bind(this, view);

        if(movieTrailersListAdapter == null)
            movieTrailersListAdapter = new MovieTrailersListAdapter(getActivity(), null);
        listViewTrailers.setAdapter(movieTrailersListAdapter);

        Bundle arguments = getArguments();
        if(arguments!=null) {
            uri = arguments.getParcelable(Constants.MOVIE_URI);

        }
        initialize();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_details, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if(movieTrailersListAdapter!= null && movieTrailersListAdapter.getItem(0)!=null)
            shareActionProvider.setShareIntent(createShareIntent());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onSortOrderChange(String sortOrder) {
        Uri uri = this.uri;
        if(uri != null) {
            long movieId = ContentUris.parseId(uri);

            Uri updatedUri = MovieEntry.buildMovieUri(movieId);
            this.uri = updatedUri;
            getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
        }
    }

    private void initialize() {
        buttonReviews.setOnClickListener(this);
        buttonFavorite.setOnClickListener(this);

    }

    private void setFavoriteAndUpdateButton(boolean isFavorite) {
        this.isFavorite = isFavorite;
        if(isFavorite)
            buttonFavorite.setBackgroundResource(R.drawable.ic_favorite);
        else
            buttonFavorite.setBackgroundResource(R.drawable.ic_not_favorite);
    }

    @Override
    public void onRetrieveSuccess(ArrayList<MovieTrailer> movieTrailers) {
        movieTrailersListAdapter.setMovieTrailers(movieTrailers);
        movieTrailersListAdapter.notifyDataSetChanged();

        if(shareActionProvider!=null)
            shareActionProvider.setShareIntent(createShareIntent());
    }

    @Override
    public void onRetrieveFailure() {
    }

    public void onReviewsClick(View view) {
        Intent intent = new Intent(getActivity(), MovieReviewsActivity.class);
        intent.putExtra(Constants.OBJECT_MOVIE, movie);
        getActivity().startActivity(intent);
    }

    public void onFavoriteClick(View view) {
        int rowId = -1;
        if(isFavorite) { //Already in favorites; Set as not favorite
            ContentValues updatedValues = new ContentValues();
            updatedValues.put(MovieEntry.COLUMN_IS_FAVORITE, false);
            rowId = getContext().getContentResolver().update(MovieEntry.CONTENT_URI,
                    updatedValues, MovieEntry.COLUMN_ID + " = ? ",
                    new String[]{String.valueOf(movie.getId())});

        } else { //Not in favorites; Set as favorite
            ContentValues values = new ContentValues();
            values.put(MovieEntry.COLUMN_ID, movie.getId());
            values.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
            values.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
            values.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getRelease_date());
            values.put(MovieEntry.COLUMN_POPULARITY, movie.getPopularity());
            values.put(MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVote_average());
            values.put(MovieEntry.COLUMN_VOTE_COUNT, movie.getVote_count());
            values.put(MovieEntry.COLUMN_POSTER_PATH, movie.getPoster_path());
            values.put(MovieEntry.COLUMN_BACKDROP_PATH, movie.getBackdrop_path());
            values.put(MovieEntry.COLUMN_IS_FAVORITE, true);

            Uri uri = getContext().getContentResolver().insert(MovieEntry.CONTENT_URI, values);
            rowId = (int) ContentUris.parseId(uri);
        }

        if(rowId>0)
            setFavoriteAndUpdateButton(!isFavorite);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_reviews:
                onReviewsClick(v);
                break;

            case R.id.imageButton_favorite:
                onFavoriteClick(v);
        }
    }

    private Intent createShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setType("plain/text");
        if(movieTrailersListAdapter!= null && movieTrailersListAdapter.getItem(0)!=null) {
            String url = "https://www.youtube.com/watch?v=" + movieTrailersListAdapter.getItem(0).getKey();
            intent.putExtra(Intent.EXTRA_TEXT, url);
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, movie.getTitle());
        }

        return intent;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if(uri != null) {
            return new CursorLoader(getActivity(), uri, null, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data!=null && data.moveToFirst()) {
            int movieId = data.getInt(data.getColumnIndex(MovieEntry.COLUMN_ID));
            int voteCount = data.getInt(data.getColumnIndex(MovieEntry.COLUMN_VOTE_COUNT));
            float voteAverage = data.getFloat(data.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE));
            boolean hasVideo = Boolean.getBoolean(
                    data.getString(data.getColumnIndex(MovieEntry.COLUMN_VIDEO)));
            String title = data.getString(data.getColumnIndex(MovieEntry.COLUMN_TITLE));
            float popularity = data.getFloat(data.getColumnIndex(MovieEntry.COLUMN_POPULARITY));
            String posterPath = data.getString(data.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
            String releaseDate = data.getString(data.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE));
            String overview = data.getString(data.getColumnIndex(MovieEntry.COLUMN_OVERVIEW));
            String backdropPath = data.getString(data.getColumnIndex(MovieEntry.COLUMN_BACKDROP_PATH));
            boolean isFavorite = data.getInt(data.getColumnIndex(MovieEntry.COLUMN_IS_FAVORITE)) == 0?
                    false : true;
            movie = new Movie(voteCount, voteAverage, hasVideo, title, popularity, posterPath,
                    releaseDate, overview, null, null, movieId, null, backdropPath, false, isFavorite);

            String imageUrl = movie.constructImageURL(IMAGE_SIZE);
            Picasso.with(getActivity()).load(imageUrl).into(imageView);
            textViewTitle.setText(movie.getTitle());
            textViewReleaseDate.setText(movie.getRelease_date());
            textViewVoteAverage.setText(String.valueOf(movie.getVote_average()));
            textViewOverview.setText(movie.getOverview());

            Uri uri = MovieEntry.buildMovieUri(movie.getId());
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);

            if(cursor!=null && cursor.moveToFirst() && isFavorite) {
                setFavoriteAndUpdateButton(true);
            } else
                setFavoriteAndUpdateButton(false);

            if (shareActionProvider != null) {
                shareActionProvider.setShareIntent(createShareIntent());
            }

            GetMovieTrailersTask getMovieTrailersTask = new GetMovieTrailersTask(getActivity(), this, this);
            getMovieTrailersTask.execute(String.valueOf(movie.getId()));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
