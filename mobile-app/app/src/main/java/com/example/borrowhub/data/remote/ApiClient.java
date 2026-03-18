package com.example.borrowhub.data.remote;

import com.example.borrowhub.data.local.SessionManager;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.borrowhub.BuildConfig;
import com.example.borrowhub.data.remote.api.ApiService;

public class ApiClient {
    // 127.0.0.1 is used with 'adb reverse tcp:8000 tcp:8000'
    private static final String BASE_URL = BuildConfig.BASE_URL;

    private static ApiClient instance;
    private final Retrofit retrofit;
    private final SessionManager sessionManager;

    private ApiClient(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();
                    String authToken = this.sessionManager.getAuthToken();

                    if (authToken == null || authToken.trim().isEmpty()) {
                        return chain.proceed(originalRequest);
                    }

                    String bearerToken = authToken.startsWith("Bearer ") ? authToken : "Bearer " + authToken;
                    Request requestWithAuth = originalRequest.newBuilder()
                            .header("Authorization", bearerToken)
                            .build();

                    return chain.proceed(requestWithAuth);
                })
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
    /**
     * Returns a singleton ApiClient instance.
     * The first provided SessionManager is retained for the process lifetime.
     */
    public static synchronized ApiClient getInstance(SessionManager sessionManager) {
        if (instance == null) {
            instance = new ApiClient(sessionManager);
        }
        return instance;
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}
