package com.kkevn.ledsign.ui.settings;

import android.content.Context;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.kkevn.ledsign.R;

public class AccentColorPreference extends DialogPreference implements Preference.OnPreferenceChangeListener {

    private Context context;

    public AccentColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOnPreferenceChangeListener(this);
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        // update preference's current color preview on start
        LinearLayout ll = (LinearLayout) holder.findViewById(R.id.ll_accent_color);
        ll.setBackgroundColor(getSharedPreferences().getInt(context.getResources().getString(R.string.pref_color_key), 0));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object o) {
        // do something o = new value
        Toast.makeText(getContext(), "" + o.toString(), Toast.LENGTH_SHORT).show();
        return false;
    }
}
