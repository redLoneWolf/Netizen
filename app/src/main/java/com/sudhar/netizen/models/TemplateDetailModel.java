package com.sudhar.netizen.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TemplateDetailModel {
    @SerializedName("username")
    private String Username;

    @SerializedName("id")
    private String Id;

    @SerializedName("description")
    private String Description;

    @SerializedName("images")
    private List<ImageModel> Images;


    @SerializedName("content_type")
    private String ContentType;

    @SerializedName("is_following")
    private boolean is_following;

    @SerializedName("is_liked")
    private boolean is_liked;

    @SerializedName("likes")
    private int Likes;

    @SerializedName("user_id")
    private int UserId;

    @SerializedName("is_bookmarked")
    private boolean is_bookmarked;

    @SerializedName("created")
    private String Created;

    @SerializedName("comments")
    private List<Comment> Comments;

    public String getUsername() {
        return Username;
    }

    public String getId() {
        return Id;
    }

    public String getDescription() {
        return Description;
    }

    public boolean is_liked() {
        return is_liked;
    }

    public int getLikes() {
        return Likes;
    }

    public boolean is_following() {
        return is_following;
    }

    public boolean is_bookmarked() {
        return is_bookmarked;
    }

    public String getContentType() {
        return ContentType;
    }

    public List<ImageModel> getImages() {
        return Images;
    }

    public int getUserId() {
        return UserId;
    }

    public List<Comment> getComments() {
        return Comments;
    }

    public String getCreated() {
        return Created;
    }

    public static final DiffUtil.ItemCallback<TemplateDetailModel> CALLBACK = new DiffUtil.ItemCallback<TemplateDetailModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull TemplateDetailModel templateDetailModel, @NonNull TemplateDetailModel t1) {
            return templateDetailModel.getId() == t1.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TemplateDetailModel memeDetailModel, @NonNull TemplateDetailModel t1) {
            Log.d("Template", "areContentsTheSame: ");
            return true;
        }
    };
}
