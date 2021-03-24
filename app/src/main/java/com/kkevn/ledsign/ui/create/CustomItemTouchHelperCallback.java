/**
 * CustomItemTouchHelperCallback is the object used to override several ItemTouchHelper.Callback
 * functions for changing the behavior of how certain gestures should affect the effects list
 * during profile creation, namely drag and drop.
 *
 * @source https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-6a6f0c422efd
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.create;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class CustomItemTouchHelperCallback extends ItemTouchHelper.Callback {

    // declare relevant variables
    private final ItemTouchHelperAdapter itemTouchHelperAdapter;

    /**
     * Constructor for this CustomItemTouchHelperCallback.
     *
     * @param {ItemTouchHelperAdapter} adapter: Adapter class implementing this type of interface.
     */
    public CustomItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {

        // initialize this callback's variables
        this.itemTouchHelperAdapter = adapter;
    }

    /**
     * Returns whether or not long press drag is enabled.
     *
     * @return {boolean} Long press drag status.
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    /**
     * Returns whether or not item view swipe is enabled.
     *
     * @return {boolean} Item view swipe status.
     */
    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    /**
     * Returns the permissible movement flags such as vertical or horizontal movement.
     *
     * @param {RecyclerView} recyclerView: RecyclerView to apply movement flags to.
     * @param {RecyclerView.ViewHolder} viewHolder: ViewHolder item in the above RecycerView.
     *
     * @return {int} Movement flags.
     */
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {

        // enable vertical movement but not horizontal swipe movement
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, 0);
    }

    /**
     * Specifies the actions to occur during drag movement between the given ViewHolder items in the
     * given RecyclerView list.
     *
     * @param {RecyclerView} recyclerView: RecyclerView to apply movement to.
     * @param {RecyclerView.ViewHolder} viewHolder: ViewHolder item being moved.
     * @param {RecyclerView.ViewHolder} target: ViewHolder item to move to.
     *
     * @return {boolean} Status of the movement.
     */
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        itemTouchHelperAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    /**
     * Specifies the actions to occur during swipe movement for the given ViewHolder item at the
     * given position.
     *
     * @param {RecyclerView.ViewHolder} viewHolder: ViewHolder item being swiped.
     * @param {int} i: Index of ViewHolder item being swiped.
     */
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        itemTouchHelperAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}