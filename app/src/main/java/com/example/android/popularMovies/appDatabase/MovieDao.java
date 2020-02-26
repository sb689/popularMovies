package com.example.android.popularMovies.appDatabase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.android.popularMovies.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("Select * from Movie")
    LiveData <List<Movie>> loadFavoriteMovies();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMovie(Movie movie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("Select * from Movie where movieId= :id")
    LiveData<Movie> getMovieById(int id);
}
