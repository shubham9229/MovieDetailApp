package com.example.movieapplication.presentation.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.movieapplication.R;
import com.example.movieapplication.presentation.viewmodel.SearchMoviesViewModel;
import com.example.movieapplication.databinding.SearchMoviesFragmentLayoutBinding;
import com.example.movieapplication.presentation.adapter.MovieAdapter;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchMoviesFragment extends Fragment implements MovieAdapter.MovieAdapterInterface{
    private SearchMoviesFragmentLayoutBinding binding;
    private MovieAdapter savedMoviesAdapter;
    @Inject
    SearchMoviesViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SearchMoviesFragmentLayoutBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        savedMoviesAdapter = new MovieAdapter(this);

        binding.recyclerView.setAdapter(savedMoviesAdapter);

        binding.goTextBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable s = binding.searchBoxEt.getText();
                if (s!= null) {
                    String enteredText = s.toString();
                    viewModel.fetchSearchedMovies(enteredText);
                    binding.progressBar.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.getSearchedMovies().observe(getViewLifecycleOwner(), movies -> {
            binding.progressBar.setVisibility(View.GONE);
            savedMoviesAdapter.submitList(movies);
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClickOfMovie(String title, String overview, String url, int id) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("overview", overview);
        bundle.putString("url", url);
        NavHostFragment.findNavController(this).navigate(R.id.movieDetailFragment, bundle);
    }
}
