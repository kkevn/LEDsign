/**
 * AccentColorPreference is the custom preference for the application-wide changeable accent color.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.settings;

import android.content.Context;
import android.support.v7.preference.DialogPreference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.kkevn.ledsign.R;

public class AccentColorPreference extends DialogPreference {

    // declare relevant variables
    private Context context;

    /**
     * Constructor for this AccentColorPreference.
     *
     * @param {Context} context: Reference to context of the current activity.
     * @param {AttributeSet} attrs: Set of attributes.
     */
    public AccentColorPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        // initialize this preference's variables
        this.context = context;
    }

    /**
     * Binds the contents of the PreferenceViewHolder with updated contents. Essentially sets the
     * accent color preview widget color to the accent color stored in shared preferences.
     *
     * @param {PreferenceViewHolder} holder: The ViewHolder that should have its contents updated.
     */
    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        // update preference's current color preview widget layout
        LinearLayout ll = (LinearLayout) holder.findViewById(R.id.ll_accent_color);
        ll.setBackgroundColor(getSharedPreferences().getInt(context.getResources().getString(R.string.pref_color_key), 0));
    }
}