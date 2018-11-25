package kr.zagros.shwan.moviemvvm.DataSource.client;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private String url;

    private ApiClient(String url) {
        this.url = url;
    }

    public static ApiClient create(String url) {
        return new ApiClient(url);
    }

    private Retrofit getRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(this.url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                // .addConverterFactory(ScalarsConverterFactory.create())
                // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public <T> T create(final Class<T> service) {
        return getRetrofit().create(service);
    }
}
