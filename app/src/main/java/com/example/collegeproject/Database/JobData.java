
package com.example.collegeproject.Database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JobData {

    @SerializedName("jobdata")
    @Expose
    private List<Jobdatum> jobdata = null;

    public List<Jobdatum> getJobdata() {
        return jobdata;
    }

    public void setJobdata(List<Jobdatum> jobdata) {
        this.jobdata = jobdata;
    }

}
