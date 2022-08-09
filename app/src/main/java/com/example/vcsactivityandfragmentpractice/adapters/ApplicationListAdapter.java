package com.example.vcsactivityandfragmentpractice.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vcsactivityandfragmentpractice.R;
import com.example.vcsactivityandfragmentpractice.fragments.ApplicationSearchFragment;

import java.util.ArrayList;

public class ApplicationListAdapter extends BaseAdapter {
    private ArrayList<ApplicationSearchFragment.InstalledApplication> appList;
    private Activity activity;
    private boolean isAdapterForRecentSearch = false;

    public ApplicationListAdapter(Activity activity, ArrayList<ApplicationSearchFragment.InstalledApplication> appList, boolean isAdapterForRecentSearch) {
        this.appList = appList;
        this.activity = activity;
        this.isAdapterForRecentSearch = isAdapterForRecentSearch;
    }

    public ApplicationListAdapter(Activity activity) {
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
        TextView recentSearchLabel = convertView.findViewById(R.id.recent_search_label);

        if (isAdapterForRecentSearch)
            recentSearchLabel.setVisibility(View.VISIBLE);
        else
            recentSearchLabel.setVisibility(View.GONE);

        appNameTextView.setText(appList.get(i).getAppName());
        appIconImageView.setImageDrawable(appList.get(i).getIcon());

        return convertView;
    }

    public void setAppList(ArrayList<ApplicationSearchFragment.InstalledApplication> appList) {
        this.appList = appList;
    }

    public void setAdapterForRecentSearch(boolean adapterForRecentSearch) {
        isAdapterForRecentSearch = adapterForRecentSearch;
    }
}
