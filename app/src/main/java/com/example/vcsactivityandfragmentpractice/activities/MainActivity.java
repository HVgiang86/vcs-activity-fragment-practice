package com.example.vcsactivityandfragmentpractice.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vcsactivityandfragmentpractice.R;
import com.example.vcsactivityandfragmentpractice.fragments.ApplicationSearchFragment;
import com.example.vcsactivityandfragmentpractice.fragments.DeviceInfoFragment;

import java.util.Random;

public class MainActivity extends AppCompatActivity{
    private int mTextViewColor;
    private TextView mTextView;
    private final String COLOR_KEY = "color_key";
    private final int TIME_INTERVAL = 2000;     // 2000 milliseconds: Time passed between two back pressed
    private long mBackPressedTime;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private final String DEVICE_INFORMATION_FRAGMENT_TAG = "Device_Information_Fragment";
    private final String SEARCH_FOR_APPLICATION_FRAGMENT_TAG = "Search_For_Application";

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

        //setup navigation drawer
        mDrawer = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                                                        R.string.navigation_drawer_open,
                                                        R.string.navigation_drawer_close);

        if (mDrawer != null) {
            mDrawer.addDrawerListener(mToggle);
        }
        mToggle.syncState();

    }

    //Handle event that show device information action has been chosen
    public void showDeviceInfo(MenuItem item) {
        mDrawer.closeDrawer(GravityCompat.START);
        Toast.makeText(getApplicationContext(), "Device Info Action Selected!",Toast.LENGTH_SHORT).show();
        showDeviceInformation();
    }

    //Handle event that application search action has been chosen
    public void searchForApplication(MenuItem item) {
        mDrawer.closeDrawer(GravityCompat.START);
        Toast.makeText(getApplicationContext(), "Search For Application Action Selected!",Toast.LENGTH_SHORT).show();
        searchForAnApplication();
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

        //check fragments displaying
        //if fragments are now displayed, do not show Toast or exit Application. Just remove fragments from BackStack
        DeviceInfoFragment deviceInfoFragment = (DeviceInfoFragment) getSupportFragmentManager()
                                                                    .findFragmentByTag(DEVICE_INFORMATION_FRAGMENT_TAG);
        ApplicationSearchFragment applicationSearchFragment = (ApplicationSearchFragment) getSupportFragmentManager()
                                                                    .findFragmentByTag(SEARCH_FOR_APPLICATION_FRAGMENT_TAG);

        if ((deviceInfoFragment != null && deviceInfoFragment.isVisible()) ||
                (applicationSearchFragment != null && applicationSearchFragment.isVisible())) {
            super.onBackPressed();
        }
        else {

            //if time between two back press faster than 2 seconds, exit application
            //else, notify user to press back button again
            if (mBackPressedTime + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
            }
            else {
                //update last Back pressed time
                mBackPressedTime = System.currentTimeMillis();
                Toast.makeText(this, "Nhấn nút back lần nữa để thoát ứng dụng!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    //Show device information with a fragment
    public void showDeviceInformation() {
        DeviceInfoFragment deviceInfoFragment = DeviceInfoFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, deviceInfoFragment,DEVICE_INFORMATION_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();

    }

    //Show Application Searching UI with a fragment
    public void searchForAnApplication() {
        ApplicationSearchFragment applicationSearchFragment = ApplicationSearchFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, applicationSearchFragment,SEARCH_FOR_APPLICATION_FRAGMENT_TAG)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }
}