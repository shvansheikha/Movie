package kr.zagros.shwan.moviemvvm.DataSource.PagedSources;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import kr.zagros.shwan.moviemvvm.DataSource.Client.ApiService;
import kr.zagros.shwan.moviemvvm.DataSource.Store.DataStoreImp;
import kr.zagros.shwan.moviemvvm.Entities.Movie;
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

    MoviesDataSource(Executor executor, ApiService apiService) {
        this.executor = executor;
        this.apiService = apiService;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull LoadInitialCallback<Long, Movie> callback) {
        apiService.getMovies(Config.API_DEFAULT_PAGE_KEY).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String responseString;
                List<Movie> movieList;
                if (response.isSuccessful() && response.code() == 200) {
                    try {
                        responseString = response.body().string();
                        movieList = JSONParser.getMovieList(responseString);
                        callback.onResult(movieList, null, (long) 2);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e(TAG, "onResponse: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) { }

    @Override
    public void loadAfter(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, Movie> callback) { }
}
