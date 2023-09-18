package com.academia.everest.retrofit;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    @POST("http://103.94.159.179:9019/ebl/api/verify")
    Call<ApiResponse> postData(@Body Map<String, String> map);

    @Multipart
    @POST("/v1/pub/valuation/upload")
    Call<ResponseBody> uploadValuationFile(
            @Part MultipartBody.Part file,
            @Query("token") String token,
            @Query("requestId") String requestId,
            @Query("remarks") String remarks
    );
}
