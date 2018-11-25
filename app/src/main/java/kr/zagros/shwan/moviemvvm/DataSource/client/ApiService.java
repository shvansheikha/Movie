package kr.zagros.shwan.moviemvvm.DataSource.client;

import kr.zagros.shwan.moviemvvm.Entities.MovieResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * Get a list of movies in theatres.
     */

    @GET("v1/movies")
    Call<MovieResponse> getMovies(@Query("page") int page);

}
