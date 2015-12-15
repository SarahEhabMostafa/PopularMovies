package com.sarahehabm.popularmovies.model;

/**
 * Created by Sarah E. Mostafa on 12-Dec-15.
 */
public class MovieRating {
    private String id;
    private String author;
    private String content;
    private String url;

    public MovieRating() {
    }

    public MovieRating(String id, String author, String content, String url) {

        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
