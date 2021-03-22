/**
 * PairedListViewHolder is a wrapper object used to contain the relevant fields for the item that
 * the PairedListViewAdapter adapts.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.bluetooth;

import android.view.View;
import android.widget.TextView;

import com.kkevn.ledsign.R;

public class PairedListViewHolder {

    // declare TextViews found in this holder
    TextView tv_device_name;
    TextView tv_device_address;

    /**
     * Constructor for this PairedListViewHolder.
     *
     * @param {View} v: The view containing the items in this holder.
     */
    PairedListViewHolder(View v) {
        tv_device_name = (TextView) v.findViewById(R.id.tv_device_name);
        tv_device_address = (TextView) v.findViewById(R.id.tv_device_address);
    }
}