package com.sudhar.netizen.RoomDB.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.sudhar.netizen.RoomDB.Daos.MainDao;
import com.sudhar.netizen.RoomDB.Daos.RemoteKeyDao;
import com.sudhar.netizen.RoomDB.Entities.CommentEntity;
import com.sudhar.netizen.RoomDB.Entities.ImageModelEntity;
import com.sudhar.netizen.RoomDB.Entities.MemeEntity;
import com.sudhar.netizen.RoomDB.Entities.RemoteKey;
import com.sudhar.netizen.RoomDB.Entities.TemplateEntity;

@Database(entities = {MemeEntity.class, TemplateEntity.class, ImageModelEntity.class, CommentEntity.class, RemoteKey.class}, version = 21, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract MainDao mainDao();

    public abstract RemoteKeyDao remoteKeyDao();

    private static AppDatabase instance;

    // Database name to be used
    public static final String NAME = "MemeDatabase";

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, NAME).fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return instance;

    }
}
