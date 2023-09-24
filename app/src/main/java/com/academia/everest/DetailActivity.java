package com.academia.everest;

import static android.os.Environment.getExternalStorageDirectory;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.academia.everest.retrofit.ApiService;
import com.academia.everest.retrofit.RetrofitClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Subodh Bhandari on 9/21/2023
 */

public class DetailActivity extends AppCompatActivity {

    ApiService apiService = RetrofitClient.getClient();

    private static final int FILE_PICKER_REQUEST_CODE = 1998;
    private Uri selectedFileUri;
    String jsonData;
    String requestId;
    String tokenFromLogin;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        requestId = getIntent().getStringExtra("requestId");
        tokenFromLogin = getIntent().getStringExtra("token");
        jsonData = getIntent().getStringExtra("jsonData");
        Log.d("DetailActivity", "jsonData: " + jsonData);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, FILE_PICKER_REQUEST_CODE);
        }

        if (jsonData != null && !jsonData.isEmpty()) {

            Gson gson = new Gson();
            JsonObject jsonObject;
            try {
                jsonObject = gson.fromJson(jsonData, JsonObject.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                showJsonParsingError();
                return;
            }
            if (jsonObject != null) {
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
                branchNameTextView.setText(branchName);
                contactPersonNameTextView.setText(contactPersonName);
                contactNumberTextView.setText(contactNumber);
                collateralTypeTextView.setText(collateralType);
                collateralOwnerNameTextView.setText(collateralOwnerName);
                collateralAddressTextView.setText(collateralAddress);
            } else {
                Toast.makeText(this, "Error: Invalid JSON data", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Error: jsonData is null", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void showJsonParsingError() {
        Toast.makeText(this, "Error: Invalid JSON data", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onUploadButtonClick(View view) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, FILE_PICKER_REQUEST_CODE);
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");  // Allow any file type
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            try {
                startActivityForResult(
                        Intent.createChooser(intent, "Select a File"),
                        FILE_PICKER_REQUEST_CODE);
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(this, "File picker not found. Please install a file picker app.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == FILE_PICKER_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with file selection
                onUploadButtonClick(null); // Calxl the file selection logic here
            } else {
                Toast.makeText(this, "Permission denied. Cannot select a file.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                selectedFileUri = data.getData();
                String selectedFileName = getFileNameFromUri(selectedFileUri);
                TextView selectedFileNameTextView = findViewById(R.id.selectedFileNameTextView);
                selectedFileNameTextView.setText("Selected File: " + selectedFileName);
                selectedFileNameTextView.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private MultipartBody.Part getMultipartFromUri(Uri uri) {
        String mimeType = getContentResolver().getType(uri); // Get the MIME type of the file
        String displayName = null;

        if (uri.getScheme().equals("content")) {
            String[] projection = {MediaStore.Images.ImageColumns.DISPLAY_NAME};
            try (Cursor cursor = getContentResolver().query(uri, projection, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DISPLAY_NAME);
                    displayName = cursor.getString(index);
                }
            }
        } else if (uri.getScheme().equals("file")) {
            displayName = new File(uri.getPath()).getName();
        }

        if (displayName != null) {
            RequestBody requestBody = null;
            try {
                requestBody = RequestBody.create(MediaType.parse(mimeType), String.valueOf(getContentResolver().openInputStream(uri)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            return MultipartBody.Part.createFormData("file", displayName, Objects.requireNonNull(requestBody));
        } else {
            return null;
        }
    }

    private String getFileNameFromUri(Uri uri) {
        String displayName = null;
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            displayName = cursor.getString(nameIndex);
            cursor.close();
        }

        return displayName;
    }

    private String getFilePathFromUri(Uri uri) {
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if (uri.toString().startsWith("content://com.android.providers.downloads.documents")) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.parseLong(docId));
                return getDataColumn(contentUri, null, null);
            } else if (uri.toString().startsWith("content://com.android.externalstorage.documents")) {
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return getExternalStorageDirectory() + "/" + split[1];
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private String getDataColumn(Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public void verifyButtonClick(View view) {
        EditText remarksEditText = findViewById(R.id.remarksEditText);
        String remarks = remarksEditText.getText().toString();
        if (selectedFileUri != null) {
            showVerificationPopup(selectedFileUri, remarks);
        } else {
            // Handle the case where no file has been selected
            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show();
        }
    }

    private void showVerificationPopup(Uri selectedFileUri, String remarks) {

        View dialogView = getLayoutInflater().inflate(R.layout.custom_popup_layout, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(dialogView);

        EditText tokenEditText = dialogView.findViewById(R.id.tokenEditText1);
        Button submitButton = dialogView.findViewById(R.id.submitButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        AlertDialog alertDialog = dialogBuilder.create();

        alertDialog.setCancelable(false);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = tokenEditText.getText().toString();
                if (Objects.equals(tokenFromLogin, token)) {
                    performApiRequest(token, remarks, selectedFileUri, requestId);
                    alertDialog.dismiss();
//                    finishAffinity();
                } else {
                    Toast.makeText(v.getContext(), "Token Did Not Matched!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
    }

    private String getRealPathFromUri(Uri uri) {
        if ("content".equalsIgnoreCase(uri.getScheme()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                ParcelFileDescriptor parcelFileDescriptor = getContentResolver().openFileDescriptor(uri, "r");
                if (parcelFileDescriptor != null) {
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    FileInputStream inputStream = new FileInputStream(fileDescriptor);
                    File file = new File(getCacheDir(), "temp_file");
                    FileOutputStream outputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        outputStream.write(buffer, 0, length);
                    }
                    outputStream.close();
                    inputStream.close();
                    parcelFileDescriptor.close();
                    return file.getAbsolutePath();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } else {
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String filePath = cursor.getString(column_index);
                cursor.close();
                return filePath;
            } else {
                return uri.getPath();
            }
        }
    }

    private void performApiRequest(String token, String remarks, Uri fileUri, String reqId) {

        String filePath = getRealPathFromUri(fileUri);
        if (filePath == null) {
            Toast.makeText(this, "Selected file does not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(this, "Selected file does not exist", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody fileRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), fileRequestBody);

        RequestBody tokenRequestBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody remarksRequestBody = RequestBody.create(MediaType.parse("text/plain"), remarks);
        RequestBody reqIdRequestBody = RequestBody.create(MediaType.parse("text/plain"), reqId);

        Call<ResponseBody> call = apiService.uploadValuationFile(
                filePart,
                tokenRequestBody,
                remarksRequestBody,
                reqIdRequestBody
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    showSuccessDialog();
                } else {
                    String errorMessage = "Unknown Error";
                    try {
                        assert response.errorBody() != null;
                        JSONObject errorObject = new JSONObject(response.errorBody().string());
                        errorMessage = errorObject.optString("message", errorMessage);
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("API Response", "Error Body: " + errorMessage);
                    Toast.makeText(DetailActivity.this, "API Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(DetailActivity.this, "Unable to Submit!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSuccessDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setMessage("Upload successful!");
        dialogBuilder.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            redirectToLoginActivity();
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void redirectToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
