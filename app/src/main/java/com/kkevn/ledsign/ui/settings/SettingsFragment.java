/**
 * SettingsFragment is the fragment containing the list of changeable application preferences.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.kkevn.ledsign.R;

public class SettingsFragment extends Fragment {

    /**
     * Returns a view that contains the layout of this fragment that includes a list of changeable
     * preferences.
     *
     * @param {LayoutInflater} inflater: LayoutInflater object used to inflate the layout.
     * @param {ViewGroup} container: Parent view that this fragment's UI should attach to.
     * @param {Bundle} savedInstanceState: Bundle object containing activity's previous state.
     *
     * @return {View} View containing list of preferences.
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflate the fragment's layout
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        // place the FrameLayout containing the list of preferences into this fragment
        getFragmentManager().beginTransaction().replace(R.id.fl_settings, new SettingsPreferenceFragment()).commit();

        // return this populated view
        return root;
    }
}