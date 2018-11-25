package kr.zagros.shwan.moviemvvm.viewmodels;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import kr.zagros.shwan.moviemvvm.DataSource.client.ApiClient;
import kr.zagros.shwan.moviemvvm.DataSource.client.ApiService;
import kr.zagros.shwan.moviemvvm.DataSource.store.DataStoreImp;
import kr.zagros.shwan.moviemvvm.Entities.MovieResponse;
import kr.zagros.shwan.moviemvvm.utils.Config;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainVM extends ViewModel {

    private MutableLiveData<MovieResponse> responseLiveData;
    private DataStoreImp storeImp;


    @SuppressLint("CheckResult")
    public MainVM() {
        this.storeImp = new DataStoreImp();
        this.responseLiveData = new MutableLiveData<>();

        storeImp.getMovies(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<MovieResponse>() {
                    @Override
                    public void onSuccess(MovieResponse movieResponse) {
                        responseLiveData.postValue(movieResponse);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e("shvansheikha", "onFailure: "+e.getMessage() );
                    }
                });
    }


    public MutableLiveData<MovieResponse> getResponseLiveData() {
        return this.responseLiveData;
    }

}
