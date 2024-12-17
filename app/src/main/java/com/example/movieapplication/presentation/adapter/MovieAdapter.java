package com.example.movieapplication.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapplication.R;
import com.example.movieapplication.domain.entity.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList = new ArrayList<>();
    private MovieAdapterInterface listener;

    public MovieAdapter(MovieAdapterInterface listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void submitList(List<Movie> movies) {
        if (movies != null) {
            movieList = movies;
            notifyDataSetChanged();
        }
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView;
        private final ImageView posterImageView;

        private final View item;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movie_title);
            posterImageView = itemView.findViewById(R.id.movie_poster);
            item = itemView;
        }

        public void bind(Movie movie) {
            String url = "https://image.tmdb.org/t/p/w500" + movie.getPoster_path();
            titleTextView.setText(movie.getTitle());
            Glide.with(posterImageView.getContext())
                    .load(url)
                    .into(posterImageView);
            item.setOnClickListener(v -> listener.onClickOfMovie(movie.getTitle(), movie.getOverview(), url, movie.getId()));
        }
    }

    public interface MovieAdapterInterface {
        void onClickOfMovie(String title, String overview, String url, int id);
    }
}
