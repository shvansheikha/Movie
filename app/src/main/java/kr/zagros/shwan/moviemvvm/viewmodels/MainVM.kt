package kr.zagros.shwan.moviemvvm.viewmodels


import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import android.util.Log

import java.util.concurrent.Executor
import java.util.concurrent.Executors

import kr.zagros.shwan.moviemvvm.DataSource.Client.ApiClient
import kr.zagros.shwan.moviemvvm.DataSource.Client.ApiService
import kr.zagros.shwan.moviemvvm.DataSource.PagedSources.MoviesDataSource
import kr.zagros.shwan.moviemvvm.DataSource.PagedSources.MoviesDataSourceFactory
import kr.zagros.shwan.moviemvvm.Entities.Movie
import kr.zagros.shwan.moviemvvm.Entities.NetworkState
import kr.zagros.shwan.moviemvvm.utils.Config

class MainVM : ViewModel() {

    val responseLiveData: LiveData<PagedList<Movie>>
    val networkStateLiveData: LiveData<NetworkState>
    private val executor: Executor
    private val factory: MoviesDataSourceFactory
    private val dataSource: LiveData<MoviesDataSource>


    init {
        this.executor = Executors.newFixedThreadPool(5)
        val apiService = ApiClient.create(Config.BASE_URL_ONE).create(ApiService::class.java)
        factory = MoviesDataSourceFactory(apiService, executor)
        dataSource = factory.mutableLiveData

        networkStateLiveData = Transformations.switchMap(factory.mutableLiveData) { source ->
            Log.d("TAG", "apply: network change")
            source.networkState
        }

        val pageConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build()

        responseLiveData = LivePagedListBuilder<Long, Movie>(factory, pageConfig)
                .setFetchExecutor(executor)
                .build()

    }
}
