package com.example.collegeproject.Database;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppClient {
    public static final String URLDATA = "https://amaandhanerawala.000webhostapp.com/";

    public static Retrofit getclient() {
        return new Retrofit.Builder()
                .baseUrl(URLDATA)
                .addConverterFactory(GsonConverterFactory.create()).build();
    }
}
