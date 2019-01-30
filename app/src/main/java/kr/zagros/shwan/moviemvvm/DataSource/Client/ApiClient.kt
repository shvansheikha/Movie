package kr.zagros.shwan.moviemvvm.DataSource.Client

import java.util.concurrent.TimeUnit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient private constructor(private val url: String) {

    private// .addConverterFactory(ScalarsConverterFactory.create())
    // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    val retrofit: Retrofit
        get() {
            val client = OkHttpClient.Builder()
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .build()

            return Retrofit.Builder()
                    .baseUrl(this.url)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

    fun <T> create(service: Class<T>): T {
        return retrofit.create(service)
    }

    companion object {

        fun create(url: String): ApiClient {
            return ApiClient(url)
        }
    }
}
