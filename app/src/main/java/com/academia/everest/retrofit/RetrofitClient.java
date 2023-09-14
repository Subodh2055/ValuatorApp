package com.academia.everest.retrofit;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://103.94.159.179:9019/ebl/"; // Use 10.0.2.2 for localhost in the Android Emulator

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient okHttp = new OkHttpClient.Builder().build();
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttp).build();
        }
        return retrofit;
    }


}
