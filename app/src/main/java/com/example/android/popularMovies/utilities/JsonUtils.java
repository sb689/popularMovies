package com.example.android.popularMovies.utilities;

import com.example.android.popularMovies.model.Movie;
import com.example.android.popularMovies.model.Review;
import com.example.android.popularMovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {


    public static List<Movie> parseMovieJsonData(String movieData) throws JSONException {

        final String M_TITLE = "original_title";
        final String M_POSTER_PATH = "poster_path";
        final String M_OVERVIEW = "overview";
        final String M_VOTE_AVERAGE = "vote_average";
        final String M_RELEASE_DATE = "release_date";
        final String M_RESULTS_ARR = "results";
        final String M_ID = "id";

        List<Movie> movies = new ArrayList<>();

        JSONObject movieJson = new JSONObject(movieData);
        //get results array

        JSONArray resultsJsonArr = movieJson.getJSONArray(M_RESULTS_ARR);
        //parse array and create Movie arr
        for(int i= 0; i< resultsJsonArr.length(); i++)
        {
            JSONObject movieJsonObj = resultsJsonArr.getJSONObject(i);
            int id = movieJsonObj.getInt(M_ID);
            String title = movieJsonObj.getString(M_TITLE);
            String posterPath = movieJsonObj.getString(M_POSTER_PATH) == null? "" : movieJsonObj.getString(M_POSTER_PATH);
            String overview = movieJsonObj.getString(M_OVERVIEW);
            double voteAverage = movieJsonObj.getDouble(M_VOTE_AVERAGE);
            String releaseDate = movieJsonObj.getString(M_RELEASE_DATE);

            Movie movie = new Movie();
            movie.setMovieId(id);
            movie.setOriginalTitle(title);
            movie.setPosterPath(posterPath);
            movie.setOverview(overview);
            movie.setVoteAverage(voteAverage);
            movie.setReleaseDate(releaseDate);

            movies.add(movie);

        }
        return movies;

    }


    public static List<Review> parseReviewJsonData(String reviewData) throws JSONException
    {

        final String KEY_AUTHOR = "author";
        final String KEY_CONTENT = "content";
        final String KEY_ID = "id";
        final String KEY_URL = "url";
        final String KEY_RESULTS = "results";

        JSONObject reviewJsonObjAll = new JSONObject(reviewData);
        JSONArray resultArr = reviewJsonObjAll.getJSONArray(KEY_RESULTS);

        List<Review> reviewList = new ArrayList<>();
        for(int i=0; i< resultArr.length(); i++)
        {
            JSONObject reviewJsonObj = resultArr.getJSONObject(i);
            Review review = new Review();
            review.setId(reviewJsonObj.getString(KEY_ID));
            review.setAuthor(reviewJsonObj.getString(KEY_AUTHOR));
            review.setContent(reviewJsonObj.getString(KEY_CONTENT));
            review.setUrl(reviewJsonObj.getString(KEY_URL));

            reviewList.add(review);
        }

        return reviewList;
    }

    public static List<Trailer> parseTrailerJsonData(String trailerData) throws JSONException
    {

        final String KEY_ID = "id";
        final String KEY_KEY = "key";
        final String KEY_SITE = "site";
        final String KEY_RESULTS = "results";
//        final String KEY_NAME = "name";
//        final String KEY_SIZE = "size";
//        final String KEY_TYPE = "type";



        JSONObject trailerJsonObjAll = new JSONObject(trailerData);
        JSONArray resultArr = trailerJsonObjAll.getJSONArray(KEY_RESULTS);

        List<Trailer> trailerList = new ArrayList<>();
        for(int i=0; i< resultArr.length(); i++)
        {
            JSONObject trailerJsonObject = resultArr.getJSONObject(i);
            Trailer trailer = new Trailer();
            trailer.setId(trailerJsonObject.getString(KEY_ID));
            trailer.setKey(trailerJsonObject.getString(KEY_KEY));
            trailer.setSite(trailerJsonObject.getString(KEY_SITE));

//            trailer.setName(trailerJsonObject.getString(KEY_NAME));
//            trailer.setSize(trailerJsonObject.getInt(KEY_SIZE));
//            trailer.setType(trailerJsonObject.getString(KEY_TYPE));

            trailerList.add(trailer);

        }
        return trailerList;
    }

}
