package com.example.movieapplication.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.movieapplication.databinding.MovieDetailFragmentLayoutBinding;
import com.example.movieapplication.presentation.viewmodel.HomeViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MovieDetailsFragment extends Fragment {

    private MovieDetailFragmentLayoutBinding binding;
    private Boolean isSaved = false;

    @Inject
    HomeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MovieDetailFragmentLayoutBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            String title = getArguments().getString("title");
            String overview = getArguments().getString("overview");
            String url = getArguments().getString("url");
            int id = getArguments().getInt("id");

            binding.title.setText(title);
            binding.plot.setText(overview);
            Glide.with(getContext()).load(url).into(binding.poster);
            binding.btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isSaved) {
                        binding.btnSave.setText("Unsave");
                        isSaved = true;
                        viewModel.saveMovieInDb(id, 1);
                    } else  {
                        binding.btnSave.setText("Save");
                        isSaved = false;
                        viewModel.saveMovieInDb(id, 0);
                    }
                }
            });
        }
    }
}
