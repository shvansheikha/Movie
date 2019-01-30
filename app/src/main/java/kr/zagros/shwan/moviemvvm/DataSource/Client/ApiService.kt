package kr.zagros.shwan.moviemvvm.DataSource.Client

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @get:GET(".")
    val a: Call<ResponseBody>

    /**
     * Get a list of movies in theatres.
     */

    @GET("v1/movies")
    fun getMovies(@Query("page") page: Long): Call<ResponseBody>

}
