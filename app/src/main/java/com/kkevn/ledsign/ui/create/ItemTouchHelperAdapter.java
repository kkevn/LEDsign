/**
 * ItemTouchHelperAdapter is the interface meant to be implemented by the custom Adapter class that
 * is used in the View where movements are permitted.
 *
 * @source https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-6a6f0c422efd
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.create;

public interface ItemTouchHelperAdapter {

    /**
     * Swaps the position of the specified object to and from the specified positions.
     *
     * @param {int} fromPos: Position of object before being moved.
     * @param {int} toPos: Position of object after being moved.
     *
     * @return {boolean} Status of the movement.
     */
    boolean onItemMove(int fromPos, int toPos);

    /**
     * Action to perform on the object at the specified position once dismissed from the movement.
     *
     * @param {int} pos: Position of object to dismiss.
     */
    void onItemDismiss(int pos);
}