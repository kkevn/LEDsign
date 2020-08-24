package com.kkevn.ledsign.ui.create;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkevn.ledsign.R;

public class ListViewHolder {
    ImageView iv_icon;
    TextView tv_effect;
    TextView tv_param;
    ImageView iv_drag;

    ListViewHolder(View v) {
        iv_icon = (ImageView) v.findViewById(R.id.iv_icon);
        tv_effect = (TextView) v.findViewById(R.id.tv_effect);
        tv_param = (TextView) v.findViewById(R.id.tv_param);
        iv_drag = (ImageView) v.findViewById(R.id.iv_drag);
    }
}
