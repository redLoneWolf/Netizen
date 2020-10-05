package com.sudhar.netizen.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ContentList {

    @SerializedName("next")
    private String NextPage;

    @SerializedName("count")
    private int Count;

    @SerializedName("results")
    private List<UserContentModel> contents;

    public String getNextPage() {
        return NextPage;
    }

    public int getCount() {
        return Count;
    }

    public List<UserContentModel> getContents() {
        return contents;
    }

    public static class UserContentModel {

        @SerializedName("id")
        private String id;

        @SerializedName("images")
        private String imageUrl;

        public String getId() {
            return id;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
