package com.example.vcsactivityandfragmentpractice.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vcsactivityandfragmentpractice.R;
import com.example.vcsactivityandfragmentpractice.fragments.ApplicationSearchFragment;

import java.util.ArrayList;

public class ApplicationListAdapter extends RecyclerView.Adapter<ApplicationListAdapter.ViewHolder>{
    private ArrayList<ApplicationSearchFragment.InstalledApplication> appList;
    private ArrayList<ApplicationSearchFragment.InstalledApplication> recentList;
    private boolean isAdapterForRecentSearch = false;
    private final Context context;


    public ApplicationListAdapter(ArrayList<ApplicationSearchFragment.InstalledApplication> appList, Context context) {
        this.appList = appList;
        this.context = context;
        recentList = new ArrayList<>();
    }

    public void setAdapterForRecentSearch(boolean adapterForRecentSearch) {
        isAdapterForRecentSearch = adapterForRecentSearch;
    }

    public void setAppList(ArrayList<ApplicationSearchFragment.InstalledApplication> appList) {
        this.appList = appList;
    }

    public ArrayList<ApplicationSearchFragment.InstalledApplication> getRecentList() {
        return recentList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView appIconImageView = holder.appIconImageView;
        TextView appNameTextView = holder.appNameTextView;
        TextView recentSearchLabel = holder.recentSearchLabel;
        if (isAdapterForRecentSearch)
            recentSearchLabel.setVisibility(View.VISIBLE);
        else
            recentSearchLabel.setVisibility(View.GONE);

        appNameTextView.setText(appList.get(position).getAppName());
        appIconImageView.setImageDrawable(appList.get(position).getIcon());

        holder.setOnItemClickListener((View view, int i) -> {

            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(appList.get(i).getPackageName());
            if (launchIntent != null) {
                if (!recentList.contains(appList.get(i)))
                    recentList.add(appList.get(i));

                context.startActivity(launchIntent);
            } else {
                Toast.makeText(context, "There is no package available in android", Toast.LENGTH_LONG).show();
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.application_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView appIconImageView;
        public TextView appNameTextView;
        public TextView recentSearchLabel;
        private ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            appIconImageView = itemView.findViewById(R.id.app_icon_iv);
            appNameTextView = itemView.findViewById(R.id.app_name_tv);
            recentSearchLabel = itemView.findViewById(R.id.recent_search_label);

            itemView.setOnClickListener(this);
        }

        public void setOnItemClickListener(ItemClickListener listener) {
            this.itemClickListener = listener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }



}
