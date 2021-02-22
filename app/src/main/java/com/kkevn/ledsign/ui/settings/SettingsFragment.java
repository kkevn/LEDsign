package com.kkevn.ledsign.ui.settings;

import android.os.Bundle;
import android.support.v14.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.kkevn.ledsign.R;

public class SettingsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        /*if (root.findViewById(R.id.fl_settings) != null) {

            if (savedInstanceState != null) {
                return root;
            }

            getFragmentManager().beginTransaction().add(R.id.fl_settings, new SettingsPreferenceFragment()).commit();
            //getFragmentManager().beginTransaction().replace(R.id.fl_settings, new SettingsPreferenceFragment()).commit();
        }*/

        getFragmentManager().beginTransaction().replace(R.id.fl_settings, new SettingsPreferenceFragment()).commit();

        return root;
    }
}