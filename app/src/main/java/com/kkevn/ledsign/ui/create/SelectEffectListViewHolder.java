/**
 * SelectEffectListViewHolder is a wrapper object used to contain the relevant fields for the item
 * that the SelectEffectListViewAdapter adapts.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.create;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkevn.ledsign.R;

public class SelectEffectListViewHolder {

    // declare relevant variables
    ImageView iv_icon_select;
    TextView tv_effect_select;

    /**
     * Constructor for a custom view holder for a single effect item in the selectable effects list.
     *
     * @param {View} v: The view of a single item in the list.
     */
    SelectEffectListViewHolder(View v) {
        iv_icon_select = (ImageView) v.findViewById(R.id.iv_icon_select);
        tv_effect_select = (TextView) v.findViewById(R.id.tv_effect_select);
    }
}