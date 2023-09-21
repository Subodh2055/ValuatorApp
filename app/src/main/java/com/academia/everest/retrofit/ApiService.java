package com.academia.everest.retrofit;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @POST("http://103.94.159.179:9019/ebl/api/verify")
    Call<ApiResponse> postData(@Body Map<String, String> map);

    @Multipart
    @POST("http://103.94.159.179:9019/ebl/api/upload-file")
    Call<ResponseBody> uploadValuationFile(
            @Part MultipartBody.Part file,
            @Part("token") RequestBody token,
            @Part("remarks") RequestBody remarks,
            @Part("reqId") RequestBody reqId
    );
}
