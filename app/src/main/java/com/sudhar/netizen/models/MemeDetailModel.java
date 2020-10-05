package com.sudhar.netizen.models;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MemeDetailModel {

    @SerializedName("images")
    private List<ImageModel> Images;

    @SerializedName("description")
    private String Description;

    @SerializedName("username")
    private String Username;

    @SerializedName("id")
    private String id;

    @SerializedName("created")
    private String Created;

    @SerializedName("content_type")
    private String ContentType;

    @SerializedName("is_following")
    private boolean is_following;

    @SerializedName("is_liked")
    private boolean is_liked;

    @SerializedName("likes")
    private int Likes;

    @SerializedName("is_bookmarked")
    private boolean is_bookmarked;

    @SerializedName("comments")
    private List<Comment> Comments;

    @SerializedName("user_id")
    private int UserId;

    public List<ImageModel> getImages() {
        return Images;
    }

    public String getDescription() {
        return Description;
    }

    public String getUsername() {
        return Username;
    }

    public String getId() {
        return id;
    }

    public String getCreated() {
        return Created;
    }

    public String getContentType() {
        return ContentType;
    }

    public boolean is_liked() {
        return is_liked;
    }

    public boolean is_following() {
        return is_following;
    }

    public int getLikes() {
        return Likes;
    }

    public int getUserId() {
        return UserId;
    }

    public boolean is_bookmarked() {
        return is_bookmarked;
    }

    public List<Comment> getComments() {
        return Comments;
    }


    public static final DiffUtil.ItemCallback<MemeDetailModel> CALLBACK = new DiffUtil.ItemCallback<MemeDetailModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull MemeDetailModel memeDetailModel, @NonNull MemeDetailModel t1) {
            return memeDetailModel.getId() == t1.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MemeDetailModel memeDetailModel, @NonNull MemeDetailModel t1) {

            return true;
        }
    };
}

