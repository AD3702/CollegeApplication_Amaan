package com.example.collegeproject.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.Registrationdatum;
import com.example.collegeproject.Database.UserData;
import com.example.collegeproject.R;
import com.google.android.material.textfield.TextInputEditText;
import com.lib.customedittext.CustomEditText;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPassword extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText new_password, confirm_password;
    Button reset_password;
    String new_Password;
    MKLoader reset_loader;
    ArrayList<Registrationdatum> arrayList;
    String user_id_reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initializeUI();
        user_id_reset = getIntent().getStringExtra("id");
        reset_password.setOnClickListener(this);
    }

    public void initializeUI() {
        reset_loader = findViewById(R.id.reset_loader);
        reset_loader.setVisibility(View.INVISIBLE);
        new_password = findViewById(R.id.reset_password_new);
        confirm_password = findViewById(R.id.reset_password_confirm);
        reset_password = findViewById(R.id.reset_password_page_btn);
    }

    @Override
    public void onClick(View v) {
        if (v == reset_password) {
            check_password();
        }
    }

    public void check_password() {
        if (new_password.getText().toString().equals(confirm_password.getText().toString())) {
            new_Password = new_password.getText().toString();
            reset_pasword_func();
        } else {
            Toast.makeText(ResetPassword.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
        }
    }

    public void reset_pasword_func() {

        reset_loader.setVisibility(View.VISIBLE);
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> registerCall = apiInterface.reset_password(user_id_reset, new_Password);
        registerCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                try {
                    arrayList = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                    reset_loader.setVisibility(View.INVISIBLE);
                    AwesomeSuccessDialog awesomeSuccessDialog = new AwesomeSuccessDialog(ResetPassword.this);
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
                                    startActivity(new Intent(ResetPassword.this, Login.class));
                                    finish();
                                }
                            }).show();

                } catch (Exception e) {
                    Log.e("Error", String.valueOf(e));
                    reset_loader.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Log.e("Error", String.valueOf(t));
                reset_loader.setVisibility(View.INVISIBLE);
            }
        });
    }

}