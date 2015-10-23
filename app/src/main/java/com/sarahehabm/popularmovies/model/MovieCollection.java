package com.sarahehabm.popularmovies.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Sarah E. Mostafa on 14-Oct-15.
 */
public class MovieCollection {
    private int page;
    private List<Movie> results;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }

    public static MovieCollection initFromJSON(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<MovieCollection>(){}.getType());
    }
}
