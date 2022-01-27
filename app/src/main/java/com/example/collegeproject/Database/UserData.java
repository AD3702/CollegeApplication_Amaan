
package com.example.collegeproject.Database;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

    @SerializedName("registrationdata")
    @Expose
    private List<Registrationdatum> registrationdata = null;

    public List<Registrationdatum> getRegistrationdata() {
        return registrationdata;
    }

    public void setRegistrationdata(List<Registrationdatum> registrationdata) {
        this.registrationdata = registrationdata;
    }

}
