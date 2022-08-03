package com.example.vcsactivityandfragmentpractice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.vcsactivityandfragmentpractice.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private int mTextViewColor = Color.argb(255, 255, 0, 19);
    private TextView mTextView;
    private final String COLOR_KEY = "color_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.tv_welcome_vcs);


        mTextView.setTextColor(mTextViewColor);




    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(COLOR_KEY, mTextViewColor);
    }

    //handle home button press event
    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        Random random = new Random();
        mTextViewColor = Color.argb(255, random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256));
    }
}