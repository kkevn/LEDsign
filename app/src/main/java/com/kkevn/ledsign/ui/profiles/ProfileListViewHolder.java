/**
 * ProfileListViewHolder is a wrapper object used to contain the relevant fields for the item that
 * the ProfileListViewAdapter adapts. Also stores and recycles the views as they are scrolled off
 * screen.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.profiles;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.kkevn.ledsign.R;

public class ProfileListViewHolder extends RecyclerView.ViewHolder {

    // declare relevant variables
    TextView tv_count;
    TextView tv_profile;
    TextView tv_date;
    LinearLayout ll_remove, ll_duplicate, ll_edit;
    SwipeRevealLayout swipeRevealLayout;

    /**
     * Constructor for a custom view holder for a single profile item in the profile list.
     *
     * @param {View} itemView: The view of a single item in the list.
     */
    ProfileListViewHolder(View itemView) {
        super(itemView);

        // find the TextViews and other necessary layouts from the provided View
        tv_count = itemView.findViewById(R.id.tv_count);
        tv_profile = itemView.findViewById(R.id.tv_profile);
        tv_date = itemView.findViewById(R.id.tv_date);
        ll_remove = itemView.findViewById(R.id.ll_remove);
        ll_duplicate = itemView.findViewById(R.id.ll_duplicate);
        ll_edit = itemView.findViewById(R.id.ll_edit);
        swipeRevealLayout = itemView.findViewById(R.id.swipe_profile_layout);
    }
}