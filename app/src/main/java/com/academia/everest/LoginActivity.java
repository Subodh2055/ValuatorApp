package com.academia.everest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.academia.everest.retrofit.ApiResponse;
import com.academia.everest.retrofit.ApiService;
import com.academia.everest.retrofit.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText requestIdEditText;
    private EditText tokenEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestIdEditText = findViewById(R.id.requestIdEditText);
        tokenEditText = findViewById(R.id.tokenEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String requestedId = requestIdEditText.getText().toString().trim();
                String token = tokenEditText.getText().toString().trim();

                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

                Map<String, String> map = new HashMap<>();
                map.put("token", token);
                map.put("requestId", requestedId);
                Call<ApiResponse> call = apiService.postData(map);

                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            ApiResponse responseData = response.body();

                            Intent intent = new Intent(LoginActivity.this, DetailActivity.class);
                            JSONObject jsonObject = new JSONObject();
                            try {
                                if( responseData != null) {
                                    jsonObject.put("requestedDate", responseData.getRequestedDate());
                                    jsonObject.put("branchName", responseData.getBranchName());
                                    jsonObject.put("contactPersonName", responseData.getContactPersonName());
                                    jsonObject.put("contactNumber", responseData.getContactNumber());
                                    jsonObject.put("collateralType", responseData.getCollateralType());
                                    jsonObject.put("collateralOwnerName", responseData.getCollateralOwnerName());
                                    jsonObject.put("collateralAddress", responseData.getCollateralAddress());
                                } else {
                                    Toast.makeText(LoginActivity.this, "Response Data: " + response.message(), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            String jsonData = jsonObject.toString();

                            intent.putExtra("jsonData", jsonData);
                            startActivity(intent);

                        } else {
                            // Handle the error
                            Toast.makeText(LoginActivity.this, "API Error: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        // Handle network or other errors here
                        t.printStackTrace();
                        Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });
//                Intent intent = new Intent(LoginActivity.this, DetailActivity.class);
//
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("requestedDate", "2023-09-14 11:23:39.107");
//                    jsonObject.put("branchName", "Koteshwor");
//                    jsonObject.put("contactPersonName", "contact person name");
//                    jsonObject.put("contactNumber", "9988998876");
//                    jsonObject.put("collateralType", "Land Security");
//                    jsonObject.put("collateralOwnerName", "owner Name");
//                    jsonObject.put("collateralAddress", "add, Aamchok, Bhojpur, Province 1");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                String jsonData = jsonObject.toString();
//
//                intent.putExtra("jsonData", jsonData);
//
//                startActivity(intent);
            }
        });
    }
}
