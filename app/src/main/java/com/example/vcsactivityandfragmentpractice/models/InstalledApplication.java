package com.example.vcsactivityandfragmentpractice.models;

import android.graphics.drawable.Drawable;

public class InstalledApplication {
    private String appName;
    private Drawable icon;
    private String packageName;

    public String getAppName() {
        return appName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
