package kr.zagros.shwan.moviemvvm.viewmodels;

import android.annotation.SuppressLint;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import android.util.Log;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import kr.zagros.shwan.moviemvvm.DataSource.Client.ApiClient;
import kr.zagros.shwan.moviemvvm.DataSource.Client.ApiService;
import kr.zagros.shwan.moviemvvm.DataSource.PagedSources.MoviesDataSource;
import kr.zagros.shwan.moviemvvm.DataSource.PagedSources.MoviesDataSourceFactory;
import kr.zagros.shwan.moviemvvm.Entities.Movie;
import kr.zagros.shwan.moviemvvm.Entities.NetworkState;
import kr.zagros.shwan.moviemvvm.utils.Config;

public class MainVM extends ViewModel {

    private LiveData<PagedList<Movie>> responseListMoves;
    private LiveData<NetworkState> networkStateLiveData;
    private Executor executor;
    private MoviesDataSourceFactory factory;
    private LiveData<MoviesDataSource> dataSource;


    @SuppressLint("CheckResult")
    public MainVM() {
        this.executor = Executors.newFixedThreadPool(5);
        ApiService apiService = ApiClient.create(Config.BASE_URL_ONE).create(ApiService.class);
        factory = new MoviesDataSourceFactory(apiService, executor);
        dataSource = factory.getMutableLiveData();

        networkStateLiveData = Transformations.switchMap(factory.getMutableLiveData(), source -> {
            Log.d("TAG", "apply: network change");
            return source.getNetworkState();
        });

        PagedList.Config pageConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        responseListMoves = (new LivePagedListBuilder<Long, Movie>(factory, pageConfig))
                .setFetchExecutor(executor)
                .build();

    }


    public LiveData<PagedList<Movie>> getResponseLiveData() {
        return this.responseListMoves;
    }

    public LiveData<NetworkState> getNetworkStateLiveData() {
        return networkStateLiveData;
    }
}
