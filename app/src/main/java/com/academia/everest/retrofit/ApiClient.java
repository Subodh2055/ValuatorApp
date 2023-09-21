package com.academia.everest.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author Subodh Bhandari on 9/21/2023
 */

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8086/";

    public static Retrofit getClient() {
        OkHttpClient okHttp = new OkHttpClient.Builder().build();
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttp)
                    .build();
        }
}