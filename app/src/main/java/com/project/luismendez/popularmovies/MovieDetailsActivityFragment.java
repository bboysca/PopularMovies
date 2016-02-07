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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.project.luismendez.popularmovies.model.Movie;

import java.util.Calendar;
import java.util.Date;

/**
 * Fragment for showing the details about the movie selected
 */
public class MovieDetailsActivityFragment extends Fragment {

    private static final float RELEASE_DATE_SIZE = .75f;

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        Intent intent = getActivity().getIntent();
        final Movie movie = intent.getParcelableExtra(Movie.MOVIE_PARCEL);

        final ImageView posterView = (ImageView) rootView.findViewById(R.id.details_poster);
        ViewTreeObserver viewTreeObserver = posterView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new MyOnGlobalLayoutListener(posterView, movie));

        TextView titleText = (TextView) rootView.findViewById(R.id.details_title);
        titleText.setText(getMovieTitle(movie));

        TextView ratingText = (TextView) rootView.findViewById(R.id.details_rating);
        String ratingFormat = getActivity().getString(R.string.details_rating_format);
        ratingText.setText(String.format(ratingFormat, String.valueOf(movie.getRating())));

        TextView overviewTextView = (TextView) rootView.findViewById(R.id.details_overview);
        overviewTextView.setText(movie.getOverview());

        return rootView;
    }

    /**
     * Draws the poster when the view is ready
     */
    private class MyOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private final ImageView mPosterView;
        private final Movie mMovie;

        public MyOnGlobalLayoutListener(ImageView posterView, Movie movie) {
            this.mPosterView = posterView;
            this.mMovie = movie;
        }

        public void onGlobalLayout() {
            int posterWidth = mPosterView.getMeasuredWidth();
            int posterHeight = (3 * posterWidth / 2);
            //resize placeholder to be same size as poster (since poster size isn't static)
            Drawable placeholderImg =
                    ContextCompat.getDrawable(getActivity(), R.drawable.poster_placeholder);
            Bitmap bitmap = ((BitmapDrawable) placeholderImg).getBitmap();
            Drawable scaledPlaceholder = new BitmapDrawable(getActivity().getResources(),
                    Bitmap.createScaledBitmap(bitmap, posterWidth, posterHeight, true));
            Glide.with(getContext())
                    .load(PosterAdapter.MOVIEDB_IMAGE_PATH + mMovie.getImageUrl())
                    .placeholder(scaledPlaceholder)
                    .override(posterWidth, posterHeight)
                    .into(mPosterView);
        }
    }

    /**
     * If the movie has a known release date, combine the year as part of the title with the
     * text size of the year being a fraction smaller.
     * eg. Gladiator (2000)
     */
    private CharSequence getMovieTitle(Movie movie) {
        String title = movie.getTitle();
        Date releaseDate = movie.getReleaseDate();
        if (releaseDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(releaseDate);
            int calendarYear = calendar.get(Calendar.YEAR);
            String releaseYear = " (" + String.valueOf(calendarYear) + ")";

            String completeString = title + releaseYear;
            SpannableString styledString = new SpannableString(title + releaseYear);
            styledString.setSpan(new RelativeSizeSpan(RELEASE_DATE_SIZE), title.length() + 1,
                    completeString.length(), 0);
            return styledString;
        } else {
            return title;
        }
    }
}
