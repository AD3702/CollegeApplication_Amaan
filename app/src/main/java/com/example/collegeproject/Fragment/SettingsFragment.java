package com.example.collegeproject.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.collegeproject.Activity.ContactUs;
import com.example.collegeproject.Activity.EditUserProfile;
import com.example.collegeproject.Activity.Feedback;
import com.example.collegeproject.Activity.FollowUS;
import com.example.collegeproject.Activity.MainActivity;
import com.example.collegeproject.Activity.MasterActivity;
import com.example.collegeproject.Activity.Security;
import com.example.collegeproject.Activity.UserCard;
import com.example.collegeproject.Adapter.DownLoadImageTask;
import com.example.collegeproject.Database.APIInterface;
import com.example.collegeproject.Database.AppClient;
import com.example.collegeproject.Database.Registrationdatum;
import com.example.collegeproject.Database.UserData;
import com.example.collegeproject.R;
import com.tuyenmonkey.mkloader.MKLoader;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    Toolbar toolbar_settings;
    LinearLayout feedback_settings, edit_profile_settings, logout, change_password_settings, settings_share, settings_contact_us, settings_follow_us;
    CardView user_info_main_card_settings;
    TextView settings_name, settings_education;
    ArrayList<Registrationdatum> loginRegistrationdata;
    String settings_Name, settings_Education, userID, user_education_1, user_education_2, user_image_set;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String USER_ID = "user_id";
    public static final String LOGGED_IN = "log_in";
    public static final String SECURITY_SWITCH = "security_switch";
    String isloggedin;
    CircleImageView settingsprofileimage;
    boolean security_enabled;
    MKLoader settings_loader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        initializeUI(view);
        toolbar_setup();
        Intent sharedprefIntent = getActivity().getIntent();
        sharedPref();
        loadData();
        feedback_settings.setOnClickListener(this);
        edit_profile_settings.setOnClickListener(this);
        user_info_main_card_settings.setOnClickListener(this);
        change_password_settings.setOnClickListener(this);
        logout.setOnClickListener(this);
        settings_share.setOnClickListener(this);
        settings_contact_us.setOnClickListener(this);
        settings_follow_us.setOnClickListener(this);
        return view;
    }

    public void initializeUI(View view) {
        settings_contact_us = view.findViewById(R.id.settings_contact_us);
        settings_share = view.findViewById(R.id.settings_share);
        settings_follow_us = view.findViewById(R.id.settings_follow_us);
        change_password_settings = view.findViewById(R.id.security_settings);
        loginRegistrationdata = new ArrayList<>();
        settingsprofileimage = view.findViewById(R.id.setting_profile_image);
        settings_name = view.findViewById(R.id.settings_name);
        settings_education = view.findViewById(R.id.settings_education);
        toolbar_settings = view.findViewById(R.id.toolbar_actionbar);
        feedback_settings = view.findViewById(R.id.feedback_settings);
        edit_profile_settings = view.findViewById(R.id.edit_profile_settings);
        user_info_main_card_settings = view.findViewById(R.id.user_info_main_card_settings);
        settings_loader = view.findViewById(R.id.settings_loader);
        logout = view.findViewById(R.id.logout);
    }

    public void toolbar_setup() {
        toolbar_settings.setBackground(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.main_color)));
        toolbar_settings.setTitleTextColor(ContextCompat.getColor(getActivity(), R.color.white));
        toolbar_settings.setTitle("Settings");
    }

    @Override
    public void onClick(View view) {
        if (view == feedback_settings) {
            Intent intent_settings_feedback = new Intent(getActivity(), Feedback.class);
            startActivity(intent_settings_feedback);
        }
        if (view == edit_profile_settings) {
            Intent intent_settings_feedback = new Intent(getActivity(), UserCard.class);
            startActivity(intent_settings_feedback);
        }
        if (view == user_info_main_card_settings) {
            Intent intent_settings_main_profile = new Intent(getActivity(), EditUserProfile.class);
            startActivity(intent_settings_main_profile);
        }
        if (view == logout) {
            MasterActivity.masteractivity.finish();
            startActivity(new Intent(getActivity(), MainActivity.class));
            isloggedin = "logged_out";
            security_enabled = false;
            sharedPreferences = this.getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LOGGED_IN, isloggedin);
            editor.putBoolean(SECURITY_SWITCH, security_enabled);
            editor.commit();
        }
        if (view == change_password_settings) {
            startActivity(new Intent(getActivity(), Security.class));
        }
        if (view == settings_contact_us) {
            startActivity(new Intent(getActivity(), ContactUs.class));
        }
        if (view == settings_follow_us) {
            startActivity(new Intent(getActivity(), FollowUS.class));
        }
        if (view == settings_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            /*This will be the actual content you wish you share.*/
            String shareBody = "HELLO THIS IS A USER RESUME APP DOWNLOAD THIS APPLICATION FROM PLAY STORE";
            /*The type of the content is text, obviously.*/
            intent.setType("text/plain");
            /*Applying information Subject and Body.*/
            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(intent, ""));
        }
    }

    public void sharedPref() {
        sharedPreferences = this.getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userID = sharedPreferences.getString(USER_ID, "");
        /*SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LOGGED_IN, isloggedin);
        editor.commit();*/
    }

    public void loadData() {
        APIInterface apiInterface = AppClient.getclient().create(APIInterface.class);
        Call<UserData> registrationCall = apiInterface.find_user_id(userID);
        registrationCall.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, Response<UserData> response) {
                try {
                    loginRegistrationdata = (ArrayList<Registrationdatum>) response.body().getRegistrationdata();
                    settings_Name = loginRegistrationdata.get(0).getUserName();
                    user_education_1 = loginRegistrationdata.get(0).getUserCollegeDegree();
                    user_education_2 = loginRegistrationdata.get(0).getUserSemester();
                    user_image_set = loginRegistrationdata.get(0).getUserProfileImage();
                    setUserDetails();
                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {

            }
        });
    }

    public void setUserDetails() {
        settings_Education = user_education_1 + " ( " + user_education_2 + " )";
        settings_education.setText(settings_Education);
        settings_name.setText(settings_Name);
        new DownLoadImageTask(settingsprofileimage, settings_loader).execute(user_image_set);
        Toast.makeText(getActivity(), "" + user_image_set, Toast.LENGTH_SHORT).show();
    }

}