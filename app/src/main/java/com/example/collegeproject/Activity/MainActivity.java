package com.example.collegeproject.Activity;

import android.app.ActivityOptions;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.collegeproject.R;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String LOGGED_IN = "log_in";
    String isloggedin;
    ImageView main_transition_image;
    public static MainActivity mainActivity;
    private static int SPLASH_SCREEN_TIME_OUT = 800;
    private static int CODE_AUTHENTICATION_VERIFICATION = 241;
    Intent intent_main;
    boolean security_enabled = false;
    public static final String SECURITY_SWITCH = "security_switch";

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

        main_transition_image = findViewById(R.id.transition_main_splash_image);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isloggedin.equals("logged_in")) {
                    if (security_enabled == true) {
                        KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
                        if (km.isKeyguardSecure()) {
                            Intent i = km.createConfirmDeviceCredentialIntent("Authentication required", "Provide your biometrics to login ");
                            startActivityForResult(i, CODE_AUTHENTICATION_VERIFICATION);
                        } else {
                            Toast.makeText(MainActivity.this, "No any security setup done by user(pattern or password or pin or fingerprint", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        intent_main = new Intent(MainActivity.this, MasterActivity.class);
                        startActivity(intent_main);
                    }
                    intent_main = new Intent(MainActivity.this, MasterActivity.class);
                }
                if (isloggedin.equals("upload_image")) {
                    startActivity(new Intent(MainActivity.this, ImageAddActivity.class));
                }

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