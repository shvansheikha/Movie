package kr.zagros.shwan.moviemvvm.DataSource.PagedSources;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;
import android.util.Log;

import java.util.concurrent.Executor;

import kr.zagros.shwan.moviemvvm.DataSource.Client.ApiService;

public class MoviesDataSourceFactory extends DataSource.Factory {
    private static final String TAG = "MoviesDataSourceFactory";

    private MoviesDataSource moviesDataSource;
    private MutableLiveData<MoviesDataSource> mutableLiveData;

    private ApiService apiService;
    private Executor executor;

    public MoviesDataSourceFactory(ApiService apiService, Executor executor) {
        this.apiService = apiService;
        this.executor = executor;
        this.mutableLiveData = new MutableLiveData<>();
    }

    @Override
    public DataSource create() {
        Log.e(TAG, "create: " );
        moviesDataSource=new MoviesDataSource(executor,apiService);
        mutableLiveData.postValue(moviesDataSource);
        return moviesDataSource;
    }

    public MutableLiveData<MoviesDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
