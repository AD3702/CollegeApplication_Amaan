package com.example.collegeproject.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeWarningDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.Registrationdatum;
import com.example.collegeproject.Database.UserData;
import com.example.collegeproject.Mail.SendMail;
import com.example.collegeproject.R;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.Random;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPView extends AppCompatActivity implements OTPListener, View.OnClickListener {

    public Button registerotpsubmitbtn;
    ArrayList<Registrationdatum> registrationdatumArrayList;
    public static String register_Name, register_Email, register_Contact, register_Address, register_Area, register_Location, register_DOB, register_Password, register_College_Name, register_Semester, register_College_Degree;
    TextView regisiteredemailtext;
    String randomOTPString;
    OtpTextView otpTextView;
    MKLoader mkLoader_otp;
    String temp_id;
    AwesomeWarningDialog awesomeWarningDialog;
    private boolean confirmation;
    SharedPreferences sharedPreferences;
    Random random_forgot;
    int random_num;
    String random_otp;
    public static final String mypreference = "mypref";
    public static final String REGISTER_NAME = "register_name";
    public static final String REGISTER_EMAIL = "register_email";
    public static final String REGISTER_CONTACT = "register_contact";
    public static final String REGISTER_ADDRESS = "register_address";
    public static final String REGISTER_AREA = "register_area";
    public static final String REGISTER_LOCATION = "register_location";
    public static final String REGISTER_DOB = "register_dob";
    public static final String REGISTER_PASSWORD = "register_password";
    public static final String REGISTER_COLLEGE_NAME = "register_college_name";
    public static final String REGISTER_COLLEGE_SEMESTER = "register_college_semester";
    public static final String REGISTER_COLLEGE_DEGREE = "register_college_degree";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpview);
        initializeUI();
        Random_number();
        sharedPref();
        try {
            SendMail sendMail = new SendMail(OTPView.this, register_Email, "Confirmation", "New Registration request has been sent for you account.\nIf its not you kindly ignore this email.\nTo register your OTP is: " + random_otp, mkLoader_otp);
            sendMail.execute();
        } catch (Exception e) {

        }
        otpTextView.setOtpListener(this);
        registerotpsubmitbtn.setOnClickListener(this);
    }

    public void initializeUI() {
        mkLoader_otp = findViewById(R.id.otp_loader);
        registrationdatumArrayList = new ArrayList<>();
        registerotpsubmitbtn = findViewById(R.id.registerotpsubmitbtn);
        otpTextView = findViewById(R.id.registerconfirmotp);
        regisiteredemailtext = findViewById(R.id.otpview_emailtv);
        confirmation = false;
    }

    public void Register_database() {

        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> registerCall = apiInterface.insert(register_Name, register_Email, register_Contact, register_Address, register_Area, register_Location, register_DOB, register_Password, register_College_Name, register_Semester, register_College_Degree);
        registerCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                registrationdatumArrayList = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                temp_id = registrationdatumArrayList.get(0).getUserId();
                if (registrationdatumArrayList == null) {
                } else {
                    displayAlertDialog();
                    mkLoader_otp.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.e("Error", String.valueOf(t));
                mkLoader_otp.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void displayAlertDialog() {
        try {
            awesomeWarningDialog = new AwesomeWarningDialog(this);
            awesomeWarningDialog.setTitle("Attention")
                    .setMessage("By registering on our application you have agreed to our terms and conditions. Also uploading fake resumes and any unusual activity on your account can lead to permanent account ban.")
                    .setColoredCircle(R.color.main_color)
                    .setDialogIconAndColor(R.drawable.ic_dialog_warning, R.color.white)
                    .setCancelable(true)
                    .setButtonText(getString(R.string.dialog_ok_button))
                    .setButtonBackgroundColor(R.color.red)
                    .setButtonText(getString(R.string.dialog_ok_button));
            awesomeWarningDialog.setWarningButtonClick(new Closure() {
                @Override
                public void exec() {
                    Register.registerActivity.finish();
                    Intent otpintent = new Intent(OTPView.this, Login.class);
                    startActivity(otpintent);
                    finish();
                }
            })
                    .show();
        } catch (Exception e) {

        }
    }

    public void sharedPref() {
        sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
        register_Email = sharedPreferences.getString(REGISTER_EMAIL, "");
        regisiteredemailtext.setText(register_Email);
        register_Name = sharedPreferences.getString(REGISTER_NAME, "");
        register_Contact = sharedPreferences.getString(REGISTER_CONTACT, "");
        register_Address = sharedPreferences.getString(REGISTER_ADDRESS, "");
        register_Area = sharedPreferences.getString(REGISTER_AREA, "");
        register_Location = sharedPreferences.getString(REGISTER_LOCATION, "");
        register_DOB = sharedPreferences.getString(REGISTER_DOB, "");
        register_Password = sharedPreferences.getString(REGISTER_PASSWORD, "");
        register_College_Name = sharedPreferences.getString(REGISTER_COLLEGE_NAME, "");
        register_College_Degree = sharedPreferences.getString(REGISTER_COLLEGE_DEGREE, "");
        register_Semester = sharedPreferences.getString(REGISTER_COLLEGE_SEMESTER, "");
    }

    public void Random_number() {
        random_forgot = new Random();
        random_num = random_forgot.nextInt(999999 - 100000) + 100000;
        random_otp = String.valueOf(random_num);
    }

    @Override
    public void onInteractionListener() {

    }

    @Override
    public void onOTPComplete(String otp) {
        if (otp.equals(random_otp)) {
            confirmation = true;
        } else {
            confirmation = false;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == registerotpsubmitbtn) {
            if (confirmation == true) {
                Register_database();
                /*Intent intent_register = new Intent(OTPView.this, Login.class);
                startActivity(intent_register);
                finish();*/
            } else {
                otpTextView.showError();
            }
        }
    }
}