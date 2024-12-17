package com.example.movieapplication.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapplication.data.repo.MovieRepository;
import com.example.movieapplication.domain.entity.Movie;

import java.util.List;

import javax.inject.Inject;

public class SearchMoviesViewModel extends ViewModel {

    private final MutableLiveData<List<Movie>> searchedMovies = new MutableLiveData<>();
    private final MovieRepository repository;

    @Inject
    public SearchMoviesViewModel(MovieRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Movie>> getSearchedMovies() {
        return searchedMovies;
    }

    public void fetchSearchedMovies(String searchTerm) {
        repository.getSearchMovies(searchTerm).observeForever(searchedMovies::setValue);
    }
}
