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
import com.example.vcsactivityandfragmentpractice.fragments.DeviceInfoFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.Random;

public class MainActivity extends AppCompatActivity{
    private int mTextViewColor;
    private TextView mTextView;
    private final String COLOR_KEY = "color_key";
    private final int TIME_INTERVAL = 2000;     // 2000 milliseconds: Time passed between two back pressed
    private long mBackPressedTime;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;

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

        NavigationView navigationView = findViewById(R.id.navigation_view);

        if (navigationView != null) {
            //Handle when navigation drawer menu item selected
            MenuItem showDeviceInfoAction = navigationView.getMenu().findItem(R.id.show_device_information);
            MenuItem searchForApplicationAction = navigationView.getMenu().findItem(R.id.search_for_application);

            showDeviceInfoAction.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    mDrawer.closeDrawer(GravityCompat.START);
                    Toast.makeText(getApplicationContext(), "Device Info Action Selected!",Toast.LENGTH_SHORT).show();
                    showDeviceInformation();
                    return true;
                }
            });

            searchForApplicationAction.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    mDrawer.closeDrawer(GravityCompat.START);
                    Toast.makeText(getApplicationContext(), "Search For Application Action Selected!",Toast.LENGTH_SHORT).show();
                    searchForAnApplication();
                    return true;
                }
            });
        }

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

    /*//handle back button press event
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
        super.onBackPressed();
    }*/



    //Show device information with a fragment
    public void showDeviceInformation() {
        DeviceInfoFragment deviceInfoFragment = DeviceInfoFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, deviceInfoFragment)
                .addToBackStack(null)
                .commit();

    }

    //Show Application Searching UI with a fragment
    public void searchForAnApplication() {

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mToggle.onConfigurationChanged(newConfig);
    }
}