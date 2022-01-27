package com.example.collegeproject.Activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.collegeproject.R;

public class Feedback extends AppCompatActivity {

    Toolbar toolbar_feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initializeUI();
        toolbar_setup();
    }

    public void initializeUI(){
        toolbar_feedback = findViewById(R.id.toolbar_actionbar_feedback);
    }

    public void toolbar_setup() {
        toolbar_feedback.setBackground(new ColorDrawable(ContextCompat.getColor(this, R.color.main_color)));
        toolbar_feedback.setTitleTextColor(ContextCompat.getColor(this, R.color.white));
        toolbar_feedback.setTitle("Feedback");
    }
}