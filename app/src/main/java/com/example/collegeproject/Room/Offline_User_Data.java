package com.example.collegeproject.Activity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user_details")

public class Offline_User_Data implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int user_id;

    @ColumnInfo(name = "user_name")
    private String user_name;

    @ColumnInfo(name = "user_email")
    private String user_email;

    @ColumnInfo(name = "user_email")
    private String user_contact;

    @ColumnInfo(name = "user_email")
    private String user_address;

    @ColumnInfo(name = "user_email")
    private String user_area;

    @ColumnInfo(name = "user_email")
    private String user_location;

    @ColumnInfo(name = "user_email")
    private String user_english_speaking;

    @ColumnInfo(name = "user_email")
    private String user_date_of_birth;

    @ColumnInfo(name = "user_email")
    private String user_password;

    @ColumnInfo(name = "user_email")
    private String user_college_name;

    @ColumnInfo(name = "user_email")
    private String user_semester;

    @ColumnInfo(name = "user_email")
    private String user_college_degree;

    @ColumnInfo(name = "user_email")
    private String user_card_type;

    @ColumnInfo(name = "user_email")
    private String user_profile_image;

    @ColumnInfo(name = "user_email")
    private String user_resume_pdf;

    @ColumnInfo(name = "user_email")
    private String user_college_id_card;

    @ColumnInfo(name = "user_email")
    private String user_view_job_type;

    @ColumnInfo(name = "user_email")
    private String user_verification_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_contact() {
        return user_contact;
    }

    public void setUser_contact(String user_contact) {
        this.user_contact = user_contact;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_area() {
        return user_area;
    }

    public void setUser_area(String user_area) {
        this.user_area = user_area;
    }

    public String getUser_location() {
        return user_location;
    }

    public void setUser_location(String user_location) {
        this.user_location = user_location;
    }

    public String getUser_english_speaking() {
        return user_english_speaking;
    }

    public void setUser_english_speaking(String user_english_speaking) {
        this.user_english_speaking = user_english_speaking;
    }

    public String getUser_date_of_birth() {
        return user_date_of_birth;
    }

    public void setUser_date_of_birth(String user_date_of_birth) {
        this.user_date_of_birth = user_date_of_birth;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

    public String getUser_college_name() {
        return user_college_name;
    }

    public void setUser_college_name(String user_college_name) {
        this.user_college_name = user_college_name;
    }

    public String getUser_semester() {
        return user_semester;
    }

    public void setUser_semester(String user_semester) {
        this.user_semester = user_semester;
    }

    public String getUser_college_degree() {
        return user_college_degree;
    }

    public void setUser_college_degree(String user_college_degree) {
        this.user_college_degree = user_college_degree;
    }

    public String getUser_card_type() {
        return user_card_type;
    }

    public void setUser_card_type(String user_card_type) {
        this.user_card_type = user_card_type;
    }

    public String getUser_profile_image() {
        return user_profile_image;
    }

    public void setUser_profile_image(String user_profile_image) {
        this.user_profile_image = user_profile_image;
    }

    public String getUser_resume_pdf() {
        return user_resume_pdf;
    }

    public void setUser_resume_pdf(String user_resume_pdf) {
        this.user_resume_pdf = user_resume_pdf;
    }

    public String getUser_college_id_card() {
        return user_college_id_card;
    }

    public void setUser_college_id_card(String user_college_id_card) {
        this.user_college_id_card = user_college_id_card;
    }

    public String getUser_view_job_type() {
        return user_view_job_type;
    }

    public void setUser_view_job_type(String user_view_job_type) {
        this.user_view_job_type = user_view_job_type;
    }

    public String getUser_verification_id() {
        return user_verification_id;
    }

    public void setUser_verification_id(String user_verification_id) {
        this.user_verification_id = user_verification_id;
    }
}
