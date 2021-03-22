/**
 * PairedListViewAdapter is the Adapter object used to contain the blueprint for how to adapt the
 * individual paired Bluetooth devices from a list into a single View.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kkevn.ledsign.R;

import java.util.ArrayList;

public class PairedListViewAdapter extends ArrayAdapter<BluetoothDevice> {

    // declare relevant variables
    private ArrayList<BluetoothDevice> mPairedDevices;
    private LayoutInflater mInflater;
    private Context context;

    /**
     * Constructor for this PairedListViewAdapter.
     *
     * @param {Context} context: Reference to context of the current activity.
     * @param {ArrayList<BluetoothDevice>} objects: List of paired BluetoothDevice objects.
     */
    public PairedListViewAdapter(Context context, ArrayList<BluetoothDevice> objects) {
        super(context, 0, objects);

        // initialize this adapter's variables
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.mPairedDevices = objects;
    }

    /**
     * Returns a view that adapts the item at the given index.
     *
     * @param {int} i: Index of the item to adapt.
     * @param {View} view: The old view to reuse.
     * @param {ViewGroup} viewGroup: The parent this view will eventually be attached to.
     *
     * @return {View} View containing the adapted item.
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // grab the current view
        View v = view;

        // initialize a ViewHolder
        PairedListViewHolder viewHolder = null;

        // if the view is not recycled
        if (v == null) {

            // inflate the current row with a layout
            v = mInflater.inflate(R.layout.list_paired_bt, null, true);

            // create the view holder
            viewHolder = new PairedListViewHolder(v);

            // store a tag to the view
            v.setTag(viewHolder);
        }

        // otherwise view was null
        else {

            // update view without inflating a new row
            viewHolder = (PairedListViewHolder) v.getTag();
        }

        // populate item based on current position
        viewHolder.tv_device_name.setText(mPairedDevices.get(i).getName());
        viewHolder.tv_device_address.setText("[" + mPairedDevices.get(i).getAddress() + "]");

        // get app theme to know which colors to apply to TextView items
        String theme = PreferenceManager.getDefaultSharedPreferences(context).getString(context.getString(R.string.pref_theme_key), "");
        viewHolder.tv_device_name.setTextColor(theme.equals("0") ? Color.BLACK : Color.WHITE);
        viewHolder.tv_device_address.setTextColor(theme.equals("0") ? Color.BLACK : Color.WHITE);

        // return the view
        return v;
    }
}