/**
 * AccentColorPreferenceDialog is the DialogFragment object used to display the prompt for selecting
 * one of all available accent colors that will accentuate the app. Successful selection adjusts the
 * current app styling to use the selected accent color and displays the selected color in the
 * widget layout of the relevant Settings Preference. Used exclusively when tapping the accent color
 * preference in the Settings page.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.kkevn.ledsign.R;

public class AccentColorPreferenceDialog extends DialogFragment {

    // declare relevant variables
    private GridView gv_list;
    private Button b_cancel;
    private AccentColorGridViewAdapter accentColorGridView;

    /**
     * Returns an instance of this custom dialog.
     *
     * @return {AccentColorPreferenceDialog} Instance of this custom dialog.
     */
    public static AccentColorPreferenceDialog newInstance() {
        return new AccentColorPreferenceDialog();
    }

    /**
     * Returns a view that contains the layout of this custom dialog.
     *
     * @param {LayoutInflater} inflater: LayoutInflater object used to inflate the layout.
     * @param {ViewGroup} container: Parent view that this fragment's UI should attach to.
     * @param {Bundle} savedInstanceState: Bundle object containing activity's previous state.
     *
     * @return {View} View containing list of all available accent colors.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // inflate the dialog's layout
        View view = inflater.inflate(R.layout.dialogpreference_color_accent, container, false);

        // find the GridView and Button in this dialog's layout
        gv_list = view.findViewById(R.id.gv_list);
        b_cancel = view.findViewById(R.id.b_cancel);

        // initialize and set adapter for this dialog
        accentColorGridView = new AccentColorGridViewAdapter(getContext());
        gv_list.setAdapter(accentColorGridView);

        // set listener for the list in this dialog
        gv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // update the SharedPreferences for accent color with the selection
                String COLOR_KEY = getResources().getString(R.string.pref_color_key);
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(COLOR_KEY, (Integer) adapterView.getAdapter().getItem(i));
                editor.commit();

                // dismiss the dialog after confirming a selection
                dismiss();
            }
        });

        // update submit button with current accent color
        int preferenceAccentColor = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(getResources().getString(R.string.pref_color_key), 0);
        b_cancel.setTextColor(preferenceAccentColor);
        b_cancel.setOnClickListener(e -> this.dismiss());

        // return this customized view
        return view;
    }
}