
package com.example.collegeproject.Database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Registrationdatum {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_contact")
    @Expose
    private String userContact;
    @SerializedName("user_address")
    @Expose
    private String userAddress;
    @SerializedName("user_area")
    @Expose
    private String userArea;
    @SerializedName("user_location")
    @Expose
    private String userLocation;
    @SerializedName("user_english_speaking")
    @Expose
    private String userEnglishSpeaking;
    @SerializedName("user_date_of_birth")
    @Expose
    private String userDateOfBirth;
    @SerializedName("user_password")
    @Expose
    private String userPassword;
    @SerializedName("user_college_name")
    @Expose
    private String userCollegeName;
    @SerializedName("user_semester")
    @Expose
    private String userSemester;
    @SerializedName("user_college_degree")
    @Expose
    private String userCollegeDegree;
    @SerializedName("user_card_type")
    @Expose
    private String userCardType;
    @SerializedName("user_profile_image")
    @Expose
    private String userProfileImage;
    @SerializedName("user_resume_pdf")
    @Expose
    private String userResumePdf;
    @SerializedName("user_college_id_card")
    @Expose
    private Object userCollegeIdCard;
    @SerializedName("user_view_job_type")
    @Expose
    private String userViewJobType;
    @SerializedName("user_verification_id")
    @Expose
    private String userVerificationId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserArea() {
        return userArea;
    }

    public void setUserArea(String userArea) {
        this.userArea = userArea;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserEnglishSpeaking() {
        return userEnglishSpeaking;
    }

    public void setUserEnglishSpeaking(String userEnglishSpeaking) {
        this.userEnglishSpeaking = userEnglishSpeaking;
    }

    public String getUserDateOfBirth() {
        return userDateOfBirth;
    }

    public void setUserDateOfBirth(String userDateOfBirth) {
        this.userDateOfBirth = userDateOfBirth;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserCollegeName() {
        return userCollegeName;
    }

    public void setUserCollegeName(String userCollegeName) {
        this.userCollegeName = userCollegeName;
    }

    public String getUserSemester() {
        return userSemester;
    }

    public void setUserSemester(String userSemester) {
        this.userSemester = userSemester;
    }

    public String getUserCollegeDegree() {
        return userCollegeDegree;
    }

    public void setUserCollegeDegree(String userCollegeDegree) {
        this.userCollegeDegree = userCollegeDegree;
    }

    public String getUserCardType() {
        return userCardType;
    }

    public void setUserCardType(String userCardType) {
        this.userCardType = userCardType;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public String getUserResumePdf() {
        return userResumePdf;
    }

    public void setUserResumePdf(String userResumePdf) {
        this.userResumePdf = userResumePdf;
    }

    public Object getUserCollegeIdCard() {
        return userCollegeIdCard;
    }

    public void setUserCollegeIdCard(Object userCollegeIdCard) {
        this.userCollegeIdCard = userCollegeIdCard;
    }

    public String getUserViewJobType() {
        return userViewJobType;
    }

    public void setUserViewJobType(String userViewJobType) {
        this.userViewJobType = userViewJobType;
    }

    public String getUserVerificationId() {
        return userVerificationId;
    }

    public void setUserVerificationId(String userVerificationId) {
        this.userVerificationId = userVerificationId;
    }

}
