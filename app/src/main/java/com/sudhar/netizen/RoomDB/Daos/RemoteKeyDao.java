package com.sudhar.netizen.RoomDB.Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.sudhar.netizen.RoomDB.Entities.RemoteKey;

import io.reactivex.rxjava3.core.Single;

@Dao
public interface RemoteKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrReplace(RemoteKey remoteKey);

    @Query("SELECT * FROM remote_keys WHERE label = :query")
    Single<RemoteKey> remoteKeyByQuerySingle(String query);

    @Query("DELETE FROM remote_keys WHERE label = :query")
    void deleteByQuery(String query);
}