package kr.zagros.shwan.moviemvvm.DataSource.Store

import java.io.IOException

import io.reactivex.Single
import kr.zagros.shwan.moviemvvm.DataSource.Client.ApiClient
import kr.zagros.shwan.moviemvvm.DataSource.Client.ApiService
import kr.zagros.shwan.moviemvvm.Entities.MovieResponse
import kr.zagros.shwan.moviemvvm.utils.Config

class DataStoreImp : DataStore {

    private val apiService: ApiService

    init {
        this.apiService = ApiClient.create(Config.BASE_URL_ONE).create(ApiService::class.java)
    }

    //region Main
    override fun getMovies(page: Int): Single<MovieResponse> {
        return Single.create { emitter ->
            val main = CreateHome(page)
            if (!emitter.isDisposed && main != null) {
                emitter.onSuccess(main)
            } else {
                emitter.onError(Throwable())
            }

        }
    }

    private fun CreateHome(page: Int): MovieResponse {
        return MovieResponse()
        /*  try {
            return new MovieResponse();
            //return apiService.getMovies(page).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;*/
    }
    //endregion---------------------------------------------------
}
