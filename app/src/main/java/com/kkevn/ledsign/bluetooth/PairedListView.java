package com.kkevn.ledsign.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.kkevn.ledsign.R;

import java.util.ArrayList;

// MainActivity.BTD
// BluetoothDevice

public class PairedListView extends ArrayAdapter<BluetoothDevice> {

    private ArrayList<BluetoothDevice> mPairedDevices;
    private LayoutInflater mInflater;

    public PairedListView(Context context, ArrayList<BluetoothDevice> objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from(context);
        this.mPairedDevices = objects;
    }

    public String yolo() {
        return "";
    }

    //@Override
    public BluetoothDevice getat(int i) {
        return mPairedDevices.get(i);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        // grab the current view
        View v = view;

        // initialize a ViewHolder
        PairedListViewHolder viewHolder = null;

        // if the view is not recycled
        if (v == null) {

            // inflate the current row with a layout
            //LayoutInflater layoutInflater = context.getLayoutInflater();
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

        // return the view
        return v;
    }
}
