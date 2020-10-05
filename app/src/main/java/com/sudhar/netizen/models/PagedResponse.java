package com.sudhar.netizen.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PagedResponse<T> {

    @SerializedName("next")
    private String NextPage;

    @SerializedName("count")
    private int Count;

    @SerializedName("results")
    private List<T> results;

    public String getNextPage() {
        return NextPage;
    }

    public int getCount() {
        return Count;
    }

    public List<T> getResults() {
        return results;
    }
}
