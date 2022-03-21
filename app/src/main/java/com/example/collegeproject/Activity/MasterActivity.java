package com.example.collegeproject.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.collegeproject.Fragment.HomeFragment;
import com.example.collegeproject.Fragment.SettingsFragment;
import com.example.collegeproject.Fragment.TotalApplicationFragment;
import com.example.collegeproject.Fragment.UploadResumeFragment;
import com.example.collegeproject.R;
import com.gauravk.bubblenavigation.BubbleNavigationConstraintView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.luseen.spacenavigation.SpaceNavigationView;

public class MasterActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Bundle savedInstanceState;
    private long pressedTime;
    public static MasterActivity masteractivity;
    SpaceNavigationView spaceNavigationView;
    BubbleNavigationConstraintView ssCustomBottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        this.savedInstanceState = savedInstanceState;
        masteractivity = this;
//        loadData();
        loaddata2();
    }

    public void loaddata2() {
        ssCustomBottomNavigation = findViewById(R.id.bottom_navigation_);
        ssCustomBottomNavigation.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                if (position == 0) {
                    HomeFragment home = new HomeFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment, home);
                    ft.commit();
                }
                if (position == 2) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment, new UploadResumeFragment());
                    ft.commit();
                }
                if (position == 1) {
                    TotalApplicationFragment totalApplicationFragment = new TotalApplicationFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment, totalApplicationFragment);
                    ft.commit();
                }
                if (position == 3) {
                    SettingsFragment settingsFragment = new SettingsFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment, settingsFragment);
                    ft.commit();
                }
            }
        });
        HomeFragment home = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, home);
        ft.commit();
    }


    public void oneditprofbackpressed() {
        SettingsFragment settingsFragment = new SettingsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, settingsFragment);
        ft.commit();
    }


    @Override
    public void onBackPressed() {
        if (pressedTime + 1000 > System.currentTimeMillis()) {
            super.onBackPressed();
            MainActivity.mainActivity.finish();
            finish();
        } else {
            HomeFragment home = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, home);
            ft.commit();
            ssCustomBottomNavigation.setCurrentActiveItem(0);
        }
        pressedTime = System.currentTimeMillis();
    }
}