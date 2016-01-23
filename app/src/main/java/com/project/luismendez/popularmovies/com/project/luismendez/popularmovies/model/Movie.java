package com.project.luismendez.popularmovies.com.project.luismendez.popularmovies.model;

import java.util.Date;

public class Movie {

    private String mTitle;
    private String mImageUrl;
    private String mOverview;
    private double mRating;
    private Date mReleaseDate;

    public Movie(String title, String imageUrl, String overview, double rating, Date releaseDate) {
        this.mTitle = title;
        this.mImageUrl = imageUrl;
        this.mOverview = overview;
        this.mRating = rating;
        this.mReleaseDate = releaseDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getOverview() {
        return mOverview;
    }

    public double getRating() {
        return mRating;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public String toString() {
        return "title: " + mTitle + " " +
                "imageUrl: " + mImageUrl + " " +
                "overview: " + mOverview + " " +
                "rating: " + mRating + " " +
                "releaseDate: " + mReleaseDate;
    }
}
