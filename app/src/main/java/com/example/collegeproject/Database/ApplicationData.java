
package com.example.collegeproject.Database;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ApplicationData {

    @SerializedName("applicationdata")
    @Expose
    private List<com.example.collegeproject.Database.Applicationdatum> applicationdata = null;

    public List<com.example.collegeproject.Database.Applicationdatum> getApplicationdata() {
        return applicationdata;
    }

    public void setApplicationdata(List<com.example.collegeproject.Database.Applicationdatum> applicationdata) {
        this.applicationdata = applicationdata;
    }

}
