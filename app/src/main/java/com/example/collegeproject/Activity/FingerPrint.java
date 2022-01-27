package com.example.collegeproject.Activity;

import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.collegeproject.R;

public class FingerPrint extends AppCompatActivity {

    private static int CODE_AUTHENTICATION_VERIFICATION = 241;


    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print);
        KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        if (km.isKeyguardSecure()) {

            Intent i = km.createConfirmDeviceCredentialIntent("Authentication required", "Use your fingerprint to login ");
            startActivityForResult(i, CODE_AUTHENTICATION_VERIFICATION);
        } else
            Toast.makeText(this, "No any security setup done by user(pattern or password or pin or fingerprint", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CODE_AUTHENTICATION_VERIFICATION) {
//            Toast.makeText(this, "Success: Verified user's identity", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failure: Unable to verify user's identity", Toast.LENGTH_SHORT).show();
        }
    }
}