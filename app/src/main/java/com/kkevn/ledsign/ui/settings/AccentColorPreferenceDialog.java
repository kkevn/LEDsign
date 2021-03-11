package com.kkevn.ledsign.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.kkevn.ledsign.R;

public class AccentColorPreferenceDialog extends DialogFragment {

    private GridView gv_list;
    private Button b_cancel;
    private AccentColorGridView accentColorGridView;

    public static AccentColorPreferenceDialog newInstance() {
        return new AccentColorPreferenceDialog();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogpreference_color_accent, container, false);

        // init UI, list adapter, listeners
        gv_list = view.findViewById(R.id.gv_list);
        b_cancel = view.findViewById(R.id.b_cancel);

        accentColorGridView = new AccentColorGridView(getContext());
        gv_list.setAdapter(accentColorGridView);
        gv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String COLOR_KEY = getResources().getString(R.string.pref_color_key);
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(COLOR_KEY, (Integer) adapterView.getAdapter().getItem(i));
                editor.commit();

                //Toast.makeText(getContext(), "Color: " + adapterView.getAdapter().getItem(i), Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        // update submit button with current accent color
        int preferenceAccentColor = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(getResources().getString(R.string.pref_color_key), 0);
        b_cancel.setTextColor(preferenceAccentColor);
        b_cancel.setOnClickListener(e -> this.dismiss());
        
        return view;
    }
}
