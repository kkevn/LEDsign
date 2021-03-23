/**
 * EffectListViewHolder is a wrapper object used to contain the relevant fields for the item that
 * the EffectListViewAdapter adapts. Also stores and recycles the views as they are scrolled off
 * screen.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.create;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.kkevn.ledsign.R;

public class EffectListViewHolder extends RecyclerView.ViewHolder {

    // declare relevant variables
    ImageView iv_icon;
    TextView tv_effect;
    TextView tv_param;
    ImageView iv_drag;
    LinearLayout ll_remove, ll_duplicate, ll_edit;
    SwipeRevealLayout swipeRevealLayout;

    /**
     * Constructor for a custom view holder for a single effect item in the effects list.
     *
     * @param {View} itemView: The view of a single item in the list.
     */
    EffectListViewHolder(View itemView) {
        super(itemView);

        // find the ImageViews, TextViews and other necessary layouts from the provided View
        iv_icon = itemView.findViewById(R.id.iv_icon);
        tv_effect = itemView.findViewById(R.id.tv_effect);
        tv_param = itemView.findViewById(R.id.tv_param);
        iv_drag = itemView.findViewById(R.id.iv_drag);
        ll_remove = itemView.findViewById(R.id.ll_remove);
        ll_duplicate = itemView.findViewById(R.id.ll_duplicate);
        ll_edit = itemView.findViewById(R.id.ll_edit);
        swipeRevealLayout = itemView.findViewById(R.id.swipe_layout);
    }
}