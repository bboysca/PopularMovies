package com.project.luismendez.popularmovies.com.project.luismendez.popularmovies.model;

import java.util.Date;

public class Movie {

    private String title;
    private String imageUrl;
    private String overview;
    private double rating;
    private Date releaseDate;

    public Movie(String title, String imageUrl, String overview, double rating, Date releaseDate) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.overview = overview;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return rating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }
}
