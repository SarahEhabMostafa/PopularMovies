package com.sarahehabm.popularmovies.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sarahehabm.popularmovies.Constants;
import com.sarahehabm.popularmovies.R;
import com.sarahehabm.popularmovies.controller.GetMovieReviewsTask;
import com.sarahehabm.popularmovies.controller.OnMovieRatingsRetrievedListener;
import com.sarahehabm.popularmovies.controller.OnRetrieveListener;
import com.sarahehabm.popularmovies.model.Movie;
import com.sarahehabm.popularmovies.model.MovieRating;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieReviewsActivity extends AppCompatActivity implements OnRetrieveListener, OnMovieRatingsRetrievedListener {
    @Bind(R.id.listView_Reviews) ListView listViewReviews;
    @Bind(R.id.textView_no_content) TextView textViewNoContent;

    private Movie movie;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_reviews);
        ButterKnife.bind(this);

        if(getIntent()!=null && getIntent().hasExtra(Constants.OBJECT_MOVIE))
            movie = (Movie) getIntent().getExtras().get(Constants.OBJECT_MOVIE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(movie.getTitle());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (getActionBar() != null) {
            getActionBar().setTitle(movie.getTitle());
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        GetMovieReviewsTask getMovieReviewsTask = new GetMovieReviewsTask(this, this, this);
        getMovieReviewsTask.execute(String.valueOf(movie.getId()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        progressDialog = ProgressDialog.show(this, "", "Loading..", true, false);
    }

    @Override
    public void hideProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onRetrieveSuccess(ArrayList<MovieRating> movieRatings) {
        if(movieRatings!=null && !movieRatings.isEmpty()) {

            textViewNoContent.setVisibility(View.GONE);
            listViewReviews.setVisibility(View.VISIBLE);

            List<String> ratings_str = new ArrayList<String>();
            for (MovieRating movieRating: movieRatings) {
                ratings_str.add(movieRating.getContent());
            }

            listViewReviews.setAdapter(new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, ratings_str));
        } else {
            textViewNoContent.setVisibility(View.VISIBLE);
            listViewReviews.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRetrieveFailure() {
        textViewNoContent.setVisibility(View.VISIBLE);
        listViewReviews.setVisibility(View.GONE);
    }
}
