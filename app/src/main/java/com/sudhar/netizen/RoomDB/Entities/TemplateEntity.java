package com.sudhar.netizen.RoomDB.Entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.sudhar.netizen.RoomDB.DateTimeConverter;

import java.time.OffsetDateTime;

@Entity(tableName = "template_table")
public class TemplateEntity {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id;

    private String description;

    private String username;

    @NonNull
    @TypeConverters(DateTimeConverter.class)
    private OffsetDateTime created;

    @NonNull
    private String contentType;

    private boolean is_following;

    private boolean is_liked;

    private int likes;

    private boolean is_bookmarked;

    private int userId;

    @NonNull
    private String identifier;

    public TemplateEntity(String id, String description, String username, @NonNull OffsetDateTime created, @NonNull String contentType, boolean is_following, boolean is_liked, int likes, boolean is_bookmarked, int userId, @NonNull String identifier) {
        this.id = id;
        this.description = description;
        this.username = username;
        this.created = created;
        this.contentType = contentType;
        this.is_following = is_following;
        this.is_liked = is_liked;
        this.likes = likes;
        this.is_bookmarked = is_bookmarked;
        this.userId = userId;
        this.identifier = this.contentType + "-" + this.id;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    @NonNull
    public OffsetDateTime getCreated() {
        return created;
    }

    @NonNull
    public String getContentType() {
        return contentType;
    }

    public boolean is_following() {
        return is_following;
    }

    public boolean is_liked() {
        return is_liked;
    }

    public int getLikes() {
        return likes;
    }

    public boolean is_bookmarked() {
        return is_bookmarked;
    }

    public int getUserId() {
        return userId;
    }


    @NonNull
    public String getIdentifier() {
        return identifier;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIs_following(boolean is_following) {
        this.is_following = is_following;
    }

    public void setIs_liked(boolean is_liked) {
        this.is_liked = is_liked;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setIs_bookmarked(boolean is_bookmarked) {
        this.is_bookmarked = is_bookmarked;
    }
}
