package com.example.collegeproject.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserImageDao {

    @Insert
    void insert_image(ImagesData... imagesData);

    @Query("SELECT * FROM ImagesData")
    List<ImagesData> getAllImage();

    @Delete
    void reset(List<ImagesData> imagesDataList);

}
