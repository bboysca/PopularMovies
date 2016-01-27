package com.project.luismendez.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.luismendez.popularmovies.com.project.luismendez.popularmovies.model.Movie;

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
        Movie movie = intent.getParcelableExtra(MovieGridActivityFragment.MOVIE_PARCEL);
        TextView textView = (TextView) rootView.findViewById(R.id.detail_text);
        textView.setText(movie.getTitle());
        return rootView;
    }
}
