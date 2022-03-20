package com.example.collegeproject.Activity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.collegeproject.R;

public class FollowUS extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar_follow_us;
    TextView instagram, facebook, twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_us);
        initializeUI();
        toolbar_setup();
        instagram.setOnClickListener(this);
        facebook.setOnClickListener(this);
        twitter.setOnClickListener(this);
    }

    public void initializeUI() {
        toolbar_follow_us = findViewById(R.id.toolbar_actionbar_follow_us);
        instagram = findViewById(R.id.follow_is_instagram_id);
        facebook = findViewById(R.id.follow_us_facebook_id);
        twitter = findViewById(R.id.follow_us_twitter_id);
    }

    public void toolbar_setup() {
        toolbar_follow_us.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.main_color)));
        toolbar_follow_us.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar_follow_us.setTitle("Follow Us");
    }

    @Override
    public void onClick(View view) {
        if (view == instagram) {
            try {
                Uri uri = Uri.parse("http://instagram.com/_u/a.d.3702");
                Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                likeIng.setPackage("com.instagram.android");

                try {
                    startActivity(likeIng);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.instagram.com/a.d.3702/")));
                }
            } catch (Exception e) {
                Log.e("INSTA ERROR", String.valueOf(e));
                Toast.makeText(FollowUS.this, "Intagram Not Installed", Toast.LENGTH_SHORT).show();
            }
        }
        if (view == facebook) {
            Intent intent = null;
            try {
                getPackageManager().getPackageInfo("com.facebook.katana", 0);
                String url = "https://www.facebook.com/" + "amaan.dhanerawala";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + url));
            } catch (Exception e) {
                // no Facebook app, revert to browser

                String url = "https://facebook.com/" + "amaan.dhanerawala";
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
            }
            this.startActivity(intent);
        }
        if (view == twitter) {

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + "@PraxwareT")));
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + "@PraxwareT")));
            }

        }
    }
}