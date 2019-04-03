package kr.zagros.shwan.moviemvvm.views.activities


import androidx.lifecycle.ViewModelProviders
import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager

import android.widget.Toast
import androidx.lifecycle.Observer

import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.content_main.*
import kr.zagros.shwan.moviemvvm.Entities.NetworkState
import kr.zagros.shwan.moviemvvm.R
import kr.zagros.shwan.moviemvvm.databinding.ActivityMain2Binding

import kr.zagros.shwan.moviemvvm.viewmodels.MainVM
import kr.zagros.shwan.moviemvvm.views.adapters.MoviesAdapter

class MainActivity : AppCompatActivity(), RetryCallback {

    private var mainVM: MainVM? = null
    private var binding: ActivityMain2Binding? = null

    private var moviesAdapter: MoviesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainVM = ViewModelProviders.of(this).get(MainVM::class.java)

        initNavigationDrawer()
        initRecyclerView()

        initObserversDataFromViewModel()

    }

    private fun initObserversDataFromViewModel() {
        mainVM!!.responseLiveData.observe(this, Observer { movie -> moviesAdapter!!.submitList(movie) })
        mainVM!!.networkStateLiveData.observe(this, Observer { networkState -> moviesAdapter!!.setMyNetworkState(networkState) })
        mainVM!!.firstDataState.observe(this, Observer { networkState ->
            run {
                if (networkState == NetworkState.LOADED)
                    main_progress.visibility = View.GONE
            }
        })
    }

    private fun initNavigationDrawer() {
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        setSupportActionBar(binding!!.toolbar)
        val toggle = ActionBarDrawerToggle(this, binding!!.drawerLayout, binding!!.toolbar, R.string.Open, R.string.Close)
        toggle.syncState()
        binding!!.drawerLayout.addDrawerListener(toggle)
    }

    private fun initRecyclerView() {
        moviesAdapter = MoviesAdapter(this, this)
        binding!!.content.list.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding!!.content.list.adapter = moviesAdapter
    }

    override fun retryGetData() {
        Toast.makeText(this, "retry", Toast.LENGTH_SHORT).show()
    }
}
