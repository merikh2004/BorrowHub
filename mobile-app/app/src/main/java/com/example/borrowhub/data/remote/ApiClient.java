package com.example.borrowhub.data.remote;

import android.util.Log;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.borrowhub.BuildConfig;
import com.example.borrowhub.data.remote.api.ApiService;

public class ApiClient {
    // 127.0.0.1 is used with 'adb reverse tcp:8000 tcp:8000'
    private static final String BASE_URL = BuildConfig.BASE_URL;

    private static ApiClient instance;
    private final Retrofit retrofit;

    private ApiClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}
