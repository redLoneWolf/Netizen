package com.sudhar.netizen.models;

import com.google.gson.annotations.SerializedName;
import com.sudhar.netizen.NetUtils.RestInterface;

public class ImageModel {


    @SerializedName("id")
    private String id;

    @SerializedName("image")
    private String ImageUrl;


    public String getId() {
        return id;
    }

    public String getImageUrl() {
        return RestInterface.IP + ImageUrl;
    }

    public ImageModel(String id, String imageUrl) {
        this.id = id;
        ImageUrl = imageUrl;
    }

    public String getImage() {
        return ImageUrl;
    }
}
