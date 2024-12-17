package com.example.movieapplication.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.movieapplication.domain.entity.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovies(List<Movie> movies);

    @Query("SELECT * FROM movies WHERE type=:type")
    List<Movie> getAllMovies(String type);

    @Query("SELECT * FROM movies WHERE isSaved=1")
    List<Movie> getSavedMovies();

    @Query("UPDATE movies SET isSaved = :saveStatus WHERE id = :movieId")
    void updateSaveStatus(int movieId, int saveStatus);

    @Delete
    void deleteMovie(Movie movie);
}
