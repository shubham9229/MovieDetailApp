package com.example.movieapplication.presentation.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.movieapplication.presentation.viewmodel.HomeViewModel;
import com.example.movieapplication.presentation.adapter.MovieAdapter;
import com.example.movieapplication.R;
import com.example.movieapplication.databinding.HomeFragmentLayoutBinding;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment implements MovieAdapter.MovieAdapterInterface {

    private HomeFragmentLayoutBinding binding;
    private MovieAdapter trendingAdapter;
    private MovieAdapter nowPlayingAdapter;
    @Inject
    HomeViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeFragmentLayoutBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        // Setup RecyclerViews

        binding.trendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.nowPlayingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        trendingAdapter = new MovieAdapter(this);
        nowPlayingAdapter = new MovieAdapter(this);

        binding.trendingRecyclerView.setAdapter(trendingAdapter);
        binding.nowPlayingRecyclerView.setAdapter(nowPlayingAdapter);


        viewModel.getTrendingMovies().observe(getViewLifecycleOwner(), movies -> trendingAdapter.submitList(movies));

        viewModel.getNowPlayingMovies().observe(getViewLifecycleOwner(), movies -> nowPlayingAdapter.submitList(movies));

        viewModel.fetchTrendingMovies();
        viewModel.fetchNowPlayingMovies();

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
        bundle.putInt("id",id);
        NavHostFragment.findNavController(this).navigate(R.id.movieDetailFragment, bundle);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            NavHostFragment.findNavController(this).navigate(R.id.searchMoviesFragment);
            return true;
        } else if (item.getItemId() == R.id.action_save) {
            NavHostFragment.findNavController(this).navigate(R.id.savedMoviesFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
