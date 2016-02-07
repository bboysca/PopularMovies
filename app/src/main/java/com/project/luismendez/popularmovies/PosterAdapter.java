package com.project.luismendez.popularmovies;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.project.luismendez.popularmovies.model.Movie;

import java.util.ArrayList;

public class PosterAdapter extends ArrayAdapter<Movie> {

    public final static String MOVIEDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w185/";

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

    // Create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }

        //Determine the width/height based on the number of columns
        Resources resources = mContext.getResources();
        int columnWidth = resources.getDisplayMetrics().widthPixels;
        int numColumns = resources.getInteger(R.integer.grid_columns);
        int posterWidth = columnWidth / numColumns;
        int posterHeight = (3 * posterWidth / 2);

        //Resize placeholder to be same size as poster (since poster size isn't static)
        Drawable placeholder = ContextCompat.getDrawable(mContext, R.drawable.poster_placeholder);
        Bitmap bitmap = ((BitmapDrawable) placeholder).getBitmap();
        Drawable scaledPlaceholder = new BitmapDrawable(resources,
                Bitmap.createScaledBitmap(bitmap, posterWidth, posterHeight, true));
        Glide.with(mContext)
                .load(MOVIEDB_IMAGE_PATH + mMovies.get(position).getImageUrl())
                .centerCrop()
                .override(posterWidth, posterHeight)
                .placeholder(scaledPlaceholder)
                .into(imageView);
        imageView.setContentDescription(mMovies.get(position).getTitle());
        return imageView;
    }
}
