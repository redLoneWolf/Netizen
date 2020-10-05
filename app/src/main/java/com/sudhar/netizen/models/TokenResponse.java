package com.sudhar.netizen.models;

import com.google.gson.annotations.SerializedName;

public class TokenResponse {
    @SerializedName("access")
    private String mAccessToken;
    @SerializedName("refresh")
    private String mRefreshToken;

    public String getAccessToken() {
        return mAccessToken;
    }

    public String getRefreshToken() {
        return mRefreshToken;
    }





}

