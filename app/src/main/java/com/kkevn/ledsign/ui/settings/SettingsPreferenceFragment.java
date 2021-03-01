package com.kkevn.ledsign.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.widget.LinearLayout;

import com.kkevn.ledsign.R;

public class SettingsPreferenceFragment extends PreferenceFragmentCompat {

    private LinearLayout ll_color;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }*/

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

                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(getResources().getString(R.string.pref_default_name_key), name);
                    editor.commit();

                    ((EditTextPreference)preference).setText(name);

                    preference.setSummary(name);
                } else if (key.equals(getResources().getString(R.string.pref_sensitivity_key))) {
                    double value = (sharedPreferences.getInt(getResources().getString(R.string.pref_sensitivity_key), 0) + 1.0) / 2.0 ;
                    preference.setSummary("" + value + "x");
                } else if (key.equals(getResources().getString(R.string.pref_color_key))) {
                    int color = sharedPreferences.getInt(getResources().getString(R.string.pref_color_key), 0);
                    ll_color = getActivity().findViewById(R.id.ll_accent_color);
                    ll_color.setBackgroundColor(color);
                    //preference.setWidgetLayoutResource(ll_color.getId());
                }
            }
        };
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

        ((EditTextPreference)pref_name).setText(name);

        // update the SeekBarPreference summary with current sensitivity value
        String SENSITIVITY_KEY = getResources().getString(R.string.pref_sensitivity_key);
        Preference pref_sens = findPreference(SENSITIVITY_KEY);
        double value = (getPreferenceScreen().getSharedPreferences().getInt(SENSITIVITY_KEY, 0) + 1.0) / 2.0;
        pref_sens.setSummary("" + value + "x");

        // update the AccentColorPreference widget with current color
        String COLOR_KEY = getResources().getString(R.string.pref_color_key);
        Preference pref_color = findPreference(COLOR_KEY);
        int color = getPreferenceScreen().getSharedPreferences().getInt(COLOR_KEY, 0);
        if (ll_color != null) {
            ll_color = getActivity().findViewById(R.id.ll_accent_color);
            ll_color.setBackgroundColor(color);
            //pref_color.setWidgetLayoutResource(ll_color.getId());
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (preference instanceof AccentColorPreference) {
            AccentColorPreferenceDialog.newInstance().show(getFragmentManager(), null);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
