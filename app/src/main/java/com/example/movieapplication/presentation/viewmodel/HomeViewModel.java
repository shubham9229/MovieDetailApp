package com.example.movieapplication.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapplication.domain.entity.Movie;
import com.example.movieapplication.data.repo.MovieRepository;

import java.util.List;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<Movie>> trendingMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> nowPlayingMovies = new MutableLiveData<>();
    private final MutableLiveData<List<Movie>> savedMovies = new MutableLiveData<>();
    private final MovieRepository repository;

    @Inject
    public HomeViewModel(MovieRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Movie>> getTrendingMovies() {
        return trendingMovies;
    }

    public LiveData<List<Movie>> getNowPlayingMovies() {
        return nowPlayingMovies;
    }

    public LiveData<List<Movie>> getSavedMovies() {
        return savedMovies;
    }

    public void fetchTrendingMovies() {
        repository.getTrendingMovies().observeForever(trendingMovies::setValue);
    }

    public void fetchNowPlayingMovies() {
        repository.getNowPlayingMovies().observeForever(nowPlayingMovies::setValue);
    }

    public void fetchSavedMovies() {
        repository.getSavedMovies().observeForever(savedMovies::setValue);
    }

    public void saveMovieInDb(int id, int saveStatus) {
        repository.saveMovieInDb(id, saveStatus);
    }

}
