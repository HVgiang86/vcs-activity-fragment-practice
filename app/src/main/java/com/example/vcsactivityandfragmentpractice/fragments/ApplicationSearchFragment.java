package com.example.vcsactivityandfragmentpractice.fragments;

import android.content.Intent;
import android.content.pm.PackageInfo;
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
import com.example.vcsactivityandfragmentpractice.models.InstalledApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ApplicationSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApplicationSearchFragment extends Fragment {
    private ListView resultListView;
    private ArrayList<InstalledApplication> appList = new ArrayList<>();

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

        //Create List of installed application
        List<PackageInfo> packs = getContext().getPackageManager().getInstalledPackages(0);
        for (PackageInfo p : packs) {
            if (p.versionName != null) {
                InstalledApplication app = new InstalledApplication();
                String name = p.applicationInfo.loadLabel(getContext().getPackageManager()).toString();
                app.setAppName(name);
                app.setPackageName(p.applicationInfo.packageName);
                app.setIcon(p.applicationInfo.loadIcon(getContext().getPackageManager()));
                appList.add(app);

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
                    Toast.makeText(getContext(), "Query: " + query, Toast.LENGTH_SHORT).show();
                    doSearch(query.trim());
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

        //return
        return rootView;
    }

    public void doSearch(String query) {
        ArrayList<InstalledApplication> result = new ArrayList<>();
        for (InstalledApplication app : appList) {
            String appName = app.getAppName();
            if (appName.toLowerCase().contains(query.toLowerCase())) {
                result.add(app);
            }
        }
        if (appList.size() != 0) {
            Toast.makeText(getContext(), "Result: " + result.get(0).getAppName(), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "Not found!", Toast.LENGTH_SHORT).show();
        }

        ApplicationListAdapter adapter = new ApplicationListAdapter(getActivity(), result);
        resultListView.setAdapter(adapter);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                Intent launchIntent = getContext().getPackageManager().getLaunchIntentForPackage(result.get(i).getPackageName());
                if (launchIntent != null) {
                    startActivity(launchIntent);
                } else {
                    Toast.makeText(getContext(), "There is no package available in android", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}