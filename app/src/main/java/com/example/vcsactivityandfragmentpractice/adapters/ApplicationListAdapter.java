package com.example.vcsactivityandfragmentpractice.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vcsactivityandfragmentpractice.R;
import com.example.vcsactivityandfragmentpractice.models.InstalledApplication;

import java.util.ArrayList;

public class ApplicationListAdapter extends BaseAdapter {
    private ArrayList<InstalledApplication> appList;
    Activity activity;

    public ApplicationListAdapter(Activity activity, ArrayList<InstalledApplication> appList) {
        this.appList = appList;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public Object getItem(int position) {
        return appList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        convertView = activity.getLayoutInflater().inflate(R.layout.application_list, parent, false);

        ImageView appIconImageView = convertView.findViewById(R.id.app_icon_iv);
        TextView appNameTextView = convertView.findViewById(R.id.app_name_tv);

        appNameTextView.setText(appList.get(i).getAppName());
        appIconImageView.setImageDrawable(appList.get(i).getIcon());

        return convertView;
    }


}
