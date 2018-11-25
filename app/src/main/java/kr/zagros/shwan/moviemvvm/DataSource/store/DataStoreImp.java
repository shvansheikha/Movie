package kr.zagros.shwan.moviemvvm.DataSource.store;

import java.io.IOException;

import io.reactivex.Single;
import kr.zagros.shwan.moviemvvm.DataSource.client.ApiClient;
import kr.zagros.shwan.moviemvvm.DataSource.client.ApiService;
import kr.zagros.shwan.moviemvvm.Entities.MovieResponse;
import kr.zagros.shwan.moviemvvm.utils.Config;

public class DataStoreImp implements DataStore {

    private ApiService apiService;

    public DataStoreImp() {
        this.apiService = ApiClient.create(Config.BASE_URL_ONE).create(ApiService.class);
    }

    @Override
    public Single<MovieResponse> getMovies(int page) {
        return Single.create(emitter -> {
            MovieResponse main = CreateHome(page);
            if (!emitter.isDisposed() && main != null) {
                emitter.onSuccess(main);
            } else {
                emitter.onError(new Throwable());
            }

        });
    }

    private MovieResponse CreateHome(int page) {
        try {
            return apiService.getMovies(page).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
