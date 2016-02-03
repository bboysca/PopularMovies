package com.project.luismendez.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.project.luismendez.popularmovies.com.project.luismendez.popularmovies.model.Movie;

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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieGridActivityFragment extends Fragment {

    public static final String MOVIE_PARCEL = "movieParcel";

    private static final String DISCOVER_MOVIES_URL = "http://api.themoviedb.org/3/discover/movie";
    //TODO Find some way to change w185 depending on device size
    private final static String MOVIEDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w185/";
    private static final String MOVIE_GRID_TAG = "MovieGrid";

    //TODO REMOVE
    private static final String API_KEY = "b94000a39a594e7c14f98f869f804839";

    private static final Map<String, String> SORT_MAP;
    static {
        Map<String, String> sortMap = new HashMap<>();
        sortMap.put("0", "popularity");
        sortMap.put("1", "vote_average");
        SORT_MAP = Collections.unmodifiableMap(sortMap);
    }


    private PosterAdapter mPosterAdapter;

    public MovieGridActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.movie_grid_view);

        // TODO
        // references to our images
        String[] mThumbIds = {
                MOVIEDB_IMAGE_PATH + "nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"
        };

        ArrayList<Movie> movieList = new ArrayList<Movie>();

        mPosterAdapter = new PosterAdapter(getActivity(), R.layout.fragment_movies, movieList);
        gridview.setAdapter(mPosterAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent detailIntent = new Intent(getActivity(), MovieDetailsActivity.class)
                        .putExtra(MOVIE_PARCEL, (Movie) parent.getItemAtPosition(position));
                startActivity(detailIntent);
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Uri.Builder uri = Uri.parse(DISCOVER_MOVIES_URL).buildUpon();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortPrefValue = sharedPref.getString("movieSort", "0");
        Log.d("PREFERENCE", sortPrefValue);

        String sortBy = SORT_MAP.get(sortPrefValue);

        uri.appendQueryParameter("sort_by", sortBy + ".desc");
        uri.appendQueryParameter("api_key", API_KEY);
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
                mPosterAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Not able to find movies", Toast.LENGTH_SHORT);
            }
        }

        private Movie[] downloadUrl(String url) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String response;

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
                    // TODO Handle edge case
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Append new line for debugging purposes
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    // TODO Handle edge case
                    return null;
                }
                response = buffer.toString();
            } catch (IOException e) {
                Log.e(MOVIE_GRID_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        //TODO Handle error case
                        Log.e(MOVIE_GRID_TAG, "Error closing stream", e);
                    }
                }
            }

            Movie[] movies;
            try {
                movies = parseMoviesFromJson(response);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(MOVIE_GRID_TAG, "Error parsing response data");
                return null;
            }

            for (Movie movie : movies) {
                Log.d(MOVIE_GRID_TAG, movie.toString());
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
