/**
 * DragListener is the interface meant to be implemented by the activity or fragment that contains
 * the View where drag movement is expected.
 *
 * @source https://medium.com/@ipaulpro/drag-and-swipe-with-recyclerview-6a6f0c422efd
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.create;

import android.support.v7.widget.RecyclerView;

public interface DragListener {

    /**
     * Action to perform on the specified ViewHolder once drag movement is initiated.
     *
     * @param {RecyclerView.ViewHolder} viewHolder: ViewHolder item to manipulate.
     */
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
}