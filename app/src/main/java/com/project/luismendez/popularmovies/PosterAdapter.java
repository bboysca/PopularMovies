package com.project.luismendez.popularmovies;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.project.luismendez.popularmovies.com.project.luismendez.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PosterAdapter extends ArrayAdapter<Movie> {

    private final static String MOVIEDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w185/";

    private Context mContext;
    private int mLayoutId;
    private ArrayList<Movie> mMovies;

    public PosterAdapter(Context context, int layoutId, ArrayList<Movie> movies) {
        super(context, layoutId, movies);
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mMovies = movies;
    }

    public int getCount() {
        return mMovies.size();
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView2;
        if (convertView == null) {
            imageView2 = new ImageView(mContext);
        } else {
            imageView2 = (ImageView) convertView;
        }

        int columnWidth = mContext.getResources().getDisplayMetrics().widthPixels;
        int posterWidth;
        int posterHeight;
        if (Configuration.ORIENTATION_PORTRAIT == mContext.getResources().getConfiguration().orientation) {
            posterWidth = columnWidth / 2; //2 columns portrait, 3 columns landscape
            posterHeight = (3 * columnWidth / 4); //1 * 1.5 poster aspect ratio
        } else {
            posterWidth = columnWidth / 3;
            posterHeight = columnWidth / 2;
        }

        Picasso.with(mContext)
                .load(MOVIEDB_IMAGE_PATH + mMovies.get(position).getImageUrl())
                .centerCrop().resize(posterWidth, posterHeight)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView2);
        return imageView2;
    }
}
