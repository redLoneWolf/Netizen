package com.sudhar.netizen.models;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserModel {
    @SerializedName("next")
    private String NextPage;

    @SerializedName("count")
    private int Count;

    @SerializedName("results")
    private List<JsonObject> users;

    public String getNextPage() {
        return NextPage;
    }

    public int getCount() {
        return Count;
    }

    public List<JsonObject> getUsers() {
        return users;
    }
}
