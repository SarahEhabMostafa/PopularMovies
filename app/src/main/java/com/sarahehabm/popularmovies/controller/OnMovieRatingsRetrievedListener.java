package com.sarahehabm.popularmovies.controller;

import com.sarahehabm.popularmovies.model.MovieRating;

import java.util.ArrayList;

/**
 * Created by Sarah E. Mostafa on 12-Dec-15.
 */
public interface OnMovieRatingsRetrievedListener {
    void onRetrieveSuccess(ArrayList<MovieRating> movieRatings);
    void onRetrieveFailure();
}
