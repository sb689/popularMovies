package com.example.android.popularMovies.utilities;

import android.net.Uri;

import com.example.android.popularMovies.BuildConfig;
import com.example.android.popularMovies.DetailActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185/";
    private static final String API_KEY = BuildConfig.MOVIE_DB_API_KEY;

    private static final String KEY_PARAM_API = "api_key";
    private static final String KEY_PARAM_REVIEW = "reviews";
    private static final String KEY_PARAM_TRAILER = "videos";

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?";
    private static final String YOUTUBE_KEY_PARAM_VIDEO = "v";

    public static String buildUrl(String searchCriteria)
    {

        Uri uri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                .appendEncodedPath(searchCriteria)
                .appendQueryParameter(KEY_PARAM_API, API_KEY)
                .build();
       // Log.v(TAG, "builtUri ::::::::::::::::::::::::" + uri.toString());
       String searchQuery = uri.toString();

        return searchQuery;
    }

    public static Uri buildMoviePosterUrl(String posterPath)
    {
        Uri uri = Uri.parse(POSTER_BASE_URL).buildUpon()
                .appendEncodedPath(posterPath)
                .build();
        // Log.v(TAG, "-------------------poster uri:" + uri.toString());

        return uri;
    }

    public static String buildExtraInfoUri(int movieId, String action)
    {
       Uri retUri = null;

        if(action.equals(DetailActivity.ACTION_GET_REVIEWS))
       {
           retUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                   .appendEncodedPath(Integer.toString( movieId))
                   .appendEncodedPath(KEY_PARAM_REVIEW)
                   .appendQueryParameter(KEY_PARAM_API,API_KEY)
                   .build();
       }
        else if(action.equals(DetailActivity.ACTION_GET_TRAILERS))
        {
            retUri = Uri.parse(MOVIE_DB_BASE_URL).buildUpon()
                    .appendEncodedPath(Integer.toString( movieId))
                    .appendEncodedPath(KEY_PARAM_TRAILER)
                    .appendQueryParameter(KEY_PARAM_API,API_KEY)
                    .build();
        }
        return retUri.toString();
    }

    public static Uri getYoutubeUri(String key){

        Uri retUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendQueryParameter(YOUTUBE_KEY_PARAM_VIDEO,key)
                .build();
        return retUri;
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
