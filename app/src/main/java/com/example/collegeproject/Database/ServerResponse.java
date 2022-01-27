package com.example.collegeproject.Database;

import com.google.gson.annotations.SerializedName;

public class ServerResponse {
    @SerializedName("success")
    boolean success;
    @SerializedName("message")
    String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
