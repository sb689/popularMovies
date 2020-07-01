package com.example.android.popularMovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.databinding.DataBindingUtil;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.popularMovies.appDatabase.AppDatabase;
import com.example.android.popularMovies.databinding.ActivityDetailBinding;
import com.example.android.popularMovies.model.Movie;
import com.example.android.popularMovies.model.Review;
import com.example.android.popularMovies.model.Trailer;
import com.example.android.popularMovies.movieDetailInfo.MovieExtraInfo;
import com.example.android.popularMovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements MovieTrailerAdapter.PlayTrailerListener {


    private static final String TAG = DetailActivity.class.getSimpleName();

    public static final String ACTION_GET_TRAILERS = "get_trailers";
    public static final String ACTION_GET_REVIEWS = "get_reviews";

    private static MovieTrailerAdapter mMovieTrailerAdapter;
    private static MovieReviewAdapter mMovieReviewAdapter;

    private static Movie mMovie;
    private static AppDatabase mDatabase;
    static ActivityDetailBinding mDataBinding;
    private Toast mToast;

    private int mFavButtonState;
    private static final int BUTTON_STATE_ADD = 0;
    private static final int BUTTON_STATE_REMOVE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar!= null){
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
        mDatabase = AppDatabase.getInstance(getApplicationContext());
        mDataBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_detail);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra(getString(R.string.intent_package_key));
        mMovie = movie;

        bindDataToView();

      //getting trailers data in background
        getShowTrailersAndReviews();
    }


    private void getShowTrailersAndReviews() {

        mMovieReviewAdapter = new MovieReviewAdapter();
        mMovieTrailerAdapter = new MovieTrailerAdapter(this);
        bindTrailers();
        bindReviews();
        new FetchTrailers(this).onStartLoading();
        new FetchReviews(this).onStartLoading();

    }

    private void bindReviews(){

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mDataBinding.rvReviews.setLayoutManager(layoutManager);
        mDataBinding.rvReviews.setAdapter(mMovieReviewAdapter);
        mDataBinding.rvReviews.setHasFixedSize(true);

        mDataBinding.tvErrorMessageTrailer.setVisibility(View.INVISIBLE);
        mDataBinding.rvReviews.setVisibility(View.VISIBLE);

    }

    private void bindTrailers(){



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mDataBinding.rvTrailers.setLayoutManager(layoutManager);
        mDataBinding.rvTrailers.setAdapter(mMovieTrailerAdapter);
        mDataBinding.rvTrailers.setHasFixedSize(true);

        mDataBinding.tvErrorMessageTrailer.setVisibility(View.INVISIBLE);
        mDataBinding.rvTrailers.setVisibility(View.VISIBLE);

    }

    private void bindDataToView(){



//        int deviceOrientation = getResources().getConfiguration().orientation;
//        int imageRatio = IMAGE_HEIGHT_RATIO;
//        int titleRatio = TITLE_HEIGHT_RATIO;
//
//        if(deviceOrientation == Configuration.ORIENTATION_LANDSCAPE){
//            imageRatio = IMAGE_HEIGHT_RATION_LAND;
//            mDataBinding.tvDetailMovieTitle.setTextSize(16);
//            titleRatio = TITLE_HEIGHT_RATION_LAND;
//        }

        //int titleHeight = (int) MainActivity.displayScreenHeight/ titleRatio;
        mDataBinding.tvDetailMovieTitle.setText(mMovie.getOriginalTitle());
        //mDataBinding.tvDetailMovieTitle.getLayoutParams().height = titleHeight;

        String releaseDateStr = mMovie.getReleaseDate();
        if(releaseDateStr != null || !releaseDateStr.isEmpty()) {
            String[] dates = releaseDateStr.split("-");
            mDataBinding.tvDetailReleaseYear.setText(dates[0]);
        }

        String rating =  mMovie.getVoteAverage() + "/10";
        mDataBinding.tvDetailRating.setText(rating);

        mDataBinding.tvDetailOverview.setText(mMovie.getOverview());

        //setting poster

       // mDataBinding.ivDetailPoster.getLayoutParams().height = (int) MainActivity.displayScreenHeight/imageRatio;
        String posterPath = mMovie.getPosterPath();
        Uri posterUri = NetworkUtils.buildMoviePosterUrl(posterPath);
        //Log.v(TAG, "::::::::::::::::::posterPath uri is:" + posterUri.toString());

        if (posterUri != null) {
            Picasso.get().load(posterUri).into(mDataBinding.ivDetailPoster);
        }

        //set favorite button text

        if(MainActivity.mFavoriteMovieIds.contains(mMovie.getMovieId())) {
            mFavButtonState = BUTTON_STATE_REMOVE;

        }else{
            mFavButtonState = BUTTON_STATE_ADD;

        }

    }


    private static class FetchTrailers extends AsyncTaskLoader<List<Trailer>>{


        public FetchTrailers(@NonNull Context context) {
            super(context);

        }

        @Override
        protected void onStartLoading() {
            mDataBinding.pbLoadingTrailer.setVisibility(View.VISIBLE);
            forceLoad();
        }

        @Override
        public List<Trailer> loadInBackground() {
            List<Trailer> trailers = null;
            try {
                trailers = MovieExtraInfo.fetchMovieTrailers(mMovie.getMovieId(), getContext());
            }
            catch (IOException |JSONException e) {
                e.printStackTrace();
                return null;
            }
            //Log.v(TAG, ":::::::::::::::fetching TRAILERS from network");
            return trailers;
        }

        @Override
        public void deliverResult(@Nullable List<Trailer> data) {

            mDataBinding.pbLoadingTrailer.setVisibility(View.INVISIBLE);
            if(data == null){
                showErrorMessage();
            }
            else {
                mMovieTrailerAdapter.setmTrailerList(data);

            }
            super.deliverResult(data);
        }
    }


    private static class FetchReviews extends AsyncTaskLoader<List<Review>>{


        public FetchReviews(@NonNull Context context) {
            super(context);

        }

        @Override
        protected void onStartLoading() {

                mDataBinding.pbLoadingTrailer.setVisibility(View.VISIBLE);
                forceLoad();

        }

        @Override
        public List<Review> loadInBackground() {
            List<Review> reviews = null;
            try {
                reviews = MovieExtraInfo.fetchMovieReviews(mMovie.getMovieId(), getContext());

            }  catch (IOException |JSONException e) {
                e.printStackTrace();
                return null;
            }
            //Log.v(TAG, ":::::::::::::::fetching Review from Network");
            return reviews;
        }


        @Override
        public void deliverResult(@Nullable List<Review> data) {

            mDataBinding.pbLoadingTrailer.setVisibility(View.INVISIBLE);
            if(data == null){
                showErrorMessage();
            }
            else {

                mMovieReviewAdapter.setmReviewList(data);
            }
            super.deliverResult(data);
        }
    }



    private static void showErrorMessage(){

        mDataBinding.rvReviews.setVisibility(View.INVISIBLE);
        mDataBinding.rvTrailers.setVisibility(View.INVISIBLE);
        mDataBinding.tvErrorMessageTrailer.setVisibility(View.VISIBLE);
    }



    public void addFavorite(final View view){

        switch (mFavButtonState){
            case BUTTON_STATE_ADD:
                new addMovieToFav().execute(mMovie);
                mFavButtonState = BUTTON_STATE_REMOVE;
                showToast(getString(R.string.added_to_favorite));
                break;

            case BUTTON_STATE_REMOVE:
                new removeMovie().execute(mMovie);
                mFavButtonState = BUTTON_STATE_ADD;
                showToast(getString(R.string.removed_from_favorite));
                break;
        }

    }

    private void showToast(String msg){
        mToast = null;
        mToast = Toast.makeText(this,msg,Toast.LENGTH_SHORT );
        mToast.show();
    }

    @Override
    public void playTapped(int position) {

        Trailer trailer = mMovieTrailerAdapter.getmTrailerList().get(position);
        Uri youtubeUri = NetworkUtils.getYoutubeUri(trailer.getKey());
       // Log.v(TAG, ":::::::::::: video uri is " + youtubeUri.toString() );

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(youtubeUri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void movieTrailerAdapterItemClicked(int position) {

    }


    private static class addMovieToFav extends AsyncTask<Movie,Void,Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            mDatabase.movieDao().insertMovie(mMovie);
            return null;
        }


    }

    private static class removeMovie extends AsyncTask<Movie,Void,Void>{

        @Override
        protected Void doInBackground(Movie... movies) {
            mDatabase.movieDao().deleteMovie(mMovie);
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.detail_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_share_trailer:
                shareFirstTrailer();
                return true;

            default: return super.onOptionsItemSelected(item);
        }
    }


    private void shareFirstTrailer(){

            if(mMovieTrailerAdapter.getmTrailerList() == null){
                String msg = getString(R.string.share_trailer_error_msg);
                showToast(msg);
                return;
            }
            String mimeType = "text/plain";
            String title = getString(R.string.share_trailer_title);
            String key = mMovieTrailerAdapter.getmTrailerList().get(0).getKey();
            Uri uri = NetworkUtils.getYoutubeUri(key);


            ShareCompat.IntentBuilder.from(this)
                    .setChooserTitle(title)
                    .setType(mimeType)
                    .setText(uri.toString())
                    .startChooser();

    }
}
