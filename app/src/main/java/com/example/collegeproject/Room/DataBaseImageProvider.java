package com.example.collegeproject.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {ImagesData.class}, version = 1)
@TypeConverters({ImageBitmapString.class})
public abstract class DataBaseImageProvider extends RoomDatabase {
    public abstract UserImageDao userImageDao();

    public static DataBaseImageProvider dataBaseImageProvider = null;

    public static DataBaseImageProvider getDbConnection(Context context) {
        if (dataBaseImageProvider == null) {
            dataBaseImageProvider = Room.databaseBuilder(context.getApplicationContext(), DataBaseImageProvider.class, "image_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return dataBaseImageProvider;
    }

}
