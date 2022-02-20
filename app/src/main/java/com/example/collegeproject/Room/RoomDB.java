package com.example.collegeproject.Activity;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Offline_User_Data.class}, version = 1, exportSchema = false)

public abstract class RoomDB extends RoomDatabase {

    private static RoomDB roomDB;

    private static String DATABSE_NAME = "user_database";

    public synchronized static RoomDB getInstance(Context context) {
        if (roomDB == null) {
            roomDB = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABSE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return roomDB;
    }

    public abstract 

}
