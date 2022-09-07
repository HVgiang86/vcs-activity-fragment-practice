package com.example.vcsactivityandfragmentpractice.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vcsactivityandfragmentpractice.R;
import com.example.vcsactivityandfragmentpractice.adapters.ApplicationListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ApplicationSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApplicationSearchFragment extends Fragment {
    private RecyclerView resultRecyclerView;
    private ArrayList<InstalledApplication> allAppList;
    private ArrayList<InstalledApplication> recentList;
    private ApplicationListAdapter adapter;

    //Data Model for installed application
    public static class InstalledApplication {
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

    public ApplicationSearchFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     */
    public static ApplicationSearchFragment newInstance() {
        return new ApplicationSearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Init array lists
        allAppList = new ArrayList<>();
        recentList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_application_search, container, false);

        SearchView searchView = rootView.findViewById(R.id.search_view);
        resultRecyclerView = rootView.findViewById(R.id.search_result_recycler_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showResult(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchView.getQuery().toString().length() == 0)
                    showRecentSearch();
                else
                    showResult(searchView.getQuery().toString());

                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            if (searchView.getQuery().toString().length() == 0) {
                showRecentSearch();
                return true;
            }
            return false;
        });

        //return
        return rootView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Create List of all installed application
        //this method does not work correctly on Android 11 (API 30) and higher
        PackageManager pm = getContext().getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = getContext().getPackageManager().queryIntentActivities( mainIntent, 0);
        for (int i = 0; i < pkgAppsList.size(); ++i) {
            InstalledApplication app = new InstalledApplication();
            app.setAppName(pkgAppsList.get(i).loadLabel(pm).toString());
            app.setPackageName(pkgAppsList.get(i).activityInfo.packageName);
            app.setIcon(pkgAppsList.get(i).loadIcon(pm));
            allAppList.add(app);
        }

        adapter = new ApplicationListAdapter(allAppList,getContext());
        resultRecyclerView.setAdapter(adapter);
    }

    private ArrayList<InstalledApplication> searchForApp(String query) {

        query = query.trim();
        ArrayList<InstalledApplication> result = new ArrayList<>();
        Intent canLaunchChecking;
        for (InstalledApplication app : allAppList) {
            //Search for Application name contains query
            if (app.getAppName().toLowerCase().contains(query.toLowerCase())) {
                //check that package is launchable or not. If can launch, add to result list
                canLaunchChecking = getContext().getPackageManager().getLaunchIntentForPackage(app.getPackageName());
                if (canLaunchChecking != null) {
                    result.add(app);
                }
            }
        }
        return  result;
    }

    public void showResult(String query) {
        ArrayList<InstalledApplication> resultList = searchForApp(query);

        if (resultList.size() != 0) {
            adapter.setAppList(resultList);
            adapter.setAdapterForRecentSearch(false);
            adapter.notifyDataSetChanged();
        }
        else {
            showRecentSearch();
        }
    }

    public void showRecentSearch() {
        recentList = adapter.getRecentList();
        adapter.setAppList(recentList);
        adapter.setAdapterForRecentSearch(true);
        adapter.notifyDataSetChanged();
    }
}