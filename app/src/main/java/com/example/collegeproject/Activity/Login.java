package com.example.collegeproject.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.Registrationdatum;
import com.example.collegeproject.Database.UserData;
import com.example.collegeproject.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements View.OnClickListener {

    TextView forgot_pass;
    ImageView newuser;
    MKLoader login_loader;
    Button login_page_btn;
    ArrayList<Registrationdatum> loginRegistrationdata;
    public static Activity loginActivity;
    TextInputEditText login_email, login_password;
    LinearLayout main_layout;
    TextInputLayout email_layout, password_layout;
    String loginemailstring, loginpasswordstring, user_id, user_name, user_education_1, user_education_2, user_profile_photo_check, user_resume_check;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";
    public static final String LOGGED_IN = "log_in";
    ImageView header_image;
    String verify_id, isloggedin;
    TextView main_login_text, sub_text;
    float v = 0;
    boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeUI();
        animations();
//        MainActivity.mainActivity.finish();
        forgot_pass.setOnClickListener(this);
        newuser.setOnClickListener(this);
        login_page_btn.setOnClickListener(this);

    }

    public void connectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            Toast.makeText(Login.this, "Not Connected to the Internet", Toast.LENGTH_SHORT).show();
            connected = false;
        }
    }

    public void initializeUI() {
        login_loader = findViewById(R.id.login_loader);
        login_loader.setVisibility(View.INVISIBLE);
        newuser = findViewById(R.id.newuser);
        forgot_pass = findViewById(R.id.forgot_password);
        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_page_btn = findViewById(R.id.login_page_btn);
        email_layout = findViewById(R.id.login_email_layout);
        password_layout = findViewById(R.id.login_password_layout);
        main_layout = findViewById(R.id.main_lin_layout);
        main_login_text = findViewById(R.id.main_login_text);
        sub_text = findViewById(R.id.sub_text);
        header_image = findViewById(R.id.header_image);
        loginActivity = this;
    }

    private void animations() {

        email_layout.setTranslationX(400);
        password_layout.setTranslationX(400);
        forgot_pass.setTranslationX(400);
        newuser.setTranslationX(400);
        login_page_btn.setTranslationX(400);
        main_login_text.setTranslationX(400);
        sub_text.setTranslationX(400);
//        header_image.setTranslationY(400);

        email_layout.setAlpha(v);
        password_layout.setAlpha(v);
        forgot_pass.setAlpha(v);
        newuser.setAlpha(v);
        login_page_btn.setAlpha(v);
//        header_image.setAlpha(v);
        main_login_text.setAlpha(v);
        sub_text.setAlpha(v);

        sub_text.animate().translationX(0).alpha(1).setDuration(400).setStartDelay(100).start();
        main_login_text.animate().translationX(0).alpha(1).setDuration(400).setStartDelay(100).start();
        email_layout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(200).start();
        password_layout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(250).start();
        forgot_pass.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(350).start();
        newuser.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(350).start();
        login_page_btn.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(400).start();
//        header_image.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(400).start();

    }

    @Override
    public void onClick(View view) {
        if (view == newuser) {
            Intent intent = new Intent(Login.this, Register.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
            finish();
        }
        if (view == login_page_btn) {
            connectivity();
            if (connected == true) {
                onclicklogin();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    startActivity(new Intent(android.provider.Settings.Panel.ACTION_INTERNET_CONNECTIVITY));
                }
//                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        }
        if (view == forgot_pass) {
            startActivity(new Intent(Login.this, ForgotPassword.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
        }
    }

    private void onclicklogin() {
        loginemailstring = login_email.getText().toString();
        loginpasswordstring = login_password.getText().toString();
        if (loginpasswordstring.equals("") || loginemailstring.equals("")) {
            if (loginemailstring.equals("")) {
                Toast.makeText(Login.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            }
            if (loginpasswordstring.equals("")) {
                Toast.makeText(Login.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            }
        } else {
            login_loader.setVisibility(View.VISIBLE);
            logindata();
        }
    }

    private void logindata() {

        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> userDataCall = apiInterface.login(loginemailstring, loginpasswordstring);
        userDataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                try {

                    loginRegistrationdata = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                    user_id = loginRegistrationdata.get(0).getUserId();
                    user_name = loginRegistrationdata.get(0).getUserName();
                    user_education_1 = loginRegistrationdata.get(0).getUserCollegeDegree();
                    user_education_2 = loginRegistrationdata.get(0).getUserSemester();
                    user_profile_photo_check = loginRegistrationdata.get(0).getUserProfileImage();
                    user_resume_check = loginRegistrationdata.get(0).getUserResumePdf();
                    verify_id = loginRegistrationdata.get(0).getUserVerificationId();
                    successonlogin();
                    sharedpref();

                } catch (Exception e) {
                    Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", String.valueOf(e));
                    login_loader.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {

            }
        });

    }

    private void successonlogin() {

        if (user_profile_photo_check.equals("")) {
            Intent main_intent = new Intent(Login.this, ImageAddActivity.class);
            MainActivity.mainActivity.finish();
            isloggedin = "upload_image";
            sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LOGGED_IN, isloggedin);
            editor.commit();
//            Toast.makeText(Login.this, "" + user_id, Toast.LENGTH_SHORT).show();
            startActivity(main_intent);
            finish();
        } else {
            if (user_resume_check.equals("")) {
                Intent main_intent = new Intent(Login.this, UploadResume.class);
                MainActivity.mainActivity.finish();
                isloggedin = "upload_resume";
                sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(LOGGED_IN, isloggedin);
                editor.commit();
//                Toast.makeText(Login.this, "" + user_id, Toast.LENGTH_SHORT).show();
                startActivity(main_intent);
                finish();
            } else {
                if (verify_id.equals("1")) {
                    Intent main_intent = new Intent(Login.this, MasterActivity.class);
                    MainActivity.mainActivity.finish();
                    isloggedin = "logged_in";
                    sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(LOGGED_IN, isloggedin);
                    editor.commit();
//                    Toast.makeText(Login.this, "" + user_id, Toast.LENGTH_SHORT).show();
                    startActivity(main_intent);
                    finish();
                } /*else {
                    login_loader.setVisibility(View.INVISIBLE);
                    Intent main_intent = new Intent(Login.this, Verification.class);
                    Toast.makeText(Login.this, "" + user_id, Toast.LENGTH_SHORT).show();
                    startActivity(main_intent);
                }*/
            }
        }
    }

    public void sharedpref() {
        sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_ID, user_id);
        editor.putString("resume_check", user_resume_check);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainActivity.mainActivity.finish();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}