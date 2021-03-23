package com.kkevn.ledsign.ui.create;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

// https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-6a6f0c422efd

public class CustomItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter itemTouchHelperAdapter;

    public CustomItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        this.itemTouchHelperAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        itemTouchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        itemTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}