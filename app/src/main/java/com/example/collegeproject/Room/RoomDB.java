package com.example.collegeproject.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Offline_User_Data.class}, version = 1, exportSchema = false)

public abstract class RoomDB extends RoomDatabase {

    private static RoomDB roomDB;

    private static String DATABASE_NAME = "user_database";

    public synchronized static RoomDB getInstance(Context context) {
        if (roomDB == null) {
            roomDB = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return roomDB;
    }

    public abstract UserDao userDao();

}
