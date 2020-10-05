package com.sudhar.netizen.models;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Comment {

    @SerializedName("id")
    private String id;

    @SerializedName("username")
    private String Username;

    @SerializedName("user_id")
    private int UserId;

    @SerializedName("body")
    private String Body;

    @SerializedName("created")
    private String Created;

    @SerializedName("replies_count")
    private int RepliesCount;

    @SerializedName("parent_id")
    private String parentId;

    @SerializedName("content_type")
    private String contentType;


    @SerializedName("object_id")
    private String objectId;

    @SerializedName("replies")
    private List<Comment> replies;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return Username;
    }

    public int getUserId() {
        return UserId;
    }

    public String getBody() {
        return Body;
    }

    public String getCreated() {
        return Created;
    }

    public int getRepliesCount() {
        return RepliesCount;
    }

    public String getParentId() {
        return parentId;
    }

    public String getContentType() {
        return contentType;
    }

    public String getObjectId() {
        return objectId;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public static final DiffUtil.ItemCallback<Comment> CALLBACK = new DiffUtil.ItemCallback<Comment>() {
        @Override
        public boolean areItemsTheSame(@NonNull Comment memeDetailModel, @NonNull Comment t1) {
            return memeDetailModel.getId().equals(t1.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Comment memeDetailModel, @NonNull Comment t1) {

            return memeDetailModel.getId().equals(t1.getId());
        }
    };
}
