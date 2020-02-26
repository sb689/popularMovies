package com.example.android.popularMovies.appDatabase;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.android.popularMovies.model.Movie;

@Database(entities = {Movie.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "popularmovies";
    private static AppDatabase sInstance;

//
//    static final Migration MIGRATION_1_3 = new Migration(1, 3) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE Movie DROP COLUMN inFavoriteList");
//        }
//    };



    public static AppDatabase getInstance(Context context)
    {
        if(sInstance == null)
        {
            synchronized (LOCK)
            {
                sInstance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                       .build();
            }
        }
        return sInstance;

    }

    public abstract MovieDao movieDao();

}
