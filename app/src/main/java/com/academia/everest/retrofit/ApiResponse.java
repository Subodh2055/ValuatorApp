package com.academia.everest.retrofit;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Subodh Bhandari on 9/21/2023
 */

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {
    @SerializedName("branchName")
    private String branchName;

    @SerializedName("message")
    private String message;

    @SerializedName("token")
    private String token;

    @SerializedName("requestId")
    private String requestId;

    @SerializedName("requestedDate")
    private String requestedDate;

    @SerializedName("contactPersonName")
    private String contactPersonName;

    @SerializedName("collateralOwnerName")
    private String collateralOwnerName;

    @SerializedName("collateralAddress")
    private String collateralAddress;

    @SerializedName("contactNumber")
    private String contactNumber;

    @SerializedName("collateralType")
    private String collateralType;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getCollateralType() {
        return collateralType;
    }

    public void setCollateralType(String collateralType) {
        this.collateralType = collateralType;
    }

    public String getCollateralOwnerName() {
        return collateralOwnerName;
    }

    public void setCollateralOwnerName(String collateralOwnerName) {
        this.collateralOwnerName = collateralOwnerName;
    }

    public String getCollateralAddress() {
        return collateralAddress;
    }

    public void setCollateralAddress(String collateralAddress) {
        this.collateralAddress = collateralAddress;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
}
