package com.sarahehabm.popularmovies.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.sarahehabm.popularmovies.Constants;
import com.sarahehabm.popularmovies.R;
import com.sarahehabm.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity {
    private Movie movie;
    private ImageView imageView;
    private TextView textViewTitle, textViewReleaseDate, textViewVoteAverage, textViewOverview;

    private final String IMAGE_SIZE = "original";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        movie = (Movie) getIntent().getExtras().get(Constants.OBJECT_MOVIE);

        initialize();
        if(getSupportActionBar()!=null)
            getSupportActionBar().setTitle(movie.getTitle());
        if(getActionBar()!=null)
            getActionBar().setTitle(movie.getTitle());
    }

    private void initialize() {
        imageView = (ImageView) findViewById(R.id.imageView);
        textViewTitle = (TextView) findViewById(R.id.textView_movie_title);
        textViewReleaseDate = (TextView) findViewById(R.id.textView_movie_release_date);
        textViewVoteAverage = (TextView) findViewById(R.id.textView_movie_vote_average);
        textViewOverview = (TextView) findViewById(R.id.textView_movie_overview);

        String imageUrl = movie.constructImageURL(IMAGE_SIZE);
        Picasso.with(this).load(imageUrl).into(imageView);
        textViewTitle.setText(movie.getTitle());
        textViewReleaseDate.setText(movie.getRelease_date());
        textViewVoteAverage.setText(String.valueOf(movie.getVote_average()));
        textViewOverview.setText(movie.getOverview());
    }
}
