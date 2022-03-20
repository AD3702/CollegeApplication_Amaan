package com.example.collegeproject.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.Registrationdatum;
import com.example.collegeproject.Database.UserData;
import com.example.collegeproject.Mail.SendMailForgot;
import com.example.collegeproject.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.tuyenmonkey.mkloader.MKLoader;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener, OTPListener {

    TextInputEditText forgot_email;
    TextInputLayout forgot_email_layout;
    MKLoader mkLoader_forgot;
    Button submit_email_forgot, submit_otp_forgot;
    OtpTextView otpView_forgot;
    LinearLayout linearLayout_otp, resend_otp_layout;
    Random random_forgot;
    int random_num;
    String user_id_forgot;
    String random_forgot_otp;
    ArrayList<Registrationdatum> arrayList;
    private boolean confirmation = false;
    TextView resend_otp, resend_otp_timer;
    private static int OTP_TIME_OUT = 120000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initializeUI();
        Random_number();
        submit_email_forgot.setOnClickListener(this);
        submit_otp_forgot.setOnClickListener(this);
        otpView_forgot.setOtpListener(this);
        resend_otp.setOnClickListener(this);
    }

    public void initializeUI() {
        forgot_email = findViewById(R.id.forgot_password_email);
        forgot_email_layout = findViewById(R.id.forgot_password_email_layout);
        mkLoader_forgot = findViewById(R.id.forgot_loader);
        mkLoader_forgot.setVisibility(View.INVISIBLE);
        submit_email_forgot = findViewById(R.id.forgot_password_page_btn);
        submit_otp_forgot = findViewById(R.id.forgot_password_otpsubmitbtn);
        otpView_forgot = findViewById(R.id.forgot_password_otp);
        linearLayout_otp = findViewById(R.id.linear_otp_layout);
        linearLayout_otp.setVisibility(View.INVISIBLE);
        resend_otp = findViewById(R.id.resend_otp);
        resend_otp_timer = findViewById(R.id.resend_otp_timer);
        resend_otp_layout = findViewById(R.id.resend_otp_layout);
        confirmation = false;
    }

    public void Random_number() {
        random_forgot = new Random();
        random_num = random_forgot.nextInt(999999 - 100000) + 100000;
        random_forgot_otp = String.valueOf(random_num);
    }

    @Override
    public void onClick(View v) {
        if (v == submit_email_forgot) {
            setTimer();
            load_from_email();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Random_number();
                }
            }, OTP_TIME_OUT);
        }

        if (v == resend_otp) {
            load_from_email();
            setTimer();
            resend_otp.setText("Resend OTP in:");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Random_number();
                }
            }, OTP_TIME_OUT);
        }

        if (v == submit_otp_forgot) {
            if (confirmation == true) {
                Intent intent_forgot = new Intent(ForgotPassword.this, ResetPassword.class);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
                intent_forgot.putExtra("id", user_id_forgot);
                startActivity(intent_forgot);
                finish();
            } else {
                otpView_forgot.showError();
            }
        }
    }

    public void setTimer() {
        new CountDownTimer(120000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                resend_otp_timer.setText(f.format(min) + ":" + f.format(sec));
            }

            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                resend_otp_timer.setText("");
                resend_otp.setText("Resend OTP");

            }
        }.start();
    }

    public void load_from_email() {
        mkLoader_forgot.setVisibility(View.VISIBLE);
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> registerCall = apiInterface.find_email(forgot_email.getText().toString());
        registerCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                try {
                    arrayList = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                    user_id_forgot = arrayList.get(0).getUserId();
//                    Toast.makeText(ForgotPassword.this, "" + random_forgot_otp, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(ForgotPassword.this, "" + forgot_email.getText().toString(), Toast.LENGTH_SHORT).show();
                    SendMailForgot sendMail = new SendMailForgot(ForgotPassword.this, forgot_email.getText().toString(), "Reset Password", "Reset password request has been sent for you account.\nIf its not you kindly ignore this email.\nTo reset password your OTP is: " + random_forgot_otp + "\nThis OTP will be valid till 2 minutes ", mkLoader_forgot, linearLayout_otp, resend_otp_layout);
                    sendMail.execute();
                    submit_email_forgot.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    mkLoader_forgot.setVisibility(View.INVISIBLE);
                    Toast.makeText(ForgotPassword.this, "Enter Correct Email ID", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.e("Error", String.valueOf(t));
                mkLoader_forgot.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onInteractionListener() {

    }

    @Override
    public void onOTPComplete(String otp) {
        if (otp.equals(random_forgot_otp)) {
            confirmation = true;
        } else {
            confirmation = false;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        /*Intent intent = new Intent(ForgotPassword.this, Login.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
        startActivity(intent);*/
        finish();
    }
}