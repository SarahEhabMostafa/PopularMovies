package com.sarahehabm.popularmovies.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.sarahehabm.popularmovies.Constants;
import com.sarahehabm.popularmovies.controller.GetMoviesTask;
import com.sarahehabm.popularmovies.controller.OnDataRetrieveSuccessListener;
import com.sarahehabm.popularmovies.controller.OnRetrieveListener;
import com.sarahehabm.popularmovies.R;
import com.sarahehabm.popularmovies.model.Movie;

import java.util.ArrayList;

public class PopularMoviesActivity extends AppCompatActivity implements OnDataRetrieveSuccessListener, OnRetrieveListener, AdapterView.OnItemClickListener {
    private final String TAG = PopularMoviesActivity.class.getSimpleName();

    private GridView gridView;
    private MoviesGridAdapter moviesGridAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        PreferenceManager.setDefaultValues(getBaseContext(), R.xml.pref_general, true);

        initializeViews();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String[] preference_values_sort_order = getResources().getStringArray(R.array.preference_values_sort_order);
        String sortOrder = sharedPreferences.getString(getString(R.string.preference_key_sort_order), preference_values_sort_order[0]);

        GetMoviesTask getMoviesTask = new GetMoviesTask(this, this, this);
        getMoviesTask.execute(sortOrder);
    }

    private void initializeViews() {
        gridView = (GridView) findViewById(R.id.movies_grid);
        moviesGridAdapter = new MoviesGridAdapter(this, null);
        gridView.setAdapter(moviesGridAdapter);

        gridView.setOnItemClickListener(this);
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
    public void updateUI(ArrayList<Movie> movies) {
        moviesGridAdapter.setMovies(movies);
        moviesGridAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", "Loading..", true, false);
    }

    @Override
    public void hideProgress() {
        if(progressDialog!=null)
            progressDialog.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Movie movie = (Movie)gridView.getAdapter().getItem(position);
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Constants.OBJECT_MOVIE, movie);
        startActivity(intent);
    }
}
