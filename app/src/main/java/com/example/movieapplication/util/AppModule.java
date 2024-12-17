package com.example.movieapplication.util;

import android.content.Context;

import androidx.room.Room;

import com.example.movieapplication.data.dao.MovieDao;
import com.example.movieapplication.data.service.MovieService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public static MovieService provideMovieService() {
        return new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieService.class);
    }

    @Provides
    @Singleton
    public static AppDatabase provideDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, "movie_database")
                .build();
    }

    @Provides
    public static MovieDao provideMovieDao(AppDatabase database) {
        return database.movieDao();
    }
}
