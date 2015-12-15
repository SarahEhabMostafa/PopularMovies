package com.sarahehabm.popularmovies.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.sarahehabm.popularmovies.Constants;
import com.sarahehabm.popularmovies.R;
import com.sarahehabm.popularmovies.controller.OnItemSelectedCallback;

public class PopularMoviesActivity extends AppCompatActivity implements OnItemSelectedCallback {
    private final String TAG = PopularMoviesActivity.class.getSimpleName();
    private final String DETAIL_FRAGMENT_TAG = MovieDetailFragment.class.getSimpleName();

    private SharedPreferences sharedPreferences;
    private boolean isTablet;
    private String mSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        PreferenceManager.setDefaultValues(getBaseContext(), R.xml.pref_general, true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mSortOrder = sharedPreferences.getString(getString(R.string.preference_key_sort_order),
                getString(R.string.preference_default_sort_order));
        isTablet = (findViewById(R.id.movie_detail_container) != null)? true : false;

        if(isTablet) {
            if(savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, new MovieDetailFragment(),
                            DETAIL_FRAGMENT_TAG)
                    .commit();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
        supportInvalidateOptionsMenu();


        String sortOrder = sharedPreferences.getString(getString(R.string.preference_key_sort_order),
                getString(R.string.preference_default_sort_order));


        if(sortOrder!= null && !sortOrder.equals(mSortOrder)) {
            MovieListFragment movieListFragment = (MovieListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.movie_list_container);
            if(movieListFragment !=null) {
                movieListFragment.onSortOrderChange();
            }

            MovieDetailFragment detailFragment = (MovieDetailFragment) getSupportFragmentManager()
                    .findFragmentByTag(DETAIL_FRAGMENT_TAG);
            if(detailFragment != null)
                detailFragment.onSortOrderChange(sortOrder);

            mSortOrder = sortOrder;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_popular_movies, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri uri) {
        if (isTablet) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.MOVIE_URI, uri);

            MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
            movieDetailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, movieDetailFragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class)
                    .setData(uri);
            startActivity(intent);
        }
    }
}
