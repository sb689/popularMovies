package com.example.android.popularMovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.android.popularMovies.appDatabase.FavoriteMoviesViewModel;
import com.example.android.popularMovies.databinding.ActivityMainBinding;
import com.example.android.popularMovies.model.Movie;
import com.example.android.popularMovies.utilities.JsonUtils;
import com.example.android.popularMovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements
        MovieAdapter.MovieAdapterClickListener, LoaderManager.LoaderCallbacks<List<Movie>> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GRID_SPAN_COUNT = 2;
    private static final int GRID_IMAGE_RATIO = 2;
    private static final int GRID_SPAN_COUNT_LAND = 3;
    private static final int GRID_IMAGE_RATIO_LAND = 1;

    public static int displayScreenHeight = 0;
    private static final String LOADER_SEARCH_CRITERIA_EXTRA = "searchCriteria";
    private static final int MOVIE_LOADER_ID = 7;
    private static final String LOADER_QUERY_EXTRA = "extra_data";


    public static List<Integer> mFavoriteMovieIds = new ArrayList<>();
    private static List<Movie> mFavoriteMovies = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private String mSearchCriteria;
    ActivityMainBinding mDataBinding;
    private MovieAdapter mMovieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        int imageRatio = GRID_IMAGE_RATIO;
        int spanCount = GRID_SPAN_COUNT;
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            spanCount = GRID_SPAN_COUNT_LAND;
            imageRatio = GRID_IMAGE_RATIO_LAND;
        }
        int viewHolderHeight = getCalculatedHeight(imageRatio);
        mMovieAdapter = new MovieAdapter(this,viewHolderHeight);

        //int posterWidth = 230; // size in pixels (just a random size). You may use other values.

        //GridLayoutManager layoutManager =new GridLayoutManager(this, calculateBestSpanCount(posterWidth));

        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        layoutManager.setReverseLayout(false);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);

        mRecyclerView = mDataBinding.rvRecyclerView;
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.setHasFixedSize(true);

        mSearchCriteria = getString(R.string.search_popular);
        getFavourites();
        loadMovieData(mSearchCriteria,false );


    }



    //result of using this method was bad. count on pixel to calculate image size is a bad idea.
    private int calculateBestSpanCount(int posterWidth) {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float screenWidth = outMetrics.widthPixels;
        return Math.round(screenWidth / posterWidth);
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


    private void loadMovieData(String searchCriteria, boolean restartLoader)
    {
        showMovieView();
        String searchQuery = NetworkUtils.buildUrl(searchCriteria, MainActivity.this);
        //Log.v(TAG, ":::::::::::::::::::searchQuery in loadMovieData:" + searchQuery);
        Bundle bundle = new Bundle();
        bundle.putString(LOADER_QUERY_EXTRA, searchQuery);
        bundle.putString(LOADER_SEARCH_CRITERIA_EXTRA, searchCriteria);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<List<Movie>> loader = loaderManager.getLoader(MOVIE_LOADER_ID);
        if(restartLoader)
        {
            loaderManager.restartLoader(MOVIE_LOADER_ID, bundle, this);
        }
        else{
            loaderManager.initLoader(MOVIE_LOADER_ID, bundle, this);
        }

    }



    private void showErrorMessage()
    {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mDataBinding.tvErrorMessage.setVisibility(View.VISIBLE);
    }

    private void showMovieView()
    {
        mDataBinding.tvErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void movieAdapterOnClick(int position) {

        Movie movie = mMovieAdapter.getmMovieList().get(position);
        Class destination = DetailActivity.class;
        Intent intent = new Intent(this,destination);
        intent.putExtra(getString(R.string.intent_package_key), movie );
        intent.putExtra(getString(R.string.intent_package_search_by_key),mSearchCriteria);
        startActivity(intent);
    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, @Nullable final Bundle args) {

        return new AsyncTaskLoader<List<Movie>>(this) {

            List<Movie> mCachedMovieList = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }

                String searchBy = args.getString(LOADER_SEARCH_CRITERIA_EXTRA);
                if(searchBy.equals(getString(R.string.search_favorite)))
                {
                    mCachedMovieList = mFavoriteMovies;
                }
               if (mCachedMovieList != null) {
                    deliverResult(mCachedMovieList);

                } else {
                    mDataBinding.pbLoading.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(@Nullable List<Movie> data) {

                mCachedMovieList = data;
                super.deliverResult(data);
            }

            @Nullable
            @Override
            public List<Movie> loadInBackground() {

                List<Movie> movieList;

                String searchQueryStr = (String) args.get(LOADER_QUERY_EXTRA);
                URL movieUrl = null;

                try {
                    movieUrl = new URL(searchQueryStr);

                    String movieResponse = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                   Log.v(TAG, "--------------------------movie url :" + movieResponse);

                    movieList = JsonUtils.parseMovieJsonData(movieResponse);
                    //Log.v(TAG, "--------------------------received movies array, length :" + movieList.size());
                    return movieList;

                } catch (IOException |JSONException e) {
                    e.printStackTrace();
                    return null;
                }

            }
        };

    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> data) {

        mDataBinding.pbLoading.setVisibility(View.INVISIBLE);
        if(data!= null) {
            showMovieView();
            mMovieAdapter.setmMovieList(data);

        }else{
            showErrorMessage();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {

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
               // Log.v(TAG,"::::::::::::::popular menu selected");
                mMovieAdapter.setmMovieList(null);
                mSearchCriteria = getString(R.string.search_popular);
                loadMovieData(mSearchCriteria, true);
                return true;

                case R.id.action_rated:
               // Log.v(TAG,"::::::::::::::top rated menu selected");
                mMovieAdapter.setmMovieList(null);
                mSearchCriteria = getString(R.string.search_top_rated);
                loadMovieData(mSearchCriteria, true);
                return true;

            case R.id.action_favorite:
                mMovieAdapter.setmMovieList(null);
                mSearchCriteria = getString(R.string.search_favorite);
                //getFavourites();
                loadMovieData(mSearchCriteria, true);
                return true;

                default: return super.onOptionsItemSelected(item);
        }

    }

    private void getFavourites(){

      ViewModelProvider.Factory factory = ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication());
        ViewModelProvider provider = new ViewModelProvider(this, factory);
        final FavoriteMoviesViewModel viewModel = provider.get(FavoriteMoviesViewModel.class);
        viewModel.getFavMovieList().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                mFavoriteMovies = movies;
                //Log.v(TAG, ":::::::::::::::inside LiveData observer");

                createFavMovieList(movies);

                String ids = "";
                for(int a: mFavoriteMovieIds)
                    ids += a + ",";
                //Log.v(TAG, "fav movie ids inside observer: " + ids );
                if(mSearchCriteria.equals(getString(R.string.search_favorite))) {
                    loadMovieData(mSearchCriteria, true);
                }
            }
        });
    }


    private void createFavMovieList(List<Movie> movies)
    {
        mFavoriteMovieIds.clear();
        for(Movie m: movies){
            mFavoriteMovieIds.add(m.getMovieId());
        }
    }


}
