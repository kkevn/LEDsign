package com.kkevn.ledsign.ui.create;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkevn.ledsign.R;

public class SelectEffectListViewHolder {
    ImageView iv_icon_select;
    TextView tv_effect_select;

    SelectEffectListViewHolder(View v) {
        iv_icon_select = (ImageView) v.findViewById(R.id.iv_icon_select);
        tv_effect_select = (TextView) v.findViewById(R.id.tv_effect_select);
    }
}
