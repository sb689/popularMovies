package com.example.android.popularMovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularMovies.model.Movie;
import com.example.android.popularMovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {


    private static final String TAG = DetailActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView mOriginalTitleTV;
        ImageView mPosterIv;
        TextView mReleaseDate;
        TextView mVoteAverage;
        TextView mOverview;

        mOriginalTitleTV = (TextView) findViewById(R.id.tv_detail_movie_title);
        mPosterIv = (ImageView) findViewById(R.id.iv_detail_poster);
        mReleaseDate = (TextView) findViewById(R.id.tv_detail_release_year);
        mVoteAverage = (TextView) findViewById(R.id.tv_detail_rating);
        mOverview = (TextView) findViewById(R.id.tv_detail_overview);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(getString(R.string.intent_package_key));

        mOriginalTitleTV.getLayoutParams().height = (int) MainActivity.displayScreenHeight / 5;
        mOriginalTitleTV.setText(movie.getOriginalTitle());

        String releaseDateStr = movie.getReleaseDate();
       if(releaseDateStr != null || !releaseDateStr.isEmpty()) {
           String[] dates = releaseDateStr.split("-");
           mReleaseDate.setText(dates[0]);
       }

        String rating =  movie.getVoteAverage() + "/10";
        mVoteAverage.setText(rating);
        mOverview.setText(movie.getOverview());

        //setting poster
        mPosterIv.getLayoutParams().height = (int) MainActivity.displayScreenHeight/3;
        String posterPath = movie.getPosterPath();
        Uri posterUri = NetworkUtils.buildMoviePosterUrl(posterPath);
        //Log.v(TAG, "::::::::::::::::::posterPath uri is:" + posterUri.toString());

        if (posterUri != null) {
            Picasso.get().load(posterUri).into(mPosterIv);

        }

    }
}
