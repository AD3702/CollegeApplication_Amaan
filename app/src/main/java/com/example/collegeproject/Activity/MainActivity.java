package com.example.collegeproject.Activity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.collegeproject.Adapter.DownloadImageOnLogin;
import com.example.collegeproject.R;
import com.example.collegeproject.Room.Offline_User_Data;
import com.example.collegeproject.Room.RoomDB;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String LOGGED_IN = "log_in";
    String isloggedin;
    ImageView main_transition_image;
    LinearLayout progress_layout;
    public static MainActivity mainActivity;
    private static int SPLASH_SCREEN_TIME_OUT = 800;
    private static int CODE_AUTHENTICATION_VERIFICATION = 241;
    Intent intent_main, image_intent;
    boolean security_enabled = false;
    public static final String SECURITY_SWITCH = "security_switch";
    public static final String IMAGE_UPLOAD_CHECK = "image_upload_check";
    public Uri on_login_image;
    boolean connected = false;
    Button try_again;



    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedpref();
        super.onCreate(savedInstanceState);
        mainActivity = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        //This method is used so that your splash activity
        //can cover the entire screen.

        setContentView(R.layout.activity_main);
        //this will bind your MainActivity.class file with activity_main.
        progress_layout = findViewById(R.id.progress_layout_linear);
        progress_layout.setVisibility(View.INVISIBLE);
        try_again = findViewById(R.id.try_again_btn);
        try_again.setVisibility(View.INVISIBLE);
        main_transition_image = findViewById(R.id.transition_main_splash_image);
        connectivity_master();
        if (connected == true) {
            if (isloggedin.equals("logged_in") == false) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (isloggedin.equals("upload_resume")) {
                                startActivity(new Intent(MainActivity.this, UploadResume.class));
                        }
                        if (isloggedin.equals("logged_out")) {
                            Intent intent = new Intent(MainActivity.this, Login.class);
                            Pair[] pairs = new Pair[1];
                            pairs[0] = new Pair<View, String>(main_transition_image, "logo_image_transition");
                            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                            startActivity(intent, activityOptions.toBundle());
                        }
                    }
                }, SPLASH_SCREEN_TIME_OUT);
            } else {
                if (security_enabled == false) {
                    progress_layout.setVisibility(View.VISIBLE);
                    RoomDB roomDB = RoomDB.getInstance(MainActivity.this.getApplicationContext());
                    List<Offline_User_Data> offline_user_dataList = roomDB.userDao().getlistData();
                    String img_on_login = offline_user_dataList.get(0).getUser_profile_image();
//                    Toast.makeText(mainActivity, "" + img_on_login, Toast.LENGTH_SHORT).show();
                    try {
                        new DownloadImageOnLogin(MainActivity.this).execute(img_on_login);
                        sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(IMAGE_UPLOAD_CHECK, "not_uploaded");
                        editor.commit();
                    } catch (Exception e) {
                        Log.e("IMAGEDOWNLOADERROR", String.valueOf(e));
                    }
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                startActivity(new Intent(android.provider.Settings.Panel.ACTION_INTERNET_CONNECTIVITY));
                try_again.setVisibility(View.VISIBLE);
            }
//                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
        }
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public void go_to_master() {
        image_intent = new Intent(mainActivity, MasterActivity.class);
        image_intent.setData(on_login_image);
        startActivity(image_intent);
        /*if (isloggedin.equals("logged_in")) {
            if (security_enabled == true) {
                KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                if (km.isKeyguardSecure()) {
                    Intent i = km.createConfirmDeviceCredentialIntent("Authentication required", "Provide your biometrics to login ");
                    startActivityForResult(i, CODE_AUTHENTICATION_VERIFICATION);
                } else {
                    Toast.makeText(MainActivity.this, "No any security setup done by user(pattern or password or pin or fingerprint", Toast.LENGTH_SHORT).show();
                }
            } else {
            }
        }*/
    }

    public void connectivity_master() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            Toast.makeText(MainActivity.this, "Not Connected to the Internet", Toast.LENGTH_SHORT).show();
            connected = false;
        }
    }

    public void sharedpref() {
        sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
        isloggedin = sharedPreferences.getString(LOGGED_IN, "logged_out");
        security_enabled = sharedPreferences.getBoolean(SECURITY_SWITCH, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CODE_AUTHENTICATION_VERIFICATION) {
//            Toast.makeText(this, "Success: Verified user's identity", Toast.LENGTH_SHORT).show();
            startActivity(intent_main);
        } else {
            Toast.makeText(this, "Failure: Unable to verify user's identity", Toast.LENGTH_SHORT).show();
        }
    }

}