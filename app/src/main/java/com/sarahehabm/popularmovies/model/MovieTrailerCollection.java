package com.sarahehabm.popularmovies.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Sarah E. Mostafa on 12-Dec-15.
 */
public class MovieTrailerCollection {
    private String id;
    private List<MovieTrailer> results;

    public MovieTrailerCollection() {
    }

    public MovieTrailerCollection(String id, List<MovieTrailer> results) {

        this.id = id;
        this.results = results;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<MovieTrailer> getResults() {
        return results;
    }

    public void setResults(List<MovieTrailer> results) {
        this.results = results;
    }

    public static MovieTrailerCollection initFromJSON(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<MovieTrailerCollection>(){}.getType());
    }
}
