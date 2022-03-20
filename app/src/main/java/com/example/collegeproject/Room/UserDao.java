package com.example.collegeproject.Room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert()
    void insert_user_data(Offline_User_Data... offline_user_data);

    @Delete
    void delete(Offline_User_Data offline_user_data);

    @Delete
    void reset(List<Offline_User_Data> offline_user_data);

    @Query("SELECT * FROM user_details")
    List<Offline_User_Data> getlistData();

    /*@Query("update user_details set user_name = :user_name,user_email = :user_email,user_contact = :user_contact ,user_date_of_birth= :user_date_of_birth ,user_address = :user_address ,user_area = :user_area ,user_location = :user_location ,user_english_speaking= :user_english_speaking  where user_id=:user_id ")
    int update_edit_prof(int user_id, String user_name, String user_email, String user_contact, String user_date_of_birth, String user_address, String user_area, String user_location, String user_english_speaking);*/
    @Update
    void update_edit_prof(Offline_User_Data offline_user_data);

}
