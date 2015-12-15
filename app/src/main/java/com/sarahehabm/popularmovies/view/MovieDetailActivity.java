package com.sarahehabm.popularmovies.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.sarahehabm.popularmovies.Constants;
import com.sarahehabm.popularmovies.R;

public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.MOVIE_URI, getIntent().getData());

            MovieDetailFragment detailFragment = new MovieDetailFragment();
            detailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, detailFragment)
                    .commit();

            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle("");
            if (getActionBar() != null)
                getActionBar().setTitle("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
