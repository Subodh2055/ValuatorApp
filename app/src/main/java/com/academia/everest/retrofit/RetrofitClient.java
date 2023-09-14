package com.academia.everest.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://103.94.159.179:9019/ebl/";

    public static Retrofit getClient() {
        OkHttpClient okHttp = new OkHttpClient.Builder().build();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp).build();
    }
}
