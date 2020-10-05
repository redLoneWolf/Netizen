package com.sudhar.netizen.RoomDB;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class CustomApplication extends Application {

//    AppDatabase myDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        // when upgrading versions, kill the original tables by using fallbackToDestructiveMigration()
//        myDatabase = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.NAME).fallbackToDestructiveMigration().build();
    }

//    public AppDatabase getMyDatabase() {
//        return myDatabase;
//    }
}
