package com.sudhar.netizen.RoomDB.Entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "remote_keys")
public class RemoteKey {

    @PrimaryKey
    @NonNull
    public String label;

    public Integer nextKey;
    public boolean reachedEnd;

    public RemoteKey(@NonNull String label, Integer nextKey, boolean reachedEnd) {
        this.label = label;
        this.nextKey = nextKey;
        this.reachedEnd = reachedEnd;
    }

    public boolean isReachedEnd() {
        return reachedEnd;
    }

    public String getLabel() {
        return label;
    }

    public Integer getNextKey() {
        return nextKey;
    }
}
