package com.sarahehabm.popularmovies.controller;

import com.sarahehabm.popularmovies.model.MovieTrailer;

import java.util.ArrayList;

/**
 * Created by Sarah E. Mostafa on 14-Oct-15.
 */
public interface OnMovieTrailersRetrievedListener {
    void onRetrieveSuccess(ArrayList<MovieTrailer> movieTrailers);
    void onRetrieveFailure();
}
