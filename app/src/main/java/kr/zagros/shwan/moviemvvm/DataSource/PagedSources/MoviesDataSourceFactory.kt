package kr.zagros.shwan.moviemvvm.DataSource.PagedSources

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import android.util.Log

import java.util.concurrent.Executor

import kr.zagros.shwan.moviemvvm.DataSource.Client.ApiService
import kr.zagros.shwan.moviemvvm.Entities.Movie

class MoviesDataSourceFactory(private val apiService: ApiService, private val executor: Executor) : DataSource.Factory<Long, Movie>() {

    private var moviesDataSource: MoviesDataSource? = null
    val mutableLiveData: MutableLiveData<MoviesDataSource> = MutableLiveData()

    override fun create(): DataSource<Long, Movie> {
        Log.e(TAG, "create: ")
        moviesDataSource = MoviesDataSource(executor, apiService)
        mutableLiveData.postValue(moviesDataSource)
        return moviesDataSource as MoviesDataSource
    }

    companion object {
        private const val TAG = "MoviesDataSourceFactory"
    }
}
