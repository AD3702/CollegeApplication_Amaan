package com.example.collegeproject.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.Registrationdatum;
import com.example.collegeproject.Database.UserData;
import com.example.collegeproject.R;
import com.example.collegeproject.Room.Offline_User_Data;
import com.example.collegeproject.Room.RoomDB;
import com.lib.customedittext.CustomEditText;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserCard extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar_edit_card;
    FrameLayout info_card, info_card_1, info_card_2;
    CircleImageView left_btn, right_btn, user_card_image, user_card_image1;
    String user_card_image_string;
    TextView card_name, card_name_1, card_name_2;
    TextView card_education, card_education_1, card_education_2;
    TextView card_area, card_area_1, card_area_2;
    TextView card_location, card_location_1, card_location_2;
    String user_name, user_education, user_area, user_location, edit_education_1, edit_education_2;
    ArrayList<Registrationdatum> editprofdatalist_online;
    String get_user_id;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";
    MKLoader card_loader;
    LinearLayout edit_card_btn, edit_card_details;
    Button edit_card_btn_save;
    CustomEditText card_username_edit, card_area_edit, card_location_edit;
    int card_type, card_type_temp;
    RoomDB roomDB_user_card;
    List<Offline_User_Data> editprofdatalist;
    Uri user_card_image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_card);
        initializeUI();
        toolbar_setup();
        Intent usercard_image_intent = getIntent();
        user_card_image_uri = usercard_image_intent.getData();
        sharedPref();
        load_edit_card_offline_date();
        left_btn.setOnClickListener(this);
        right_btn.setOnClickListener(this);
        edit_card_btn.setOnClickListener(this);
        edit_card_btn_save.setOnClickListener(this);
    }

    public void load_edit_card_offline_date() {
        try {
            roomDB_user_card = RoomDB.getInstance(this.getApplicationContext());
            editprofdatalist = roomDB_user_card.userDao().getlistData();
            user_name = editprofdatalist.get(0).getUser_name();
            edit_education_1 = editprofdatalist.get(0).getUser_college_degree();
            edit_education_2 = editprofdatalist.get(0).getUser_semester();
            user_area = editprofdatalist.get(0).getUser_area();
            user_location = editprofdatalist.get(0).getUser_location();
            card_type = editprofdatalist.get(0).getUser_card_type();
            user_card_image_string = editprofdatalist.get(0).getUser_profile_image();
            card_loader.setVisibility(View.INVISIBLE);
            setCard_type();
            setdetails_pre();

        } catch (Exception e) {
            Log.e("OFFLINE ERROR", String.valueOf(e));
        }
    }


    @SuppressLint("WrongConstant")
    public void sharedPref() {
        sharedPreferences = getSharedPreferences(mypreference, MODE_APPEND);
        get_user_id = sharedPreferences.getString(USER_ID, "");
    }

    public void initializeUI() {
        user_card_image = findViewById(R.id.user_card_image);
        user_card_image1 = findViewById(R.id.user_card_image_1);
        card_username_edit = findViewById(R.id.edit_card_name);
        card_area_edit = findViewById(R.id.edit_card_area);
        card_location_edit = findViewById(R.id.edit_card_location);
        edit_card_details = findViewById(R.id.edit_card_details);
        edit_card_btn = findViewById(R.id.edit_card_btn);
        edit_card_btn_save = findViewById(R.id.edit_card_btn_save);
        card_loader = findViewById(R.id.edit_card_loader);
        toolbar_edit_card = findViewById(R.id.toolbar_actionbar_editprofile);
        info_card = findViewById(R.id.info_card_frame);
        info_card_1 = findViewById(R.id.info_card_frame_1);
        left_btn = findViewById(R.id.left_card);
        right_btn = findViewById(R.id.right_card);
        left_btn.setVisibility(View.INVISIBLE);
        right_btn.setVisibility(View.INVISIBLE);
        info_card_2 = findViewById(R.id.info_card_frame_2);
        card_name = findViewById(R.id.card_name);
        card_name_1 = findViewById(R.id.card_name_1);
        card_name_2 = findViewById(R.id.card_name_2);
        card_education = findViewById(R.id.card_education);
        card_education_1 = findViewById(R.id.card_education_1);
        card_education_2 = findViewById(R.id.card_education_2);
        card_area = findViewById(R.id.card_area);
        card_area_1 = findViewById(R.id.card_area_1);
        card_area_2 = findViewById(R.id.card_area_2);
        card_location = findViewById(R.id.card_location);
        card_location_1 = findViewById(R.id.card_location_1);
        card_location_2 = findViewById(R.id.card_location_2);
    }

    public void toolbar_setup() {
        toolbar_edit_card.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.main_color)));
        toolbar_edit_card.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar_edit_card.setTitle("Edit Card");
    }

    @Override
    public void onClick(View view) {
        if (view == left_btn) {
            if (info_card_2.getVisibility() == View.VISIBLE) {
                info_card_1.setVisibility(View.VISIBLE);
                info_card_2.setVisibility(View.INVISIBLE);
            } else if (info_card_1.getVisibility() == View.VISIBLE) {
                info_card_1.setVisibility(View.INVISIBLE);
                info_card.setVisibility(View.VISIBLE);
            } else if (info_card.getVisibility() == View.VISIBLE) {
                info_card_2.setVisibility(View.VISIBLE);
                info_card.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(UserCard.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }
        if (view == right_btn) {
            if (info_card_2.getVisibility() == View.VISIBLE) {
                info_card_1.setVisibility(View.VISIBLE);
                info_card_2.setVisibility(View.INVISIBLE);
            } else if (info_card_1.getVisibility() == View.VISIBLE) {
                info_card_1.setVisibility(View.INVISIBLE);
                info_card.setVisibility(View.VISIBLE);
            } else if (info_card.getVisibility() == View.VISIBLE) {
                info_card_2.setVisibility(View.VISIBLE);
                info_card.setVisibility(View.INVISIBLE);
            } else {
                Toast.makeText(UserCard.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        }
        if (view == edit_card_btn) {
            edit_card_details.setVisibility(View.VISIBLE);
            edit_card_btn_save.setVisibility(View.VISIBLE);
            left_btn.setVisibility(View.VISIBLE);
            right_btn.setVisibility(View.VISIBLE);
        }
        if (view == edit_card_btn_save) {
            card_loader.setVisibility(View.VISIBLE);
            setdetailonsave();
            getdetails_post();
            edit_card_details.setVisibility(View.INVISIBLE);
            edit_card_btn_save.setVisibility(View.INVISIBLE);
            left_btn.setVisibility(View.INVISIBLE);
            right_btn.setVisibility(View.INVISIBLE);
        }
    }

//    public void loadData() {
//        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
//        Call<UserData> registrationCall = apiInterface.find_user_id(get_user_id);
//        registrationCall.enqueue(new Callback<UserData>() {
//            @Override
//            public void onResponse(Call<UserData> call, Response<UserData> response) {
//                try {
//                    editprofdatalist = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
//                    user_name = editprofdatalist.get(0).getUserName();
//                    edit_education_1 = editprofdatalist.get(0).getUserCollegeDegree();
//                    edit_education_2 = editprofdatalist.get(0).getUserSemester();
//                    user_area = editprofdatalist.get(0).getUserArea();
//                    user_location = editprofdatalist.get(0).getUserLocation();
//                    card_type = Integer.parseInt(editprofdatalist.get(0).getUserCardType());
//                    user_card_image_string = editprofdatalist.get(0).getUserProfileImage();
//                    setCard_type();
//                    setdetails_pre();
//
//                } catch (Exception e) {
//
//                }
//                card_loader.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onFailure(Call<UserData> call, Throwable t) {
//
//            }
//        });
//    }

    public void setdetails_pre() {
        card_name.setText(user_name);
        card_name_1.setText(user_name);
        card_name_2.setText(user_name);
        user_education = edit_education_1 + " ( " + edit_education_2 + " )";
        card_education.setText(user_education);
        card_education_1.setText(user_education);
        card_education_2.setText(user_education);
        card_area.setText(user_area);
        card_area_1.setText(user_area);
        card_area_2.setText(user_area);
        card_location.setText(user_location);
        card_location_1.setText(user_location);
        card_location_2.setText(user_location);
        card_username_edit.setText(user_name);
        card_area_edit.setText(user_area);
        card_location_edit.setText(user_location);
//        Toast.makeText(UserCard.this, "" + user_card_image_string, Toast.LENGTH_SHORT).show();
//        new DownLoadImageTask(user_card_image, card_loader_image).execute(user_card_image_string);
//        new DownLoadImageTask(user_card_image1, card_loader_image_1).execute(user_card_image_string);
        user_card_image.setImageURI(user_card_image_uri);
        user_card_image1.setImageURI(user_card_image_uri);
    }

    public void setData_edit() {
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> registrationCall = apiInterface.update_card(user_name, user_area, user_location, card_type_temp, get_user_id);
        registrationCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                try {
                    Offline_User_Data offline_user_data = editprofdatalist.get(0);
                    offline_user_data.setUser_name(user_name);
                    offline_user_data.setUser_area(user_area);
                    offline_user_data.setUser_location(user_location);
                    offline_user_data.setUser_card_type(card_type);
                    roomDB_user_card.userDao().update_edit_prof(offline_user_data);
                    Toast.makeText(UserCard.this, "" + editprofdatalist.get(0).getUser_address(), Toast.LENGTH_SHORT).show();
                    setdetails_post();

                } catch (Exception e) {

                }
                card_loader.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                Toast.makeText(UserCard.this, "Error", Toast.LENGTH_SHORT).show();
                card_loader.setVisibility(View.INVISIBLE);
                Log.e("Error", String.valueOf(t));
            }
        });
    }

    public void getdetails_post() {
        user_name = card_username_edit.getText().toString();
        user_area = card_area_edit.getText().toString();
        user_location = card_location_edit.getText().toString();
        card_type_temp = set_car_int_type();
        Toast.makeText(UserCard.this, "" + card_type_temp, Toast.LENGTH_SHORT).show();
        setData_edit();
    }

    public void setdetails_post() {
        card_name.setText(user_name);
        card_name_1.setText(user_name);
        card_name_2.setText(user_name);
        user_education = edit_education_1 + " ( " + edit_education_2 + " )";
        card_education.setText(user_education);
        card_education_1.setText(user_education);
        card_education_2.setText(user_education);
        card_area.setText(user_area);
        card_area_1.setText(user_area);
        card_area_2.setText(user_area);
        card_location.setText(user_location);
        card_location_1.setText(user_location);
        card_location_2.setText(user_location);
        card_username_edit.setText(user_name);
        card_area_edit.setText(user_area);
        card_username_edit.setText(user_name);
        card_location_edit.setText(user_location);
    }

    public void setCard_type() {
        if (card_type == 1) {
            info_card_2.setVisibility(View.VISIBLE);
            info_card_1.setVisibility(View.INVISIBLE);
            info_card.setVisibility(View.INVISIBLE);
        }
        if (card_type == 2) {
            info_card_1.setVisibility(View.VISIBLE);
            info_card_2.setVisibility(View.INVISIBLE);
            info_card.setVisibility(View.INVISIBLE);
        }
        if (card_type == 3) {
            info_card.setVisibility(View.VISIBLE);
            info_card_1.setVisibility(View.INVISIBLE);
            info_card_2.setVisibility(View.INVISIBLE);
        }
    }

    public int set_car_int_type() {
        if (info_card_2.getVisibility() == View.VISIBLE) {
            card_type = 1;
        }
        if (info_card_1.getVisibility() == View.VISIBLE) {
            card_type = 2;
        }
        if (info_card.getVisibility() == View.VISIBLE) {
            card_type = 3;
        }
        return card_type;
    }

    public void setdetailonsave() {
        user_name = card_username_edit.getText().toString();
        user_area = card_area_edit.getText().toString();
        user_location = card_location_edit.getText().toString();
        card_name.setText(user_name);
        card_name_1.setText(user_name);
        card_name_2.setText(user_name);
        card_area.setText(user_area);
        card_area_1.setText(user_area);
        card_area_2.setText(user_area);
        card_location.setText(user_location);
        card_location_1.setText(user_location);
        card_location_2.setText(user_location);
        card_username_edit.setText(user_name);
    }
}