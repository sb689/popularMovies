package com.example.android.popularMovies.appDatabase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.popularMovies.model.Movie;

import java.util.List;

public class FavoriteMoviesViewModel extends AndroidViewModel {

    private LiveData<List<Movie>>  mFavMovieList;

    public FavoriteMoviesViewModel(@NonNull Application application) {
        super(application);
        mFavMovieList = AppDatabase.getInstance(
                this.getApplication()).movieDao().loadFavoriteMovies();
    }

    public LiveData<List<Movie>> getFavMovieList() {
        return mFavMovieList;
    }


}
