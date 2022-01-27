
package com.example.collegeproject.Database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Jobdatum {

    @SerializedName("company_id")
    @Expose
    private String companyId;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("company_email")
    @Expose
    private String companyEmail;
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
    @SerializedName("company_contact_number")
    @Expose
    private String company_contact_number;

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

    public String getCompany_contact_number() {
        return company_contact_number;
    }

    public void setCompany_contact_number(String company_contact_number) {
        this.company_contact_number = company_contact_number;
    }
}
