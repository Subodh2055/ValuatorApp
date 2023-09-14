package com.academia.everest.retrofit;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {
    @SerializedName("branchName")
    private String branchName;

    @SerializedName("message")
    private String message;
}
