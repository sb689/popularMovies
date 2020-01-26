package com.example.android.popularMovies.utilities;

import com.example.android.popularMovies.model.Movie;

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


}
