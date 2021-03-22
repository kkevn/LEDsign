/**
 * AccentColorGridViewAdapter is the Adapter object used to contain the blueprint for how to adapt
 * the individual accent color options from a list into a single View.
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.ui.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.kkevn.ledsign.R;

public class AccentColorGridViewAdapter extends BaseAdapter {

    // declare relevant variables
    private int[] colors;
    private LayoutInflater mInflater;
    private int width;

    /**
     * Constructor for this AccentColorGridViewAdapter.
     *
     * @param {Context} context: Reference to context of the current activity.
     */
    public AccentColorGridViewAdapter(Context context) {

        // get the pixel width of the current device
        width = context.getResources().getSystem().getDisplayMetrics().widthPixels;

        // initialize this adapter's variables
        this.mInflater = LayoutInflater.from(context);
        this.colors = context.getResources().getIntArray(R.array.accents);
    }

    /**
     * Returns a count of the items in this adapted list.
     *
     * @return {int} Count of items.
     */
    @Override
    public int getCount() {
        return colors.length;
    }

    /**
     * Returns the item at the specified index.
     *
     * @param {int} i: Index of the item.
     *
     * @return {Object} Item object.
     */
    @Override
    public Object getItem(int i) {
        return colors[i];
    }

    /**
     * Returns the ID of the item at the specified index.
     *
     * @param {int} i: Index of the item.
     *
     * @return {long} ID of item.
     */
    @Override
    public long getItemId(int i) {
        return colors[i];
    }

    /**
     * Returns a view that adapts the item at the given index.
     *
     * @param {int} i: Index of the item to adapt.
     * @param {View} view: The old view to reuse.
     * @param {ViewGroup} viewGroup: The parent this view will eventually be attached to.
     *
     * @return {View} View containing the adapted item.
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        // grab the current view
        View v = view;

        // initialize a ViewHolder
        AccentColorGridViewHolder viewHolder = null;

        // if the view is not recycled
        if (v == null) {

            // inflate the current row with a layout
            v = mInflater.inflate(R.layout.list_color_accent, null, true);

            // adjust the width of this view relative to the width of this device
            v.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, (int) (width * 0.1)));

            // create the view holder
            viewHolder = new AccentColorGridViewHolder(v);

            // store a tag to the view
            v.setTag(viewHolder);
        }

        // otherwise view was null
        else {

            // update view without inflating a new row
            viewHolder = (AccentColorGridViewHolder) v.getTag();
        }

        // populate item based on current position
        viewHolder.iv_color.setBackgroundColor(colors[i]);

        // return the view
        return v;
    }
}