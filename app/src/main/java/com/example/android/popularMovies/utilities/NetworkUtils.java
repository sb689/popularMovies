package com.example.android.popularMovies.utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String MOVIE_DB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";


    private static final String API_KEY_PARAM = "api_key";


    public static URL buildUrl(String searchCriteria, String api_key)
    {

        Uri uri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendEncodedPath(searchCriteria)
                .appendQueryParameter(API_KEY_PARAM, api_key)
                .build();
        Log.v(TAG, "builtUri ::::::::::::::::::::::::" + uri.toString());
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static Uri buildMoviePosterUrl(String posterPath)
    {
        Uri uri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendEncodedPath(posterPath)
                .build();
        // Log.v(TAG, "-------------------poster uri:" + uri.toString());

        return uri;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }




}
