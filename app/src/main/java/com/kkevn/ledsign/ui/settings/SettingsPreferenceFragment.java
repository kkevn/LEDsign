/**
 * SettingsPreferenceFragment is the fragment that builds and handles the list of changeable
 * application preferences.
 *
 * @author Kevin Kowalski
 */

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

    // declare relevant variables
    private LinearLayout ll_color;
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    /**
     * Called during onCreate() to supply the preferences for this fragment. Contains a listener for
     * how the changed preferences should behave.
     *
     * @param {Bundle} savedInstanceState: Bundle object containing activity's previous state.
     * @param {String} rootKey: Key for determining if this fragment should be rooted.
     */
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        // add the preference list layout from resources
        addPreferencesFromResource(R.xml.preferences);

        // add a listener to determine what the changed preferences should do or affect
        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                // get the current preference changed
                Preference preference = findPreference(key);

                // determine which preference was changed
                if (key.equals(getResources().getString(R.string.pref_default_name_key))) {

                    // validate the new custom default profile name
                    String name = sharedPreferences.getString(getResources().getString(R.string.pref_default_name_key), "");
                    name = name.trim();
                    if (name.isEmpty()) {
                        name = getResources().getString(R.string.menu_new_profile);
                    } else if (name.length() > 24) {
                        name = name.substring(0, 24);
                    }
                    name = name.replaceAll("[^A-Za-z0-9_ \\-]","");

                    // commit the validated name to shared preferences
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString(getResources().getString(R.string.pref_default_name_key), name);
                    editor.commit();

                    // update the preference and summary to have this value
                    ((EditTextPreference)preference).setText(name);
                    preference.setSummary(name);

                } else if (key.equals(getResources().getString(R.string.pref_sensitivity_key))) {

                    // update the SeekBar summary to reflect the current sensitivity value
                    double value = (sharedPreferences.getInt(getResources().getString(R.string.pref_sensitivity_key), 0) + 1.0) / 2.0;
                    preference.setSummary("" + value + "x");

                } else if (key.equals(getResources().getString(R.string.pref_theme_key))) {

                    // recreate the activity to initiate the theme change
                    getActivity().recreate();

                } else if (key.equals(getResources().getString(R.string.pref_color_key))) {

                    // update the accent color preview widget layout with the selected change
                    int color = sharedPreferences.getInt(getResources().getString(R.string.pref_color_key), 0);
                    ll_color = getActivity().findViewById(R.id.ll_accent_color);
                    ll_color.setBackgroundColor(color);

                    // recreate the activity to initiate the color accent change
                    getActivity().recreate();
                }
            }
        };
    }

    /**
     * Called when the fragment is visible to the user and actively running. Essentially updates the
     * several preferences to appear properly when resuming the fragment.
     */
    @Override
    public void onResume() {
        super.onResume();

        // register the preference change listener
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);

        // ---

        // fetch the currently input default profile name preference
        String DEFAULT_NAME_KEY = getResources().getString(R.string.pref_default_name_key);
        Preference pref_name = findPreference(DEFAULT_NAME_KEY);
        String name = getPreferenceScreen().getSharedPreferences().getString(DEFAULT_NAME_KEY, "");

        // trim its whitespace, prevent empty or too long names, and remove any invalid characters
        name = name.trim();
        if (name.isEmpty()) {
            name = getResources().getString(R.string.menu_new_profile);
        } else if (name.length() > 24) {
            name = name.substring(0, 24);
        }
        name = name.replaceAll("[^A-Za-z0-9_ \\-]","");

        // commit the validated custom default profile name to preferences
        SharedPreferences.Editor editor = getPreferenceScreen().getSharedPreferences().edit();
        editor.putString(getResources().getString(R.string.pref_default_name_key), name);
        editor.commit();

        // update the preference and summary to have this value
        ((EditTextPreference)pref_name).setText(name);
        pref_name.setSummary(name);

        // ---

        // update the SeekBarPreference summary with current sensitivity value
        String SENSITIVITY_KEY = getResources().getString(R.string.pref_sensitivity_key);
        Preference pref_sens = findPreference(SENSITIVITY_KEY);
        double value = (getPreferenceScreen().getSharedPreferences().getInt(SENSITIVITY_KEY, 0) + 1.0) / 2.0;
        pref_sens.setSummary("" + value + "x");

        // ---

        // update the AccentColorPreference widget with current color
        String COLOR_KEY = getResources().getString(R.string.pref_color_key);
        int color = getPreferenceScreen().getSharedPreferences().getInt(COLOR_KEY, 0);
        if (ll_color != null) {
            ll_color = getActivity().findViewById(R.id.ll_accent_color);
            ll_color.setBackgroundColor(color);
        }

        // ---
    }

    /**
     * Called when the fragment is no longer resumed.
     */
    @Override
    public void onPause() {
        super.onPause();

        // unregister the preference change listener
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    /**
     * Called when a preference requests to display a dialog.
     *
     * @param {Preference} preference: Preference object requesting the dialog.
     */
    @Override
    public void onDisplayPreferenceDialog(Preference preference) {

        // check for a call from AccentColorPreference
        if (preference instanceof AccentColorPreference) {

            // show an instance of the AccentColorPreferenceDialog
            AccentColorPreferenceDialog.newInstance().show(getFragmentManager(), null);

        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}