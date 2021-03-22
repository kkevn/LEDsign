/**
 * AccentColorGridViewHolder is a wrapper object used to contain the relevant fields for the item
 * that the AccentColorGridViewAdapter adapts.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.settings;

import android.view.View;
import android.widget.ImageView;

import com.kkevn.ledsign.R;

public class AccentColorGridViewHolder {

    // declare ImageView found in this holder
    ImageView iv_color;

    /**
     * Constructor for this AccentColorGridViewHolder.
     *
     * @param {View} v: The view containing the items in this holder.
     */
    AccentColorGridViewHolder(View v) {
        iv_color = (ImageView) v.findViewById(R.id.iv_color);
    }
}