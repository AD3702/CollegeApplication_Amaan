package com.example.collegeproject.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.ApplicationData;
import com.example.collegeproject.Database.JobData;
import com.example.collegeproject.Database.Jobdatum;
import com.example.collegeproject.R;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyCard extends AppCompatActivity implements View.OnClickListener {

    AwesomeSuccessDialog awesomeSuccessDialog;
    AwesomeWarningDialog awesomeWarningDialog;
    Button main_apply_for_btn, main_applied_for_btn;
    MKLoader company_card_loader;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "long";
    ArrayList<Jobdatum> jobdatumArrayList;
    String check_apply;
    LinearLayout company_location_company_card;
    TextView company_name, job_post, salary, minimum_education, english_level, job_experience, job_description, job_timings, job_working_days, company_address, company_contact_number, company_email, company_website;
    String company_name_string, job_post_string, user_id, salary_string, minimum_education_string, english_level_string, job_experience_string, job_description_string, job_timings_string, job_working_days_string, company_address_string, company_contact_number_string, company_email_string, company_website_string, com_id_string, job_id_string, company_latitude, company_longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_card);
        initializeUI();
        sharedpref();
        com_id_string = getIntent().getStringExtra("company_id");
        job_id_string = getIntent().getStringExtra("job_id");
        check_apply = getIntent().getStringExtra("check_apply_applied");
        if (check_apply.equals("applied")) {
            main_apply_for_btn.setVisibility(View.INVISIBLE);
            main_applied_for_btn.setVisibility(View.VISIBLE);
        } else {
            main_apply_for_btn.setVisibility(View.VISIBLE);
            main_applied_for_btn.setVisibility(View.INVISIBLE);
        }
        load_data();
        main_apply_for_btn.setOnClickListener(this);
        main_applied_for_btn.setOnClickListener(this);
        company_location_company_card.setOnClickListener(this);
    }


    public void initializeUI() {
        company_card_loader = findViewById(R.id.company_card_loader);
        main_applied_for_btn = findViewById(R.id.main_page_appliedbtn);
        main_apply_for_btn = findViewById(R.id.main_page_applybtn);
        company_name = findViewById(R.id.company_name);
        job_post = findViewById(R.id.job_post);
        salary = findViewById(R.id.job_salary);
        minimum_education = findViewById(R.id.job_minimum_education);
        english_level = findViewById(R.id.job_english_level);
        job_experience = findViewById(R.id.job_experience);
        job_description = findViewById(R.id.job_description);
        job_timings = findViewById(R.id.job_timings);
        job_working_days = findViewById(R.id.job_workings_days);
        company_address = findViewById(R.id.company_address);
        company_contact_number = findViewById(R.id.company_contact);
        company_email = findViewById(R.id.company_email);
        company_website = findViewById(R.id.company_website);
        company_location_company_card = findViewById(R.id.company_location_company_card);
    }

    public void set_details() {
        company_name.setText(company_name_string);
        job_post.setText(job_post_string);
        salary.setText(salary_string);
        minimum_education.setText(minimum_education_string);
        english_level.setText(english_level_string);
        job_experience.setText(job_experience_string);
        job_description.setText(job_description_string);
        job_timings.setText(job_timings_string);
        job_working_days.setText(job_working_days_string);
        company_address.setText(company_address_string);
        company_contact_number.setText(company_contact_number_string);
        company_email.setText(company_email_string);
        company_website.setText(company_website_string);
    }

    public void load_data() {
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<JobData> jobDataCall = apiInterface.getjobdetails(com_id_string, job_id_string);
        jobDataCall.enqueue(new Callback<JobData>() {
            @Override
            public void onResponse(Call<JobData> call, Response<JobData> response) {
                try {
                    jobdatumArrayList = (ArrayList<Jobdatum>) response.body().getJobdata();
                    company_name_string = jobdatumArrayList.get(0).getCompanyName();
                    job_post_string = jobdatumArrayList.get(0).getJobPost();
                    salary_string = jobdatumArrayList.get(0).getJobSalary();
                    minimum_education_string = jobdatumArrayList.get(0).getJobMinimumEducation();
                    english_level_string = jobdatumArrayList.get(0).getJobEnglishSpeakingLevel();
                    job_experience_string = jobdatumArrayList.get(0).getJobExperience();
                    job_description_string = jobdatumArrayList.get(0).getJobDescription();
                    job_timings_string = jobdatumArrayList.get(0).getJobTimings();
                    job_working_days_string = jobdatumArrayList.get(0).getJobWorkingDays();
                    company_address_string = jobdatumArrayList.get(0).getCompanyAddress();
                    company_email_string = jobdatumArrayList.get(0).getCompanyEmail();
                    company_contact_number_string = jobdatumArrayList.get(0).getCompanyContactNumber();
                    company_website_string = jobdatumArrayList.get(0).getCompanyWebsite();
                    company_latitude = jobdatumArrayList.get(0).getCompanyLat();
                    company_longitude = jobdatumArrayList.get(0).getCompanyLong();
                    sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(LATITUDE, company_latitude);
                    editor.putString(LONGITUDE, company_longitude);
                    editor.commit();
                    set_details();
                    company_card_loader.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    Toast.makeText(CompanyCard.this, "Error", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JobData> call, Throwable t) {
                Toast.makeText(CompanyCard.this, "Error_1", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void displaySuccessAlertDialog() {
        try {
            awesomeSuccessDialog = new AwesomeSuccessDialog(this);
            awesomeSuccessDialog.setTitle("Confirmation")
                    .setMessage("Are you sure you want to apply?")
                    .setColoredCircle(R.color.main_color)
                    .setDialogIconAndColor(R.drawable.ic_dialog_info, R.color.white)
                    .setCancelable(true)
                    .setPositiveButtonText("YES")
                    .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                    .setPositiveButtonTextColor(R.color.white)
                    .setNegativeButtonText("No")
                    .setNegativeButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                    .setNegativeButtonTextColor(R.color.white)
                    .setPositiveButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            apply_for_job_data();
                            main_apply_for_btn.setVisibility(View.INVISIBLE);
                            main_applied_for_btn.setVisibility(View.VISIBLE);
                        }
                    })
                    .setNegativeButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            //click
                        }
                    })
                    .show();
        } catch (Exception e) {

        }
    }

    public void displayErrorAlertDialog() {
        try {
            awesomeWarningDialog = new AwesomeWarningDialog(this);
            awesomeWarningDialog.setTitle("Error")
                    .setMessage("You cannot cancel your application once you have applied for a job")
                    .setColoredCircle(R.color.main_color)
                    .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                    .setCancelable(true)
                    .setButtonText("OK")
                    .setButtonBackgroundColor(R.color.red);
            awesomeWarningDialog.setWarningButtonClick(new Closure() {
                @Override
                public void exec() {

                }
            })
                    .show();
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View view) {
        if (view == main_apply_for_btn) {
            displaySuccessAlertDialog();
        }
        if (view == main_applied_for_btn) {
            displayErrorAlertDialog();
        }
        if (view == company_location_company_card) {
            startActivity(new Intent(CompanyCard.this, MapsActivity.class));
        }
    }

    public String get_date() {
        int mYear, mMonth, mDay;
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mMonth = mMonth + 1;
        String curr_date = mYear + "-" + (mMonth) + "-" + mDay;
        return curr_date;
    }

    public void apply_for_job_data() {
        company_card_loader.setVisibility(View.VISIBLE);
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        String current_date = get_date();
        Call<ApplicationData> applicationDataCall = apiInterface.apply_for_job(com_id_string, user_id, job_id_string, current_date);
        applicationDataCall.enqueue(new Callback<ApplicationData>() {
            @Override
            public void onResponse(Call<ApplicationData> call, Response<ApplicationData> response) {
                company_card_loader.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ApplicationData> call, Throwable t) {

            }
        });
    }

    public void sharedpref() {
        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString(USER_ID, "");
    }
}