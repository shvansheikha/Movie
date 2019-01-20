package kr.zagros.shwan.moviemvvm.DataSource.PagedSources;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;
import androidx.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import kr.zagros.shwan.moviemvvm.DataSource.Client.ApiService;
import kr.zagros.shwan.moviemvvm.DataSource.Store.DataStoreImp;
import kr.zagros.shwan.moviemvvm.Entities.Movie;
import kr.zagros.shwan.moviemvvm.Entities.NetworkState;
import kr.zagros.shwan.moviemvvm.utils.Config;
import kr.zagros.shwan.moviemvvm.utils.netUtils.JSONParser;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesDataSource extends PageKeyedDataSource<Long, Movie> {
    private static final String TAG = "MoviesDataSource";
    private Executor executor;
    private ApiService apiService;
    private DataStoreImp storeImp;

    private LoadParams<Long> params;
    private LoadCallback<Long, Movie> callback;

    private MutableLiveData<NetworkState> networkState;

    MoviesDataSource(Executor executor, ApiService apiService) {
        this.executor = executor;
        this.apiService = apiService;
        this.networkState = new MutableLiveData<>();
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Movie> callback) {
        Log.e(TAG, "loadInitial: ");
        networkState.postValue(NetworkState.LOADING);
        apiService.getMovies(Config.API_DEFAULT_PAGE_KEY).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseString;
                List<Movie> movieList;
                if (response.isSuccessful() && response.code() == 200) {
                    try {
                        networkState.postValue(NetworkState.LOADED);
                        responseString = response.body().string();
                        movieList = JSONParser.getMovieList(responseString);
                        for (Movie m:movieList) {
                            Log.e(TAG, "onResponse: "+m.getId() );
                        }
                        callback.onResult(movieList, null, (long) 2);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.message());
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message(), null));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                String errorMessage = t.getMessage();
                Log.e(TAG, "onFailure: "+errorMessage );
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage, t));
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) {
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) {
        Log.e(TAG, "load page: " + params.key);
        networkState.postValue(NetworkState.LOADING);
        apiService.getMovies(params.key).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject responseJson;
                String responseString;
                List<Movie> moviesList;
                Long nextKey;
                if (response.isSuccessful() && response.code() == 200) {
                    try {
                        networkState.postValue(NetworkState.LOADED);
                        responseString = response.body().string();
                        moviesList = JSONParser.getMovieList(responseString);
                        responseJson = new JSONObject(responseString);
                        nextKey = (params.key == responseJson.getJSONObject("metadata").getInt("page_count")) ? null : params.key + 1;

                        callback.onResult(moviesList, nextKey);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onResponse error " + response.message());
                    networkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String errorMessage = t.getMessage();
                Log.e(TAG, "onFailure: "+errorMessage );
                networkState.postValue(new NetworkState(NetworkState.Status.FAILED, errorMessage, t));
            }
        });

    }
}
