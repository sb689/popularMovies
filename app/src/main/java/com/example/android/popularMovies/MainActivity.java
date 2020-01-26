package com.example.android.popularMovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.android.popularMovies.model.Movie;
import com.example.android.popularMovies.utilities.JsonUtils;
import com.example.android.popularMovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GRID_SPAN_COUNT = 2;
    private static final int GRID_IMAGE_RATIO = 2;
    private static final String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    public static int displayScreenHeight = 0;


    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageTv;
    private ProgressBar mProgressBarPb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_recycler_view);

        int viewHolderHeight = getCalculatedHeight(GRID_IMAGE_RATIO);
        mMovieAdapter = new MovieAdapter(this,viewHolderHeight);
        mErrorMessageTv = (TextView) findViewById(R.id.tv_error_message);
        mProgressBarPb = (ProgressBar) findViewById(R.id.pb_loading);


        GridLayoutManager layoutManager = new GridLayoutManager(this,GRID_SPAN_COUNT);
        layoutManager.setReverseLayout(false);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.setHasFixedSize(true);

        loadMovieData(getString(R.string.search_popular));

    }


    private int getCalculatedHeight(int divBy)
    {
        /*the display matrix code is copied from stackOverflow
        */

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int imageViewHeight = (int) height/divBy;
        displayScreenHeight = height;
       // Log.v(TAG, "::::::::::::::::::::::::::dimension height=" + height + ", width = " + width);
        return imageViewHeight;

    }


    private void loadMovieData(String searchCriteria)
    {
        showMovieView();
        new FetchMovieTask().execute(searchCriteria);
    }

    private void showErrorMessage()
    {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageTv.setVisibility(View.VISIBLE);
    }

    private void showMovieView()
    {
        mErrorMessageTv.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void movieAdapterOnClick(int position) {

        Movie movie = mMovieAdapter.getmMovieList().get(position);
        Class destination = DetailActivity.class;
        Intent intent = new Intent(this,destination);
        intent.putExtra(getString(R.string.intent_package_key), movie );
        startActivity(intent);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBarPb.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Movie> doInBackground(String... strings) {


            if(strings.length == 0)
                return null;

            List<Movie> movieList;

            URL movieUrl = NetworkUtils.buildUrl(strings[0], API_KEY);
            try {
                String movieResponse = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                // Log.v(TAG, "--------------------------received movies data :" + movieResponse);

                movieList = JsonUtils.parseMovieJsonData(movieResponse);
                //Log.v(TAG, "--------------------------received movies array, length :" + movieList.size());
                return movieList;

            } catch (IOException |JSONException e) {
                e.printStackTrace();
                return null;
            }


        }

        @Override
        protected void onPostExecute(List<Movie> movies) {

            mProgressBarPb.setVisibility(View.INVISIBLE);
            if(movies!= null) {
                showMovieView();
                mMovieAdapter.setmMovieList(movies);
                //Log.v(TAG, "-----------------------" + "recyclerView visibility =" + mRecyclerView.getVisibility());
            }else{
                showErrorMessage();
            }
            super.onPostExecute(movies);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_popular:
                //Log.v(TAG,"::::::::::::::popular menu selected");
                mMovieAdapter.setmMovieList(null);
                loadMovieData(getString(R.string.search_popular));
                return true;
            case R.id.action_rated:
                //Log.v(TAG,"::::::::::::::top rated menu selected");
                mMovieAdapter.setmMovieList(null);
                loadMovieData(getString(R.string.search_top_rated));
                return true;
            default: return super.onOptionsItemSelected(item);
        }

    }


}
