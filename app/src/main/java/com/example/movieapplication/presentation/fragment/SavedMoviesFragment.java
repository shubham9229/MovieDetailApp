package com.example.movieapplication.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.movieapplication.R;
import com.example.movieapplication.databinding.SavedListingLayoutFragmentBinding;
import com.example.movieapplication.presentation.adapter.MovieAdapter;
import com.example.movieapplication.presentation.viewmodel.HomeViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SavedMoviesFragment extends Fragment implements MovieAdapter.MovieAdapterInterface{

    private SavedListingLayoutFragmentBinding binding;
    private MovieAdapter savedAdapter;

    @Inject
    HomeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SavedListingLayoutFragmentBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.savedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        savedAdapter = new MovieAdapter(this);

        binding.savedRecyclerView.setAdapter(savedAdapter);

        viewModel.getSavedMovies().observe(getViewLifecycleOwner(), movies -> {
            savedAdapter.submitList(movies);
        });

        viewModel.fetchSavedMovies();
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
