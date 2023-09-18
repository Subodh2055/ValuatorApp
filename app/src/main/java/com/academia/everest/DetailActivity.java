package com.academia.everest;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.academia.everest.retrofit.ApiClient;
import com.academia.everest.retrofit.ApiService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private static final int FILE_PICKER_REQUEST_CODE = 123; // Define a request code (use any unique integer)
    private Uri selectedFileUri; // To store the selected file URI
     String jsonData ; // Declare jsonData as a class variable
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        String newJsonData = intent.getStringExtra("jsonData");
//        if (newJsonData != null && !newJsonData.isEmpty()) {
//            jsonData = newJsonData;
//            // Update your UI with the new JSON data here
//        }
//    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        jsonData = getIntent().getStringExtra("jsonData");

        if (jsonData != null && !jsonData.isEmpty()) {
            // Parse JSON using Gson
            Gson gson = new Gson();
            JsonObject jsonObject = null;
            try {
                jsonObject = gson.fromJson(jsonData, JsonObject.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                // Handle the JSON parsing error here
                showJsonParsingError();
                return; // Return early to avoid further processing
            }
            // Check if jsonObject is not null
            if (jsonObject != null) {
                // Get individual fields from JSON
                String branchName = jsonObject.has("branchName") ? jsonObject.get("branchName").getAsString() : "";
                String contactPersonName = jsonObject.has("contactPersonName") ? jsonObject.get("contactPersonName").getAsString() : "";
                String contactNumber = jsonObject.has("contactNumber") ? jsonObject.get("contactNumber").getAsString() : "";
                String collateralType = jsonObject.has("collateralType") ? jsonObject.get("collateralType").getAsString() : "";
                String collateralOwnerName = jsonObject.has("collateralOwnerName") ? jsonObject.get("collateralOwnerName").getAsString() : "";
                String collateralAddress = jsonObject.has("collateralAddress") ? jsonObject.get("collateralAddress").getAsString() : "";

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
            } else {
                // Handle the case where jsonObject is null
                Toast.makeText(this, "Error: Invalid JSON data", Toast.LENGTH_SHORT).show();
                finish();            }
        } else {
            // Handle the case where jsonData is null or empty
            Toast.makeText(this, "Error: jsonData is null", Toast.LENGTH_SHORT).show();
            finish();
        }

//        TextView selectedFileNameTextView = findViewById(R.id.selectedFileNameTextView);
//        selectedFileNameTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleSelectedFileTextViewClick();
//            }
//        });
    }
    private void showJsonParsingError() {
        Toast.makeText(this, "Error: Invalid JSON data", Toast.LENGTH_SHORT).show();
        finish();
    }
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void handleSelectedFileTextViewClick() {
        if (selectedFileUri != null) {
            String fileExtension = getFileExtension(selectedFileUri);
            Log.d("SelectedFileUri", selectedFileUri.toString());

            if (fileExtension != null && !fileExtension.isEmpty()) {

                if (fileExtension != null && fileExtension.equals("txt")) {
                // Handle text file
                handleTextFile(selectedFileUri);
            } else if (isImageFile(fileExtension)) {
                // Handle image file
                handleImageFile(selectedFileUri);
            } else if (fileExtension != null && fileExtension.equals("pdf")) {
                // Handle PDF file
                handlePdfFile(selectedFileUri);
            } else {
                // Handle other file types
                handleOtherFileTypes(selectedFileUri);
            }
        }
        }else {
            Toast.makeText(this, "Error: Invalid file extension", Toast.LENGTH_SHORT).show();

        }
    }
    private boolean isImageFile(String fileExtension) {
        return fileExtension != null && (fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png"));
    }

    private void handleTextFile(Uri fileUri) {
        // Handle text file
        // For example, you can read and display its content
        try {
            InputStream inputStream = getContentResolver().openInputStream(fileUri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            // Display the content in a TextView or toast it
            Toast.makeText(this, "Text File Content:\n" + content.toString(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleImageFile(Uri fileUri) {
        // Handle image file
        // Display the image in an ImageView
        ImageView imageView = findViewById(R.id.imageView); // Assuming you have an ImageView in your layout
        imageView.setImageURI(fileUri);
        imageView.setVisibility(View.VISIBLE);
    }

    private void handlePdfFile(Uri fileUri) {
        // Handle PDF file
        // Open the PDF using an appropriate viewer
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(fileUri, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            // Handle the case where there's no PDF viewer installed
            e.printStackTrace();
        }
    }

    private void handleOtherFileTypes(Uri fileUri) {
        // Handle other file types (e.g., non-supported files)
        // You can implement custom logic here or open with external apps
        Toast.makeText(this, "Unsupported File Type", Toast.LENGTH_SHORT).show();
    }


    public void onUploadButtonClick(View view) {
        // Open a file picker dialog
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");  // Allow any file type
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File"),
                    FILE_PICKER_REQUEST_CODE);
        } catch (ActivityNotFoundException ex) {
            // Handle errors, e.g., display a message to the user
            Toast.makeText(this, "File picker not found. Please install a file picker app.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            // Handle the selected file here
            selectedFileUri = data.getData();
            String selectedFileName = getFileName(selectedFileUri);
            Uri fileUri = selectedFileUri;
            String filePath = getRealPathFromUri(fileUri);
            // Display the selected file name
            TextView selectedFileNameTextView = findViewById(R.id.selectedFileNameTextView);
            selectedFileNameTextView.setText("Selected File: " + selectedFileName);
            selectedFileNameTextView.setVisibility(View.VISIBLE); // Make it visible

            // Detect the file type by its extension (e.g., .txt)
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(selectedFileUri.toString());

            if (fileExtension != null && fileExtension.equals("txt")) {
                // It's a text file, read and display its content
                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedFileUri);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    // Display the content in a TextView
                    TextView fileContentTextView = findViewById(R.id.fileContentTextView);
                    fileContentTextView.setText(content.toString());
                    fileContentTextView.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                if (fileExtension != null && (fileExtension.equals("jpg") || fileExtension.equals("jpeg") || fileExtension.equals("png"))) {
                    ImageView imageView = findViewById(R.id.imageView); // Assuming you have an ImageView in your layout
                    imageView.setImageURI(selectedFileUri);
                    imageView.setVisibility(View.VISIBLE);
                } else if (fileExtension != null && fileExtension.equals("pdf")) {
                    // Example for opening PDF files using an Intent:
                    Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                    pdfIntent.setDataAndType(selectedFileUri, "application/pdf");
                    pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    try {
                        startActivity(pdfIntent);
                    } catch (ActivityNotFoundException e) {
                        // Handle the case where there's no PDF viewer installed
                        e.printStackTrace();
                    }
                } else {
                    // For other file types, you can implement handling as needed
                }
            }
        }
    }



    // Utility method to get the file name from a URI
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            String[] projection = {android.provider.MediaStore.Images.ImageColumns.DISPLAY_NAME};
            try (android.database.Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(android.provider.MediaStore.Images.ImageColumns.DISPLAY_NAME);
                    result = cursor.getString(index);
                }
            }
        } else if (uri.getScheme().equals("file")) {
            result = new File(uri.getPath()).getName();
        }
        return result;
    }
    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        }
        return uri.getPath(); // Fallback to Uri path if cursor is null
    }

    public void onSaveButtonClick(View view) {
        String requestedId = "6810631f-0bca-4e87-aad1-326945824d4d-1694669918997";
        String token = "471632";
        String remarks = "gedddd";
        String filePath = getRealPathFromUri(selectedFileUri); // Replace 'fileUri' with the actual Uri
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("token", token);
        hashMap.put("requestId", requestedId);
        hashMap.put("remarks", remarks);
        // Assuming you have the required values for these variables

        // Create a File object from the file path
        File file = new File(filePath);

        // Create a RequestBody from the file
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.uploadValuationFile(filePart, token, requestedId, remarks);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    // Handle success
                    // You can parse the response if needed
                } else {
                    // Handle failure
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle network errors
            }
        });
    }
}
