package kr.zagros.shwan.moviemvvm.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import kr.zagros.shwan.moviemvvm.DataSource.Client.ApiClient;
import kr.zagros.shwan.moviemvvm.DataSource.Client.ApiService;
import kr.zagros.shwan.moviemvvm.R;
import kr.zagros.shwan.moviemvvm.utils.Config;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ApiService apiService = ApiClient.create(Config.BASE_URL_TWO).create(ApiService.class);

        apiService.getA().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    try {
                       String responseString = response.body().string();
                        Log.e("TAG", "onResponse: "+responseString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {

            }
        });
    }
}
