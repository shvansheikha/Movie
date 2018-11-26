package kr.zagros.shwan.moviemvvm.viewmodels;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import kr.zagros.shwan.moviemvvm.DataSource.Client.ApiClient;
import kr.zagros.shwan.moviemvvm.DataSource.Client.ApiService;
import kr.zagros.shwan.moviemvvm.DataSource.PagedSources.MoviesDataSource;
import kr.zagros.shwan.moviemvvm.DataSource.PagedSources.MoviesDataSourceFactory;
import kr.zagros.shwan.moviemvvm.Entities.Movie;
import kr.zagros.shwan.moviemvvm.utils.Config;

public class MainVM extends ViewModel {

    private LiveData<PagedList<Movie>> responselistMoves;
    private Executor executor;
    private MoviesDataSourceFactory factory;
    private LiveData<MoviesDataSource> dataSource;



    @SuppressLint("CheckResult")
    public MainVM() {
        this.executor= Executors.newFixedThreadPool(5);
        ApiService apiService=ApiClient.create(Config.BASE_URL_ONE).create(ApiService.class);
        factory=new MoviesDataSourceFactory(apiService,executor);
        dataSource=factory.getMutableLiveData();

        PagedList.Config pageConfig=(new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(10)
                .build();

        responselistMoves=(new LivePagedListBuilder<Long,Movie>(factory,pageConfig))
                .setFetchExecutor(executor)
                .build();

    }


    public LiveData<PagedList<Movie>> getResponseLiveData() {
        return this.responselistMoves;
    }

}
