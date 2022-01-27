package com.example.collegeproject.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity implements View.OnClickListener {

    TextView main_register_text;
    ImageView header_image_register;
    Spinner register_spinner_semester, register_degree_spinner;
    public static TextInputEditText register_name, register_email, register_contact, register_address, register_area, register_location, register_dob, register_password, register_college_name;
    TextInputLayout register_name_layout, register_email_layout, register_contact_layout, register_address_layout, register_area_layout, register_location_layout, register_dob_layout, register_password_layout, register_college_name_layout;
    List<String> semester = new ArrayList<String>();
    List<String> degree = new ArrayList<String>();
    private int mYear, mMonth, mDay;
    DatePickerDialog datePickerDialog;
    MKLoader register_loader;
    Random random;
    Button signup;
    ImageView olduser;
    public static Activity registerActivity;
    ArrayList<Registrationdatum> arrayList;
    SharedPreferences sharedPreferences;
    public static String register_Name, register_Email, register_Contact, register_Address, register_Area, register_Location, register_DOB, register_Password, register_College_Name, register_Semester, register_College_Degree;
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
    float v = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeUI();
        setSpinner();
        animation();
        register_spinner_semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                register_Semester = semester.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        register_degree_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                register_College_Degree = degree.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        register_dob_layout.setOnClickListener(this);
        olduser.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    public void initializeUI() {
        random = new Random();
        register_loader = findViewById(R.id.register_loader);
        register_loader.setVisibility(View.INVISIBLE);
        olduser = findViewById(R.id.olduser);
        register_spinner_semester = findViewById(R.id.register_select_semester);
        register_degree_spinner = findViewById(R.id.register_select_degree);
        register_dob_layout = findViewById(R.id.register_dateofbirth_layout);
        signup = findViewById(R.id.signupbtn);
        main_register_text = findViewById(R.id.main_register_text);
        register_area = findViewById(R.id.register_area);
        register_location = findViewById(R.id.register_city);
        register_name = findViewById(R.id.register_name);
        register_email = findViewById(R.id.register_emailid);
        register_dob = findViewById(R.id.register_dateofbirth);
        register_contact = findViewById(R.id.register_contact);
        register_address = findViewById(R.id.register_address);
        register_password = findViewById(R.id.register_password);
        register_college_name = findViewById(R.id.register_college_name);
        register_area_layout = findViewById(R.id.register_area_layout);
        register_location_layout = findViewById(R.id.register_city_layout);
        register_name_layout = findViewById(R.id.register_name_layout);
        register_email_layout = findViewById(R.id.register_emailid_layout);
        register_dob_layout = findViewById(R.id.register_dateofbirth_layout);
        register_contact_layout = findViewById(R.id.register_contact_layout);
        register_address_layout = findViewById(R.id.register_address_layout);
        register_password_layout = findViewById(R.id.register_password_layout);
        register_college_name_layout = findViewById(R.id.register_college_name_layout);
        header_image_register = findViewById(R.id.header_image_register);
        registerActivity = this;
    }

    public void setSpinner() {
        semester.add("Select Semester");
        semester.add("1");
        semester.add("2");
        semester.add("3");
        semester.add("4");
        semester.add("5");
        semester.add("6");
        semester.add("7");
        semester.add("8");
        semester.add("Passed Out");

        degree.add("Select Degree");
        degree.add("B.Tech/B.E.(CE)");
        degree.add("B.Tech/B.E.(CS)");
        degree.add("B.Tech/B.E.(IT)");
        degree.add("M.Tech/M.E.(CE)");
        degree.add("M.Tech/M.E.(CS)");
        degree.add("M.Tech/M.E.(IT)");
        degree.add("BCA");
        degree.add("MCA");
        degree.add("Bsc. IT");
        degree.add("Msc. IT");

        ArrayAdapter sem = new ArrayAdapter(this, android.R.layout.simple_spinner_item, semester);
        ArrayAdapter degree_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, degree);
        sem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        degree_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        register_spinner_semester.setAdapter(sem);
        register_degree_spinner.setAdapter(degree_adapter);
    }

    public void animation() {
        register_name_layout.setTranslationX(400);
        register_email_layout.setTranslationX(400);
        register_contact_layout.setTranslationX(400);
        register_address_layout.setTranslationX(400);
        register_area_layout.setTranslationX(400);
        register_location_layout.setTranslationX(400);
        register_dob_layout.setTranslationX(400);
        register_password_layout.setTranslationX(400);
        register_college_name_layout.setTranslationX(400);
        olduser.setTranslationX(400);
        signup.setTranslationX(400);
        main_register_text.setTranslationX(400);
        header_image_register.setTranslationY(400);

        register_name_layout.setAlpha(v);
        register_email_layout.setAlpha(v);
        register_contact_layout.setAlpha(v);
        register_address_layout.setAlpha(v);
        register_area_layout.setAlpha(v);
        register_location_layout.setAlpha(v);
        register_dob_layout.setAlpha(v);
        register_password_layout.setAlpha(v);
        register_college_name_layout.setAlpha(v);
        olduser.setAlpha(v);
        signup.setAlpha(v);
        header_image_register.setAlpha(v);
        main_register_text.setAlpha(v);


        main_register_text.animate().translationX(0).alpha(1).setDuration(400).setStartDelay(100).start();
        register_name_layout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(200).start();
        register_email_layout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(200).start();
        register_contact_layout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(200).start();
        register_address_layout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(200).start();
        register_area_layout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(200).start();
        register_location_layout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(200).start();
        register_dob_layout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(200).start();
        register_password_layout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(200).start();
        register_college_name_layout.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(200).start();
        olduser.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(350).start();
        signup.animate().translationX(0).alpha(1).setDuration(500).setStartDelay(400).start();
        header_image_register.animate().translationY(0).alpha(1).setDuration(500).setStartDelay(400).start();

    }

    @Override
    public void onClick(View view) {
        if (view == olduser) {
            startActivity(new Intent(Register.this, Login.class));
            overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
            finish();
        }
        if (view == register_dob_layout) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                    register_dob.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                }
            }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (view == signup) {
            register_Name = register_name.getText().toString();
            register_Email = register_email.getText().toString();
            register_Contact = register_contact.getText().toString();
            register_Address = register_address.getText().toString();
            register_DOB = register_dob.getText().toString();
            register_Password = register_password.getText().toString();
            register_College_Name = register_college_name.getText().toString();
            register_Area = register_area.getText().toString();
            register_Location = register_location.getText().toString();
            load_from_email();
        }
    }


    public void check_details() {
        if (register_Name.equals("") || register_Email.equals("") || register_College_Degree.equals("Select Degree") || register_Semester.equals("Select Semester") || register_Location.equals("") || register_Area.equals("") || register_Location.equals("") || register_Contact.equals("") || register_DOB.equals("") || register_Password.equals("") || register_College_Name.equals("")) {
            register_loader.setVisibility(View.INVISIBLE);
            if (register_Name.equals("")) {
                register_name.setError("Enter Name");
            } else {
                register_name.setError(null);
            }
            if (register_Email.equals("")) {
                register_email.setError("Enter Email");
            } else {
                register_email.setError(null);
            }
            if (register_College_Degree.equals("Select Degree")) {
                Toast.makeText(Register.this, "Please Select Valid Degree", Toast.LENGTH_SHORT).show();
            }
            if (register_Semester.equals("Select Degree")) {
                Toast.makeText(Register.this, "Please Select Valid Semester", Toast.LENGTH_SHORT).show();
            }
            if (register_Contact.equals("")) {
                register_contact.setError("Enter Contact");
            } else {
                register_contact.setError(null);
            }
            if (register_Address.equals("")) {
                register_address.setError("Enter Contact");
            } else {
                register_address.setError(null);
            }
            if (register_DOB.equals("")) {
                register_dob.setError("Enter Date of Birth");
            } else {
                register_dob.setError(null);
            }
            if (register_Password.equals("")) {
                register_password.setError("Enter Password");
            } else {
                register_password.setError(null);
            }
            if (register_College_Name.equals("")) {
                register_college_name.setError("Enter College Name");
            } else {
                register_college_name.setError(null);
            }
            if (register_Area.equals("")) {
                register_area.setError("Enter Area");
            } else {
                register_area.setError(null);
            }
            if (register_Location.equals("")) {
                register_location.setError("Enter Location");
            } else {
                register_location.setError(null);
            }
        } else {
            sharedPref();
            setdata();
            Toast.makeText(Register.this, "" + register_Email, Toast.LENGTH_SHORT).show();
            try {
                register_loader.setVisibility(View.INVISIBLE);
                startActivity(new Intent(Register.this, OTPView.class));
            } catch (Exception e) {
                Log.e("MAILERROR", String.valueOf(e));
            }
        }
    }

    public void load_from_email() {
        register_loader.setVisibility(View.VISIBLE);
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> registerCall = apiInterface.user_show();
        registerCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                try {
                    arrayList = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                    int len = arrayList.size();
                    int i;
                    int temp_check_email = 0;
                    int temp_check_contact = 0;
                    for (i = 0; i < len; i++) {
                        if (arrayList.get(i).getUserEmail().equals(register_Email)) {
                            temp_check_email++;
                        }
                    }
                    for (i = 0; i < len; i++) {
                        if (arrayList.get(i).getUserContact().equals(register_Contact)) {
                            temp_check_contact++;
                        }
                    }
                    if (temp_check_email != 0 || temp_check_contact != 0) {
                        if (temp_check_email != 0) {
                            Toast.makeText(Register.this, "Email Address already taken", Toast.LENGTH_SHORT).show();
                            register_loader.setVisibility(View.INVISIBLE);
                        }
                        if (temp_check_contact != 0) {
                            Toast.makeText(Register.this, "Phone Number already taken", Toast.LENGTH_SHORT).show();
                            register_loader.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        check_details();
                    }
                } catch (Exception e) {
                    Log.e("ERROR", String.valueOf(e));
                    register_loader.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                register_loader.setVisibility(View.INVISIBLE);
                Log.e("Error_Failed", String.valueOf(t));
                Toast.makeText(Register.this, "Failed Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setdata() {
        sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
        register_Name = sharedPreferences.getString(REGISTER_NAME, "");
        register_Email = sharedPreferences.getString(REGISTER_EMAIL, "");
        register_Contact = sharedPreferences.getString(REGISTER_CONTACT, "");
        register_Address = sharedPreferences.getString(REGISTER_ADDRESS, "");
        register_Area = sharedPreferences.getString(REGISTER_AREA, "");
        register_Location = sharedPreferences.getString(REGISTER_LOCATION, "");
        register_DOB = sharedPreferences.getString(REGISTER_DOB, "");
        register_Password = sharedPreferences.getString(REGISTER_PASSWORD, "");
        register_College_Name = sharedPreferences.getString(REGISTER_COLLEGE_NAME, "");
        register_name.setText(register_Name);
        register_email.setText(register_Email);
        register_contact.setText(register_Contact);
        register_address.setText(register_Address);
        register_area.setText(register_Area);
        register_location.setText(register_Location);
        register_dob.setText(register_DOB);
        register_password.setText(register_Password);
        register_college_name.setText(register_College_Name);
    }

    public void sharedPref() {
        sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(REGISTER_EMAIL, register_Email);
        editor.putString(REGISTER_NAME, register_Name);
        editor.putString(REGISTER_CONTACT, register_Contact);
        editor.putString(REGISTER_ADDRESS, register_Address);
        editor.putString(REGISTER_AREA, register_Area);
        editor.putString(REGISTER_LOCATION, register_Location);
        editor.putString(REGISTER_DOB, register_DOB);
        editor.putString(REGISTER_PASSWORD, register_Password);
        editor.putString(REGISTER_COLLEGE_NAME, register_College_Name);
        editor.putString(REGISTER_COLLEGE_SEMESTER, register_Semester);
        editor.putString(REGISTER_COLLEGE_DEGREE, register_College_Degree);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Register.this, Login.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

}