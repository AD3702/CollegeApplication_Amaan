package com.example.collegeproject.Activity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.ApplicationData;
import com.example.collegeproject.Database.JobData;
import com.example.collegeproject.Database.Jobdatum;
import com.example.collegeproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
    ImageView company_card_image_view;
    String check_apply;
    LinearLayout company_location_company_card;
    TextView company_name, job_post, salary, minimum_education, english_level, job_experience, job_description, job_timings, job_working_days, company_address, company_contact_number, company_email, company_website;
    String company_name_string, job_post_string, user_id, salary_string, minimum_education_string, english_level_string, job_experience_string, job_description_string, job_timings_string, job_working_days_string, company_address_string, company_contact_number_string, company_email_string, company_website_string, com_id_string, job_id_string, company_latitude, company_longitude;
    private String company_image_string;
    Dialog mDialog;
    ImageView full_screen_image;
    CardView view_image_card_view;

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
        company_card_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog = new Dialog(CompanyCard.this, R.style.Theme_With_Action_Bar);
                mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mDialog.setContentView(R.layout.dialog_fullscreen);
                full_screen_image = mDialog.findViewById(R.id.full_screen_image);
                view_image_card_view = mDialog.findViewById(R.id.view_image_cardview);
                FloatingActionButton save_image = mDialog.findViewById(R.id.save_image_gallery);
                SwipeListener swipeListener = new SwipeListener(view_image_card_view);
                save_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bitmap bitmap;
                        if (company_card_image_view.getDrawable() instanceof BitmapDrawable) {
                            bitmap = ((BitmapDrawable) company_card_image_view.getDrawable()).getBitmap();
                        } else {
                            Drawable d = company_card_image_view.getDrawable();
                            bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(bitmap);
                            d.draw(canvas);
                        }
                        saveImage(bitmap);
                    }
                });
                mDialog.setCancelable(true);
                try {
                    Picasso.get().load(company_image_string).into(full_screen_image);
                    mDialog.show();
                } catch (Exception e) {
                    Picasso.get().load(company_image_string).centerCrop().resize(350, 350).into(full_screen_image);
                }
            }
        });
    }

    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(200);
        view.startAnimation(animate);
    }

    private class SwipeListener implements View.OnTouchListener {

        GestureDetector gestureDetector;

        SwipeListener(View view) {
            int SWIPE_DISTANCE_THRESHOLD = 250;
            int SWIPE_VELOCITY_THRESHOLD = 250;

            GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    boolean result = false;
                    try {
                        float diffY = e2.getY() - e1.getY();
                        float diffX = e2.getX() - e1.getX();
                        if (Math.abs(diffX) > Math.abs(diffY)) {
                            if (Math.abs(diffX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                                if (diffX > 0) {
//                                    Toast.makeText(MainActivity.this, "Right", Toast.LENGTH_SHORT).show();
                                } else {
                                    //Left
//                                    Toast.makeText(MainActivity.this, "Left", Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            }
                        } else if (Math.abs(diffY) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffY > 0) {
                                //Down
//                                Toast.makeText(ListView.this, "Down", Toast.LENGTH_SHORT).show();
                                slideDown(view_image_card_view);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mDialog.dismiss();
                                    }
                                }, 200);

                            }
//                                textInputLayout.setTranslationY(200);
//                                textInputLayout.setAlpha(0);
//                                textInputLayout.animate().translationY(0).alpha(1).setDuration(200).setStartDelay(0).start();
                        } else {
//                                CreateTaskBotttomSheetFragment createTaskBotttomSheetFragment = new CreateTaskBotttomSheetFragment();
//                                createTaskBotttomSheetFragment.show(getSupportFragmentManager(), createTaskBotttomSheetFragment.getTag());
                        }
                        return true;
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return false;
                }
            };
            gestureDetector = new GestureDetector(simpleOnGestureListener);
            view.setOnTouchListener(this);
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return gestureDetector.onTouchEvent(motionEvent);
        }
    }

    private ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        }
        return values;
    }

    private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
                Toast.makeText(CompanyCard.this, "Image Saved", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveImage(Bitmap bitmap) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            ContentValues values = contentValues();
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + getString(R.string.app_name));
            values.put(MediaStore.Images.Media.IS_PENDING, true);

            Uri uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try {
                    saveImageToStream(bitmap, CompanyCard.this.getContentResolver().openOutputStream(uri));
                    values.put(MediaStore.Images.Media.IS_PENDING, false);
                    this.getContentResolver().update(uri, values, null, null);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + '/' + getString(R.string.app_name));

            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".png";
            File file = new File(directory, fileName);
            try {
                saveImageToStream(bitmap, new FileOutputStream(file));
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    public void initializeUI() {
        company_card_loader = findViewById(R.id.company_card_loader);
        main_applied_for_btn = findViewById(R.id.main_page_appliedbtn);
        main_apply_for_btn = findViewById(R.id.main_page_applybtn);
        company_name = findViewById(R.id.company_name);
        job_post = findViewById(R.id.job_post);
        salary = findViewById(R.id.job_salary);
        company_card_image_view = findViewById(R.id.company_card_image_view);
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
        Picasso.get().load(company_image_string).centerCrop().resize(96, 96).into(company_card_image_view);
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
                    company_image_string = jobdatumArrayList.get(0).getCompanyLogo();
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
                    Log.e("ERROR_LOG", String.valueOf(e));
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