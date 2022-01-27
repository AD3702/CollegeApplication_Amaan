package com.example.collegeproject.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
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

public class Security extends AppCompatActivity implements View.OnClickListener {


    CardView change_password_card, security_card;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";
    public static final String SECURITY_SWITCH = "security_switch";
    ArrayList<Registrationdatum> registrationdatumArrayList;
    TextInputLayout input_layout_old_password, input_layout_new_password, input_layout_confirm_password;
    TextView textView_old_password, textView_new_password, forgot_password;
    Button submit_old_password, submit_new_password;
    MKLoader mkLoader;
    Toolbar toolbar_change_password;
    String user_id, old_password_string, old_password_string_server, new_password_string, confirm_password_string;
    TextInputEditText old_password, new_password, confirm_password;
    boolean security_enabled;
    Switch aSwitch_security;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initializeUI();
        setuptoolbar();
        sharedPref();
        security_switch_set();
        aSwitch_security.setChecked(security_enabled);
        submit_old_password.setOnClickListener(this);
        submit_new_password.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
        change_password_card.setOnClickListener(this);
        aSwitch_security.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    security_enabled = true;
                    security_switch();
                } else {
                    security_enabled = false;
                    security_switch();
                }
            }
        });
    }

    public void security_switch() {
        sharedPreferences = Security.this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SECURITY_SWITCH, security_enabled);
        editor.commit();
    }

    public void security_switch_set() {
        sharedPreferences = Security.this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        security_enabled = sharedPreferences.getBoolean(SECURITY_SWITCH, false);
    }

    public void sharedPref() {
        sharedPreferences = Security.this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString(USER_ID, "");
    }

    public void initializeUI() {
        forgot_password = findViewById(R.id.forgot_password_change);
        submit_old_password = findViewById(R.id.submit_old_password);
        submit_new_password = findViewById(R.id.submit_new_password);
        textView_new_password = findViewById(R.id.textView3);
        old_password = findViewById(R.id.change_password_old);
        new_password = findViewById(R.id.change_password_new);
        confirm_password = findViewById(R.id.change_password_confirm);
        input_layout_new_password = findViewById(R.id.change_password_new_layout);
        input_layout_confirm_password = findViewById(R.id.change_password_confirm_layout);
        mkLoader = findViewById(R.id.change_password_loader);
        toolbar_change_password = findViewById(R.id.toolbar_actionbar_change_password);
        change_password_card = findViewById(R.id.main_change_password);
        security_card = findViewById(R.id.main_enable_security);
        textView_old_password = findViewById(R.id.textView2);
        input_layout_old_password = findViewById(R.id.change_password_old_layout);
        aSwitch_security = findViewById(R.id.security_enable_switch);
        mkLoader.setVisibility(View.INVISIBLE);
    }

    public void loadData() {
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> applicationDataCall = apiInterface.find_user_id(user_id);
        applicationDataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                try {
                    mkLoader.setVisibility(View.INVISIBLE);
                    registrationdatumArrayList = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                    old_password_string_server = registrationdatumArrayList.get(0).getUserPassword();
                    if (old_password_string_server.equals(old_password_string)) {
                        submit_old_password.setVisibility(View.INVISIBLE);
                        forgot_password.setVisibility(View.INVISIBLE);
                        submit_new_password.setVisibility(View.VISIBLE);
                        textView_new_password.setVisibility(View.VISIBLE);
                        input_layout_new_password.setVisibility(View.VISIBLE);
                        input_layout_confirm_password.setVisibility(View.VISIBLE);
                        old_password.setError(null);
                    } else {
                        old_password.setError("Password is incorrect");
                    }
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {

            }
        });
    }

    public void setuptoolbar() {
        toolbar_change_password.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.main_color)));
        toolbar_change_password.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar_change_password.setTitle("Change Password");
    }

    public void change_password() {
        mkLoader.setVisibility(View.VISIBLE);
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> applicationDataCall = apiInterface.reset_password(user_id, new_password_string);
        applicationDataCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                try {
                    mkLoader.setVisibility(View.INVISIBLE);
                    registrationdatumArrayList = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                    AwesomeSuccessDialog awesomeSuccessDialog = new AwesomeSuccessDialog(Security.this);
                    awesomeSuccessDialog.setTitle("Reset Successful")
                            .setMessage("Password Changed Successfully")
                            .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                            .setCancelable(false)
                            .setPositiveButtonText("OK")
                            .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                            .setPositiveButtonTextColor(R.color.white)
                            .setPositiveButtonClick(new Closure() {
                                @Override
                                public void exec() {
                                    finish();
                                }
                            }).show();
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Toast.makeText(Security.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void check_password() {
        old_password_string = old_password.getText().toString();
        loadData();
    }

    @Override
    public void onClick(View view) {
        if (view == submit_old_password) {
            mkLoader.setVisibility(View.VISIBLE);
            check_password();
        }
        if (view == submit_new_password) {
            new_password_string = new_password.getText().toString();
            confirm_password_string = confirm_password.getText().toString();
            if (new_password_string.equals(confirm_password_string)) {
                mkLoader.setVisibility(View.VISIBLE);
                change_password();
            } else {
                Toast.makeText(Security.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            }
        }
        if (view == forgot_password) {
            startActivity(new Intent(Security.this, ForgotPassword.class));
        }
        if (view == change_password_card) {
            security_card.setVisibility(View.INVISIBLE);
            textView_old_password.setVisibility(View.VISIBLE);
            forgot_password.setVisibility(View.VISIBLE);
            input_layout_old_password.setVisibility(View.VISIBLE);
            old_password.setVisibility(View.VISIBLE);
            submit_old_password.setVisibility(View.VISIBLE);
        }
    }
}