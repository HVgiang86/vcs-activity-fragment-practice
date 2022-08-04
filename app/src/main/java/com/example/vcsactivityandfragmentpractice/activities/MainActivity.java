package com.example.vcsactivityandfragmentpractice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcsactivityandfragmentpractice.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int mTextViewColor;
    private TextView mTextView;
    private final String COLOR_KEY = "color_key";
    private final int TIME_INTERVAL = 2000;     // 2000 milliseconds: Time passed between two back pressed
    private long mBackPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.tv_welcome_vcs);

        //restore saved color to continue display
        if (savedInstanceState != null) {
            mTextViewColor = savedInstanceState.getInt(COLOR_KEY);
            mTextView.setTextColor(mTextViewColor);
        }

        //setup tool bar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        //now tool bar likes an action bar


    }


    //save color to InstanceState when change device configuration
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COLOR_KEY, mTextViewColor);
    }

    //handle home button press event
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        //random a new color and set as text color
        Random random = new Random();
        mTextViewColor = Color.argb(255, random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256));
        mTextView.setTextColor(mTextViewColor);
    }

    //handle back button press event
    @Override
    public void onBackPressed() {

        //if time between two back press faster than 2 seconds, exit application
        if (mBackPressedTime + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        }
        else {
            Toast.makeText(this, "Nhấn nút back lần nữa để thoát ứng dụng!", Toast.LENGTH_SHORT).show();
        }
        mBackPressedTime = System.currentTimeMillis();
    }




}