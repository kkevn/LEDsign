package com.kkevn.ledsign.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.SeekBarPreference;

import com.kkevn.ledsign.R;

public class SettingsPreferenceFragment extends PreferenceFragmentCompat {

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }*/

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Preference preference = findPreference(key);

                if (key.equals(getResources().getString(R.string.pref_default_name_key))) {
                    String name = sharedPreferences.getString(getResources().getString(R.string.pref_default_name_key), "");
                    name = name.trim();
                    if (name.isEmpty()) {
                        name = getResources().getString(R.string.menu_new_profile);
                    } else if (name.length() > 24) {
                        name = name.substring(0, 24);
                    }
                    name = name.replaceAll("[^A-Za-z0-9_ \\-]","");

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getResources().getString(R.string.pref_default_name_key), name);
                    editor.commit();

                    preference.setSummary(name);
                } else if (key.equals(getResources().getString(R.string.pref_sensitivity_key))) {
                    double value = (sharedPreferences.getInt(getResources().getString(R.string.pref_sensitivity_key), 0) + 1.0) / 2.0 ;
                    preference.setSummary("" + value + "x");
                }
            }
        };


        getResources().getString(R.string.pref_default_name_key);
    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        // update the EditTextPreference summary with current default profile name
        String DEFAULT_NAME_KEY = getResources().getString(R.string.pref_default_name_key);
        Preference pref_name = findPreference(DEFAULT_NAME_KEY);
        String name = getPreferenceScreen().getSharedPreferences().getString(DEFAULT_NAME_KEY, "");
        name = name.trim();
        if (name.isEmpty()) {
            name = getResources().getString(R.string.menu_new_profile);
        } else if (name.length() > 24) {
            name = name.substring(0, 24);
        }
        name = name.replaceAll("[^A-Za-z0-9_ \\-]","");

        SharedPreferences.Editor editor = getPreferenceScreen().getSharedPreferences().edit();
        editor.putString(getResources().getString(R.string.pref_default_name_key), name);
        editor.commit();
        pref_name.setSummary(name);

        // update the SeekBarPreference summary with current sensitivity value
        String SENSITIVITY_KEY = getResources().getString(R.string.pref_sensitivity_key);
        Preference pref_sens = findPreference(SENSITIVITY_KEY);
        double value = (getPreferenceScreen().getSharedPreferences().getInt(SENSITIVITY_KEY, 0) + 1.0) / 2.0;
        pref_sens.setSummary("" + value + "x");
    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }
}
