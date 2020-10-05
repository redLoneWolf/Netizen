package com.sudhar.netizen.RoomDB.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.sudhar.netizen.RoomDB.DateTimeConverter;

import java.time.OffsetDateTime;

@Entity(tableName = "images_table")
public class ImageModelEntity {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id;

    @NonNull
    @ColumnInfo(name = "objectId")
    private String objectId;

    @NonNull
    @ColumnInfo(name = "contentType")
    private String contentType;

    private String imageUrl;


    private String identifier;

    @TypeConverters({DateTimeConverter.class})
    @NonNull
    private OffsetDateTime created;

    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @NonNull
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(@NonNull String objectId) {
        this.objectId = objectId;
    }

    @NonNull
    public String getContentType() {
        return contentType;
    }

    public void setContentType(@NonNull String contentType) {
        this.contentType = contentType;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

    public void setCreated(OffsetDateTime created) {
        this.created = created;
    }

    public String getIdentifier() {
        return identifier;
    }

    public ImageModelEntity(@NonNull String id, @NonNull String objectId, @NonNull String contentType, String imageUrl, @NonNull OffsetDateTime created, String identifier) {
        this.id = id;
        this.objectId = objectId;
        this.contentType = contentType;
        this.imageUrl = imageUrl;
        this.created = created;
        this.identifier = this.contentType + "-" + this.objectId;
    }




}
