package com.project.luismendez.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.project.luismendez.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 * Fragment for movie grids
 */
public class MovieGridActivityFragment extends Fragment {

    private static final String DISCOVER_MOVIES_URL = "http://api.themoviedb.org/3/discover/movie";
    private static final String URL_PARAM_SORT = "sort_by";
    private static final String SORT_POPULARITY_VALUE = "popularity";
    private static final String SORT_RATING_VALUE = "vote_average";
    private static final String URL_PARAM_API_KEY = "api_key";
    //TODO FOR PROJECT 2: Research ways to secure API key
    private static final String API_KEY = "b94000a39a594e7c14f98f869f804839";

    private static final String MOVIE_GRID_TAG = "MovieGrid";

    private PosterAdapter mPosterAdapter;

    public MovieGridActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        GridView gridview = (GridView) rootView.findViewById(R.id.movie_grid_view);

        ArrayList<Movie> movieList = new ArrayList<Movie>();
        mPosterAdapter = new PosterAdapter(getActivity(), R.layout.fragment_movie_grid, movieList);
        gridview.setAdapter(mPosterAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent detailIntent = new Intent(getActivity(), MovieDetailsActivity.class)
                        .putExtra(Movie.MOVIE_PARCEL, (Movie) parent.getItemAtPosition(position));
                startActivity(detailIntent);
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Resources resources = getActivity().getResources();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortPrefValue = sharedPref.getString(resources.getString(R.string.settings_sort_key),
                resources.getString(R.string.settings_sort_default));

        String[] sortValues = resources.getStringArray(R.array.sort_values_array);
        String sortBy;
        if (sortValues[0].equals(sortPrefValue)) {
            sortBy = SORT_POPULARITY_VALUE;
        } else {
            sortBy = SORT_RATING_VALUE;
        }

        Uri.Builder uri = Uri.parse(DISCOVER_MOVIES_URL).buildUpon();
        uri.appendQueryParameter(URL_PARAM_SORT, sortBy + ".desc");
        uri.appendQueryParameter(URL_PARAM_API_KEY, API_KEY);
        new FetchMoviesTask().execute(uri.build().toString());
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected Movie[] doInBackground(String... urls) {
            return downloadUrl(urls[0]);
        }

        @Override
        protected void onPostExecute(Movie[] result) {
            if (result != null && result.length > 0) {
                mPosterAdapter.clear();
                mPosterAdapter.addAll(Arrays.asList(result));
            } else {
                String requestFailure =
                        getActivity().getResources().getString(R.string.grid_discover_failure);
                Toast.makeText(getActivity(), requestFailure, Toast.LENGTH_SHORT).show();
            }
        }

        private Movie[] downloadUrl(String url) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String response;

            //TODO PROJECT 2 : Look into using Volley for making requests
            try {
                URL requestUrl = new URL(url);

                // Create the request to TheMovieDB, and open the connection
                urlConnection = (HttpURLConnection) requestUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Append new line for debugging purposes
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                response = buffer.toString();
            } catch (IOException e) {
                Log.e(MOVIE_GRID_TAG, "I/O Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(MOVIE_GRID_TAG, "Error closing stream", e);
                    }
                }
            }

            //Parse the response into a Movie array that will be adapted into the grid view
            Movie[] movies;
            try {
                movies = parseMoviesFromJson(response);
            } catch (JSONException e) {
                Log.e(MOVIE_GRID_TAG, "Error parsing response data");
                return null;
            }

            return movies;
        }

        private Movie[] parseMoviesFromJson(String movieJsonStr) throws JSONException {
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieResults = movieJson.getJSONArray("results");
            int movieResultsLength = movieResults.length();
            Movie[] movies = new Movie[movieResultsLength];
            for (int i = 0; i < movieResults.length(); i++) {
                JSONObject movieResult = movieResults.getJSONObject(i);
                String id = movieResult.getString("id");
                String title = movieResult.getString("title");
                String posterUrl = movieResult.getString("poster_path");
                String overview = movieResult.getString("overview");
                double rating = movieResult.getDouble("vote_average");
                String releaseDateStr = movieResult.getString("release_date");
                Date releaseDate;
                try {
                    releaseDate = new SimpleDateFormat("yyyy-MM-dd").parse(releaseDateStr);
                } catch (ParseException e) {
                    releaseDate = null;
                }
                movies[i] = new Movie(id, title, posterUrl, overview, rating, releaseDate);
            }
            return movies;
        }
    }
}
