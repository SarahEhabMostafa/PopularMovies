package com.sarahehabm.popularmovies.controller;

import com.sarahehabm.popularmovies.model.Movie;

import java.util.ArrayList;

/**
 * Created by Sarah E. Mostafa on 14-Oct-15.
 */
public interface OnMoviesRetrieveSuccessListener {
    void updateUI(ArrayList<Movie> movies);
}
