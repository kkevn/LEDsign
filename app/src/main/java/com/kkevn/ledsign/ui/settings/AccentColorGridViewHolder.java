package com.kkevn.ledsign.ui.settings;

import android.view.View;
import android.widget.ImageView;

import com.kkevn.ledsign.R;

public class AccentColorGridViewHolder {
    ImageView iv_color;

    AccentColorGridViewHolder(View v) {
        iv_color = (ImageView) v.findViewById(R.id.iv_color);
    }
}
