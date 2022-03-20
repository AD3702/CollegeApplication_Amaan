package com.example.collegeproject.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.Registrationdatum;
import com.example.collegeproject.Database.UserData;
import com.example.collegeproject.Mail.SendMailContactUs;
import com.example.collegeproject.R;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUs extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar_contact_us;
    TextView whatsapp_intent, gmail_intent;
    EditText editText_contact_us_message;
    Button button_submit_contact_us;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";
    String user_id, user_name, user_contact, user_email;
    MKLoader mkLoader_contact_us;
    ArrayList<Registrationdatum> registrationdatumArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        initializeUI();
        toolbar_setup();
        sharedpref();
        whatsapp_intent.setOnClickListener(this);
        gmail_intent.setOnClickListener(this);
        button_submit_contact_us.setOnClickListener(this);
        mkLoader_contact_us.setVisibility(View.INVISIBLE);
    }

    public void initializeUI() {
        toolbar_contact_us = findViewById(R.id.toolbar_actionbar_contact_us);
        whatsapp_intent = findViewById(R.id.contact_us_on_whatsapp_no);
        gmail_intent = findViewById(R.id.contact_us_on_email_id);
        editText_contact_us_message = findViewById(R.id.contact_us_message);
        button_submit_contact_us = findViewById(R.id.submit_contact_us);
        mkLoader_contact_us = findViewById(R.id.contact_us_loader);
    }

    public void toolbar_setup() {
        toolbar_contact_us.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.main_color)));
        toolbar_contact_us.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar_contact_us.setTitle("Contact Us");
    }

    @Override
    public void onClick(View view) {
        if (view == whatsapp_intent) {
            try {

                Uri mUri = Uri.parse("https://api.whatsapp.com/send?phone=+917572877843&text=Hi I would like to talk to Mr.Amaan Dhanerawala");
                Intent intent = new Intent("android.intent.action.VIEW", mUri);
                intent.setPackage("com.whatsapp");
                startActivity(intent);

            } catch (Exception e) {
                Toast.makeText(this, "WhatsApp not Installed", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        if (view == gmail_intent) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", gmail_intent.getText().toString(), null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my subject text");
            startActivity(Intent.createChooser(emailIntent, null));
        }
        if (view == button_submit_contact_us) {
            String message = editText_contact_us_message.getText().toString();
            if (message.equals("")) {
                editText_contact_us_message.setError("Please Enter Message");
            } else {
                get_user_details(message);
            }
        }
    }

    public void get_user_details(String message) {
        mkLoader_contact_us.setVisibility(View.VISIBLE);
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> registrationCall = apiInterface.find_user_id(user_id);
        registrationCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                try {
                    registrationdatumArrayList = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                    user_name = registrationdatumArrayList.get(0).getUserName();
                    user_email = registrationdatumArrayList.get(0).getUserEmail();
                    user_contact = registrationdatumArrayList.get(0).getUserContact();
                    String submit_message = "User " + user_name + " has query\nHis inquiry is: " + message +
                            "\nTo contact him his email address is: " + user_email + "\nHis contact number is: " + user_contact;
                    SendMailContactUs sendMailContactUs = new SendMailContactUs(ContactUs.this, gmail_intent.getText().toString(), "User Concern", submit_message, mkLoader_contact_us, editText_contact_us_message);
                    sendMailContactUs.execute();
                } catch (Exception e) {
                    mkLoader_contact_us.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Toast.makeText(ContactUs.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sharedpref() {
        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString(USER_ID, "");
    }

}