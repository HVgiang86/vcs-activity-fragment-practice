package com.example.vcsactivityandfragmentpractice.fragments;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
    private ListView resultListView;
    private final ArrayList<InstalledApplication> allAppList = new ArrayList<>();
    private ArrayList<InstalledApplication> recentSearchList = new ArrayList<>();
    private ApplicationListAdapter adapter = new ApplicationListAdapter(getActivity());

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

        //Create List of all installed application
        List<PackageInfo> packs = getContext().getPackageManager().getInstalledPackages(0);
        for (PackageInfo p : packs) {
            if (p.versionName != null) {
                InstalledApplication app = new InstalledApplication();
                String name = p.applicationInfo.loadLabel(getContext().getPackageManager()).toString();
                app.setAppName(name);
                app.setPackageName(p.applicationInfo.packageName);
                app.setIcon(p.applicationInfo.loadIcon(getContext().getPackageManager()));
                allAppList.add(app);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_application_search, container, false);

        SearchView searchView = rootView.findViewById(R.id.search_view);
        resultListView = rootView.findViewById(R.id.search_result_list_view);

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
                    showResult(newText);

                return true;
            }
        });

        //return
        return rootView;
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
        ArrayList<InstalledApplication> result = searchForApp(query);

        if (result.size() != 0) {
            resultListView.setAdapter(adapter);
            adapter.setAppList(result);
            adapter.setAdapterForRecentSearch(false);
            adapter.notifyDataSetChanged();

            resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    Intent launchIntent = getContext().getPackageManager().getLaunchIntentForPackage(result.get(i).getPackageName());
                    if (launchIntent != null) {
                        recentSearchList.add(result.get(i));
                        startActivity(launchIntent);
                    } else {
                        Toast.makeText(getContext(), "There is no package available in android", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void showRecentSearch() {
        resultListView.setAdapter(adapter);
        adapter.setAppList(recentSearchList);
        adapter.setAdapterForRecentSearch(true);
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent launchIntent = getContext().getPackageManager().getLaunchIntentForPackage(recentSearchList.get(i).getPackageName());
                if (launchIntent != null) {
                    recentSearchList.add(recentSearchList.get(i));
                    startActivity(launchIntent);
                } else {
                    Toast.makeText(getContext(), "There is no package available in android", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}