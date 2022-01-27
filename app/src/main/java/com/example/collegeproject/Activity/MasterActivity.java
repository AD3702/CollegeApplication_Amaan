package com.example.collegeproject.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.example.collegeproject.Fragment.CourseHeader;
import com.example.collegeproject.Fragment.HomeFragment;
import com.example.collegeproject.Fragment.SettingsFragment;
import com.example.collegeproject.R;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class MasterActivity extends AppCompatActivity implements SpaceOnClickListener {

    SharedPreferences sharedPreferences;
    Bundle savedInstanceState;
    private long pressedTime;
    public static Activity masteractivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        this.savedInstanceState = savedInstanceState;
        masteractivity = this;
        loadData();
    }

    public void loadData() {
        SpaceNavigationView spaceNavigationView = (SpaceNavigationView) findViewById(R.id.bottom_navigation);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_home_black));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_course_black));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_total_jobs));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_settings_black_24));
        spaceNavigationView.setCentreButtonIcon(R.drawable.ic_resume_upload_white);
        /*spaceNavigationView.setActiveSpaceItemColor(R.color.primary);
        spaceNavigationView.setInActiveSpaceItemColor(R.color.iconscolor);*/
        spaceNavigationView.setSpaceItemIconSize(64);
        spaceNavigationView.setCentreButtonColor(ContextCompat.getColor(this, R.color.main_color));
        spaceNavigationView.setSpaceOnClickListener(this);
        HomeFragment home = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, home);
        ft.commit();
    }

    @Override
    public void onCentreButtonClick() {
        Intent intent = new Intent(MasterActivity.this, UploadResume.class);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int itemIndex, String itemName) {
        if (itemIndex == 0) {
            HomeFragment home = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, home);
            ft.commit();
        }
        if (itemIndex == 1) {
            CourseHeader courseHeader = new CourseHeader();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, courseHeader);
            ft.commit();
        }
        if (itemIndex == 2) {
            startActivity(new Intent(MasterActivity.this, TotalApplications.class));
        }
        if (itemIndex == 3) {
            SettingsFragment settingsFragment = new SettingsFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, settingsFragment);
            ft.commit();
        }

    }

    @Override
    public void onItemReselected(int itemIndex, String itemName) {
        if (itemIndex == 0) {
            HomeFragment home = new HomeFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, home);
            ft.commit();
        }
        if (itemIndex == 1) {
            CourseHeader courseHeader = new CourseHeader();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, courseHeader);
            ft.commit();
        }
        if (itemIndex == 2) {
            startActivity(new Intent(MasterActivity.this, TotalApplications.class));
        }
        if (itemIndex == 3) {
            SettingsFragment settingsFragment = new SettingsFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, settingsFragment);
            ft.commit();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            MainActivity.mainActivity.finish();
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }
}