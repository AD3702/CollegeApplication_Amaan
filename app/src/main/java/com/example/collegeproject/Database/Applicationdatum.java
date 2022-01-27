
package com.example.collegeproject.Database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Applicationdatum {

    @SerializedName("company_id")
    @Expose
    private String companyId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("company_email")
    @Expose
    private String companyEmail;
    @SerializedName("company_contact_number")
    @Expose
    private String companyContactNumber;
    @SerializedName("company_founding_date")
    @Expose
    private String companyFoundingDate;
    @SerializedName("company_password")
    @Expose
    private String companyPassword;
    @SerializedName("company_address")
    @Expose
    private String companyAddress;
    @SerializedName("company_area")
    @Expose
    private String companyArea;
    @SerializedName("company_location")
    @Expose
    private String companyLocation;
    @SerializedName("company_website")
    @Expose
    private String companyWebsite;
    @SerializedName("job_id")
    @Expose
    private String jobId;
    @SerializedName("company_job_id")
    @Expose
    private String companyJobId;
    @SerializedName("job_post")
    @Expose
    private String jobPost;
    @SerializedName("job_salary")
    @Expose
    private String jobSalary;
    @SerializedName("job_minimum_education")
    @Expose
    private String jobMinimumEducation;
    @SerializedName("job_english_speaking_level")
    @Expose
    private String jobEnglishSpeakingLevel;
    @SerializedName("job_experience")
    @Expose
    private String jobExperience;
    @SerializedName("job_description")
    @Expose
    private String jobDescription;
    @SerializedName("job_timings")
    @Expose
    private String jobTimings;
    @SerializedName("job_working_days")
    @Expose
    private String jobWorkingDays;
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
    @SerializedName("user_verification_id")
    @Expose
    private String userVerificationId;
    @SerializedName("apply_id")
    @Expose
    private String applyId;
    @SerializedName("apply_company_id")
    @Expose
    private String applyCompanyId;
    @SerializedName("apply_user_id")
    @Expose
    private String applyUserId;
    @SerializedName("apply_job_id")
    @Expose
    private String applyJobId;
    @SerializedName("application_date")
    @Expose
    private String applicationDate;
    @SerializedName("application_after_response_id")
    @Expose
    private String applicationAfterResponseId;
    @SerializedName("application_response_id")
    @Expose
    private String applicationResponseId;

    @SerializedName("job_type")
    @Expose
    private String jobType;
    @SerializedName("job_vaccancy_id")
    @Expose
    private String jobVaccancyId;
    @SerializedName("user_view_job_type")
    @Expose
    private String userViewJobType;
    @SerializedName("total_applications_for_job")
    @Expose
    private String totalApplicationForJob;
    @SerializedName("total_job_accepted")
    @Expose
    private String totalJobAccepted;
    @SerializedName("total_job_rejected")
    @Expose
    private String totalJobRejected;
    @SerializedName("company_logo")
    @Expose
    private String companyLogo;

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getjobVaccancyId() {
        return jobVaccancyId;
    }

    public void setjobVaccancyId(String jobActiveInactiveId) {
        this.jobVaccancyId = jobActiveInactiveId;
    }

    public String getApplicationResponseId() {
        return applicationResponseId;
    }

    public void setApplicationResponseId(String applicationResponseId) {
        this.applicationResponseId = applicationResponseId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyContactNumber() {
        return companyContactNumber;
    }

    public void setCompanyContactNumber(String companyContactNumber) {
        this.companyContactNumber = companyContactNumber;
    }

    public String getCompanyFoundingDate() {
        return companyFoundingDate;
    }

    public void setCompanyFoundingDate(String companyFoundingDate) {
        this.companyFoundingDate = companyFoundingDate;
    }

    public String getCompanyPassword() {
        return companyPassword;
    }

    public void setCompanyPassword(String companyPassword) {
        this.companyPassword = companyPassword;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCompanyArea() {
        return companyArea;
    }

    public void setCompanyArea(String companyArea) {
        this.companyArea = companyArea;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    public String getCompanyWebsite() {
        return companyWebsite;
    }

    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCompanyJobId() {
        return companyJobId;
    }

    public void setCompanyJobId(String companyJobId) {
        this.companyJobId = companyJobId;
    }

    public String getJobPost() {
        return jobPost;
    }

    public void setJobPost(String jobPost) {
        this.jobPost = jobPost;
    }

    public String getJobSalary() {
        return jobSalary;
    }

    public void setJobSalary(String jobSalary) {
        this.jobSalary = jobSalary;
    }

    public String getJobMinimumEducation() {
        return jobMinimumEducation;
    }

    public void setJobMinimumEducation(String jobMinimumEducation) {
        this.jobMinimumEducation = jobMinimumEducation;
    }

    public String getJobEnglishSpeakingLevel() {
        return jobEnglishSpeakingLevel;
    }

    public void setJobEnglishSpeakingLevel(String jobEnglishSpeakingLevel) {
        this.jobEnglishSpeakingLevel = jobEnglishSpeakingLevel;
    }

    public String getJobExperience() {
        return jobExperience;
    }

    public void setJobExperience(String jobExperience) {
        this.jobExperience = jobExperience;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getJobTimings() {
        return jobTimings;
    }

    public void setJobTimings(String jobTimings) {
        this.jobTimings = jobTimings;
    }

    public String getJobWorkingDays() {
        return jobWorkingDays;
    }

    public void setJobWorkingDays(String jobWorkingDays) {
        this.jobWorkingDays = jobWorkingDays;
    }

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

    public String getUserVerificationId() {
        return userVerificationId;
    }

    public void setUserVerificationId(String userVerificationId) {
        this.userVerificationId = userVerificationId;
    }

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getApplyCompanyId() {
        return applyCompanyId;
    }

    public void setApplyCompanyId(String applyCompanyId) {
        this.applyCompanyId = applyCompanyId;
    }

    public String getApplyUserId() {
        return applyUserId;
    }

    public void setApplyUserId(String applyUserId) {
        this.applyUserId = applyUserId;
    }

    public String getApplyJobId() {
        return applyJobId;
    }

    public void setApplyJobId(String applyJobId) {
        this.applyJobId = applyJobId;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(String applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getApplicationAfterResponseId() {
        return applicationAfterResponseId;
    }

    public void setApplicationAfterResponseId(String applicationAfterResponseId) {
        this.applicationAfterResponseId = applicationAfterResponseId;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobVaccancyId() {
        return jobVaccancyId;
    }

    public void setJobVaccancyId(String jobVaccancyId) {
        this.jobVaccancyId = jobVaccancyId;
    }

    public String getUserViewJobType() {
        return userViewJobType;
    }

    public void setUserViewJobType(String userViewJobType) {
        this.userViewJobType = userViewJobType;
    }

    public String getTotalApplicationForJob() {
        return totalApplicationForJob;
    }

    public void setTotalApplicationForJob(String totalApplicationForJob) {
        this.totalApplicationForJob = totalApplicationForJob;
    }

    public String getTotalJobAccepted() {
        return totalJobAccepted;
    }

    public void setTotalJobAccepted(String totalJobAccepted) {
        this.totalJobAccepted = totalJobAccepted;
    }

    public String getTotalJobRejected() {
        return totalJobRejected;
    }

    public void setTotalJobRejected(String totalJobRejected) {
        this.totalJobRejected = totalJobRejected;
    }
}
