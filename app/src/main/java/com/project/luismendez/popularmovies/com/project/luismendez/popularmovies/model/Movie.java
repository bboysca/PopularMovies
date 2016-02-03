package com.project.luismendez.popularmovies.com.project.luismendez.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Movie implements Parcelable {
    private String mId; //in case we have to make extra api calls in the future
    private String mTitle;
    private String mImageUrl;
    private String mOverview;
    private double mRating;
    private Date mReleaseDate;

    public Movie(String id, String title, String imageUrl, String overview, double rating,
                 Date releaseDate) {
        this.mId = id;
        this.mTitle = title;
        this.mImageUrl = imageUrl;
        this.mOverview = overview;
        this.mRating = rating;
        this.mReleaseDate = releaseDate;
    }

    private Movie(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mImageUrl = in.readString();
        mOverview = in.readString();
        mRating = in.readDouble();
        long time = in.readLong();
        if (time == -1) {
            mReleaseDate = null;
        } else {
            mReleaseDate = new Date(time);
        }

    }

    public String getId() {
        return mId;
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
        return "id: " + mId + " " +
                "title: " + mTitle + " " +
                "imageUrl: " + mImageUrl + " " +
                "overview: " + mOverview + " " +
                "rating: " + mRating + " " +
                "releaseDate: " + mReleaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mId);
        out.writeString(mTitle);
        out.writeString(mImageUrl);
        out.writeString(mOverview);
        out.writeDouble(mRating);
        if (mReleaseDate == null) {
            out.writeLong(-1);
        } else {
            out.writeLong(mReleaseDate.getTime());
        }

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
