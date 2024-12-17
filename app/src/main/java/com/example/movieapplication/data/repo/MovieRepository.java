package com.example.movieapplication.data.repo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapplication.data.responseBody.MovieResponse;
import com.example.movieapplication.data.service.MovieService;
import com.example.movieapplication.data.dao.MovieDao;
import com.example.movieapplication.domain.entity.Movie;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private final MovieService apiService;
    private final MovieDao movieDao;
    private final ExecutorService executorService;

    public final static String TRENDING_MOVIES = "trending";
    public final static String PLAYING_MOVIES = "playing";

    public final static String API_KEY = "0534d1e2f5ca24797b4dc760728527b9";

    @Inject
    public MovieRepository(MovieService apiService, MovieDao movieDao) {
        this.apiService = apiService;
        this.movieDao = movieDao;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Movie>> getTrendingMovies() {
        MutableLiveData<List<Movie>> trendingMovies = new MutableLiveData<>();

        apiService.getTrendingMovies(API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();


                    executorService.execute(() -> {
                        try {
                            for (Movie movie: movies) {
                                movie.setType(TRENDING_MOVIES);
                            }
                            movieDao.insertMovies(movies);
                            trendingMovies.postValue(movieDao.getAllMovies(TRENDING_MOVIES));
                        } catch (Exception e) {
                            e.printStackTrace();
                            trendingMovies.postValue(null);
                        }
                    });
                } else {
                    trendingMovies.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                t.printStackTrace();
                executorService.execute(() -> {
                    try {
                        trendingMovies.postValue(movieDao.getAllMovies(TRENDING_MOVIES));
                    } catch (Exception e) {
                        e.printStackTrace();
                        trendingMovies.postValue(null);
                    }
                });
            }
        });

        return trendingMovies;
    }

    public LiveData<List<Movie>> getNowPlayingMovies() {
        MutableLiveData<List<Movie>> playingMovies = new MutableLiveData<>();

        apiService.getNowPlayingMovies(API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Movie> movies = response.body().getResults();


                    executorService.execute(() -> {
                        try {
                            for (Movie movie: movies) {
                                movie.setType(PLAYING_MOVIES);
                            }
                            movieDao.insertMovies(movies);
                            List<Movie> movieList = movieDao.getAllMovies(PLAYING_MOVIES);
                            Collections.reverse(movieList);
                            playingMovies.postValue(movieList);
                        } catch (Exception e) {
                            playingMovies.postValue(null);
                        }
                    });
                } else {
                    playingMovies.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                t.printStackTrace();
                executorService.execute(() -> {
                    try {
                        playingMovies.postValue(movieDao.getAllMovies(PLAYING_MOVIES));
                    } catch (Exception e) {
                        playingMovies.postValue(null);
                    }
                });
            }
        });

        return playingMovies;
    }

    public LiveData<List<Movie>> getSearchMovies(String searchTerm) {
        MutableLiveData<List<Movie>> searchedMovies = new MutableLiveData<>();
        apiService.searchMovies(API_KEY, searchTerm).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    searchedMovies.postValue(response.body().getResults());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                    searchedMovies.postValue(null);
            }
        });
        return searchedMovies;
    }

    public LiveData<List<Movie>> getSavedMovies() {
        MutableLiveData<List<Movie>> savedMovies = new MutableLiveData<>();
        executorService.execute(() -> {
            try {
                List<Movie> movieList = movieDao.getAllMovies(TRENDING_MOVIES).subList(0,5);
                Collections.reverse(movieList);
                savedMovies.postValue(movieList);
            } catch (Exception e) {
                e.printStackTrace();
                savedMovies.postValue(null);
            }
        });
        return savedMovies;
    }

    public void saveMovieInDb(int id, int saveStatus) {
        executorService.execute(() -> {
            try {
                movieDao.updateSaveStatus(id, saveStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}