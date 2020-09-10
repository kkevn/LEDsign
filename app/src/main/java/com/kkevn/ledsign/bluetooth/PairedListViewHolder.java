package com.kkevn.ledsign.bluetooth;

import android.view.View;
import android.widget.TextView;

import com.kkevn.ledsign.R;

public class PairedListViewHolder {
    TextView tv_device_name;
    TextView tv_device_address;

    PairedListViewHolder(View v) {
        tv_device_name = (TextView) v.findViewById(R.id.tv_device_name);
        tv_device_address = (TextView) v.findViewById(R.id.tv_device_address);
    }
}
