package com.example.android.popularMovies.movieDetailInfo;

import android.util.Log;

import com.example.android.popularMovies.DetailActivity;
import com.example.android.popularMovies.model.Review;
import com.example.android.popularMovies.model.Trailer;
import com.example.android.popularMovies.utilities.JsonUtils;
import com.example.android.popularMovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MovieExtraInfo {

    private static final String TAG = MovieExtraInfo.class.getSimpleName();




    public static List<Review> fetchMovieReviews(int movieId) throws IOException, JSONException{

        List<Review> reviewList;

            String reviewUriStr = NetworkUtils.buildExtraInfoUri(movieId, DetailActivity.ACTION_GET_REVIEWS);
            URL reviewUrl = new URL(reviewUriStr);

            String response = NetworkUtils.getResponseFromHttpUrl(reviewUrl);
            //Log.v(TAG, "--------------------------movie url :" + reviewUriStr);

            reviewList = JsonUtils.parseReviewJsonData(response);
            //Log.v(TAG, "--------------------------received movies array, length :" + movieList.size());

            return  reviewList;

    }


    public static List<Trailer> fetchMovieTrailers(int movieId) throws IOException, JSONException {

        List<Trailer> trailerList;


            String trailerUriStr = NetworkUtils.buildExtraInfoUri(movieId, DetailActivity.ACTION_GET_TRAILERS);
            URL trailerUrl = new URL(trailerUriStr);

            String response = NetworkUtils.getResponseFromHttpUrl(trailerUrl);
            //Log.v(TAG, "--------------------------movie url :" + trailerUriStr);

            trailerList = JsonUtils.parseTrailerJsonData(response);
            //Log.v(TAG, "--------------------------received movies array, length :" + movieList.size());
            return  trailerList;


    }
}
