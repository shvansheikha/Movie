package kr.zagros.shwan.moviemvvm.views.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Objects;

import kr.zagros.shwan.moviemvvm.Entities.MovieResponse;
import kr.zagros.shwan.moviemvvm.R;
import kr.zagros.shwan.moviemvvm.databinding.ActivityMainBinding;
import kr.zagros.shwan.moviemvvm.databinding.ContentMainBinding;
import kr.zagros.shwan.moviemvvm.viewmodels.MainVM;
import kr.zagros.shwan.moviemvvm.views.adapters.MoviesAdapter;

public class MainActivity extends AppCompatActivity implements RetryCallback {

    private MainVM mainVM;
    private ActivityMainBinding binding;
    private ContentMainBinding contentMainBinding;

    private MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        contentMainBinding = ContentMainBinding.inflate(getLayoutInflater());
        mainVM = ViewModelProviders.of(this).get(MainVM.class);

        initNavigationDrawer();
        setupRecyclerView();

        setupObserversDataFromViewModel();
    }

    private void setupObserversDataFromViewModel() {
        mainVM.getResponseLiveData().observe(this, Movie -> moviesAdapter.submitList(Movie));
        mainVM.getNetworkStateLiveData().observe(this, networkState -> moviesAdapter.setMyNetworkState(networkState));
    }

    private void initNavigationDrawer() {
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(binding.toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.Open, R.string.Close);
        toggle.syncState();
        binding.drawerLayout.addDrawerListener(toggle);
    }

    private void setupRecyclerView() {
        moviesAdapter = new MoviesAdapter(this, this);
        binding.content.list.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        binding.content.list.setAdapter(moviesAdapter);
    }

    private void getMovies(MovieResponse response) {
        for (int i = 0; i < response.getData().size(); i++) {
            Log.e("shvansheikha", "getMovies: " + response.getData().get(i).getTitle());
        }
    }


    @Override
    public void retry() {
        Toast.makeText(this, "retry", Toast.LENGTH_SHORT).show();
    }
}
