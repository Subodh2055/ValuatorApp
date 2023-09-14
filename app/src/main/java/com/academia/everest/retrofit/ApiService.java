package com.academia.everest.retrofit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("http://103.94.159.179:9019/ebl/api/verify")
    Call<ApiResponse> postData(@Body Map<String, String> map);
}
