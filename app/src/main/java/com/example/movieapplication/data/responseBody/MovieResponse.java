package com.example.movieapplication.data.responseBody;

import com.example.movieapplication.domain.entity.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {

    @SerializedName("results")
    private List<Movie> results;

    // Getter for the list of movies
    public List<Movie> getResults() {
        return results;
    }

    // Setter for the list of movies
    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
