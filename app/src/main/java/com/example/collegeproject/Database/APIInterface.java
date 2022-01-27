package com.example.collegeproject.Database;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {

    @FormUrlEncoded
    @POST("jobs/login.php")
    Call<UserData> login(
            @Field("user_email") String email,
            @Field("user_password") String password
    );

    @FormUrlEncoded
    @POST("jobs/update_profile.php")
    Call<UserData> update_profile(
            @Field("user_name") String name,
            @Field("user_email") String email,
            @Field("user_contact") String contact,
            @Field("user_address") String address,
            @Field("user_area") String area,
            @Field("user_location") String location,
            @Field("user_date_of_birth") String date_of_birth,
            @Field("user_english_speaking") String english_speaking,
            @Field("user_id") String id
    );

    @FormUrlEncoded
    @POST("jobs/update_card.php")
    Call<UserData> update_card(
            @Field("user_name") String name,
            @Field("user_area") String area,
            @Field("user_location") String location,
            @Field("user_card_type") int card_type,
            @Field("user_id") String id
    );

    @FormUrlEncoded
    @POST("jobs/display_job_by_id.php")
    Call<JobData> getjobdetails(
            @Field("company_id") String company_id,
            @Field("job_id") String job_id
    );

    @GET("jobs/user_show.php")
    Call<UserData> user_show(
    );

    @FormUrlEncoded
    @POST("jobs/register.php")
    Call<UserData> insert(
            @Field("user_name") String name,
            @Field("user_email") String email,
            @Field("user_contact") String contact,
            @Field("user_address") String address,
            @Field("user_area") String area,
            @Field("user_location") String location,
            @Field("user_date_of_birth") String date_of_birth,
            @Field("user_password") String password,
            @Field("user_college_name") String college_name,
            @Field("user_semester") String semester,
            @Field("user_college_degree") String college_degree
    );

    @FormUrlEncoded
    @POST("jobs/find_email.php")
    Call<UserData> find_email(
            @Field("user_email") String user_email
    );

    @FormUrlEncoded
    @POST("jobs/reset_password.php")
    Call<UserData> reset_password(
            @Field("user_id") String user_id,
            @Field("user_password") String user_password
    );

    @Multipart
    @POST("jobs/upload_photo.php")
    Call<ServerResponse> uploadFile(@Part MultipartBody.Part file,
                                    @Part("user_id") RequestBody id);

    @Multipart
    @POST("jobs/upload_resume.php")
    Call<ServerResponse> uploadResumePDF(@Part MultipartBody.Part file,
                                         @Part("user_id") RequestBody id);

    @FormUrlEncoded
    @POST("jobs/find_user_id.php")
    Call<UserData> find_user_id(
            @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("jobs/applied_job_details.php")
    Call<ApplicationData> applied_user(
            @Field("user_id") String id
    );

    @FormUrlEncoded
    @POST("jobs/apply_for_job.php")
    Call<ApplicationData> apply_for_job(
            @Field("apply_company_id") String company_id,
            @Field("apply_user_id") String user_id,
            @Field("apply_job_id") String job_id,
            @Field("application_date") String application_date
    );

    @FormUrlEncoded
    @POST("jobs/display_job_details.php")
    Call<ApplicationData> job_show(
            @Field("user_id") String id
    );

    @FormUrlEncoded
    @POST("jobs/total_application_user.php")
    Call<ApplicationData> total_applied_job_show(
            @Field("user_id") String id
    );

    @FormUrlEncoded
    @POST("jobs/load_job_view_type.php")
    Call<ApplicationData> load_view_job_type(
            @Field("user_id") String id
    );

    @FormUrlEncoded
    @POST("jobs/update_job_type_user.php")
    Call<ApplicationData> update_view_job_type(
            @Field("user_id") String id,
            @Field("user_view_job_type") String user_view_job_type
    );

    @FormUrlEncoded
    @POST("jobs/display_job_details_by_filter.php")
    Call<ApplicationData> display_job_details_by_filter(
            @Field("user_id") String id,
            @Field("job_type") String job_type
    );

}
