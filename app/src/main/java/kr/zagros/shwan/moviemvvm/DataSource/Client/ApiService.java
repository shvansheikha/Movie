package kr.zagros.shwan.moviemvvm.DataSource.Client;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * Get a list of movies in theatres.
     */

    @GET("v1/movies")
    Call<ResponseBody> getMovies(@Query("page") long page);

}
