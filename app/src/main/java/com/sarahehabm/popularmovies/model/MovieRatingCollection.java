package com.sarahehabm.popularmovies.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by Sarah E. Mostafa on 12-Dec-15.
 */
public class MovieRatingCollection {
    private int id;
    private int page;
    private List<MovieRating> results;
    private int total_pages;
    private int total_results;

    public MovieRatingCollection() {
    }

    public MovieRatingCollection(int id, int page, List<MovieRating> results, int total_pages,
                                 int total_results) {
        this.id = id;
        this.page = page;
        this.results = results;
        this.total_pages = total_pages;
        this.total_results = total_results;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieRating> getResults() {
        return results;
    }

    public void setResults(List<MovieRating> results) {
        this.results = results;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public static MovieRatingCollection initFromJSON(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<MovieRatingCollection>(){}.getType());
    }
}
