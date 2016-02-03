package com.project.luismendez.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.luismendez.popularmovies.com.project.luismendez.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment {

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        Intent intent = getActivity().getIntent();
        final Movie movie = intent.getParcelableExtra(MovieGridActivityFragment.MOVIE_PARCEL);

        final ImageView posterView = (ImageView) rootView.findViewById(R.id.details_poster);
        ViewTreeObserver vto = posterView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                int posterWidth = posterView.getMeasuredWidth();
                int posterHeight = (3 * posterWidth / 2);
                //resize placeholder to be same size as poster (since poster size isn't static)
                Drawable dr = ContextCompat.getDrawable(getActivity(), R.mipmap.ic_launcher);
                Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
                Drawable d = new BitmapDrawable(getActivity().getResources(),
                        Bitmap.createScaledBitmap(bitmap, posterWidth, posterHeight, true));
                Picasso.with(getContext())
                        .load(PosterAdapter.MOVIEDB_IMAGE_PATH + movie.getImageUrl())
                        .placeholder(d)
                        .resize(posterWidth, posterHeight)
                        .into(posterView);
            }
        });

        TextView titleText = (TextView) rootView.findViewById(R.id.details_title);
        String title = movie.getTitle();
        String releaseYear;
        if (movie.getReleaseDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(movie.getReleaseDate());
            releaseYear = " (" + String.valueOf(calendar.get(Calendar.YEAR)) + ")";

            String completeString = title + releaseYear;
            SpannableString styledString = new SpannableString(title + releaseYear);
            styledString.setSpan(new RelativeSizeSpan(.75f), title.length() + 1, completeString.length(), 0);
            titleText.setText(styledString);
        } else {
            titleText.setText(title);
        }

        TextView ratingText = (TextView) rootView.findViewById(R.id.details_rating);
        ratingText.setText(String.valueOf(movie.getRating()) + "/10");

        TextView overviewTextView = (TextView) rootView.findViewById(R.id.details_overview);
        overviewTextView.setText(movie.getOverview());

        return rootView;
    }
}
