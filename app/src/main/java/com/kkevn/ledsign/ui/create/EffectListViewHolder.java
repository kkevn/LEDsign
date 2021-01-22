/**
 * stores and recycles views as they are scrolled off screen
 */

package com.kkevn.ledsign.ui.create;

import android.support.v13.view.DragStartHelper;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kkevn.ledsign.R;

public class EffectListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
    ImageView iv_icon;
    TextView tv_effect;
    TextView tv_param;
    ImageView iv_drag;
    private EffectListView.ItemClickListener mClickListener;

    /**
     * Constructor for a custom view holder for a single effect item in the effect list.
     *
     * @param {View} itemView: The view of a single item in the list.
     * @param {ItemClickListener} mClickListener: The reference for the click listener.
     */
    EffectListViewHolder(View itemView, EffectListView.ItemClickListener mClickListener) {
        super(itemView);

        // fill the view
        iv_icon = itemView.findViewById(R.id.iv_icon);
        tv_effect = itemView.findViewById(R.id.tv_effect);
        tv_param = itemView.findViewById(R.id.tv_param);
        iv_drag = itemView.findViewById(R.id.iv_drag);

        // enable listeners
        this.mClickListener = mClickListener;
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
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

    /**
     * Inflate a context menu with options relevant to an effect from the effect list.
     *
     * @param {ContextMenu} contextMenu: Context menu object to inflate.
     * @param {View} view: ???.
     * @param {ContextMenuInfo} contextMenuInfo: ???.
     */
    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle(R.string.menu_effect_header);
        contextMenu.add(this.getAdapterPosition(), R.id.menu_effect_edit, 0, R.string.menu_effect_edit);
        contextMenu.add(this.getAdapterPosition(), R.id.menu_effect_duplicate, 1, R.string.menu_effect_duplicate);
        contextMenu.add(this.getAdapterPosition(), R.id.menu_effect_remove, 2, R.string.menu_effect_remove);
    }
}
