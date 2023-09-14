package com.academia.everest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class DetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String jsonData = getIntent().getStringExtra("jsonData");

        // Parse JSON using Gson
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonData, JsonObject.class);

        // Get individual fields from JSON
        String branchName = jsonObject.get("branchName").getAsString();
        String contactPersonName = jsonObject.get("contactPersonName").getAsString();
        String contactNumber = jsonObject.get("contactNumber").getAsString();
        String collateralType = jsonObject.get("collateralType").getAsString();
        String collateralOwnerName = jsonObject.get("collateralOwnerName").getAsString();
        String collateralAddress = jsonObject.get("collateralAddress").getAsString();

        // Find the TextViews in your activity_detail.xml layout
        TextView branchNameTextView = findViewById(R.id.branchNameTextView);
        TextView contactPersonNameTextView = findViewById(R.id.contactPersonNameTextView);
        TextView contactNumberTextView = findViewById(R.id.contactNumberTextView);
        TextView collateralTypeTextView = findViewById(R.id.collateralTypeTextView);
        TextView collateralOwnerNameTextView = findViewById(R.id.collateralOwnerNameTextView);
        TextView collateralAddressTextView = findViewById(R.id.collateralAddressTextView);

        // Set the values to TextViews
        branchNameTextView.setText("Branch Name: " + branchName);
        contactPersonNameTextView.setText("Contact Person: " + contactPersonName);
        contactNumberTextView.setText("Contact Number: " + contactNumber);
        collateralTypeTextView.setText("Collateral Type: " + collateralType);
        collateralOwnerNameTextView.setText("Collateral Owner: " + collateralOwnerName);
        collateralAddressTextView.setText("Collateral Address: " + collateralAddress);

    }
}

