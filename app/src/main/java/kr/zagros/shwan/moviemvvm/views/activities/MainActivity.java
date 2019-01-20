package kr.zagros.shwan.moviemvvm.views.activities;


import androidx.lifecycle.ViewModelProviders;
import androidx.databinding.DataBindingUtil;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Objects;

import androidx.recyclerview.widget.RecyclerView;
import kr.zagros.shwan.moviemvvm.R;
import kr.zagros.shwan.moviemvvm.databinding.ActivityMain2Binding;

import kr.zagros.shwan.moviemvvm.viewmodels.MainVM;
import kr.zagros.shwan.moviemvvm.views.adapters.MoviesAdapter;

public class MainActivity extends AppCompatActivity implements RetryCallback {

    private MainVM mainVM;
    private ActivityMain2Binding binding;

    private MoviesAdapter moviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main2);
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
        binding.content.list.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.content.list.setAdapter(moviesAdapter);
    }

    @Override
    public void retry() {
        Toast.makeText(this, "retry", Toast.LENGTH_SHORT).show();
    }
}
