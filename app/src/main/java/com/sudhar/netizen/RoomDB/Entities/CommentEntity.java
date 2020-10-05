package com.sudhar.netizen.RoomDB.Entities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.sudhar.netizen.RoomDB.DateTimeConverter;

import java.time.OffsetDateTime;

@Entity(tableName = "comment_table")
public class CommentEntity {


    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;


    private String username;


    @Nullable
    @ColumnInfo(name = "parentId")
    private String parentId;

    @NonNull
    @ColumnInfo(name = "contentType")
    private String contentType;

    @NonNull
    @ColumnInfo(name = "objectId")
    private String objectId;

    private int userId;

    private String body;


    @TypeConverters(DateTimeConverter.class)
    @NonNull
    private OffsetDateTime created;


    private String identifier;


    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String mBody) {
        this.body = mBody;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime mCreated) {
        this.created = mCreated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String mUsername) {
        this.username = mUsername;
    }

    @Nullable
    public String getParentId() {
        return parentId;
    }

    public void setParentId(@Nullable String parentId) {
        this.parentId = parentId;
    }

    @NonNull
    public String getContentType() {
        return contentType;
    }

    public void setContentType(@NonNull String contentType) {
        this.contentType = contentType;
    }

    @NonNull
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(@NonNull String objectId) {
        this.objectId = objectId;
    }

    public String getIdentifier() {
        return identifier;
    }


    public CommentEntity(@NonNull String id, String username, @Nullable String parentId, @NonNull String contentType, @NonNull String objectId, int userId, String body, @NonNull OffsetDateTime created, String identifier) {
        this.id = id;
        this.username = username;
        this.parentId = parentId;
        this.contentType = contentType;
        this.objectId = objectId;
        this.userId = userId;
        this.body = body;
        this.created = created;
        this.identifier = this.contentType + "-" + this.objectId;
    }

    public static final DiffUtil.ItemCallback<CommentEntity> CALLBACK = new DiffUtil.ItemCallback<CommentEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull CommentEntity memeDetailModel, @NonNull CommentEntity t1) {
            return memeDetailModel.getId().equals(t1.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CommentEntity memeDetailModel, @NonNull CommentEntity t1) {

            return memeDetailModel.getId().equals(t1.getId());
        }
    };


}
