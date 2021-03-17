/**
 * stores and recycles views as they are scrolled off screen
 */

package com.kkevn.ledsign.ui.profiles;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.kkevn.ledsign.R;
import com.kkevn.ledsign.ui.create.Effect;
import com.kkevn.ledsign.ui.create.ItemTouchHelperViewHolder;

public class ProfileListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder {
    TextView tv_count;
    TextView tv_profile;
    TextView tv_date;
    private ProfileListView.ItemClickListener mClickListener;

    // swipe layout
    //ImageButton iv_remove, ib_duplicate, iv_edit;
    LinearLayout ll_remove, ll_duplicate, ll_edit;
    SwipeRevealLayout swipeRevealLayout;

    /**
     * Constructor for a custom view holder for a single profile item in the profile list.
     *
     * @param {View} itemView: The view of a single item in the list.
     * @param {ItemClickListener} mClickListener: The reference for the click listener.
     */
    ProfileListViewHolder(View itemView, ProfileListView.ItemClickListener mClickListener) {
        super(itemView);

        // fill the view
        tv_count = itemView.findViewById(R.id.tv_count);
        tv_profile = itemView.findViewById(R.id.tv_profile);
        tv_date = itemView.findViewById(R.id.tv_date);

        // enable listeners
        this.mClickListener = mClickListener;
        itemView.setOnClickListener(this);

        // swipe layout
        ll_remove = itemView.findViewById(R.id.ll_remove);
        ll_duplicate = itemView.findViewById(R.id.ll_duplicate);
        ll_edit = itemView.findViewById(R.id.ll_edit);
        swipeRevealLayout = itemView.findViewById(R.id.swipe_profile_layout);
    }

    /**
     * Enable an item-click listener on the specified view.
     *
     * @param {View} view: ???.
     */
    @Override
    public void onClick(View view) {
        if (mClickListener != null)
            mClickListener.onItemClick(view, getAdapterPosition());
    }

    @Override
    public void onItemSelected() {
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.colorSecondary));
    }

    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(), R.color.colorSecondaryLight));
    }
}