package com.sudhar.netizen.RoomDB.Entities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.sudhar.netizen.RoomDB.DateTimeConverter;

import java.time.OffsetDateTime;

@Entity(tableName = "meme_table")
public class MemeEntity {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id;

    private String description;


    private String username;


    @NonNull
    @TypeConverters(DateTimeConverter.class)
    private OffsetDateTime created;


    private String contentType;

    private boolean is_following;


    private boolean is_liked;


    private int likes;


    private boolean is_bookmarked;


    private int userId;


    @NonNull
    private String identifier;




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

    @NonNull
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public OffsetDateTime getCreated() {
        return created;
    }

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

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getIdentifier() {
        return identifier;
    }

    public MemeEntity(@NonNull String id, String description, String username, @NonNull OffsetDateTime created, String contentType, boolean is_following, boolean is_liked, int likes, boolean is_bookmarked, int userId, String identifier) {
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



    public static final DiffUtil.ItemCallback<MemeEntity> CALLBACK = new DiffUtil.ItemCallback<MemeEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull MemeEntity memeDetailModel, @NonNull MemeEntity t1) {
            return memeDetailModel.getId().equals(t1.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull MemeEntity memeDetailModel, @NonNull MemeEntity t1) {

            return memeDetailModel.getId().equals(t1.getId());
        }
    };




}
