package com.project.luismendez.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
                Log.d("DETAILS_TAG", String.valueOf(posterWidth));
                Picasso.with(getContext())
                        .load(PosterAdapter.MOVIEDB_IMAGE_PATH + movie.getImageUrl())
                        .placeholder(R.mipmap.ic_launcher)
                        .resize(posterWidth, posterHeight)
                        .into(posterView);
            }
        });

        String title = movie.getTitle();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(movie.getReleaseDate());
        String releaseYear = " (" + String.valueOf(calendar.get(Calendar.YEAR)) + ")";

        TextView titleText = (TextView) rootView.findViewById(R.id.details_title);
        String completeString = title + releaseYear;
        SpannableString styledString = new SpannableString(title + releaseYear);
        styledString.setSpan(new RelativeSizeSpan(.75f), title.length() + 1, completeString.length(), 0);
        titleText.setText(styledString);

        TextView ratingText = (TextView) rootView.findViewById(R.id.details_rating);
        ratingText.setText(String.valueOf(movie.getRating()) + "/10");

        TextView overviewTextView = (TextView) rootView.findViewById(R.id.details_overview);
        overviewTextView.setText(movie.getOverview());

        return rootView;
    }
}
