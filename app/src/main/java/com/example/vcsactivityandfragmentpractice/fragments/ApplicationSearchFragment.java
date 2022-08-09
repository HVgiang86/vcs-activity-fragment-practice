package com.example.vcsactivityandfragmentpractice.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
    private ArrayList<InstalledApplication> allAppList;
    private ArrayList<InstalledApplication> recentSearchList;

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
        recentSearchList = new ArrayList<>();
        allAppList = new ArrayList<>();

        //Create List of all installed application
        //this method does not work correctly on Android 11 (API 30) and higher
        PackageManager pm = getContext().getPackageManager();

        /*List<PackageInfo> packs = pm.getInstalledPackages(PackageManager.GET_META_DATA);
        for (PackageInfo p : packs) {
                InstalledApplication app = new InstalledApplication();
                String name = p.applicationInfo.loadLabel(pm).toString();
                app.setAppName(name);
                app.setPackageName(p.applicationInfo.packageName);
                app.setIcon(p.applicationInfo.loadIcon(pm));
                allAppList.add(app);
                Log.d(TAG,app.getAppName());
        }*/

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
                    showResult(searchView.getQuery().toString());

                return true;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if (searchView.getQuery().toString().length() == 0) {
                    showRecentSearch();
                    return true;
                }
                return false;
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
        ArrayList<InstalledApplication> resultList = searchForApp(query);

        if (resultList.size() != 0) {
            ApplicationListAdapter adapter = new ApplicationListAdapter(getActivity(), resultList, false);
            resultListView.setAdapter(adapter);
            resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    Intent launchIntent = getContext().getPackageManager().getLaunchIntentForPackage(resultList.get(i).getPackageName());
                    if (launchIntent != null) {
                        if (!recentSearchList.contains(resultList.get(i)))
                            recentSearchList.add(resultList.get(i));

                        startActivity(launchIntent);
                    } else {
                        Toast.makeText(getContext(), "There is no package available in android", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public void showRecentSearch() {

        if (recentSearchList.size() != 0) {
            ApplicationListAdapter adapter = new ApplicationListAdapter(getActivity(), recentSearchList, true);
            resultListView.setAdapter(adapter);
            resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                    Intent launchIntent = getContext().getPackageManager().getLaunchIntentForPackage(recentSearchList.get(i).getPackageName());
                    if (launchIntent != null) {
                        startActivity(launchIntent);
                    } else {
                        Toast.makeText(getContext(), "There is no package available in android", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            resultListView.setAdapter(null);
        }

    }

}