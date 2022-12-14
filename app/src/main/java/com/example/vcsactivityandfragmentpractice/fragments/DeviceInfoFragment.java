package com.example.vcsactivityandfragmentpractice.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vcsactivityandfragmentpractice.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceInfoFragment extends Fragment {


    public DeviceInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     */
    public static DeviceInfoFragment newInstance() {
        return new DeviceInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_device_info, container, false);

        TextView deviceModelTextView = rootView.findViewById(R.id.device_name_tv);
        TextView osVersionTextView = rootView.findViewById(R.id.os_version_tv);
        TextView deviceTypeTextView = rootView.findViewById(R.id.device_type_tv);

        osVersionTextView.setText(System.getProperty("os.version"));
        deviceModelTextView.setText(android.os.Build.MODEL);
        deviceTypeTextView.setText(android.os.Build.PRODUCT);

        return rootView;
    }
}