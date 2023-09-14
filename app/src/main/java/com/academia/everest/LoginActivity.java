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

//        requestIdEditText = findViewById(R.id.requestIdEditText);
//        tokenEditText = findViewById(R.id.tokenEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the values of param1 and param2 from your EditText fields
                String requestedId = "6810631f-0bca-4e87-aad1-326945824d4d-1694669918997";
                String token = "471632";

                // Assuming you have already defined the ApiService interface
                ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

                // Make the POST request
                Map<String,String>  map = new HashMap<>();
                map.put("token",token);
                map.put("requestId",requestedId);
                Call<ApiResponse> call = apiService.postData(map);
                Log.d("CallObject", call.toString());
                call.enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful()) {
                            // POST request was successful, handle the response data here
                            ApiResponse responseData = response.body();

                            // You can process responseData as needed
                            // For example, if the response contains user authentication data,
                            // you can proceed to the next activity or perform other actions.
                            // Replace "NextActivity.class" with the actual activity you want to start.
                            Intent intent = new Intent(LoginActivity.this, DetailActivity.class);
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
                Intent intent = new Intent(LoginActivity.this, DetailActivity.class);
                // Create a JSON object with actual values
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("requestedDate", "2023-09-14 11:23:39.107");
                    jsonObject.put("branchName", "Koteshwor");
                    jsonObject.put("contactPersonName", "contact person name");
                    jsonObject.put("contactNumber", "9988998876");
                    jsonObject.put("collateralType", "Land Security");
                    jsonObject.put("collateralOwnerName", "owner Name");
                    jsonObject.put("collateralAddress", "add, Aamchok, Bhojpur, Province 1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Convert the JSON object to a string
                String jsonData = jsonObject.toString();

                // Pass the JSON data as an extra
                intent.putExtra("jsonData", jsonData);

                startActivity(intent);            }
        });
    }
}
